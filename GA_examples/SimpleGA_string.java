/**
 * Partial code for a simple GA to evolve matching strings.
 * Author: Sherri Goings
 * Last Modified: 4/10/13
 **/
import java.util.*;

public class SimpleGA {
	// GA parameters, you should not change those that are declared final
	// except perhaps while testing.
	private int popSize;
	private final double mutRate = .5;
	private final int stringLength = 26;
	private Individual[] pop;
	private final String goal = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	Random rgen;

	/** Class to represent a single individual in GA population **/
	private class Individual {
		private char[] sequence;  // genome
		private int fitness;

		/** constructor creates empty genome and initializes fitness to 0 **/
		public Individual(int length) {
			sequence = new char[length];
			fitness = 0;
		}

		/** copy constructor to create a new individual from an existing one **/
		public Individual(Individual copy) {
			sequence = Arrays.copyOf(copy.sequence, copy.sequence.length);
			fitness = copy.fitness;
		}
		
		/** initialize a genome with new random characters **/
		public void fillRandom(Random rgen) {
			for (int i =0; i<sequence.length; i++) 
				sequence[i] = (char)(rgen.nextInt(26) + 65);
		}

		/** set fitness of individual to how many chars match the goal string **/
		public void evalMatch(String goal) {
			fitness = 0;
			for (int i = 0; i<sequence.length; i++) 
				// note this is comparing 2 chars, a primitive type in Java so == is ok
				if (sequence[i]==goal.charAt(i)) fitness++;
		}

		/** mutate by changing one char to a random char if probability is met **/
		public void mutate(double mutRate) {
			if (rgen.nextDouble() < mutRate) {
				int mutIndex = rgen.nextInt(sequence.length);
				sequence[mutIndex] = (char)(rgen.nextInt(26) + 65);
			}
		}

		/** print an individual's genome and fitness **/
		public String toString() {
			String s = "";
			for (int i =0; i<sequence.length; i++) 
				s += sequence[i];
			return "("+s+", "+fitness+")";
		}

		public char getCharAt(int position) {
			return sequence[position];
		}

		public char setCharAt(int position, char newChar) {
			char oldChar = sequence[position];
			sequence[position] = newChar;
			return oldChar;
		}
		
