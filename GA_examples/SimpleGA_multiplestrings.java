/**
 * GA to evolve matching strings.
 * Author: Sherri Goings
 * Last Modified: 4/17/13
 **/
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SimpleGA {
	// GA parameters, final simply means they will not change in a 
	// single execution of the program
	private final int stringLength = 16;
	private final double mutRate = 1.5/stringLength;
	private final String[] goals = { "AAAAAAAAAAAAAAAA", "BBBBBBBBBBBBBBBB", 
									 "CCCCCCCCCCCCCCCC", "AAAAAAAAAADDDDDD" };
	private final int alphabetSize = 3;
	private Individual[] pop;
	private Random rgen;
	private int popSize;

	/** Class to represent a single individual in GA population **/
	private class Individual {
		private char[] sequence;  // genome
		private int fitness;
		private double sharedFit;

		/** constructor creates empty genome and initializes fitness to 0 **/
		public Individual(int length) {
			sequence = new char[length];
			fitness = 0;
		}

		/** copy constructor to create a new individual from an existing one **/
		public Individual(Individual copy) {
			sequence = Arrays.copyOf(copy.sequence, copy.sequence.length);
			fitness = copy.fitness;
			sharedFit = copy.sharedFit;
		}
		
		/** initialize a genome with new random characters within the allowed alphabet **/
		public void fillRandom(Random rgen) {
			for (int i =0; i<sequence.length; i++) 
				sequence[i] = (char)(rgen.nextInt(alphabetSize) + 65);
		}

		/** 
		 * determine fitness of individual
		 * fitness is sum of match scores on each goal string in set, where match
		 * score on a single string is the number of additional characters that match the goal
		 * string beyond 1/2 of the total string. Or total num matches - 1/2 string length.
		 **/
		public void evalMatch(String[] goals) {
			fitness = 0;
			for (int j=0; j<goals.length; j++) {
				int partialFit = 0;
				for (int i = 0; i<sequence.length; i++) 
					// note this is comparing 2 chars, a primitive type in Java so == is ok
					if (sequence[i]==goals[j].charAt(i)) partialFit++;
				if (partialFit > sequence.length/2.0)
					fitness += partialFit - sequence.length/2.0;
			}
		}

		/** mutate by changing one char to a random char if probability is met **/
		public void mutateSingle(double mutRate) {
			if (rgen.nextDouble() < mutRate) {
				int mutIndex = rgen.nextInt(sequence.length);
				sequence[mutIndex] = (char)(rgen.nextInt(alphabetSize) + 65);
			}
		}

		/** mutate by changing each char to a random char with the given mutRate **/
		public void mutateUniform(double mutRate) {
			for (int i=0; i<sequence.length; i++) {
				if (rgen.nextDouble() < mutRate) {
					sequence[i] = (char)(rgen.nextInt(alphabetSize) + 65);
				}
			}
		}

		/** print an individual's genome and fitness **/
		public String toString() {
			String s = "";
			for (int i =0; i<sequence.length; i++) 
				s += sequence[i];
			return "("+s+", "+fitness+", "+sharedFit+")";
		}
		
	}
	
	/** Set up GA with main parameters and goal string to match **/
	public SimpleGA(int pSize) {
		popSize = pSize;
		pop = new Individual[popSize];
		rgen = new Random();
	}

	/** fill population with random individuals **/
	public void initPopulation() {
		// fill pop with random individuals, ASCII for 'A' is 65, so 
		// each char is converted value between 65 and 90
		for (int i=0; i<popSize; i++) {
			pop[i] = new Individual(stringLength);
			pop[i].fillRandom(rgen);
		}
	}

	/** determine objective fitness of all individuals in population **/
	public void evaluateAll() {
		for (int i=0; i<popSize; i++) 
			pop[i].evalMatch(goals);
	}

	/** determines the hamming distance between 2 equal-length character arrays **/
	public int hamming(char[] seq1, char[] seq2) {
		int hamDist = 0;
		for (int i=0; i<seq1.length; i++) 
			if (seq1[i] != seq2[i]) hamDist++;
		return hamDist;
	}

	/** return true if max fitness has been reached, false otherwise **/
	public boolean solved(int threshFit) {
		for (int i=0; i<popSize; i++) 
			if (pop[i].fitness >= threshFit) return true;
		return false;
	}

	/**
	 * randomizes the first steps individuals in the population by swapping
	 * each index i from 0 to steps with a random index >= i
	 **/
	private void shufflePop(int steps) {
		for (int i=0; i<steps; i++) {
			int otherIndex = rgen.nextInt(pop.length-i)+i;            
			Individual temp = pop[i];
			pop[i] = pop[otherIndex];
			pop[otherIndex] = temp;
		}
	}

	/** replace entire population by performing popSize tournaments of tournSize **/
	public void tournSelect(int tournSize) {
		Individual[] newPop = new Individual[popSize];
		for (int i=0; i<popSize; i++) {
			// each tournament replace first tournSize individuals in population
			// with randomly chosen from rest of population, then use those for tournament
			shufflePop(tournSize);
			int maxFit = 0, index = 0;
			for (int j=0; j<tournSize; j++) {
				if (pop[j].fitness > maxFit) {
					maxFit = pop[j].fitness;
					index = j;
				}
			}
			newPop[i] = new Individual(pop[index]);
		}
		pop = newPop;
	}

	/**
	 * Perform a single step of a version of crowding.  This involves choosing tSize
	 * individuals and choosing the best 2 of those to be parents, then mutating each, 
	 * combining them using uniform crossover to create 2 children, and finally replacing 
	 * genetically closest individual to each child from a sample taken from the population
	 * iff the child's fitness is greater than that most similar individual.  Otherwise
	 * the child is discarded. Note that the chosen parents will always be in this sample.
	 **/
	public void crowdingTourn(int tSize, int sampleSize, int threshold) {
		// shuffle enough population for both tournament and sample
		int numToShuffle = tSize;
		if (sampleSize>tSize) numToShuffle = sampleSize;
		shufflePop(numToShuffle);

		// determine 2 most fit individuals in tournament
		int maxFit = 0, secondFit = 0, index = 0, secondIndex = 0;
		for (int j=0; j<tSize; j++) {
			if (pop[j].fitness > secondFit) {
				if (pop[j].fitness > maxFit) {
					secondFit = maxFit;
					secondIndex = index;
					maxFit = pop[j].fitness;
					index = j;
				}
				else {
					secondFit = pop[j].fitness;
					secondIndex = j;
				}
			}
		}
		// start with children as copies of parents
		Individual child1 = new Individual(pop[index]);
		Individual child2 = new Individual(pop[secondIndex]);

		// mutate 
		child1.mutateUniform(mutRate);
		child2.mutateUniform(mutRate);

		// uniform crossover
		for (int j=0; j<stringLength; j++) {
			if (hamming(child1.sequence,child2.sequence) < threshold) {
				char temp = child1.sequence[j];
				child1.sequence[j] = child2.sequence[j];
				child2.sequence[j] = temp;
			}
		}
		// replace most similar individuals out of sample from population
		int minDist1=-1, minDist2=-1, index1=0, index2=0;
		for (int i=0; i<sampleSize; i++) {
			int curDist1 = hamming(child1.sequence, pop[i].sequence);
			int curDist2 = hamming(child2.sequence, pop[i].sequence);
			if (i==0) {
				minDist1 = curDist1;
				minDist2 = curDist2;
			}
			else {
				if (curDist1 < minDist1) {
					minDist1 = curDist1;
					index1 = i;
				}
				if (curDist2 < minDist2) {
					minDist2 = curDist2;
					index2 = i;
				}
			}
		}
		if (child1.fitness > pop[index1].fitness)
			pop[index1] = child1;
		if (child2.fitness > pop[index2].fitness)
			pop[index2] = child2;
	}

	/** 
	 * if fitPropSelect is called with no scale and no fitness sharing
	 **/
	public void fitPropSelect() {
		fitPropSelect(0,0);
	}

	/** 
	 * replace entire population using fitness proportional selection with
	 * given scale (no scaling if scale < 1) and stochastic universal sampling.
	 **/
	public void fitPropSelect(double scale, int nicheRadius) {
		Individual[] newPop = new Individual[popSize];

		// get the sum of modified fitnesses to choose the correct space for 
		// stochastic uniform sampling
		double sumFit = 0;
		for (Individual ind : pop) sumFit += getModFitness(ind, scale, nicheRadius);

		// if all sharedFits are 0, just return without changing population
		// this will look different depending on if we are using scaling or not
		if ((scale<=1 && sumFit==0) || (scale>1 && sumFit==popSize)) return;
		
		double space = sumFit/popSize;
		double curChoicePoint = space/2;
		double curSumFit = 0;
		int curPopIndex = -1;
		int newPopIndex = 0;
		
		// move through both the current and new population arrays appropriately to add
		// each chosen individual from the current pop to the correct place in the new pop
		while (newPopIndex < newPop.length) {
			if (curSumFit >= curChoicePoint) {
				newPop[newPopIndex] = new Individual(pop[curPopIndex]);
				newPopIndex++;
				curChoicePoint += space;
			}
			else {
				curPopIndex++;
				curSumFit += getModFitness(pop[curPopIndex], scale, nicheRadius);
			}
		}
		pop = newPop;
	}

	/** 
	 * Modify fitness in one or both of the following manners
	 * a scaling factor: if scale is valid (>1) fitness = scale^fitness
	 * fitness sharing: divide fitness by niche count
	 **/
	private double getModFitness(Individual ind, double scale, int nicheRadius) {
		double fitness = ind.fitness;
		if (scale > 1) fitness = Math.pow(scale, fitness);
		if (nicheRadius>0) {
			// get individual's niche count
			double nicheCount = 0;
			for (int j=0; j<popSize; j++) {
				
				// get distance between cur individuals & j and increase 
				// nicheCount appropriately if dist < nicheRadius
				int dist = hamming(ind.sequence, pop[j].sequence);
				if (dist < nicheRadius) 
					nicheCount += 1 - (double)dist/nicheRadius;
			}
			if (nicheCount<1) nicheCount = 1;
			fitness = fitness/nicheCount;
			ind.sharedFit = fitness;           // only used when printing population
		}
		return fitness;
	}
	
	/** mutate all individuals in population with given mutRate **/
	public void mutateSingle() {
		for (int i=0; i<popSize; i++) 
			pop[i].mutateSingle(mutRate);        
	}

	/** mutate all individuals in population with given mutRate **/
	public void mutateUniform() {
		for (int i=0; i<popSize; i++) 
			pop[i].mutateUniform(mutRate); 
	}

	/** 
	 * crossover every pair of individuals using 1pt crossover 
	 * chooses random index between 1 and second to last, then swaps
	 * all values before that index in the 2 individuals
	 **/
	public void crossover1pt() {
		for (int i=0; i<popSize; i+=2) {
			int crossInd = rgen.nextInt(stringLength-2)+1;
			// crossover pop[i] and pop[i+1] at index crossInd
			for (int j=0; j<crossInd; j++) {
				char temp = pop[i].sequence[j];
				pop[i].sequence[j] = pop[i+1].sequence[j];
				pop[i+1].sequence[j] = temp;
			}
		}
	}

	/** 
	 * crossover every pair of individuals using 2pt crossover 
	 * chooses random index between 0 and second to last, then 2nd 
	 * random index between already chosen index +1 and last, then 
	 * swaps all values between those 2 indices in the 2 individuals
	 **/
	public void crossover2pt() {
		for (int i=0; i<popSize; i+=2) {
			int crossInd1 = rgen.nextInt(stringLength-1);
			int crossInd2 = rgen.nextInt(stringLength - (crossInd1+1))+crossInd1+1;
			// crossover pop[i] and pop[i+1] between the 2 cross indices
			for (int j=crossInd1; j<crossInd2; j++) {
				char temp = pop[i].sequence[j];
				pop[i].sequence[j] = pop[i+1].sequence[j];
				pop[i+1].sequence[j] = temp;
			}
		}
	}

	/** shuffles order of population array **/
	public void shuffle() {
	    for (int i=0; i<pop.length; i++) {
	        int r = rgen.nextInt(pop.length-i);
	        Individual temp = pop[i];
	        pop[i] = pop[i+r];
	        pop[i+r] = temp;
	    }
	}    

	public void modCrossUni(int threshold) {
		shuffle();
		for (int i=0; i<pop.length; i+=2) {
			boolean found = false;

			for (int j=i+1; j<pop.length; j++) {
				if (hamming(pop[i].sequence,pop[j].sequence) < threshold) {
					Individual temp = pop[i+1];
	        		pop[i+1] = pop[j];
	        		pop[j] = temp;
	        		found = true;
	        		break;
				}
			}
			if (!found) {
				int index = i+rgen.nextInt(pop.length-i);
				Individual temp = pop[i+1];
	        	pop[i+1] = pop[index];
	        	pop[index] = temp;
			}
		}
		crossoverUni();
	}

	/** 
	 * crossover every pair of individuals using uniform crossover 
	 * swaps each value between 2 individuals with a 50% probability.
	 **/
	public void crossoverUni() {
		for (int i=0; i<popSize; i+=2) {
			// crossover pop[i] and pop[i+1] uniformly
			for (int j=0; j<stringLength; j++) {
				if (rgen.nextDouble()<.5) {
					char temp = pop[i].sequence[j];
					pop[i].sequence[j] = pop[i+1].sequence[j];
					pop[i+1].sequence[j] = temp;
				}
			}
		}
	}

	/** print all individuals in the population **/
	public void printPopulation() {
		System.out.println("current population:");
		for (int i=0; i<popSize; i++) 
			System.out.println(pop[i]);
		System.out.println();
	}

	/** returns the number of times the character test appears in the given sequence **/
	private int countChars(char test, char[] sequence) {
		int count = 0;
		for (char cur : sequence)
			if (cur==test) count++;
		return count;
	}

	/** (sort of) sorts population by # of each letter in their genome (selection sort) **/
	public int[] sortPopulation() {
		char cur = 'A';
		int[] curCounts = new int[alphabetSize];
		int counted = 0;

		// like selection sort, find the string that belongs at position 0 and swap it
		// with whatever is currently at 0, then do the same for all following indices
		for (int i=0; i<pop.length-1; i++) {
			int maxFound = -1;
			int maxInd = -1;
			for (int j=i; j<pop.length; j++) {
				int count = countChars(cur, pop[j].sequence);
				if (count > maxFound) {
					maxFound = count;
					maxInd = j;
				}
			}
			if (maxFound <= stringLength/2.0 || i==pop.length-2) {
				// keep track of how many strings matching more than 1/2 of 
				// each character are in population
				int index = (int)cur-65;
				curCounts[index] = i-counted;
				counted =i;

				// if there are no more strings at least 1/2 matching the current
				// character move on the the next
				if (cur==(char)(64+alphabetSize)) break;
				cur = (char)((int)cur+1);
			}
			Individual temp = pop[i];
			pop[i] = pop[maxInd];
			pop[maxInd] = temp;
		}    
		return curCounts;
	}
	 
	public static void main(String[] args) throws IOException {
		System.out.println("args: population size, selection type (t for tournament, fs for fitness proportional with fitness sharing, c for crowding), <tournament size OR scaling factor, niche radius OR sampleSize, threshold>");
		FileWriter fw = new FileWriter("/Accounts/hansond/evocomp/hw3/WriteTest6.csv");
        PrintWriter pw = new PrintWriter(fw);
        String[] selection = new String[3];
        selection[0]="t";
        selection[1]="fs";
        selection[2]="c";




        for (int popSize=500; popSize <=500; popSize+=400) {
        	int[] tournSizes = new int[5];
		    tournSizes[0]=2;
		    tournSizes[1]=3;
		    tournSizes[2]=5;
		    tournSizes[3]=(int)(0.5*popSize);
		    tournSizes[4]=popSize;
        	for (String sel : selection) {
        		if (sel.equals("fs")) {
        			for (double scaling = 1.4; scaling<=2; scaling+=.3) {
        				for (int nicheRad = 0; nicheRad<=16; nicheRad+=4) {
        					for (int threshold = 1; threshold<=16; threshold+=4) {
								SimpleGA SGA = new SimpleGA(popSize);
								boolean foundAMatch = false;
								int firstFoundGens = -1;
								int maxPossFit = 10;

								// initialization
								SGA.initPopulation();

								// stop at given # gens
								// note this should be orders of magnitude higher for Crowding since each "gen"
								// will just be a single tournament
								int gens = 0;
								int maxGens = 1000;
								while (gens<maxGens) {
									gens++;

									// evaluation
									SGA.evaluateAll();

									// don't necessarily want to stop when first find max fitness because alg may
									// still be exploring other peaks, so just save the generation this happens
									if (!foundAMatch && SGA.solved(maxPossFit)) {
										firstFoundGens = gens;
										foundAMatch = true;
									}
									SGA.fitPropSelect(scaling, nicheRad);
									
									SGA.shufflePop(popSize);
									// mutation
									SGA.mutateUniform();

									// crossover
									SGA.modCrossUni(threshold);
								}
								
								// housekeeping for end of run
								System.out.println("FINAL");
								SGA.evaluateAll();
								int[] counts = SGA.sortPopulation();
								//SGA.printPopulation(); 
								System.out.println(popSize+",");
								System.out.println(sel+",");
								System.out.println(scaling+",");
								System.out.println(nicheRad+",");
								System.out.println(threshold+",");
								System.out.println("counts: "+Arrays.toString(counts));
								//System.out.println("gens: "+gens);
								System.out.println("first found a match at: "+firstFoundGens);
								pw.print(popSize+",");
								pw.print(sel+",");
								pw.print(scaling+",");
								pw.print(nicheRad+",");
								pw.print(threshold+",");
								pw.print(Arrays.toString(counts)+",");
								pw.println(firstFoundGens);
								pw.flush();
        					}
        				}
        			}
        		}


        		else {
        			for (int tsize : tournSizes) {
        				for (int sampSize : tournSizes) {
        					for (int threshold = 1; threshold<=16; threshold+=4) {
								SimpleGA SGA = new SimpleGA(popSize);
								boolean foundAMatch = false;
								int firstFoundGens = -1;
								int maxPossFit = 10;

								// initialization
								SGA.initPopulation();

								// stop at given # gens
								// note this should be orders of magnitude higher for Crowding since each "gen"
								// will just be a single tournament
								int gens = 0;
								int maxGens = 1000;
								if (sel.equals("c")) maxGens = 10000;
								while (gens<maxGens) {
									gens++;

									// evaluation
									SGA.evaluateAll();

									// don't necessarily want to stop when first find max fitness because alg may
									// still be exploring other peaks, so just save the generation this happens
									if (!foundAMatch && SGA.solved(maxPossFit)) {
										firstFoundGens = gens;
										foundAMatch = true;
									}

									// selection
									if (sel.equals("t")) 
										SGA.tournSelect(tsize);
									else if (sel.equals("c")) {
										SGA.crowdingTourn(tsize, sampSize, threshold);
										// skip mutation and crossover below since was done as part of crowding
										continue;
									}
									else {
										System.out.println("invalid selection type arg, must be t or fp");
										System.exit(1);
									}
									SGA.shufflePop(popSize);
									// mutation
									SGA.mutateUniform();

									// crossover
									SGA.modCrossUni(threshold);
								}
								
								// housekeeping for end of run
								System.out.println("FINAL");
								SGA.evaluateAll();
								int[] counts = SGA.sortPopulation();
								//SGA.printPopulation(); 
								System.out.println(popSize+",");
								System.out.println(sel+",");
								System.out.println(tsize+",");
								System.out.println(sampSize+",");
								System.out.println(threshold+",");
								System.out.println("counts: "+Arrays.toString(counts));

								//System.out.println("gens: "+gens);
								System.out.println("first found a match at: "+firstFoundGens);
								pw.print(popSize+",");
								pw.print(sel+",");
								pw.print(tsize+",");
								pw.print(sampSize+",");
								pw.print(threshold+",");
								pw.print(Arrays.toString(counts)+",");
								pw.println(firstFoundGens);
								pw.flush();

        					}
        				}
        			}
        		}
        		pw.println();
        		pw.println();
        		pw.println();
        	}
        }

        pw.flush();
		pw.close();
		fw.close();
	}

	// public static void main2(String[] args) {
	// 	System.out.println("args: population size, selection type (t for tournament, fs for fitness proportional with fitness sharing, c for crowding), <tournament size OR scaling factor, niche radius OR sampleSize, threshold>");

	// 	int popSize = Integer.parseInt(args[0]);
	// 	SimpleGA SGA = new SimpleGA(popSize);
	// 	boolean foundAMatch = false;
	// 	int firstFoundGens = -1;
	// 	int maxPossFit = 8;

	// 	// initialization
	// 	SGA.initPopulation();

	// 	// stop at given # gens
	// 	// note this should be orders of magnitude higher for Crowding since each "gen"
	// 	// will just be a single tournament
	// 	int gens = 0;
	// 	int maxGens = 1000;
	// 	if (args[1].equals("c")) maxGens = 10000;
	// 	while (gens<maxGens) {
	// 		gens++;

	// 		// evaluation
	// 		SGA.evaluateAll();

	// 		// don't necessarily want to stop when first find max fitness because alg may
	// 		// still be exploring other peaks, so just save the generation this happens
	// 		if (!foundAMatch && SGA.solved(maxPossFit)) {
	// 			firstFoundGens = gens;
	// 			foundAMatch = true;
	// 		}

	// 		// selection
	// 		if (args[1].equals("t")) 
	// 			SGA.tournSelect(Integer.parseInt(args[2]));
	// 		else if (args[1].equals("fs")) 
	// 			SGA.fitPropSelect(Double.parseDouble(args[2]), Integer.parseInt(args[3]));
	// 		else if (args[1].equals("c")) {
	// 			SGA.crowdingTourn(Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]));
	// 			// skip mutation and crossover below since was done as part of crowding
	// 			continue;
	// 		}
	// 		else {
	// 			System.out.println("invalid selection type arg, must be t or fp");
	// 			System.exit(1);
	// 		}
			
	// 		// mutation
	// 		SGA.mutateUniform();

	// 		// crossover
	// 		SGA.modCrossUni(Integer.parseInt(args[4]));
	// 	}
		
	// 	// housekeeping for end of run
	// 	System.out.println("FINAL");
	// 	SGA.evaluateAll();
	// 	int[] counts = SGA.sortPopulation();
	// 	SGA.printPopulation(); 
	// 	System.out.println("counts: "+Arrays.toString(counts));
	// 	System.out.println("gens: "+gens);
	// 	System.out.println("first found a match at: "+firstFoundGens);
	// }
	
}
