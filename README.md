# EAAI NSG Challenge: Parameterized Poker Squares
### Artificial Intelligence Research on Solitaire Games

The goal for this project is to create an intelligent poker squares player for the EAAI NSG Challenge as a way to delve into recent research of [Monte Carlo Tree Search Methods](http://www.cameronius.com/cv/mcts-survey-master.pdf) and other game-based search problems.

You can read a bit about the contest [here](http://tinyurl.com/ppokersqrs).

Documentation for the contest code is available [here](http://cs.gettysburg.edu/~tneller/games/pokersquares/eaai/dist/141017/doc/index.html).

### TODO
#### public class Rule()
- [ ] Check hand against the card that was just flipped.
- [ ] Rule only occurs at non-terminal points in the tree.
- [ ] Rule puts card into row/column and checks what hand it returns in point system
- [ ] Implement replaceNode()
- [ ] Implement mutation

##### Determining score?

- [ ] Implement parametrized suit check
- [ ] Implement parametrized sequence check
- [ ] Implement standard hand check for scoring
- [ ] Determine the cutoff for deciding on right or left subtree
- [ ] Implement a randomized reasonable cutoff mechanism


#### public class Decision()

- [x] Create a Decision class
- [ ] Implement replaceNode()
- [ ] Implement mutation


##### Types of Decisions to Implement
- [x] row/col with least cards
- [x] row/col with most cards
- [ ] row/col with most of a suit
- [ ] row/col with most of a rank
- [ ] row/col extends straight
- [ ] random()
- [ ] left() // as left as possible
- [ ] up() // as up as possible


#### public class Forest()

- [ ] Create Forest (TreeFactory) class
- [ ] Figure out depth limit for trees
- [ ] Implement a general fitness function (score over games)
- [ ] Make a mutation engine (for some with a small probability)
- [ ] Implement candidate selection for crossover
- [ ] Implement crossover
- [ ] Determine a logical way to build trees while maintaining decision structure
