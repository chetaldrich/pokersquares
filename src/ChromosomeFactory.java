import java.util.*;

public class ChromosomeFactory {
    private final int LENGTH = 100;
    private ArrayList<Rule> ruleList;

    public ChromosomeFactory() {
        ruleList = new ArrayList<Rule>(LENGTH);
    }


    /**
     * Creates a set of beginning rules for the decision trees.
     *
     */
    public void createChromosomes() {

    }


    /**
     * Determine the fitness of each of the trees.
     * Runs 50-100 (?) games and averages to determine the
     * fitness of each tree.
     */
    public int[] assessFitness() {
        int[] fitnesses = {1, 2};
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
