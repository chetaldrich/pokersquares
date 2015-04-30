import java.util.Random;
import java.lang.String;

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
        boolean rc = randomGenerator.nextBoolean();

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
        //TODO: implement
        String label = "Label based on type of decision";
        return label;
    }

    /**
     * Determines the row/column with the
     * least amount of cards, and gives a free position in that row/column.
     * @return A position in the grid
     */
    private int[] leastCards() {
        // TODO: implement
        int[] position = {1,1};
        return position;
    }

    /**
     * Determines the row/column with the
     * most cards, and gives a free position in that row/column.
     * @return A position in the grid
     */
    private int[] mostCards() {
        // TODO: implement
        int[] position = {1,1};
        return position;
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
    public int[] evaluate() {
        int[] position = {1,1};
        return position;
    }


}
