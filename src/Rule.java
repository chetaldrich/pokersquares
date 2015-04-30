import java.util.Random;

public class Rule {

    private final Rule condition;
    private final Rule left;
    private final Rule right;
    Random randy;
    boolean row;
    int whichRC;
    int pointThresh;


    public Rule() {

        // initialize the rule nodes
        condition = new Rule();
        left = new Rule();
        right = new Rule();

        randy = new Random();
        // determine if checking row or column
        boolean randBool = randy.nextBoolean();
        row = randBool;

        // which row or column to check
        int randInt = randy.nextInt(5);
        whichRC = randInt;
        // at what point threshold (for a hand) should the thingy go left/right
        // NEED TO FIGURE OUT HOW ON EARTH WE'RE GOING TO DO THIS
        // PROBABLY SOMETHING WITH MEDIANS AND WHATNOT
        // IDK IT'S CRAZY
        pointThresh = 0;
    }


    /**
     * @return A String that represents the function or value of this node.
     */
    String getLabel() {
        String label = "Label based on row/col, rank/straight/suit";
        return label;
    }


    private boolean checkRow(Card[][] grid, Card curCard) {
        // put card into the row that we care about
        Card[] potentialHand = grid[whichRC];

        // get value of hand
        return true;

    }

    private boolean checkColumn(Card[][] grid, Card curCard) {
        // put card into the column that we care about
        Card[] potentialHand;

        // get value of hand
        return true;

    }

    /**
     * Evaluates the node at this level and returns one of the child nodes.
     * @return A boolean, indicating which direction to move in the tree.
     */
    public boolean evaluate(Card[][] grid, Card curCard) {
        boolean direction;
        if (row) {
            direction = checkRow(grid, curCard);
        } else {
            direction = checkColumn(grid, curCard);
        }
        return direction;
    }

    /**
     * Retrieves a direct sub-rule from this rule.
     * @param index The index of a child rule. 0 is left, 1 is right.
     * @return The node at the specified position.
     */
    Rule getChild(boolean decision) {
        if (decision) {
            return right;
        } else {
            return left;
        }
    }


}
