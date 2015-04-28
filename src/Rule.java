import java.util.Random;

public class Rule {
    
    Card[][] grid = new Card[5][5];
    boolean row;
    boolean column;
    int rank;
    String suit;

    public Rule() {
        for (int row = 0; row < 5; row++)
            for (int col = 0; col < 5; col++)
                grid[row][col] = null;
        String[] possibleSuits = ["hearts","spades","clubs","diamonds"];
        boolean randBool = nextBoolean();
        row = randBool;
        column = !row;
        int randInt = nextInt(13);
        rank = 
        randInt = nextInt(4);
        suit = possibleSuits[randInt];

    }

    private int checkRow(int rowNum) {
        return 1;
    }

    private int checkColumn(int colNum) {
        return 1;
    }

    public dosomething(Card[][] grid) {
        this.grid = grid;
        if (row) {
            checkRow(rownumber)
        } else if (column) {
            checkColumn(colnumber)
        }
    }

}