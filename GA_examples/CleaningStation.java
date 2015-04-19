import java.io.*;
import java.util.*;

public class CleaningStation {
	private ArrayList<CleanerFish> cleanFishies;
	private ArrayList<ClientFish> clientFishies;
	private int cleanPopSize;
	private int clientPopSize;
	private int generations;
	private Random rgen;
	
	public CleaningStation() {
		cleanPopSize = 20;
		clientPopSize = 1;
		cleanFishies = makeCleaners();
		clientFishies = makeClients();
		generations = 500;
		rgen = new Random();
	}
	
	// initialize populations
	private ArrayList<CleanerFish> makeCleaners() {
		ArrayList<CleanerFish> fishArr = new ArrayList<CleanerFish>();
		for (int i = 0; i<cleanPopSize; i++) {
			fishArr.add(new CleanerFish());
		}
		return fishArr;
	}
	private ArrayList<ClientFish> makeClients() {
		ArrayList<ClientFish> fishArr = new ArrayList<ClientFish>();
		for (int i = 0; i<clientPopSize; i++) {
			fishArr.add(new ClientFish());
		}
		return fishArr;
	}
	
	// performs mutation on all the cleaners
	public void mutateCleaners() {
		for (CleanerFish cf : cleanFishies) {
			cf.mutate();
		}
	}
	
	// crosses over all cleaners
	public void crossoverCleaners() {
		Collections.shuffle(cleanFishies);
		for (int i = 0; i<cleanPopSize; i+=2) {
			cleanFishies.get(i).crossover(cleanFishies.get(i+1));
		}
	}
	
	// simulates the interaction between cleaners and a client
	public void cleanThem() {
		for (CleanerFish cf : cleanFishies) {
			cf.simulateInteraction(clientFishies.get(rgen.nextInt(clientPopSize)));
		}
	}
	
	/** 
	 * replace entire population using fitness proportional selection with
	 * given scale (no scaling if scale < 1) and stochastic universal sampling.
	 **/
	public void fitPropSelect(double scale) {
		ArrayList<CleanerFish> newPop = new ArrayList<CleanerFish>();
		
		// simulate all the interactions
		cleanThem();
		// get the sum of fitnesses to choose the correct space for 
		// stochastic uniform sampling
		double sumFit = 0;
		if (scale>1) {
			for (CleanerFish ind : cleanFishies) sumFit += Math.pow(scale, ind.getFitness());
		} else {
			for (CleanerFish ind : cleanFishies) sumFit += ind.getFitness();
		}

//		// if all sharedFits are 0, just return without changing population
//		// this will look different depending on if we are using scaling or not
//		if ((scale<=1 && sumFit==0) || (scale>1 && sumFit==popSize)) return;
		
		double space = sumFit/cleanPopSize;
		double curChoicePoint = space/2;
		double curSumFit = 0;
		int curPopIndex = -1;
		int newPopIndex = 0;
		
		
		// IF THERE'S A PROBLEM, IT'S MOST LIKELY HERE
		// move through both the current and new population arrays appropriately to add
		// each chosen individual from the current pop to the correct place in the new pop
		while (newPopIndex < cleanFishies.size()) {
			if (curSumFit >= curChoicePoint) {
				CleanerFish tempFish = cleanFishies.get(curPopIndex);
				tempFish.reset();
				newPop.add(tempFish);
				newPopIndex++;
				curChoicePoint += space;
			}
			else {
				curPopIndex++;
				if (scale>1) {
					curSumFit += Math.pow(scale, cleanFishies.get(curPopIndex).getFitness());
				} else {
					curSumFit += cleanFishies.get(curPopIndex).getFitness();
				}
			}
		}
		cleanFishies = newPop;
	}
	
	public void printToFile(PrintWriter pw) {
		for (CleanerFish cf : cleanFishies) {
			char[] genome = cf.getGenome();
			int fitness = cf.getFitness();
			for (char c : genome) {
				pw.print(c+",");
			}
			pw.println(fitness);
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		FileWriter fw = new FileWriter("/Accounts/hansond/evocomp/TwoBites/FishData.csv");
		PrintWriter pw = new PrintWriter(fw);
		CleaningStation whaleWash = new CleaningStation();
		for (int gens = 1; gens < whaleWash.generations; gens++) {
			whaleWash.cleanThem();
			whaleWash.fitPropSelect(1.3);
			whaleWash.crossoverCleaners();
			whaleWash.mutateCleaners();
		}
		whaleWash.printToFile(pw);
	}

}
