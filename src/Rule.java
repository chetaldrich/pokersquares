import java.util.*;
import java.util.function.Predicate;

public class Rule implements Node {

    private final int HAND_SIZE = 5;
    private final int FLUSH_SCORE;
    private final int STRAIGHT_SCORE;
    private final PokerSquaresPointSystem system;
    private Node leftChild;
    private Node rightChild;
    private Node parent;
    private Random randomGenerator;
    private boolean row;
    private int rc;
    private int pointThresh;
    private Integer id;


    public Rule(PokerSquaresPointSystem system, Integer id) {
        this.id = id;
        this.system = system;

        FLUSH_SCORE = system.getHandScore(PokerHand.FLUSH);
        STRAIGHT_SCORE = system.getHandScore(PokerHand.STRAIGHT);
        int[] scores = system.getScoreTable();
        Arrays.sort(scores);
        // threshold is the median score
        pointThresh = scores[scores.length/2];

        randomGenerator = new Random();
        // determine if checking row or column
        boolean randBool = randomGenerator.nextBoolean();
        row = randBool;

        // which row or column to check
        int randInt = randomGenerator.nextInt(5);
        rc = randInt;
    }


    /**
     * {@inheritDoc}
     */
    public void setRight(Node right) {
        this.rightChild = right;
        this.rightChild.setParent(this);
    }


    /**
     * {@inheritDoc}
     */
    public void setLeft(Node left) {
        this.leftChild = left;
        this.leftChild.setParent(this);
    }


    /**
     * {@inheritDoc}
     */
    public Node getParent() {
        return this.parent;
    }


    /**
     * {@inheritDoc}
     */
    public void setParent(Node parent) {
        this.parent = parent;
    }


    /**
     * {@inheritDoc}
     */
    public Integer getID() {
        return id;
    }


    /**
     * @return A String that represents the function or value of this node.
     */
    public String getLabel() {
        String rowOrCol = row ? "row" : "column";
        String rcnum = Integer.toString(rc);
        String label = "Check the " + rowOrCol + " ranked " + rcnum;
        return label;
    }


