import java.util.Random;

public class Rule {

    Random randy;
    boolean row;
    int whichRC;
    int pointThresh;

    public Rule() {
        randy = new Random();
        // determine if checking row or column
        boolean randBool = randy.nextBoolean();
        row = randBool;
        
        // which row or column to check
        int randInt = randy.nextInt(5);
        whichRC = randInt;

        // at what point threshold (for a hand) should the thingy go left/right
        // NEED TO FIGURE OUT HOW ON EARTH WE'RE GOING TO DO THIS
        // PROBABLY SOMETHING WITH MEDIANS AND WHATNOT
        // IDK IT'S CRAZY
        pointThresh = 0;
    }

    private boolean checkRow(Card[][] grid, Card curCard) {
        // put card into the row that we care about
        Card[] potentialHand = grid[whichRC];

        // get value of hand

        return true;
    }

    private boolean checkColumn(Card[][] grid, Card curCard) {
        // put card into the column that we care about
        Card[] potentialHand;

        // get value of hand
        
        return true;
    }

    public boolean evaluate(Card[][] grid, Card curCard) {
        boolean direction;
        if (row) {
            direction = checkRow(grid, curCard);
        } else {
            direction = checkColumn(grid, curCard);
        }
        return direction;
    }

}

