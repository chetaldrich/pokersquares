import java.util.Random;
import java.lang.String;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Decision implements Node {

    /**
     * Boolean for row or column property.
     * True = row
     * False = column
     */
    boolean rc;

    /**
     * Stores which function to use for decision.
     */
    String decisionType;
    Random randomGenerator;


    public Decision() {
        decisionType = decideLabel();
        this.rc = rc;
    }

    /**
     * @return A String that determines the type of this decision.
     */
    private String decideLabel() {
        randomGenerator = new Random();
        rc = randomGenerator.nextBoolean();

        int method = randomGenerator.nextInt(8) + 1;
        String label;

            switch (method) {
                case 1: label = "leastCards";
                        break;
                case 2: label = "mostCards";
                        break;
                case 3: label = "mostSuit";
                        break;
                case 4: label = "mostRank";
                        break;
                case 5: label = "extendStraight";
                        break;
                case 6: label = "placeLeft";
                        break;
                case 7: label = "placeTop";
                        break;
                case 8: label = "placeRandom";
                        break;
                default: throw new IllegalStateException(
                                   "Invalid integer: not in 0-8 (Decision)");
            }

        return label;
    }

    /**
     * @return A String that represents the function or value of this node.
     */
    public String getLabel() {
        return rc ? decisionType + ":r" : decisionType + ":c";
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
        if (rc) {
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
     * Determines a position to place a card given
     * a sorted preference list of rows/cols.
     * @return A position in the grid
     */
    private int[] placeCard(Card[][] grid,
                            int[][] preferenceList,
                            Card drawnCard,
                            boolean rc) {
        // loop through each row/col as necessary to find a place
        // to play the card.
        for (int listPosition = 0; listPosition < 5; listPosition++) {
            // check row or column.
            int bestPosition = preferenceList[listPosition][1];
            if (rc) {
                // row
                for (int i = 0; i < 5; i++) {
                    if (grid[bestPosition][i] == null) {
                        int[] position = {bestPosition, i};
                        return position;
                    }
                }
            } else {
                // column
                for (int i = 0; i < 5; i++) {
                    if (grid[i][bestPosition] == null) {
                        int[] position = {i, bestPosition};
                        return position;
                    }
                }
            }
        }
        return placeRandom(grid, drawnCard);
    }

    /**
     * Determines the row/column with the
     * least amount of cards, and gives a free position in that row/column.
     * @return A position in the grid
     */
    private int[] leastCards(Card[][] grid, Card drawnCard) {
        Predicate<Card> isNotCard = (Card card) -> card == null;
        int[][] preferenceList = countCards(grid, isNotCard);
        return placeCard(grid, preferenceList, drawnCard, this.rc);
    }

    /**
     * Determines the row/column with the
     * most cards, and gives a free position in that row/column.
     * @return A position in the grid
     */
    private int[] mostCards(Card[][] grid, Card drawnCard) {
        Predicate<Card> isCard = (Card card) -> card != null;
        int[][] preferenceList = countCards(grid, isCard);
        return placeCard(grid, preferenceList, drawnCard, this.rc);
    }

    /**
     * Determines the row/column with the
     * most of a suit that is common with the drawn card,
     * and gives a free position in that row/column.
     * @return A position in the grid
     */
    private int[] mostSuit(Card[][] grid, Card drawnCard) {
        int currentSuit = drawnCard.getSuit();
        Predicate<Card> isSameSuit =
            (Card card) ->
                card == null ? false : card.getSuit() == currentSuit;
        int[][] preferenceList = countCards(grid, isSameSuit);
        // NOTE: might be worth it to play in a row/col that is empty
        // over a random row to build flushes.
        return placeCard(grid, preferenceList, drawnCard, this.rc);
    }

    /**
     * Determines the row/column with the
     * most of a rank that is common with the drawn card,
     * and gives a free position in that row/column.
     * @return A position in the grid
     */
    private int[] mostRank(Card[][] grid, Card drawnCard) {
        int currentRank = drawnCard.getRank();
        Predicate<Card> isSameRank =
            (Card card) ->
                card == null ? false : card.getRank() == currentRank;
        int[][] preferenceList = countCards(grid, isSameRank);
        // NOTE: might be worth it to play in a row/col that is empty
        // over a random row to build 3 and 4 of a kind.
        return placeCard(grid, preferenceList, drawnCard, this.rc);
    }

    /**
     * Determines the row/column where the card chosen builds the
     * most expedient straight, and gives a free position
     * in that row/column.
     * @return A position in the grid
     */
    private int[] extendStraight(Card[][] grid, Card drawnCard) {
        // TODO: implement
        int[] position = {1,1};
        return position;
    }

    /**
     * Determines the furthest left position that the card can be placed,
     * and then chooses from ties randomly.
     * @return A position in the grid
     */
    private int[] placeLeft(Card[][] grid, Card drawnCard) {
        int[][] preferenceList = {{5, 0}, {4, 1}, {3, 2}, {2, 3}, {1, 4}};
        return placeCard(grid, preferenceList, drawnCard, false);
    }

    /**
     * Determines the furthest position towards the top
     * that the card can be placed, and then chooses from ties randomly.
     * @return A position in the grid
     */
    private int[] placeTop(Card[][] grid, Card drawnCard) {
        int[][] preferenceList = {{5, 0}, {4, 1}, {3, 2}, {2, 3}, {1, 4}};
        return placeCard(grid, preferenceList, drawnCard, true);
    }

    /**
     * Determines a random free position to place the card.
     * @return A position in the grid
     */
    private int[] placeRandom(Card[][] grid, Card drawnCard) {
        List<int[]> freeSpaces = new ArrayList<int[]>();
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (grid[row][col] == null) {
                    int[] newPosition = {row, col};
                    freeSpaces.add(newPosition);
                }
            }
        }

        int decision = randomGenerator.nextInt(freeSpaces.size());
        int[] position = freeSpaces.get(decision);
        return position;
    }


    /**
     * Evaluates the decision at this level and returns a position
     * on the grid to place the card.
     * @return A boolean, indicating which direction to move in the tree.
     */
    public int[] evaluate(Card[][] grid, Card drawnCard) {
        switch (decisionType) {
            case "leastCards":
                return leastCards(grid, drawnCard);
            case "mostCards":
                return mostCards(grid, drawnCard);
            case "mostSuit":
                return mostSuit(grid, drawnCard);
            case "mostRank":
                return mostRank(grid, drawnCard);
            // case "extendStraight":
            //     // needs to be implemented
            //     return extendStraight(grid, drawnCard);
            case "placeLeft":
                return placeLeft(grid, drawnCard);
            case "placeTop":
                return placeTop(grid, drawnCard);
            case "placeRandom":
                return placeRandom(grid, drawnCard);
            default:
                return placeRandom(grid, drawnCard);
        }
    }

    /**
     * Changes, with a random probability, this decision into
     * another type, or changes it into a rule and gives it two
     * decision nodes below it.
     */
    public void mutate() {

    }


    /**
     * Sets the right child
     *
     */
    public void setRight(Node right) {}


    /**
     * Sets the left child
     *
     */
    public void setLeft(Node left) {}

    /**
     * Retrieves a direct sub-rule from this rule.
     * @param decisions The boolean index of a child rule.
     * 0 (false) is left, 1 (true) is right.
     * @return The node at the specified position.
     */
    public Node getChild(boolean direction) {
        return null;
    }
}
