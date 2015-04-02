import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

/* A parameterized version of the solitaire game "Poker Squares" with variable point systems.
 * Author: Todd W. Neller

Notes: 

A Poker Squares grid is represented as a 5-by-5 array of Card objects.  A null indicates an empty position.
In the solitaire game of Poker Squares, a deck is initially shuffled.
Each turn, the player draws a card from the deck and places it in any empty cell of a 5-by-5 grid.  Once placed, cards may not be moved.
After the last cell is filled, each row and column are scored according to a point system for Poker Squares hands.
For example, the American system is as follows:
100 - Royal Flush: A T-J-Q-K-A sequence all of the same suit. Example: TC, JC, QC, KC, AC.
75 - Straight Flush: Five cards in sequence all of the same suit. Example: AD, 2D, 3D, 4D, 5D.
50 - Four of a Kind: Four cards of the same rank. Example: 9C, 9D, 9H, 9S, 6H.
25 - Full House: Three cards of one rank with two cards of another rank. Example: 7S, 7C, 7D, 8H, 8S.
20 - Flush: Five cards all of the same suit. Example: AH, 2H, 3H, 5H, 8H.
15 - Straight: Five cards in sequence. Aces may be high or low but not both. (Straights do not wrap around.) Example: 8C, 9S, TH, JD, QC.
10 - Three of a Kind: Three cards of the same rank. Example: 2S, 2H, 2D, 5C, 7S.
5 - Two Pair: Two cards of one rank with two cards of another rank. Example: 3H, 3D, 4C, 4S, AC.
2 - One Pair: Two cards of one rank.  Example: 5D, 5H, TC, QS, AH.
(0 otherwise)

The player's total score is the sum of the scores for each of the 10 row and column hands.

Relevant Resources: http://tinyurl.com/pokersqrs

For our purposes, a player is considered better if it has a higher expected game score, i.e. has a higher score average over many games.

In our implementation, each turn a PokerSquaresPlayer will be passed (1) a Card object and (2) the number of milliseconds remaining in the game, 
and will return a length 2 integer array with the row and column the player placed the card.  In the event that the player makes an illegal 
play or "times out", i.e. runs out of time for play, the player loses with a final score of 10 times the minimum hand score.

This file contains not only the code to run a simple demonstration game with a random player, but also code to perform batch game testing,
and tournament evaluation.

 */

public class PokerSquares {
	
	public static final int SIZE = 5; // square grid size
	public static final long POINT_SYSTEM_MILLIS = 300000L; // EAAI-2016 contest maximum milliseconds for processing score table
	public static final long GAME_MILLIS = 30000L; // EAAI-2016 contest maximum milliseconds per game

	private PokerSquaresPlayer player; // current player
	private PokerSquaresPointSystem system; // current point system
	private long gameMillis = GAME_MILLIS; // maximum milliseconds for current game
	private boolean verbose = true; // whether or not to print move-by-move transcript of the game
	private Card[][] grid = new Card[SIZE][SIZE]; // current game grid
	private Random random = new Random(); // current game random number generator
	private int minPoints; // minimum possible score for current point system.
	
