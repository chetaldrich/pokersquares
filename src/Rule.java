import java.util.Random;

public class Rule {

    Card[][] grid = new Card[5][5];
    boolean row;
    boolean column;
    int rank;
    String suit;

    public Rule() {
        Random randy = new Random();
        String[] possibleSuits = {"hearts","spades","clubs","diamonds"};
        boolean randBool = randy.nextBoolean();
        row = randBool;
        column = !row;
        int randInt = randy.nextInt(13);
        rank = randInt;
        randInt = randy.nextInt(4);
        suit = possibleSuits[randInt];
    }

    private int checkRow(int rowNum) {
        return 1;
    }

    private int checkColumn(int colNum) {
        return 1;
    }



}

