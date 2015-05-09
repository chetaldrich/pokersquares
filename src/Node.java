
public interface Node {

	/**
     * Sets the right child to be a rule
     *
     */
    public void setRight(Node right);


    /**
     * Sets the left child to be a rule
     *
     */
    public void setLeft(Node left);


    /**
     * @return A String that represents the function or value of this node.
     */
    public String getLabel();


    /**
     * Evaluates the node at this level and returns one of the child nodes.
     * @return A boolean, indicating which direction to move in the tree.
     */
    public int[] evaluate(Card[][] grid, Card curCard);

    /**
     * Retrieves a direct sub-rule from this rule.
     * @param decisions The boolean index of a child rule.
     * 0 (false) is left, 1 (true) is right.
     * @return The node at the specified position.
     */
    public Node getChild(boolean direction);

}