	/**
	 * Create a PokerSquares game with a given player and point system.
	 * @param player Poker Squares player object
	 * @param system current Poker Squares point system
	 */
	public PokerSquares(PokerSquaresPlayer player, PokerSquaresPointSystem system) {
		this.player = player;
		this.system = system;
		minPoints = Integer.MAX_VALUE;
		for (int points : system.getScoreTable())
			if (points < minPoints)
				minPoints = points;
		minPoints *= 10;
		final PokerSquaresPlayer PLAYER = player;
		final PokerSquaresPointSystem SYSTEM = system;
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				PLAYER.setPointSystem(SYSTEM, POINT_SYSTEM_MILLIS);
			}
		});
		thread.start();
		try {
			thread.join(POINT_SYSTEM_MILLIS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Play a game of Poker Squares and return the final game score.
	 * @return final game score
	 */
	public int play() {
		player.init();

		// shuffle deck
		Stack<Card> deck = new Stack<Card>();
		for (Card card : Card.getAllCards())
			deck.push(card);
		Collections.shuffle(deck, random);
		
		// clear grid
		for (int row = 0; row < SIZE; row++)
			for (int col = 0; col < SIZE; col++)
				grid[row][col] = null;
		
		// play game
		long millisRemaining = gameMillis;
		int cardsPlaced = 0;
		while (cardsPlaced < SIZE * SIZE) {
			Card card = deck.pop();
			long startTime = System.currentTimeMillis();
			int[] play = player.getPlay(card, millisRemaining);
			millisRemaining -= System.currentTimeMillis() - startTime;
			if (millisRemaining < 0) { // times out
				System.err.println("Player Out of Time");
				return minPoints;
			}
			if (play.length != 2 || play[0] < 0 || play[0] >= SIZE || play[1] < 0 || play[1] >= SIZE || grid[play[0]][play[1]] != null) { // illegal play
				System.err.printf("Illegal play: %s\n", Arrays.toString(play));
				return minPoints;
			}
			grid[play[0]][play[1]] = card;
			cardsPlaced++;
			if (verbose) {
				system.printGrid(grid);
				System.out.println();
			}
		}
		return system.getScore(grid);
	}
	
	/**
	 * Given two-character card inputs from the given Scanner, play a game of Poker Squares and return the final
	 * game score.
	 * @param in Scanner object from which two-character card inputs will be read
	 * @return final game score
	 */
	public int play(Scanner in) {
		player.init();

		// track remaining cards
		ArrayList<Card> remaining = new ArrayList<Card>();
		for (Card card : Card.getAllCards())
			remaining.add(card);
		Collections.shuffle(remaining, random);
		
		// clear grid
		for (int row = 0; row < SIZE; row++)
			for (int col = 0; col < SIZE; col++)
				grid[row][col] = null;
		
		// play game
		long millisRemaining = gameMillis;
		int cardsPlaced = 0;
		while (cardsPlaced < SIZE * SIZE) {
			Card card = null;
			while (card == null) {
				System.out.println("Remaining cards: " + remaining);
				System.out.print("Card? ");
				String cardName = in.nextLine().trim().toUpperCase();
				card = Card.getCard(cardName);
				if (card == null) {
					System.err.println("Error: Invalid card name");
					continue;
				}
				if (!remaining.contains(card)) {
					card = null;
					System.err.println("Error: Card already played");
					continue;
				}
				remaining.remove(card);
			}
			
			long startTime = System.currentTimeMillis();
			int[] play = player.getPlay(card, millisRemaining);
			millisRemaining -= System.currentTimeMillis() - startTime;
			if (millisRemaining < 0) { // times out
				System.err.println("Player Out of Time");
				return minPoints;
			}
			if (play.length != 2 || play[0] < 0 || play[0] >= SIZE || play[1] < 0 || play[1] >= SIZE || grid[play[0]][play[1]] != null) { // illegal play
				System.err.printf("Illegal play: %s\n", Arrays.toString(play));
				return minPoints;
			}
			grid[play[0]][play[1]] = card;
			cardsPlaced++;
			if (verbose) {
				system.printGrid(grid);
				System.out.println();
			}
		}
		return system.getScore(grid);
	}
	
	/**
	 * Play a sequence of games, collecting and reporting statistics.
	 * @param numGames number of games to play
	 * @param startSeed seed of first game. Successive games use successive seeds
	 * @param verbose whether or not to provide verbose output of game play
	 * @return integer array of game scores
	 */
	public int[] playSequence(int numGames, long startSeed, boolean verbose) {
		this.verbose = verbose;
		if (verbose) {
			System.out.printf("%d games starting at seed %d\nPoint system:\n%s\n", numGames, startSeed, system);
		}
		int[] scores = new int[numGames];
		double scoreMean = 0;
		int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
		for (int i = 0; i < numGames; i++) {
			setSeed(startSeed + i);
			int score = play();
			scores[i] = score;
			scoreMean += score;
			if (scores[i] < min) min = scores[i];
			if (scores[i] > max) max = scores[i];
			System.out.println(score);
		}
		scoreMean /= numGames;
		double scoreStdDev = 0;
		for (int i = 0; i < numGames; i++) {
			double diff = scores[i] - scoreMean;
			scoreStdDev += diff * diff;
		}
		scoreStdDev = Math.sqrt(scoreStdDev / numGames);
		System.out.printf("Score Mean: %f, Standard Deviation: %f, Minimum: %d, Maximum: %d\n", scoreMean, scoreStdDev, min, max);
		return scores;
	}
	
	/**
	 * Hold a Poker Squares tournament between the given players and point systems, the number of games played by 
	 * each player with each point system, and the start seed determining the sequence of deals all will encounter, 
	 * returning an array of doubles corresponding to the tournament score of each player.
	 * Tournament scores are determined as follows. For each tournament, average scores are linearly scaled and 
	 * transformed such that the player with the maximum or minimum average score receive a tournament score of 
	 * 1.0 or 0.0 respectively, with all other players receiving linearly scaled tournament scores between 0.0 and 1.0.
	 * For each scoring system, a player receives a tournament score, and in the ith position of the return array is
	 * the sum of all tournament scores of the ith player.
	 * @param players Poker Squares players taking part in the tournament
	 * @param systems Poker Squares point systems used to evaluate players in the tournament
	 * @param gamesPerSystem the number of games that will be played by each player with each point system
	 * @param startSeed the start seed for the pseudorandom number generator that generates card deals
	 * @return the sum of the tournament scores for each of the given players
	 */
	public static double[] playTournament(
			ArrayList<PokerSquaresPlayer> players,
			ArrayList<PokerSquaresPointSystem> systems,
			int gamesPerSystem, long startSeed) {
		double[] tournamentScores = new double[players.size()];
		for (PokerSquaresPointSystem system : systems) { // for each point system
			System.out.println("Point System:\n" + system);
			int[] totalScores = new int[players.size()];
			for (int i = 0; i < players.size(); i++) { // for each player
				PokerSquaresPlayer player = players.get(i);
				System.out.printf("Player: \"%s\"\n", player.getName());
				int[] scores = new PokerSquares(player, system).playSequence(gamesPerSystem, startSeed, false);
				for (int score : scores)
					totalScores[i] += score;
				System.out.printf("Player \"%s\" total score: %d\n", player.getName(), totalScores[i]);
			}
			int maxTotal = Integer.MIN_VALUE;
			int minTotal = Integer.MAX_VALUE;
			for (int totalScore : totalScores) {
				if (totalScore > maxTotal)
					maxTotal = totalScore;
				if (totalScore < minTotal)
					minTotal = totalScore;				
			}
			for (int i = 0; i < players.size(); i++) { // for each player
				double normalizedTotal = (double) (totalScores[i] - minTotal) / (maxTotal - minTotal);
				System.out.println("Player \"" + players.get(i).getName() + "\" normalized score: " + normalizedTotal);
				tournamentScores[i] += normalizedTotal;
			}
		}
		double max = Double.NEGATIVE_INFINITY;
		for (double score : tournamentScores) {
			if (score > max)
				max = score;
		}
		System.out.printf("%20s %s\n", "Player", "Tournament Score");
		for (int i = 0; i < players.size(); i++) { // for each player
			System.out.printf("%20s %f\n", players.get(i).getName(), tournamentScores[i]);
		}
		
		return tournamentScores;
	}
	
	/**
	 * Set the seed of the game pseudorandom number generator.
	 * @param seed pseudorandom number generator seed
	 */
	private void setSeed(long seed) {
		random.setSeed(seed);
	}
	
	
	/**
	 * Demonstrate single/batch game play testing and tournament evaluation of PokerSquaresPlayers.
	 * @param args (not used)
	 */
	public static void main(String[] args) {
		// Demonstration of single game play (30 seconds)
		System.out.println("Single game demo:");
		PokerSquaresPointSystem.setSeed(0L);
		PokerSquaresPointSystem system = PokerSquaresPointSystem.getAmeritishPointSystem();
		System.out.println(system);
		new PokerSquares(new GreedyMCPlayer(2), PokerSquaresPointSystem.getAmeritishPointSystem()).play();

		// Demonstration of batch game play (30 seconds per game)
		System.out.println("\n\nBatch game demo:");
		System.out.println(system);
		new PokerSquares(new GreedyMCPlayer(2), PokerSquaresPointSystem.getAmeritishPointSystem()).playSequence(3, 0, false);
		
		// Demonstration of tournament evaluation (3 players, 2 point systems, 100 x 30s games for each of the 3*2=6 player-system pairs) 
		System.out.println("\n\nTournament evaluation demo:");
		ArrayList<PokerSquaresPlayer> players = new ArrayList<PokerSquaresPlayer>();
		players.add(new RandomPlayer());
		players.add(new GreedyMCPlayer(0));
		players.add(new GreedyMCPlayer(2));
		ArrayList<PokerSquaresPointSystem> systems = new ArrayList<PokerSquaresPointSystem>();
		PokerSquaresPointSystem.setSeed(0L);
		systems.add(PokerSquaresPointSystem.getAmeritishPointSystem());
		systems.add(PokerSquaresPointSystem.getRandomPointSystem());
		PokerSquares.playTournament(players, systems, 100, 0L); // use fewer games per system for faster testing
	}
}
