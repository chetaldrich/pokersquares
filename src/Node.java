
public interface Node extends Cloneable {

    /**
     * @return A String that represents the function or value of this node.
     */
    public String getLabel();


    /**
     * @return A unique String identifier for this node.
     */
    public Integer getID();


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
     * Gets this node's left child.
     * @return Node object
     */
    public Node getLeftChild();


    /**
     * Gets this node's right child.
     * @return Node object
     */
    public Node getRightChild();


    /**
     * Gets the parent node for this node.
     */
    public Node getParent();


    /**
     * Sets the parent node for this node.
     */
    public void setParent(Node parent);


    /**
     * Adds clonability to Node objects.
     */
    public Object clone() throws CloneNotSupportedException;

}
