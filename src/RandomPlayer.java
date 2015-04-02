import java.util.Collections;
import java.util.Stack;

/**
 * RandomPlayer - a simple example implementation of the player interface for PokerSquares that 
 * makes random placements.
 * Author: Todd W. Neller
 */
public class RandomPlayer implements PokerSquaresPlayer {

	/**
	 * a stack of all unchosen plays represented as row-major indices. Row-major indices: play (r, c) is recorded as a single integer r * SIZE + c.
	 * (See <a href="http://en.wikipedia.org/wiki/Row-major_order">http://en.wikipedia.org/wiki/Row-major_order</a>.)
	 */
	private Stack<Integer> plays = new Stack<Integer>(); 
	
	/* (non-Javadoc)
	 * @see PokerSquaresPlayer#setPointSystem(PokerSquaresPointSystem, long)
	 */
	@Override
	public void setPointSystem(PokerSquaresPointSystem system, long millis) {
		// The RandomPlayer cares about as much about the score system as a honey badger.	
	}
	
	/* (non-Javadoc)
	 * @see PokerSquaresPlayer#init()
	 */
	@Override
	public void init() { // Create a stack of a random permutation of 25 play positions (0-24) for random plays
		plays.clear();
		for (int i = 0; i < 25; i++)
			plays.push(i);
		Collections.shuffle(plays); 
	}

	/* (non-Javadoc)
	 * @see PokerSquaresPlayer#getPlay(Card, long)
	 */
	@Override
	public int[] getPlay(Card card, long millisRemaining) {
		int play = plays.pop(); // get the next random position for play
		int[] playPos = {play / 5, play % 5}; // decode it into row and column
		return playPos; // return it
	}

	/* (non-Javadoc)
	 * @see PokerSquaresPlayer#getName()
	 */
	@Override
	public String getName() {
		return "RandomPlayer";
	}

	/**
	 * Demonstrate RandomPlayer play with Ameritish point system.
	 * @param args (not used)
	 */
	public static void main(String[] args) {
		PokerSquaresPointSystem system = PokerSquaresPointSystem.getAmeritishPointSystem();
		System.out.println(system);
		new PokerSquares(new RandomPlayer(), system).play(); // play a single game
	}

}