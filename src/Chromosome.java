import java.util.*;

public class Chromosome {
    private int length;
    private ArrayList<Rule> ruleList;

    public Chromosome(int length) {
        this.length = length;
        ruleList = new ArrayList<Rule>(length);
        for (int i=0; i<length; i++) {
            // Rule tempRule = new Rule();
            // ruleList.add(tempRule);
        }
    }
}
