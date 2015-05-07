import java.util.*;
import java.util.function.Predicate;

public class Rule {

    private final int HAND_SIZE;
    private final int FLUSH_SCORE;
    private final int STRAIGHT_SCORE;
    private final PokerSquaresPointSystem pointSystem;
    private boolean isRightDec;
    private boolean isLeftDec;
    private Rule leftRule;
    private Rule rightRule;
    private Decision leftDecision;
    private Decision rightDecision;
    Random randomGenerator;
    boolean row;
    int rc;
    int pointThresh;


    public Rule(PokerSquaresPointSystem ps) {

        // it's going to have decision nodes unless told otherwise
        leftDecision = new Decision();
        rightDecision = new Decision();
        isLeftDec = true;
        isRightDec = true;

        pointSystem = ps;
        HAND_SIZE = 5;
        FLUSH_SCORE = pointSystem.getHandScore(PokerHand.FLUSH);
        STRAIGHT_SCORE = pointSystem.getHandScore(PokerHand.STRAIGHT);
        int[] scores = pointSystem.getScoreTable();
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
     * Sets the right child to be a rule
     *
     */
    public void setRight(Rule right) {
        rightRule = right;
        isRightDec = false;
    }

    /**
     * Sets the right child to be a decision
     *
     */
    public void setRight(Decision right) {
        rightDecision = right;
        isRightDec = true;
    }

    /**
     * Sets the left child to be a rule
     *
     */
    public void setLeft(Rule left) {
        leftRule = left;
        isLeftDec = false;
    }

    /**
     * Sets the left child to be a decision
     *
     */
    public void setLeft(Decision left) {
        leftDecision = left;
        isLeftDec = true;
    }

    /**
     * @return A String that represents the function or value of this node.
     */
    String getLabel() {
        String label = "Label based on row/col, rank/straight/suit";
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
        // put card into the row that we care about
        Predicate<Card> isCard = (Card card) -> card != null;
        int[][] preferenceList = countCards(grid, isCard);
        int rowChoice = preferenceList[rc][1];
        int temprc = (rc+1)%HAND_SIZE;
        while (preferenceList[temprc][0] == HAND_SIZE) {
            rowChoice = preferenceList[temprc][1];
            temprc = (temprc+1)%HAND_SIZE;
        }
        Card[] potentialHand = grid[rowChoice];
        int handLength = 0;
        for (int i=0; i<potentialHand.length; i++) {
            if (potentialHand[i] != null) handLength ++;
        }
        while (handLength >= 5) {
            int nextRC = (rc+1)%HAND_SIZE;
            potentialHand = grid[rc];
            for (int i=0; i<potentialHand.length; i++) {
                if (potentialHand[i] != null) handLength++;
            }
        }
        potentialHand[handLength] = curCard;
        int handScore = pointSystem.getHandScore(potentialHand);
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
        // put card into the column that we care about
        Predicate<Card> isCard = (Card card) -> card != null;
        int[][] preferenceList = countCards(grid, isCard);
        int columnChoice = preferenceList[rc][1];
        int temprc = (rc+1)%HAND_SIZE;
        while (preferenceList[temprc][0] == HAND_SIZE) {
            columnChoice = preferenceList[temprc][1];
            temprc = (temprc+1)%HAND_SIZE;
        }
        Card[] potentialHand = new Card[5];
        int handLength = 0;
        for (int i=0; i<potentialHand.length; i++) {
            potentialHand[i] = grid[columnChoice][i];
            if (potentialHand[i] != null) handLength++;
        }
        while (handLength >= 5) {
            for (int i=0; i<potentialHand.length; i++) {
                potentialHand[i] = grid[rc][i];
                if (potentialHand[i] != null) handLength ++;
            }
        }
        potentialHand[handLength] = curCard;
        int handScore = pointSystem.getHandScore(potentialHand);
        int suitScore = suitCheck(potentialHand);
        int seqScore = seqCheck(potentialHand);
        if (suitScore > handScore) handScore = suitScore;
        else if (seqScore > handScore) handScore = seqScore;
        return (handScore > pointThresh);
    }

    /**
     * 
     * @return An int
     */
    private int suitCheck(Card[] hand) {
        int[] suitCount = new int[HAND_SIZE];
        for (int i=0; i<HAND_SIZE; i++) {
            suitCount[i] = 0;
        }
        int maxSuit = 0;
        for (int i=0; i<HAND_SIZE; i++) {
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
     * 
     * @return An int
     */
    private int seqCheck(Card[] hand) {
        int[] ranks = new int[HAND_SIZE];
        for (int i=0; i<HAND_SIZE; i++) {
            if (hand[i] == null) {
                ranks[i] = 0;
            } else {
                ranks[i] = hand[i].getRank();                
            }
        }
        Arrays.sort(ranks);
        int first = 0;
        int seqCount = 0;
        for (int i=0; i<HAND_SIZE; i++) {
            if (ranks[i] != 0) {
                if (first == 0) {
                    first = ranks[i];
                }
                if (ranks[i]-first < 5) {
                    seqCount++;
                }
            }
        }
        if (seqCount == HAND_SIZE) {
            return STRAIGHT_SCORE;
        } else {
            return (STRAIGHT_SCORE/(HAND_SIZE-seqCount));
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
        
        if (direction) {
            return isRightDec
            ? rightDecision.evaluate(grid,curCard)
            : rightRule.evaluate(grid,curCard);
        } else {
            return isLeftDec
            ? leftDecision.evaluate(grid,curCard)
            : leftRule.evaluate(grid,curCard);
        }
    }

    /**
     * Retrieves a direct sub-rule from this rule.
     * @param decisions The boolean index of a child rule.
     * 0 (false) is left, 1 (true) is right.
     * @return The node at the specified position.
     *
     * removing for the time being because unsure about 
     * how to return two difference types
     */
    // Rule getChild(boolean direction) {
    //     if (direction) {
    //         return isRightDec
    //         ? rightDecision
    //         : rightRule;
    //     } else {
    //         return isLeftDec
    //         ? leftDecision
    //         : leftRule;
    //     }
    // }


}
