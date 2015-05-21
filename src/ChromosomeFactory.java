import java.util.*;

public class ChromosomeFactory {

    private final int LENGTH = 100;
    private final int NON_SURVIVORS = 10;
    private ArrayList<Chromosome> genePool;
    private PokerSquaresPointSystem system;
    private Random randomGenerator = new Random();



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
            newChromosome.createChromosome();
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
            Chromosome chromosome = genePool.get(i);
            FakePokerSquares evaluator = new FakePokerSquares(chromosome, system);
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

            Chromosome clone = new Chromosome();
            clone.setPointSystem(system, 0);

            Node replacement =
            cloneTree(clone, genePool.get(winnerInd).getHead());

            clone.setHead(replacement);

            genePool.set(replaceInd, clone);
        }

        Chromosome winner = genePool.get(fitnesses[0][1]);
        return winner;

    }

    /**
     * Creates a clone of the the tree starting from the head node
     */
    public Node cloneTree(Chromosome chromosome, Node head) {
        Node newHead = new Decision(system, 1);
        try {
            newHead = (Node) head.clone();
            chromosome.addID(newHead);
        } catch (CloneNotSupportedException e) {
            System.out.println("You cloned poorly");
        }
        if (newHead.getRightChild() != null) {
            Node newRight = cloneTree(chromosome, newHead.getRightChild());
            newHead.setRight(newRight);
        }
        if (newHead.getLeftChild() != null) {
            Node newLeft = cloneTree(chromosome, newHead.getLeftChild());
            newHead.setLeft(newLeft);
        }
        return newHead;
    }


    /**
     * Mutates chromosomes in the population with a certain probability.
     *
     */
    public void mutateAll() {
        float mutation;
        for (int i = 0; i < LENGTH; i++) {
            mutation = randomGenerator.nextFloat();
            if (mutation <= .3) {
                genePool.get(i).mutate();
            }
        }
    }


    /**
     * Selects candidates for crossover and determines pairings.
     *
     */
    public void crossOver() {
        Collections.shuffle(genePool);
        for (int i = 0; i < LENGTH; i += 2) {
            breedChromosome(genePool.get(i), genePool.get(i+1));
        }
    }

    /**
     * Perform crossover between two individuals.
     *
     */
    private void breedChromosome(Chromosome c1, Chromosome c2) {

    }


}
