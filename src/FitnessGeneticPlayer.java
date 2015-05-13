import java.util.*;

public class FitnessGeneticPlayer implements PokerSquaresPlayer {

    private final int SIZE = 5; // grid dimension
    private Card[][] grid = new Card[SIZE][SIZE]; // Card grid
    private int numPlays = 0; // number of Cards played into the grid so far
    private PokerSquaresPointSystem system; // point system
    private Node headNode;



    public FitnessGeneticPlayer() {
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

    public void setHead(Node head) {
        headNode = head;
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
        int[] playPosition = headNode.evaluate(grid,card);
        addToGrid(card, playPosition);
        return playPosition;
    }


    @Override
    public String getName() {
        return "FitnessGeneticPlayer";
    }


    public static void main(String[] args) {
    }

}
