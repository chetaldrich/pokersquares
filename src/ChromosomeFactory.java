import java.util.*;

public class ChromosomeFactory {
    private final int LENGTH = 100;
    private ArrayList<Rule> ruleList;
    PokerSquaresPointSystem pointSystem;

    public ChromosomeFactory(PokerSquaresPointSystem psps) {
        ruleList = new ArrayList<Rule>(LENGTH);
        pointSystem = psps;
    }


    /**
     * Creates a set of beginning rules for the decision trees.
     *
     */
    public void createChromosomes() {

    }


    private double average(int[] scores) {
        int total = 0;
        for (int i=0; i<scores.length; i++) {
            total += scores;
        }
        double aveDouble = (double)total/scores.length;
        int aveInt = (int)Math.round(aveDouble)
        return aveInt;
    }

    /**
     * Determine the fitness of each of the trees.
     * Runs 50-100 (?) games and averages to determine the
     * fitness of each tree.
     */
    public int[] assessFitness() {
        int[] fitnesses = new int[LENGTH];
        for (int i=0; i<LENGTH; i++) {
            FitnessGeneticPlayer fgp = new FitnessGeneticPlayer();
            fgp.setHead(ruleList.get(i));
            PokerSquares evaluator = new PokerSquares(fgp, pointSystem);
            int[] scores = evaluator.playSequence(100,0,false);
            fitnesses[i] = average(scores);
        }
        return fitnesses;
    }


    /**
     * Determines which chromosomes move to the next generation.
     * @param array of fitnesses of each chromosome.
     */
    public void selectNextGeneration(int[] assessFitness) {

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


}
