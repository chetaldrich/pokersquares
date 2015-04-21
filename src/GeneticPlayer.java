

public class GeneticPlayer implements PokerSquaresPlayer {



    public GeneticPlayer() {}


    @Override
    public void setPointSystem(PokerSquaresPointSystem system, long millis) {

    }


    @Override
    public void init() {

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
