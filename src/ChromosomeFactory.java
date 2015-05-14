import java.util.*;

public class ChromosomeFactory {
    
    private final int LENGTH = 100;
    private ArrayList<Rule> ruleList;
    private PokerSquaresPointSystem system;
    private static Random randomGenerator;


    public ChromosomeFactory(PokerSquaresPointSystem system) {
        ruleList = new ArrayList<Rule>(LENGTH);
        this.system = system;
    }


    /**
     * Creates a set of beginning rules for the decision trees.
     *
     */
    public void createChromosomes() {

    }


    private int average(int[] scores) {
        int total = 0;
        for (int i = 0; i < scores.length; i++) {
            total += scores[i];
        }
        double aveDouble = (double) total / scores.length;
        int aveInt = (int) Math.round(aveDouble);
        return aveInt;
    }

    /**
     * Determine the fitness of each of the trees.
     * Runs 50-100 (?) games and averages to determine the
     * fitness of each tree.
     */
    private int[][] assessFitness() {
        int[][] fitnesses = new int[LENGTH][2];
        for (int i = 0; i < LENGTH; i++) {
            FitnessGeneticPlayer fgp = new FitnessGeneticPlayer();
            fgp.setPointSystem(system,0);
            fgp.setHead(ruleList.get(i));
            PokerSquares evaluator = new PokerSquares(fgp, system);
            int[] scores = evaluator.playSequence(100, 0, false);
            fitnesses[i][0] = average(scores);
            fitnesses[i][1] = i;
        }
        return fitnesses;
    }


    /**
     * Determines which chromosomes move to the next generation.
     */
    public void selectNextGeneration() {
        int[][] fitnesses = assessFitness();
        Arrays.sort(fitnesses, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return Integer.compare(a[0], b[0]);
            }
        });
    }


    /**
     * Mutates chromosomes in the population with a certain probability.
     *
     */
    public void mutateAll() {
        for (int i = 0; i < LENGTH; i++) {
            float mutation = randomGenerator.nextFloat();
            if (mutation <= .07) {
                mutateChromosome(ruleList.get(i));
            }
        }
    }


    /**
     * Mutates individual nodes in chromosomes.
     *
     */
    private void mutateChromosome(Rule rootRule) {

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


}
