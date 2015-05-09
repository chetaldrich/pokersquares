import java.util.*;

public class GeneticPlayer implements PokerSquaresPlayer {

    private final int SIZE = 5; // grid dimension
    private Card[][] grid = new Card[SIZE][SIZE]; // Card grid
    private int numPlays = 0; // number of Cards played into the grid so far
    private PokerSquaresPointSystem system; // point system
    private Node headNode;



    public GeneticPlayer() {
    }


    @Override
    public void setPointSystem(PokerSquaresPointSystem system, long millis) {
        this.system = system;
        headNode = new Rule(system);
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

    public Node getHead() {
        return headNode;
    }


    private void addToGrid(Card card, int[] playPosition) {
        grid[playPosition[0]][playPosition[1]] = card;
    }


    public Card[][] getGrid() {
        return grid;
    }


    @Override
    public int[] getPlay(Card card, long millisRemaining) {
        // TODO: actually start picking moves;
        // only picks a single move.
        int[] playPosition = headNode.evaluate(grid,card);
        addToGrid(card, playPosition);
        return playPosition;
    }


    @Override
    public String getName() {
        return "GeneticPlayer";
    }


    public static void main(String[] args) {
        PokerSquaresPointSystem system = PokerSquaresPointSystem.getAmericanPointSystem();
        System.out.println(system);

        GeneticPlayer gp = new GeneticPlayer(system);
        // Node head = gp.getHead();
        // head.setRight(new Rule(system));
        // head.setLeft(new Rule(system));
        PokerSquares ps = new PokerSquares(gp, system);
        Node head = gp.getHead();
        head.setRight(new Rule(system));
        head.setLeft(new Rule(system));
        ps.playSequence(100, 0, false);
        // System.out.println(head.getLabel()+" "+head.getChild(true).getLabel()+" "+head.getChild(false).getLabel());
        
        RandomPlayer rp = new RandomPlayer();
        PokerSquares ps2 = new PokerSquares(rp, system);
        // ps2.playSequence(1000,0,false);
        // int max = -101;
        // int sum = 0;
        // int[] scores = new int[NUM_GAMES];
        // for (int i=0; i<NUM_GAMES; i++) {
        //     // play a single game
        //     GeneticPlayer gp = new GeneticPlayer(system);
        //     gp.getHead().setRight(new Rule(system));
        //     gp.getHead().setLeft(new Rule(system));
        //     PokerSquares ps = new PokerSquares(gp, system);
        //     scores[i] = ps.play();
        //     if (scores[i]>max) max = scores[i];
        //     sum += scores[i];
        // }
        // double ave = 1.0*sum/NUM_GAMES;
        // System.out.println("Average score: " + ave + " Best: " + max);
    }

}
