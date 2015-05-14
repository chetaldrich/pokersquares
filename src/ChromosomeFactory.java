import java.util.*;

public class ChromosomeFactory {

    private final int LENGTH = 100;
    private final int NON_SURVIVORS = 10;
    private ArrayList<Chromosome> genePool;
    private PokerSquaresPointSystem system;
    private static Random randomGenerator;



    public ChromosomeFactory(PokerSquaresPointSystem system) {
        genePool = new ArrayList<Chromosome>(LENGTH);
        this.system = system;
    }


    /**
     * Creates a set of beginning rules for the decision trees.
     *
     */
    public void createChromosomes() {
        for (int i = 0; i < LENGTH; i++) {
            Chromosome newChromosome = new Chromosome();
            newChromosome.setPointSystem(system, 0);

            Node gene = new Rule(system);
            newChromosome.setHead(gene);

            genePool.add(newChromosome);
        }
    }

    /**
     * Averages an array of integers
     */
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
            // Chromosome chromosome = new Chromosome();
            // chromosome.setPointSystem(system,0);
            // chromosome.setHead(genePool.get(i));
            Chromosome chromosome = genePool.get(i);
            PokerSquares evaluator = new PokerSquares(chromosome, system);
            int[] scores = evaluator.playSequence(50,0,false);
            fitnesses[i][0] = average(scores);
            fitnesses[i][1] = i;
        }
        return fitnesses;
    }


    /**
     * Determines which chromosomes move to the next generation.
     */
    public Chromosome selectNextGeneration() {
        int[][] fitnesses = assessFitness();
        Arrays.sort(fitnesses, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return Integer.compare(b[0], a[0]);
            }
        });

        for (int i = 0; i < NON_SURVIVORS; i++) {
            int winnerInd = fitnesses[i][1];
            int replaceInd = fitnesses[LENGTH - (i + 1)][1];

            Node replacement =
            cloneTree(genePool.get(winnerInd).getHead());

            Chromosome clone = new Chromosome();
            clone.setPointSystem(system, 0);
            clone.setHead(replacement);

            genePool.set(replaceInd, clone);
        }

        for (int i = 0; i < LENGTH; i++) {
            for (int j = 0; j < 2; j++) {
                System.out.print(fitnesses[i][j] + " ");
            }
            System.out.println();
        }

        Chromosome winner = genePool.get(fitnesses[0][1]);
        return winner;

    }

    /**
     * Creates a clone of the the tree starting from the head node
     */
    private Node cloneTree(Node head) {
        Node newHead = new Decision(system);
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
        for (int i = 0; i < LENGTH; i++) {
            float mutation = randomGenerator.nextFloat();
            if (mutation <= .07) {
                mutateChromosome(genePool.get(i));
            }
        }
    }


    /**
     * Mutates individual nodes in chromosomes.
     */
    private void mutateChromosome(Chromosome chromosome) {

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