		/** performs one sexytime with one other individual **/
		public void uniformCrossover(Individual other) {
		    for (int i=0; i<sequence.length; i++) {
		        if (rgen.nextDouble() < 0.5) {
		            char temp = sequence[i];
		            sequence[i] = other.sequence[i];
		            other.sequence[i] = temp;
		        }
		    }
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

	/** determine fitness of all individuals in population **/
	public void evaluateAll() {
		for (int i=0; i<popSize; i++) 
			pop[i].evalMatch(goal);
	}     

	/** return true if perfect match has been found, false otherwise **/
	public boolean solved() {
		for (int i=0; i<popSize; i++) 
			if (pop[i].fitness >= stringLength) return true;
		return false;
	}

	/** mutate all individuals in population **/
	public void mutate() {
		for (int i=0; i<popSize; i++) 
			pop[i].mutate(mutRate);        
	}

	/** print all individuals in the population **/
	public void printPopulation() {
		System.out.println("current population:");
		for (int i=0; i<popSize; i++) 
			System.out.println(pop[i]);
		System.out.println();
	}

	/** Does fitness proportional selection **/
    public void fitnessProportional() {
        // create array of fitnesses
        // generate N equally spaced numbers from 
        //  0.5*space_between_numbers to 
        //  sum_of_all_fitnesses - 0.5*space_between_numbers
        // for each generated number, determine index i in fitness
        //  array so array[i]>num and array[i-1]<num. pop[i] is The One
        Individual[] newPop = new Individual[popSize];
        int[] fitArray = new int[popSize];
        int prevFitness = 0;
        double u = 1.5;
        for (int i=0; i<popSize; i++) {
            prevFitness += Math.pow(u, pop[i].fitness);
            fitArray[i] = prevFitness;
        }        
        double interval = (double)prevFitness/popSize;
        double num = 0.5*interval;
        int fitIndex = 0;
        for (int i=0; i<popSize; i++) {
            while (fitArray[fitIndex]<num) {
                fitIndex++;
            }
            newPop[i] = new Individual(pop[fitIndex]);
            num += interval;
        }
        pop = newPop;
    }

	/** performs one tournament **/
	private Individual oneTourney(int tSize) {

		// shuffles the population then chooses first tSize individuals
		Individual temp = pop[0];
		int swap = 0;
		if (tSize<popSize) shuffle();
		Individual best = pop[rgen.nextInt(tSize)];
		for (int i=0; i<tSize; i++) {
			if (pop[i].fitness > best.fitness) {
				best = pop[i];
			}
		}
		return best;
	}

	/** does a tournament based selections **/
	public void tourneySelect(int tSize) {
		Individual[] nextGen = new Individual[popSize];
		for (int i=0; i<popSize; i++) {
			nextGen[i] = new Individual(oneTourney(tSize));
		}
		pop = nextGen;
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
	
	/** uniformly crosses over population **/
	public void uniformCrossover() {
	    shuffle();
	    for (int i=0; i<pop.length; i+=2) {
	        pop[i].uniformCrossover(pop[i+1]);
	    }
	}

	/** 1 point crossover **/
	public void ptCrossover() {
		for (int i=0; i<popSize; i+=2) {
			int swapPoint = rgen.nextInt(stringLength);
			char tempChar = pop[i].getCharAt(0);
			for (int p=0; p<swapPoint; p++) {
				tempChar = pop[i].getCharAt(p);
				pop[i].setCharAt(p, pop[i+1].getCharAt(p));
				pop[i+1].setCharAt(p, tempChar);
			}
		}
	}

	
	public static void main(String[] args) {
		if (args.length<3) {
			System.out.println("must include population size, selction type, and crossover type as arguments");
			return;
		}
		// command line arguments:
		// 1. pop size
		// 2. selection type (0 = fitness proportion, >0 = tournament size)
		// 3. crossover type (0 = none, 1 = 1-pt, 2 = uniform)
		// also creates new GA
		int popSize = Integer.parseInt(args[0]);
		SimpleGA SGA = new SimpleGA(popSize);
		int selection = Integer.parseInt(args[1]);
		int crossover = Integer.parseInt(args[2]);
		if (selection<0) {
			System.out.println("selection type must be 0 or greater");
			return;
		}
		if (crossover!=0 && crossover!=1 && crossover!=2) {
			System.out.println("crossover type must be a 0, 1, or 2");
			return;
		}
		

		// test GA by performing 40 runs with current parameters and determining
		// resulting average, min, and max number of generations to find a perfect match.
		// Cut runs of at 50,000 generations if haven't found a match yet.
		int totGens = 0;
		int minGens = 50000;
		int maxGens = 0;

		for (int i=0; i<40; i++) {
			SGA.initPopulation();
			int gens = 0;
			while (gens<50000) {
				SGA.evaluateAll();
				if (SGA.solved()) break;

				//choose the correct selection and crossover method
				if (selection==0) {
					SGA.fitnessProportional();
				}
				else {
					SGA.tourneySelect(selection);
				}
				if (crossover==1) {
					SGA.ptCrossover();
				}
				else if (crossover==2) {
					SGA.uniformCrossover();
				}
				SGA.mutate();
				gens++;
			}
			
			// print number gens for this run, update min and max if appropriate
			System.out.println("gens: "+gens);
			totGens += gens;
			if (gens < minGens) minGens = gens;
			if (gens > maxGens) maxGens = gens;
		}
		// print final data
		System.out.println("\nave gens: "+totGens/40.0+"  range: "+minGens + ", " + maxGens + "\n");
	}
}