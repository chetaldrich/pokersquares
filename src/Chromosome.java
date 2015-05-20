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
    private ArrayList<String> identifiers;
    private static AtomicInteger idGenerator = new AtomicInteger();
    private static final Random randomGenerator = new Random();


    public Chromosome() {
        this.identifiers = new ArrayList<String>();
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
        String id1 = this.createID();
        String id2 = this.createID();
        String id3 = this.createID();
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
        addID(head);
        headNode = head;
    }


    public Node getHead() {
        return headNode;
    }


    public void mutate() {
        int index = randomGenerator.nextInt(this.identifiers.size());
        String selectedID = this.identifiers.get(index);
        System.out.println(this.identifiers);
        Node node = findNode(this.getHead(), selectedID);
        if (node == null) return;
        String id;
        String id2;
        String id3;
        if (node instanceof Rule) {
            id = this.createID();
            if (node.getParent() == null) {
                Node mutation = new Rule(system, id);
                this.addID(mutation);
                mutation.setLeft(node.getChild(true));
                mutation.setRight(node.getChild(false));
                this.removeID(node);
                node = null;
            } else {
                Node mutation = new Rule(system, id);
                this.addID(mutation);
                mutation.setLeft(node.getChild(true));
                mutation.setRight(node.getChild(false));
                mutation.setParent(node.getParent());
                this.removeID(node);
                node = null;
            }
        } else if (node instanceof Decision) {
            id = this.createID();
            float value = randomGenerator.nextFloat();
            if (value >= .3) {
                // randomly chose to make a rule.
                if (getDepth(node, node.getID()) == DEPTH_LIMIT) {
                    // if at the depth limit, don't increase depth
                    // make a decision instead
                    Node mutation = new Decision(system, id);
                    this.addID(mutation);
                    mutation.setParent(node.getParent());
                    this.removeID(node);
                    node = null;
                } else {
                    // else, create the rule, increase depth.
                    id2 = this.createID();
                    id3 = this.createID();
                    Node mutation = new Rule(system, id);
                    Node mutationLeft = new Decision(system, id2);
                    Node mutationRight = new Decision(system, id3);
                    this.addID(mutationLeft);
                    this.addID(mutationRight);
                    this.addID(mutation);
                    mutation.setParent(node.getParent());
                    mutation.setLeft(mutationLeft);
                    mutation.setRight(mutationRight);
                    this.removeID(node);
                    node = null;
                }
            } else {
                // randomly chose to make a decision
                id = this.createID();
                Node mutation = new Decision(system, id);
                this.addID(mutation);
                mutation.setParent(node.getParent());
                this.removeID(node);
                node = null;
            }

        }

    }


    private Node findNode(Node node, String id) {
        Deque<Node> a = new ArrayDeque<Node>();
        a.addLast(node);
        while (!a.isEmpty()) {
            Node t = a.removeFirst();
            System.out.println("At node " + t.getID() + " looking for " + id);
            if (t.getID().equals(id)) {
                return t;
            }
            if (t.getChild(true) != null) {
                a.addLast(t.getChild(true));
            }
            if (t.getChild(false) != null){
                a.addLast(t.getChild(false));
            }
        }
        // return null;
        // hmmm.......
        throw new NullPointerException("Node not found.");
    }

    private int getDepthUtil(Node node, String id, int depth) {
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


    public int getDepth(Node node, String id) {
        return getDepthUtil(node, id, 1);
    }


    public static String createID() {
        return String.valueOf(idGenerator.getAndIncrement());
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
