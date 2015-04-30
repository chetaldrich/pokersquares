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
        // TODO: Restructure so decisions can be at end of the tree
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
        // TODO: NEED TO FIGURE OUT HOW ON EARTH WE'RE GOING TO DO THIS
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

    /**
     * Checks a given row number, places the card somewhere in the row,
     * and then checks what kind of hand it gives, and returns the value of
     * the hand.
     * @return A boolean, indicating which direction to move in the tree.
     */
    private boolean checkRow(Card[][] grid, Card curCard) {
        // put card into the row that we care about
        Card[] potentialHand = grid[whichRC];

        // get value of hand
        return true;
    }

    /**
     * Checks a given column number, places the card somewhere in the column,
     * and then checks what kind of hand it gives, and returns the value of
     * the hand.
     * @return A boolean, indicating which direction to move in the tree.
     */
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
        return decision ? right : left;
    }


}