    /**
     * Counts the cards in the row/column that fit a certain criterion
     * @return A list of counts sorted by the criterion in rows/columns.
     */
     private int[][] countCards(Card[][] grid, Predicate<Card> filter) {
        int[][] counts = new int[5][2];
        // make the second entry in each count the row/col number
        for (int i = 1; i < 5; i++) {
            counts[i][1] = i;
        }

        // count the number of entries in each row/col
        if (row) {
            // row
            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < 5; col++) {
                    if (filter.test(grid[row][col])) {
                        counts[row][0]++;
                    }
                }
            }
        } else {
            // column
            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < 5; col++) {
                    if (filter.test(grid[col][row])) {
                        counts[row][0]++;
                    }
                }
            }
        }
        // compares 2 dimensional int arrays
        Arrays.sort(counts, new Comparator<int[]>() {
        @Override
        public int compare(final int[] item1, final int[] item2) {
            // randomize if the values are the same
            if (item2[0] - item1[0] == 0) {
                return randomGenerator.nextBoolean() ? 1 : -1;
            }
            // otherwise, return the normal comparator
            return item2[0] - item1[0];
        }
        });

        return counts;
    }


    /**
     * Checks a given row number, places the card somewhere in the row,
     * and then checks what kind of hand it gives, and returns the value of
     * the hand.
     * @return A boolean, indicating which direction to move in the tree.
     */
    private boolean checkRow(Card[][] grid, Card curCard) {
        // choose which row we'll place the card into
        Predicate<Card> isCard = (Card card) -> card != null;
        int[][] preferenceList = countCards(grid, isCard);

        // make sure there aren't too many cards in a hand
        int temprc = rc;
        int rowChoice = preferenceList[temprc][1];
        while (preferenceList[temprc][0] >= HAND_SIZE) {
            temprc = (temprc + 1) % HAND_SIZE;
            rowChoice = preferenceList[temprc][1];
            if (temprc == rc) break;
        }

        Card[] potentialHand = grid[rowChoice].clone();
        int handLength = preferenceList[temprc][0];
        potentialHand[handLength] = curCard;
        int handScore = system.getHandScore(potentialHand);
        int suitScore = suitCheck(potentialHand);
        int seqScore = seqCheck(potentialHand);
        if (suitScore > handScore) handScore = suitScore;
        else if (seqScore > handScore) handScore = seqScore;
        return (handScore > pointThresh);
    }


    /**
     * Checks a given column number, places the card somewhere in the column,
     * and then checks what kind of hand it gives, and returns the value of
     * the hand.
     * @return A boolean, indicating which direction to move in the tree.
     */
    private boolean checkColumn(Card[][] grid, Card curCard) {
        // find counts for each column
        Predicate<Card> isCard = (Card card) -> card != null;
        int[][] preferenceList = countCards(grid, isCard);

        // find column with appropriate number of cards (< 5)
        int temprc = rc;
        int columnChoice = preferenceList[temprc][1];
        while (preferenceList[temprc][0] >= HAND_SIZE) {
            temprc = (temprc + 1) % HAND_SIZE;
            columnChoice = preferenceList[temprc][1];
            if (temprc == rc) break;
        }

        Card[] potentialHand = new Card[5];
        int handLength = preferenceList[temprc][0];
        for (int i = 0; i < potentialHand.length; i++) {
            potentialHand[i] = grid[columnChoice][i];
        }
        potentialHand[handLength] = curCard;
        int handScore = system.getHandScore(potentialHand);
        int suitScore = suitCheck(potentialHand);
        int seqScore = seqCheck(potentialHand);
        if (suitScore > handScore) handScore = suitScore;
        else if (seqScore > handScore) handScore = seqScore;
        return (handScore > pointThresh);
    }


    /**
     * Checks for groups of suits in the hand, and then
     * determines whether there is a flush. If there is a
     * partial flush, scales down the score.
     * @return A score for the hand based on flush potential.
     */
    private int suitCheck(Card[] hand) {
        int[] suitCount = new int[HAND_SIZE];
        for (int i = 0; i < HAND_SIZE; i++) {
            suitCount[i] = 0;
        }
        int maxSuit = 0;
        for (int i = 0; i < HAND_SIZE; i++) {
            if (hand[i] != null) {
                int suit = hand[i].getSuit();
                suitCount[suit]++;
                if (suitCount[suit] > maxSuit) maxSuit = suitCount[suit];
            }
        }
        if (maxSuit == HAND_SIZE) {
            return FLUSH_SCORE;
        } else {
            return (FLUSH_SCORE/(HAND_SIZE-maxSuit));
        }
    }


    /**
     * Checks for sequences in the given hand and returns
     * the score for the straight, and if the sequence is partial,
     * returns a scale down value for the score.
     * @return A score for the hand based on straight potential.
     */
    private int seqCheck(Card[] hand) {
        int[] ranks = new int[HAND_SIZE];
        for (int i = 0; i < HAND_SIZE; i++) {
            if (hand[i] == null) {
                ranks[i] = 0;
            } else {
                ranks[i] = hand[i].getRank();
            }
        }
        Arrays.sort(ranks);
        int first = 0;
        int seqCount = 0;
        for (int i = 0; i < HAND_SIZE; i++) {
            if (ranks[i] != 0) {
                if (first == 0) {
                    first = ranks[i];
                }
                if (ranks[i] - first < 5) {
                    seqCount++;
                }
            }
        }
        if (seqCount == HAND_SIZE) {
            return STRAIGHT_SCORE;
        } else {
            return (STRAIGHT_SCORE/(HAND_SIZE - seqCount));
        }
    }


    /**
     * Evaluates the node at this level and returns one of the child nodes.
     * @return A boolean, indicating which direction to move in the tree.
     */
    public int[] evaluate(Card[][] grid, Card curCard) {
        boolean direction = row
        ? checkRow(grid, curCard)
        : checkColumn(grid, curCard);

        return direction
        ? rightChild.evaluate(grid, curCard)
        : leftChild.evaluate(grid, curCard);
    }


    /**
     * {@inheritDoc}
     */
    public Node getLeftChild() {
        return leftChild;
    }


    /**
     * {@inheritDoc}
     */
    public Node getRightChild() {
        return rightChild;
    }


    /**
     * {@inheritDoc}
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


}
