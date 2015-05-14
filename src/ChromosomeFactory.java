import java.util.*;

public class ChromosomeFactory {
    private final int LENGTH = 100;
    private final int NON_SURVIVORS = 10;
    private ArrayList<Node> ruleList;
    PokerSquaresPointSystem pointSystem;

    public ChromosomeFactory(PokerSquaresPointSystem psps) {
        ruleList = new ArrayList<Node>(LENGTH);
        pointSystem = psps;
    }


    /**
     * Creates a set of beginning rules for the decision trees.
     *
     */
    public void createChromosomes() {
        for (int i=0; i<LENGTH; i++) {
            ruleList.add(new Rule(pointSystem));
        }
    }

    /**
     * Averages an array of integers
     */
    private int average(int[] scores) {
        int total = 0;
        for (int i=0; i<scores.length; i++) {
            total += scores[i];
        }
        double aveDouble = (double)total/scores.length;
        int aveInt = (int)Math.round(aveDouble);
        return aveInt;
    }

    /**
     * Determine the fitness of each of the trees.
     * Runs 50-100 (?) games and averages to determine the
     * fitness of each tree.
     */
    private int[][] assessFitness() {
        int[][] fitnesses = new int[LENGTH][2];
        for (int i=0; i<LENGTH; i++) {
            FitnessGeneticPlayer fgp = new FitnessGeneticPlayer();
            fgp.setPointSystem(pointSystem,0);
            fgp.setHead(ruleList.get(i));
            PokerSquares evaluator = new PokerSquares(fgp, pointSystem);
            int[] scores = evaluator.playSequence(50,0,false);
            fitnesses[i][0] = average(scores);
            fitnesses[i][1] = i;
        }
        return fitnesses;
    }


    /**
     * Determines which chromosomes move to the next generation.
     */
    public Node selectNextGeneration() {
        int[][] fitnesses = assessFitness();
        Arrays.sort(fitnesses, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return Integer.compare(b[0], a[0]);
            }
        });

        for (int i=0; i<NON_SURVIVORS; i++) {
            int winnerInd = fitnesses[i][1];
            int replaceInd = fitnesses[LENGTH-(i+1)][1];
            Node replacement = cloneTree(ruleList.get(winnerInd));
            ruleList.set(replaceInd,replacement);
        }

        for (int i=0; i<LENGTH; i++) {
            for (int j=0; j<2; j++) {
                System.out.print(fitnesses[i][j] + " ");
            }
            System.out.println();
        }

        Node winner = ruleList.get(fitnesses[0][1]);
        return winner;

    }

    /**
     * Creates a clone of the the tree starting from the head node
     */
    private Node cloneTree(Node head) {
        Node newHead = new Decision(pointSystem);
        try {
            newHead = (Node) head.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println("You cloned poorly");
        }
        if (newHead.getChild(true) != null) {
            Node newRight = cloneTree(newHead.getChild(true));
            newHead.setRight(newRight);
        }
        if (newHead.getChild(false) != null) {
            Node newleft = cloneTree(newHead.getChild(false));
            newHead.setLeft(newleft);
        }
        return newHead;
    }


    /**
     * Mutates chromosomes in the population with a certain probability.
     *
     */
    public void mutateAll() {

    }


    /**
     * Mutates individual nodes in chromosomes.
     *
     */
    private void mutateChromosome() {

    }


    /**
     * Selects candidates for crossover and determines pairings.
     *
     */
    public void crossOver() {

    }

    /**
     * Perform crossover between two individuals.
     *
     */
    private void breedChromosome() {

    }


    public static void main(String[] args) {
        PokerSquaresPointSystem system = PokerSquaresPointSystem.getAmericanPointSystem();
        System.out.println(system);

        ChromosomeFactory chrome = new ChromosomeFactory(system);
        chrome.createChromosomes();
        for (int i=0; i<10; i++) {
            chrome.selectNextGeneration();
        }
        
    }


}
