

public class Rule {
    
    Card[][] grid = new Card[5][5];

    public Rule() {
        for (int row = 0; row < 5; row++)
            for (int col = 0; col < 5; col++)
                grid[row][col] = null;
    }

    private int checkRow(int rowNum) {
        return 1;
    }

    private int checkColumn(int colNum) {
        return 1;
    }

}