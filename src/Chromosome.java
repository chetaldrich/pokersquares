import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.lang.NullPointerException;

public class Chromosome implements PokerSquaresPlayer {

    private final int SIZE = 5; // grid dimension
    private final int DEPTH_LIMIT = 6;
    private Card[][] grid = new Card[SIZE][SIZE]; // Card grid
    private int numPlays = 0; // number of Cards played into the grid so far
    private PokerSquaresPointSystem system; // point system
    private Node headNode;
    private ArrayList<Integer> identifiers;
    private static AtomicInteger idGenerator = new AtomicInteger();
    private static final Random randomGenerator = new Random();


    public Chromosome() {
        this.identifiers = new ArrayList<Integer>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPointSystem(PokerSquaresPointSystem system, long millis) {
        this.system = system;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        // reset game board
        for (int row = 0; row < SIZE; row++)
            for (int col = 0; col < SIZE; col++)
                grid[row][col] = null;
        // reset number of plays
        numPlays = 0;
    }

    /**
     * Creates a beginning tree with three nodes for this tree.
     */
    public void createChromosome() {
        int id1 = createID();
        int id2 = createID();
        int id3 = createID();
        Node gene = new Rule(system, id1);
        Node left = new Decision(system, id2);
        Node right = new Decision(system, id3);
        this.addID(gene);
        this.addID(left);
        this.addID(right);
        gene.setLeft(left);
        gene.setRight(right);
        this.setHead(gene);
    }


    /**
     * Sets the head of the tree for this chromosome to this node.
     */
    public void setHead(Node head) {
        headNode = head;
    }


    /**
     * Returns the head of this node.
     * @return Node object representing the root of the tree.
     */
    public Node getHead() {
        return headNode;
    }

    /**
     * Mutates a random node in this chromosome.
     */
    public void mutate() {
        int index = randomGenerator.nextInt(this.identifiers.size());
        Integer selectedID = this.identifiers.get(index);
        Node node = findNode(this.getHead(), selectedID);
        if (node instanceof Rule) {
            if (node.getParent() == null) {
                Node mutation = new Rule(system, createID());
                this.addID(mutation);
                mutation.setLeft(node.getLeftChild());
                mutation.setRight(node.getRightChild());
                this.setHead(mutation);
                this.removeID(node);
                node = null;
            } else {
                Node mutation = new Rule(system, createID());
                this.addID(mutation);
                mutation.setLeft(node.getLeftChild());
                mutation.setRight(node.getRightChild());
                mutation.setParent(node.getParent());
                if (node.getParent().getLeftChild().getID().equals(node.getID())) {
                    node.getParent().setLeft(mutation);
                } else {
                    node.getParent().setRight(mutation);
                }
                this.removeID(node);
                node = null;
            }
        } else if (node instanceof Decision) {
            float value = randomGenerator.nextFloat();
            if (value >= .3) {
                // randomly chose to make a rule.
                if (getDepth(node, node.getID()) == DEPTH_LIMIT) {
                    // if at the depth limit, don't increase depth
                    // make a decision instead
                    Node mutation = new Decision(system, createID());
                    this.addID(mutation);
                    mutation.setParent(node.getParent());
                    this.removeID(node);
                    node = null;
                } else {
                    // else, create the rule, increase depth.
                    Node mutation = new Rule(system, createID());
                    Node mutationLeft = new Decision(system, createID());
                    Node mutationRight = new Decision(system, createID());
                    this.addID(mutationLeft);
                    this.addID(mutationRight);
                    this.addID(mutation);
                    mutation.setParent(node.getParent());
                    if (node.getParent().getLeftChild().getID().equals(node.getID())) {
                        node.getParent().setLeft(mutation);
                    } else {
                        node.getParent().setRight(mutation);
                    }
                    mutation.setLeft(mutationLeft);
                    mutation.setRight(mutationRight);
                    this.removeID(node);
                    node = null;
                }
            } else {
                // randomly chose to make a decision
                Node mutation = new Decision(system, createID());
                this.addID(mutation);
                mutation.setParent(node.getParent());
                if (node.getParent().getLeftChild().getID().equals(node.getID())) {
                    node.getParent().setLeft(mutation);
                } else {
                    node.getParent().setRight(mutation);
                }
                this.removeID(node);
                node = null;
            }

        }

    }


    /**
     * Finds a node in the tree and returns the node.
     * @param Node root of the tree
     * @param Integer id value for the node
     * @return Node object
     */
    private Node findNode(Node node, Integer id) {
        Deque<Node> a = new ArrayDeque<Node>();
        a.addLast(node);
        while (!a.isEmpty()) {
            Node t = a.removeFirst();
            if (t.getID() == id) {
                return t;
            }
            if (t.getRightChild() != null) {
                a.addLast(t.getRightChild());
            }
            if (t.getLeftChild() != null){
                a.addLast(t.getLeftChild());
            }
        }
        throw new NullPointerException("Node not found.");
    }


    /**
     * A helper function for determining the depth of a particular node.
     * @param node: the root of the tree
     * @param id: the id number of the desired node in the tree
     * @param depth: recursive depth parameter
     * @return integer depth of the node in the tree
     */
    private int getDepthUtil(Node node, Integer id, int depth) {
        if (node == null) {
            return 0;
        }

        if (node.getID().equals(id)) {
            return depth;
        }

        int downlevel = getDepthUtil(node.getLeftChild(), id, depth + 1);
        if (downlevel != 0) {
            return downlevel;
        }

        downlevel = getDepthUtil(node.getRightChild(), id, depth + 1);
        return downlevel;
    }

    /**
     * Determines the depth of a particular node.
     * @param node: the root of the tree
     * @param id: the id number of the desired node in the tree
     * @return integer depth of the node in the tree
     */
    public int getDepth(Node node, int id) {
        return getDepthUtil(node, id, 1);
    }


    /**
     * Creates an id and returns it.
     * @return Integer id number
     */
    public static int createID() {
        return idGenerator.getAndIncrement();
    }


    /**
     * Adds an id number to the Chromosome instance.
     * @param Node node: the node from which to take the id
     */
    public void addID(Node node) {
        this.identifiers.add(node.getID());
    }


    /**
     * Removes an id number from the Chromosome instance.
     * @param Node node: the node from which to get the id to remove
     */
    public void removeID(Node node) {
        this.identifiers.remove(node.getID());
    }


    /**
     * {@inheritDoc}
     */
    private void addToGrid(Card card, int[] playPosition) {
        grid[playPosition[0]][playPosition[1]] = card;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int[] getPlay(Card card, long millisRemaining) {
        int[] playPosition = headNode.evaluate(grid,card);
        addToGrid(card, playPosition);
        return playPosition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "Chromosome";
    }


}
