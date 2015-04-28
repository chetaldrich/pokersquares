

public class GeneticPlayer implements PokerSquaresPlayer {

    private final int SIZE = 5; // grid dimension
    private Card[][] grid = new Card[SIZE][SIZE]; // Card grid
    private int numPlays = 0; // number of Cards played into the grid so far
    // private PokerSquaresPointSystem system; // point system



    public GeneticPlayer() {}


    @Override
    public void setPointSystem(PokerSquaresPointSystem system, long millis) {

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

    @Override
    public int[] getPlay(Card card, long millisRemaining) {
        // TODO: actually start picking moves;
        // only picks a single move.

        
        int[] playPosition = {1, 1};
        return playPosition;
    }


    @Override
    public String getName() {
        return "GeneticPlayer";
    }



    public static void main(String[] args) {
        PokerSquaresPointSystem system = PokerSquaresPointSystem.getAmeritishPointSystem();
        System.out.println("hello");
        System.out.println(system);

        // play a single game
        new PokerSquares(new GeneticPlayer(), system).play();

    }

}
