
public interface Node {


    /**
     * @return A String that represents the function or value of this node.
     */
    public String getLabel();


	/**
     * Sets the right child to be a rule
     * @param Node to set reference to.
     */
    public void setRight(Node right);


    /**
     * Sets the left child to be a rule
     * @param Node to set reference to.
     */
    public void setLeft(Node left);


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


    /**
     * Change the parameters within the node
     * sometimes change the children
     * @param type of Node (True = Decision, False = Rule)
     */
    public Node mutate(boolean type);

}
