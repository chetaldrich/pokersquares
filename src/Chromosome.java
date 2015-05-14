import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Chromosome implements PokerSquaresPlayer {

    private final int SIZE = 5; // grid dimension
    private Card[][] grid = new Card[SIZE][SIZE]; // Card grid
    private int numPlays = 0; // number of Cards played into the grid so far
    private PokerSquaresPointSystem system; // point system
    private Node headNode;
    private ArrayList<String> identifiers;
    private static AtomicInteger idGenerator = new AtomicInteger();


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
