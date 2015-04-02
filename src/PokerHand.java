
/**
 * PokerHand - An enumeration of Poker hand classes along with associated identification numbers and Strings. 
 * Provides utility methods for classifying complete or partial Poker hands.
 * @author tneller
 *
 */
public enum PokerHand {
	HIGH_CARD(0, "high card"), ONE_PAIR(1, "one pair"), TWO_PAIR(2, "two pair"), THREE_OF_A_KIND(3, "three of a kind"), 
	STRAIGHT(4, "straight"), FLUSH(5, "flush"), FULL_HOUSE(6, "full house"), 
	FOUR_OF_A_KIND(7, "four of a kind"), STRAIGHT_FLUSH(8, "straight flush"), ROYAL_FLUSH(9, "royal flush");
	
	public static final int NUM_HANDS = PokerHand.values().length;
	public int id;
	public String name;
	PokerHand(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	/**
	 * Given a Card array (possibly with null values) classifies the current Poker hand and returns the classification.
	 * @param hand - a Poker hand represented as an array of Card objects which may contain null values
	 * @return classification of the given Poker hand
	 */
	public static PokerHand getPokerHand(Card[] hand) {
		// Compute counts
		int[] rankCounts = new int[Card.NUM_RANKS];
		int[] suitCounts = new int[Card.NUM_SUITS];
		for (Card card : hand)
			if (card != null) {
				rankCounts[card.getRank()]++;
				suitCounts[card.getSuit()]++;
			}
		
		// Compute count of rank counts
		int maxOfAKind = 0;
		int[] rankCountCounts = new int[hand.length + 1];
		for (int count : rankCounts) {
				rankCountCounts[count]++;
				if (count > maxOfAKind)
					maxOfAKind = count;
			}
			
		// Flush check
		boolean hasFlush = false;
		for (int i = 0; i < Card.NUM_SUITS; i++)
			if (suitCounts[i] != 0) {
				if (suitCounts[i] == hand.length)
					hasFlush = true;
				break;
			}
		
		// Straight check
		boolean hasStraight = false;
		boolean hasRoyal = false;
		int rank = 0;
		while (rank <= Card.NUM_RANKS - 5 && rankCounts[rank] == 0)
			rank++;
		hasStraight = (rank <= Card.NUM_RANKS - 5 && rankCounts[rank] == 1 && rankCounts[rank + 1] == 1 && rankCounts[rank + 2] == 1 && rankCounts[rank + 3] == 1 && rankCounts[rank + 4] == 1);
		if (rankCounts[0] == 1 && rankCounts[12] == 1 && rankCounts[11] == 1 && rankCounts[10] == 1 && rankCounts[9] == 1) 
			hasStraight = hasRoyal = true;
		
		// Return score
		if (hasFlush) {
			if (hasRoyal)
				return PokerHand.ROYAL_FLUSH; // Royal Flush
			if (hasStraight)
				return PokerHand.STRAIGHT_FLUSH; // Straight Flush
		}
		if (maxOfAKind == 4)
			return PokerHand.FOUR_OF_A_KIND; // Four of a Kind
		if (rankCountCounts[3] == 1 && rankCountCounts[2] == 1)
			return PokerHand.FULL_HOUSE; // Full House
		if (hasFlush)
			return PokerHand.FLUSH; // Flush
		if (hasStraight)
			return PokerHand.STRAIGHT; // Straight
		if (maxOfAKind == 3)
			return PokerHand.THREE_OF_A_KIND; // Three of a Kind
		if (rankCountCounts[2] == 2)
			return PokerHand.TWO_PAIR; // Two Pair
		if (rankCountCounts[2] == 1)
			return PokerHand.ONE_PAIR; // One Pair
		return PokerHand.HIGH_CARD; // Otherwise, High Card.  This applies to empty Card arrays as well.
	}
	
	/**
	 * Given a Card array (possibly with null values) classifies the current Poker hand and returns the classification identification number.
	 * @param hand - a Poker hand represented as an array of Card objects which may contain null values
	 * @return classification identification number of the given Poker hand
	 */
	public static final int getPokerHandId(Card[] hand) {
		return getPokerHand(hand).id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString() {
		return name;
	}
}
