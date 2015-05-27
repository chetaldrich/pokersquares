import java.util.*;

public class GeneticPlayer implements PokerSquaresPlayer {

    private final int SIZE = 5; // grid dimension
    private final double SETUP_PROP = 0.2;
    private Card[][] grid = new Card[SIZE][SIZE]; // Card grid
    private int numPlays = 0; // number of Cards played into the grid so far
    private PokerSquaresPointSystem system; // point system
    private Node headNode;



    public GeneticPlayer() {
    }


    @Override
    public void setPointSystem(PokerSquaresPointSystem system, long millis) {
        this.system = system;

        ChromosomeFactory chrome = new ChromosomeFactory(system);
        chrome.createChromosomes();
        long startTime = System.currentTimeMillis();
        int count = 1;
        while((System.currentTimeMillis()-startTime) < SETUP_PROP*millis) {
            System.out.println("Generation: " + count);
            count++;
            chrome.selectNextGeneration();
            chrome.mutateAll();
            chrome.crossOver();
        }
        Chromosome best = chrome.selectNextGeneration();
        headNode = best.getHead();
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


    private void addToGrid(Card card, int[] playPosition) {
        grid[playPosition[0]][playPosition[1]] = card;
    }


    public Card[][] getGrid() {
        return grid;
    }


    @Override
    public int[] getPlay(Card card, long millisRemaining) {
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

        GeneticPlayer gp = new GeneticPlayer();
        PokerSquares ps = new PokerSquares(gp, system);

        ps.playSequence(50,0,false);

    }

}
