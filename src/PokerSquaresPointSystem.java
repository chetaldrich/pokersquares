import java.util.Random;


/**
 * @author tneller
 *
 */
public class PokerSquaresPointSystem {
	private static final Random random = new Random(); // pseudorandom number generator
	private static final int MAX_HAND_SCORE = 127; // maximum permissible hand score
	private static final int MIN_HAND_SCORE = -128; // maximum permissible hand score
	private static final int SIZE = PokerSquares.SIZE; // Poker Squares number of rows/columns
	private final int[] scores; // scores for each corresponding hand classification id number
	
	/**
	 * Create a point system given an array of hand scores. The score at index n corresponds to the score for 
	 * hand classification number n.  These will be truncated to be between the minimum and maximum allowable 
	 * hand scores.
	 * @param scores given hand scores
	 */
	public PokerSquaresPointSystem(int[] scores) {
		for (int i = 0; i < scores.length; i++)
			scores[i] = Math.max(MIN_HAND_SCORE, Math.min(MAX_HAND_SCORE, scores[i]));
		this.scores = scores;
	}
	
	/**
	 * Get the score of the given Card hand (which may contain null values).
	 * @param hand Card hand
	 * @return score of given Card hand.
	 */
	public int getHandScore(Card[] hand) {
		return scores[PokerHand.getPokerHandId(hand)];
	}
	
	/**
	 * Get the score associated with the given Poker hand classification.
	 * @param pokerHand Poker hand classification
	 * @return score associated with the given Poker hand classification
	 */
	public int getHandScore(PokerHand pokerHand) {
		return scores[pokerHand.id];
	}
	
	/**
	 * Get the score associated with the given Poker hand classification identification number.
	 * @param pokerHandId Poker hand classification identification number
	 * @return score associated with the given Poker hand classification identification number.
	 */
	public int getHandScore(int pokerHandId) {
		return scores[pokerHandId];
	}
	
	/**
	 * Return an array of scores indexed by Poker hand classification identification numbers.
	 * @return an array of scores indexed by Poker hand classification identification numbers
	 */
	public int[] getScoreTable() {
		return scores.clone();
	}
	
	/**
	 * Get the score of the given Card grid.
	 * @param grid Card grid
	 * @return score of given Card grid
	 */
	public int getScore(Card[][] grid) {
		int[] handScores = getHandScores(grid);
		int totalScore = 0;
		for (int handScore : handScores)
			totalScore += handScore;
		return totalScore;
	}
	
	/**
	 * Get an int array with the individual hand scores of rows 0 through 4 followed by columns 0 through 4. 
	 * @param grid 2D Card array representing play grid
	 * @return an int array with the individual hand scores of rows 0 through 4 followed by columns 0 through 4. 
	 */
	public int[] getHandScores(Card[][] grid) {
		int[] handScores = new int[2 * SIZE];
		for (int row = 0; row < SIZE; row++) {
			Card[] hand = new Card[SIZE];
			for (int col = 0; col < SIZE; col++)
				hand[col] = grid[row][col];
			handScores[row] = getHandScore(hand);
		}
		for (int col = 0; col < SIZE; col++) {
			Card[] hand = new Card[SIZE];
			for (int row = 0; row < SIZE; row++)
				hand[row] = grid[row][col];
			handScores[SIZE + col] = getHandScore(hand);
		}
		return handScores;
	}
	
	/**
	 * Set the seed of the game pseudorandom number generator.
	 * @param seed pseudorandom number generator seed
	 */
	public static void setSeed(long seed) {
		random.setSeed(seed);
	}
	
	/**
	 * Return a random point system with scores uniformly distributed across the range of all allowable values.
	 * @return a random point system with scores uniformly distributed across the range of all allowable values
	 */
	public static PokerSquaresPointSystem getRandomPointSystem() {
		int[] scores = new int[PokerHand.NUM_HANDS];
		for (int i = 0; i < PokerHand.NUM_HANDS; i++)
			scores[i] = random.nextInt(MAX_HAND_SCORE - MIN_HAND_SCORE + 1) + MIN_HAND_SCORE;
		return new PokerSquaresPointSystem(scores);
	}
	
	/**
	 * Return a random hypercorner point system where hand scores are either -1 or 1 with equal probability.
	 * @return a random hypercorner point system where hand scores are either -1 or 1 with equal probability
	 */
	public static PokerSquaresPointSystem getHypercornerPointSystem() {
		int[] scores = new int[PokerHand.NUM_HANDS];
		for (int i = 0; i < PokerHand.NUM_HANDS; i++)
			scores[i] = random.nextBoolean() ? 1 : -1;
		return new PokerSquaresPointSystem(scores);
	}
	
