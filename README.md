# EAAI NSG Challenge: Parameterized Poker Squares
### Artificial Intelligence Research on Solitaire Games

The goal for this project is to create an intelligent poker squares player for the EAAI NSG Challenge as a way to delve into recent research of [Monte Carlo Tree Search Methods](http://www.cameronius.com/cv/mcts-survey-master.pdf) and other game-based search problems.

You can read a bit about the contest [here](http://tinyurl.com/ppokersqrs).

Documentation for the contest code is available [here](http://cs.gettysburg.edu/~tneller/games/pokersquares/eaai/dist/141017/doc/index.html).

The current working implementation involves a genetic programming algorithm that employs evolving decision trees based on rule-based decision types. However, the current implementation does not take advantage of the 30 seconds per game (since decision trees can play the game incredibly quickly).

### TODO
- [ ] Try integrating MCTS implementation for later steps.
