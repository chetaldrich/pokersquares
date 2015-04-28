import java.util.Random;

public interface Node {

    /**
     * @return A short String that represents the function or value represented by this node.
     */
    String getLabel();


    /**
     * If this is a function (non-leaf) node, how many arguments does it take?  * For leaf nodes the answer is zero.
     * @return The arity of this function, or zero if this node is a leaf node.
     */
    int getArity();


    /**
     * Evaluates the node at this level and returns one of the child nodes.
     * @return A child node of this node.
     */
    Node evaluate(int[] programParameters);


    /**
     * Retrieves a direct sub-node from this tree.
     * @param index The index of a child node. 0 is left, 1 is right.
     * @return The node at the specified position.
     */
    Node getChild(int index);


    /**
     * Returns a new tree that is identical to this tree except with the
     * specified node replaced.
     * @param index The index of the node to replace.
     * @param newNode The replacement node.
     * @return A new tree with the node at the specified index replaced
     * with the new node.
     */
    Node replaceNode(int index, Node newNode);


    /**
     * A method to create a mutation at this node.
     * @param mutationProbability The probability that a given node will be mutated.
     * @return The mutated node (or the same node if no mutation occurred).
     */
    Node mutate(float mutationProbability);


}