	/**
	 * Return a point system where only the one Poker hand (given by Poker hand classification identification number)
	 * scores 1 point.  All other hands score nothing. 
	 * @param pokerHandId Poker hand classification identification number
	 * @return a point system where only the one Poker hand (given by Poker hand classification identification number)
	 * scores 1 point
	 */
	public static PokerSquaresPointSystem getSingleHandPointSystem(int pokerHandId) {
		int[] scores = new int[PokerHand.NUM_HANDS];
		scores[pokerHandId] = 1;
		return new PokerSquaresPointSystem(scores);
	}
	
	/**
	 * Return a point system where only one randomly selected Poker hand scores 1 point.  
	 * All other hands score nothing. 
	 * @return a point system where only one randomly selected Poker hand scores 1 point
	 */
	public static PokerSquaresPointSystem getSingleHandPointSystem() {
		return getSingleHandPointSystem(random.nextInt(PokerHand.NUM_HANDS));
	}
	
	/**
	 * Return the American point system.
	 * @return the American point system
	 */
	public static PokerSquaresPointSystem getAmericanPointSystem() {
		return new PokerSquaresPointSystem(new int[] {0, 2, 5, 10, 15, 20, 25, 50, 75, 100});
	}
	
	/**
	 * Return the British point system. Also known as the British or U.K. point system.
	 * @return the British point system
	 */
	public static PokerSquaresPointSystem getBritishPointSystem() {
		return new PokerSquaresPointSystem(new int[] {0, 1, 3, 6, 12, 5, 10, 16, 30, 30});
	}
	
	/**
	 * Return an "Ameritish" point system randomly generated between the normalized point ranges established by the American and British systems.
	 * @return an "Ameritish" point system randomly generated between the normalized point ranges established by the American and British systems
	 */
	public static PokerSquaresPointSystem getAmeritishPointSystem() {
		int[] american = {0, 2, 5, 10, 15, 20, 25, 50, 75, 100};
		int[] british = {0, 1, 3, 6, 12, 5, 10, 16, 30, 30};
		int[] ameritish = new int[10];
		ameritish[9] = random.nextInt(71) + 30; // random top score between two systems
		for (int i = 1; i < 9; i++) {
			double fracA = (double) american[i] / american[9];
			double fracB = (double) british[i] / british[9];
			double maxFrac = (fracA > fracB) ? fracA : fracB;
			double minFrac = (fracA > fracB) ? fracB : fracA;
			ameritish[i] = (int) Math.round(ameritish[9] * (random.nextDouble() * (maxFrac - minFrac) + minFrac));
		}
		return new PokerSquaresPointSystem(ameritish);
	}
	
	/**
	 * Print the given game grid and score.
	 * @param grid given game grid 
	 */
	public void printGrid(Card[][] grid) {
		// get scores
		int[] handScores = getHandScores(grid);
		int totalScore = 0;
		for (int handScore : handScores)
			totalScore += handScore;
		
		// print grid
		for (int row = 0; row < SIZE; row++) {
			for (int col = 0; col < SIZE; col++) 
				System.out.printf(" %s ", grid[row][col] == null ? "--" : grid[row][col].toString());
			System.out.printf("%3d\n", handScores[row]);
		}
		for (int col = 0; col < SIZE; col++) 
			System.out.printf("%3d ", handScores[SIZE + col]);
		System.out.printf("%3d Total\n", totalScore);
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		// Represent the point system in a text table
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%-15s %s\n", "Hand Name", "Points"));
		for (PokerHand hand : PokerHand.values())
			sb.append(String.format("%-15s %4d\n", hand.name, getHandScore(hand)));
		return sb.toString();
	}
	
	/**
	 * Demonstrate the different point systems that may be generated.
	 * @param args (not used)
	 */
	public static void main(String[] args) {
		setSeed(42); // (Different seeds yield different point systems.)
		System.out.println(getRandomPointSystem());
		System.out.println(getHypercornerPointSystem());
		System.out.println(getSingleHandPointSystem(PokerHand.THREE_OF_A_KIND.id));
		System.out.println(getSingleHandPointSystem());
		System.out.println(getAmericanPointSystem());
		System.out.println(getBritishPointSystem());
		System.out.println(getAmeritishPointSystem());
	}
}
