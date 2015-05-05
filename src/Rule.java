import java.util.Random;
import java.util.Arrays;

public class Rule {

    private final Rule condition;
    private final Rule leftRule;
    private final Rule rightRule;
    private final Decision leftDecision;
    private final Decision rightDecision;
    private final boolean makeDecision;
    private final int HAND_SIZE;
    private final int FLUSH_SCORE;
    private final int STRAIGHT_SCORE;
    private final PokerSquaresPointSystem pointSystem;
    Random randomGenerator;
    boolean row;
    int rc;
    int pointThresh;


    public Rule(PokerSquaresPointSystem ps) {

        // initialize the rule nodes
        // TODO: Restructure so decisions can be at end of the tree
        condition = new Rule(ps);
        leftRule = new Rule(ps);
        rightRule = new Rule(ps);

        leftDecision = new Decision();
        rightDecision = new Decision();


        pointSystem = ps;
        HAND_SIZE = 5;
        FLUSH_SCORE = pointSystem.getHandScore(PokerHand.FLUSH);
        STRAIGHT_SCORE = pointSystem.getHandScore(PokerHand.STRAIGHT);
        int[] scores = pointSystem.getScoreTable();
        Arrays.sort(scores);
        pointThresh = scores[scores.length/2];

        randomGenerator = new Random();
        // decide if this will go to more rules or to decisions
        boolean randBool = randomGenerator.nextBoolean();
        makeDecision = randBool;

        // determine if checking row or column
        randBool = randomGenerator.nextBoolean();
        row = randBool;

        // which row or column to check
        int randInt = randomGenerator.nextInt(5);
        rc = randInt;
        // at what point threshold (for a hand) should the rule go left/right
        pointThresh = 0;
    }


    /**
     * @return A String that represents the function or value of this node.
     */
    String getLabel() {
        String label = "Label based on row/col, rank/straight/suit";
        return label;
    }

    /**
     * Checks a given row number, places the card somewhere in the row,
     * and then checks what kind of hand it gives, and returns the value of
     * the hand.
     * @return A boolean, indicating which direction to move in the tree.
     */
    private boolean checkRow(Card[][] grid, Card curCard) {
        // put card into the row that we care about
        Card[] potentialHand = grid[rc];
        int handLength = 0;
        for (int i=0; i<potentialHand.length; i++) {
            if (potentialHand[i] != null) handLength ++;
        }
        while (handLength >= 5) {
            int nextRC = (rc+1)%HAND_SIZE;
            potentialHand = grid[rc];
            for (int i=0; i<potentialHand.length; i++) {
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
     * Checks a given column number, places the card somewhere in the column,
     * and then checks what kind of hand it gives, and returns the value of
     * the hand.
     * @return A boolean, indicating which direction to move in the tree.
     */
    private boolean checkColumn(Card[][] grid, Card curCard) {
        // put card into the column that we care about
        Card[] potentialHand = new Card[5];
        int handLength = 0;
        for (int i=0; i<potentialHand.length; i++) {
            potentialHand[i] = grid[rc][i];
            if (potentialHand[i] != null) handLength ++;
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
        for (int i=0; i<hand.length; i++) {
            int suit = hand[i].getSuit();
            suitCount[suit]++;
            if (suitCount[suit] > maxSuit) maxSuit = suitCount[suit];
        }
        if (maxSuit == 0) {
            return -101;
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
        if (seqCount == 0) {
            return -101;
        } else {
            return (STRAIGHT_SCORE/(HAND_SIZE-seqCount));
        }
    }

    /**
     * Evaluates the node at this level and returns one of the child nodes.
     * @return A boolean, indicating which direction to move in the tree.
     */
    public boolean evaluate(Card[][] grid, Card curCard) {
        boolean direction = row
        ? checkRow(grid, curCard)
        : checkColumn(grid, curCard);

        return direction;
    }

    /**
     * Retrieves a direct sub-rule from this rule.
     * @param decisions The boolean index of a child rule.
     * 0 (false) is left, 1 (true) is right.
     * @return The node at the specified position.
     */
    Rule getChild(boolean decision) {
        // if (makeDecision) {
        //     return decision ? rightDecision : leftDecision;
        // } else {
        //     return decision ? rightRule : leftRule;
        // }
        return decision ? rightRule : leftRule;
    }


}
