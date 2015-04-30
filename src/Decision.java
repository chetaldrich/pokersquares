import java.util.Random;

public class Decision {

    public Decision() {

    }

    /**
     * @return A String that determines the type of this decision.
     */
    private String decideLabel() {
        String label = "label to be determined randomly";
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
