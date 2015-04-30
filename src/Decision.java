import java.util.Random;

public class Decision {

    public Decision() {

    }

    /**
     * @return A String that represents the function or value of this node.
     */
    public String getLabel() {
        String label = "Label based on type of decision";
        return label;
    }


    /**
     * Evaluates the decision at this level and returns a position on the grid
     * to place the card.
     * @return A boolean, indicating which direction to move in the tree.
     */
    public int[] evaluate() {
        int[] position = {1,1};
        return position;
    }

    



}
