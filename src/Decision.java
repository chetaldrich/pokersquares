import java.util.Random;
import java.lang.String;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.List;
import java.util.Arrays;
import java.util.Comparator;

public class Decision {

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
     * Determines the row/column with the
     * least amount of cards, and gives a free position in that row/column.
     * @return A position in the grid
     */
    private int[] leastCards(Card[][] grid) {
        Predicate<Card> isNotCard = (Card card) -> card == null;
        int[][] rcList = countCards(grid, isNotCard);

        // loop through each row/col as necessary to find a place
        // to play the card.
        for (int listPosition = 0; listPosition < 5; listPosition++) {
            // check row or column.
            int bestPosition = rcList[listPosition][1];
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
        return placeRandom();
    }

    /**
     * Determines the row/column with the
     * most cards, and gives a free position in that row/column.
     * @return A position in the grid
     */
    private int[] mostCards(Card[][] grid) {
        Predicate<Card> isCard = (Card card) -> card != null;
        int[][] rcList = countCards(grid, isCard);

        // loop through each row/col as necessary to find a place
        // to play the card.
        for (int listPosition = 0; listPosition < 5; listPosition++) {
            // check row or column.
            int bestPosition = rcList[listPosition][1];
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
        return placeRandom();
    }

    /**
     * Determines the row/column with the
     * most of a suit that is common with the drawn card,
     * and gives a free position in that row/column.
     * @return A position in the grid
     */
    private int[] mostSuit() {
        // TODO: implement
        int[] position = {1,1};
        return position;
    }

    /**
     * Determines the row/column with the
     * most of a rank that is common with the drawn card,
     * and gives a free position in that row/column.
     * @return A position in the grid
     */
    private int[] mostRank() {
        // TODO: implement
        int[] position = {1,1};
        return position;
    }

    /**
     * Determines the row/column where the card chosen builds the
     * most expedient straight, and gives a free position
     * in that row/column.
     * @return A position in the grid
     */
    private int[] extendStraight() {
        // TODO: implement
        int[] position = {1,1};
        return position;
    }

    /**
     * Determines the furthest left position that the card can be placed,
     * and then chooses from ties randomly.
     * @return A position in the grid
     */
    private int[] placeLeft() {
        // TODO: implement
        int[] position = {1,1};
        return position;
    }

    /**
     * Determines the furthest position towards the top
     * that the card can be placed, and then chooses from ties randomly.
     * @return A position in the grid
     */
    private int[] placeTop() {
        // TODO: implement
        int[] position = {1,1};
        return position;
    }

    /**
     * Determines a random free position to place the card.
     * @return A position in the grid
     */
    private int[] placeRandom() {
        // TODO: implement
        int[] position = {1,1};
        return position;
    }


    /**
     * Evaluates the decision at this level and returns a position
     * on the grid to place the card.
     * @return A boolean, indicating which direction to move in the tree.
     */
    public int[] evaluate(Card[][] grid) {
        int[] position = {1,1};
        return position;
    }


}
