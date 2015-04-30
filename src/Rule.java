import java.util.Random;

public class Rule {

    private final Rule condition;
    private final Rule left;
    private final Rule right;

    Card[][] grid;
    boolean row;
    boolean column;
    int rank;
    int suit;
    boolean rankCheck;
    boolean suitCheck;
    boolean seqCheck;
    Random randy;
    int whichRC;
    int totalNeeded;


    public Rule() {

        // initialize the rule nodes
        condition = new Rule();
        left = new Rule();
        right = new Rule();


        randy = new Random();
        // determine if checking row or column
        boolean randBool = randy.nextBoolean();
        row = randBool;
        column = !row;
        // rank to look for
        int randInt = randy.nextInt(13);
        rank = randInt;
        // suit to look for
        randInt = randy.nextInt(4);
        suit = randInt;
        // whether it's looking for rank, suit, or a sequence
        randInt = randy.nextInt(3);
        if (randInt==0) {
            rankCheck = true;
            suitCheck = false;
            seqCheck = false;
        } else if (randInt==1) {
            rankCheck = false;
            suitCheck = true;
            seqCheck = false;
        } else {
            rankCheck = false;
            suitCheck = false;
            seqCheck = true;
        }
        // which row or column to check
        randInt = randy.nextInt(5);
        whichRC = randInt;
        randInt = randy.nextInt(5);
        totalNeeded = randInt;
    }

    /**
     * @return A String that represents the function or value of this node.
     */
    String getLabel() {
        String label = "Label based on row/col, rank/straight/suit";
        return label;
    }


    private int checkRowRank(int rowNum) {
        int rankCount = 0;
        for (int i=0; i<5; i++) {
            if (grid[rowNum][i].getRank() == rank) {
                rankCount++;
            }
        }
        return rankCount;
    }

    private int checkColumnRank(int colNum) {
        int rankCount = 0;
        for (int i=0; i<5; i++) {
            if (grid[i][colNum].getRank() == rank) {
                rankCount++;
            }
        }
        return rankCount;
    }

    private int checkRowSuit(int rowNum) {
        int suitCount = 0;
        for (int i=0; i<5; i++) {
            if (grid[rowNum][i].getSuit() == suit) {
                suitCount++;
            }
        }
        return suitCount;
    }

    private int checkColumnSuit(int colNum) {
        int suitCount = 0;
        for (int i=0; i<5; i++) {
            if (grid[i][colNum].getSuit() == suit) {
                suitCount++;
            }
        }
        return suitCount;
    }

    private int checkRowSeq(int rowNum) {
        int[] seqCount = new int[5];
        for (int i=0; i<5; i++) {
            seqCount[i] = grid[rowNum][i].getRank();
        }
        return 1;
    }

    private int checkColumnSeq(int colNum) {
        int[] seqCount = new int[5];
        for (int i=0; i<5; i++) {
            seqCount[i] = grid[i][colNum].getRank();
        }
        return 1;
    }

    /**
     * Evaluates the node at this level and returns one of the child nodes.
     * @return A boolean, indicating which direction to move in the tree.
     */
    public boolean doCheck(Card[][] grid) {
        this.grid = grid;
        int result = 0;
        if (row) {
            if (rankCheck) {
                result = checkRowRank(whichRC);
            } else if (suitCheck) {
                result = checkRowSuit(whichRC);
            } else if (seqCheck) {
                result = checkRowSeq(whichRC);
            }
        } else if (column) {
            if (rankCheck) {
                result = checkColumnRank(whichRC);
            } else if (suitCheck) {
                result = checkColumnSuit(whichRC);
            } else if (seqCheck) {
                result = checkColumnSeq(whichRC);
            }
        }
        if (result >= totalNeeded) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retrieves a direct sub-rule from this rule.
     * @param index The index of a child rule. 0 is left, 1 is right.
     * @return The node at the specified position.
     */
    Rule getChild(boolean decision) {
        if (decision) {
            return right;
        } else {
            return left;
        }
    }


}
