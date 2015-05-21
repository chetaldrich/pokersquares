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


    @Override
    public void setPointSystem(PokerSquaresPointSystem system, long millis) {
        this.system = system;
    }


    @Override
    public void init() {
        // reset game board
        for (int row = 0; row < SIZE; row++)
            for (int col = 0; col < SIZE; col++)
                grid[row][col] = null;
        // reset number of plays
        numPlays = 0;
    }

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

    public void setHead(Node head) {
        headNode = head;
    }


    public Node getHead() {
        return headNode;
    }


    public void mutate() {
        int index = randomGenerator.nextInt(this.identifiers.size());
        Integer selectedID = this.identifiers.get(index);
        Node node = findNode(this.getHead(), selectedID);
        if (node instanceof Rule) {
            if (node.getParent() == null) {
                Node mutation = new Rule(system, createID());
                this.addID(mutation);
                mutation.setLeft(node.getChild(false));
                mutation.setRight(node.getChild(true));
                this.setHead(mutation);
                this.removeID(node);
                node = null;
            } else {
                Node mutation = new Rule(system, createID());
                this.addID(mutation);
                mutation.setLeft(node.getChild(false));
                mutation.setRight(node.getChild(true));
                mutation.setParent(node.getParent());
                if (node.getParent().getChild(false).getID().equals(node.getID())) {
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
                    if (node.getParent().getChild(false).getID().equals(node.getID())) {
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
                if (node.getParent().getChild(false).getID().equals(node.getID())) {
                    node.getParent().setLeft(mutation);
                } else {
                    node.getParent().setRight(mutation);
                }
                this.removeID(node);
                node = null;
            }

        }

    }


    private Node findNode(Node node, Integer id) {
        Deque<Node> a = new ArrayDeque<Node>();
        a.addLast(node);
        while (!a.isEmpty()) {
            Node t = a.removeFirst();
            if (t.getID() == id) {
                return t;
            }
            if (t.getChild(true) != null) {
                a.addLast(t.getChild(true));
            }
            if (t.getChild(false) != null){
                a.addLast(t.getChild(false));
            }
        }
        throw new NullPointerException("Node not found.");
    }

    private int getDepthUtil(Node node, Integer id, int depth) {
        if (node == null) {
            return 0;
        }

        if (node.getID().equals(id)) {
            return depth;
        }

        int downlevel = getDepthUtil(node.getChild(false), id, depth + 1);
        if (downlevel != 0) {
            return downlevel;
        }

        downlevel = getDepthUtil(node.getChild(true), id, depth + 1);
        return downlevel;
    }


    public int getDepth(Node node, int id) {
        return getDepthUtil(node, id, 1);
    }


    public static int createID() {
        return idGenerator.getAndIncrement();
    }


    public void addID(Node node) {
        this.identifiers.add(node.getID());
    }

    public void removeID(Node node) {
        this.identifiers.remove(node.getID());
    }


    private void addToGrid(Card card, int[] playPosition) {
        grid[playPosition[0]][playPosition[1]] = card;
    }


    @Override
    public int[] getPlay(Card card, long millisRemaining) {
        int[] playPosition = headNode.evaluate(grid,card);
        addToGrid(card, playPosition);
        return playPosition;
    }


    @Override
    public String getName() {
        return "Chromosome";
    }


}
