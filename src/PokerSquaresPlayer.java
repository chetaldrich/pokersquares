
/**
 * PokerSquaresPlayer - a simple player interface for PokerSquares.
 * For each score system, there will be a call to setScoreSystem followed by the calls of one or more games.
 * For each game, there will be a call to init() followed by 25 calls to getPlay(Card, long).
 * Author: Todd W. Neller
 */
public interface PokerSquaresPlayer {
	
	/**
	 * setScoreSystem - provides the player with the point system that will be used in subsequent games 
	 * until a new point system is specified by the same method.  Players can assume that (1) this will be  
	 * the first method called on a PokerSquaresPlayer, and (2) it will be followed immediately by a call  
	 * to init().  The amount of time allotted to the player to prepare for using this system is given by 
	 * parameter <code>millis</code>.  After the given number of milliseconds, or after the player returns 
	 * from this method, the player should be ready to play.  It is permissible to perform such computation
	 * on a separate thread, but the player should be able to play immediately after the return from this 
	 * method or after the allotted time, whichever comes first. 
	 * @param system - new score system
	 * @param millis - number of milliseconds allotted for player processing of the new point system
	 */
	void setPointSystem(PokerSquaresPointSystem system, long millis); 
	
	/**
	 * init - initializes the player before each game
	 */
	void init();
	
	/**
	 * getPlay - gets the current play position for a given card within the allotted number of milliseconds.
	 * Each card passed to getPlay has been drawn from the game deck.
	 * Each legal returned move will be made for the player.
	 * Thus, this method contains all information necessary to maintain current game state for the player.
	 * It is the player's responsibility to record its own moves, which and how many cards have been played, etc.
	 * @param card - card just drawn.
	 * @param millisRemaining - remaining milliseconds for play in the rest of the player's game.
	 * @return a 2D int array with the chosen (row, col) position for play of the given card.
	 */
	int[] getPlay(Card card, long millisRemaining);
	
	
	/**
	 * getName - gets the uniquely identifying name of the Poker Squares player.  The name should be 20 characters or less.
	 * @return unique player name
	 */
	public String getName();
	
}
