Brandon Gower-Winter (GWRBRA001)
EC Task Assigment

This repository consists of two parts:

1. Java Code
2. Python Code

The Java Code contains all of the work outlined in the task assignment handout. 

It contains working code for:

1. A Genetic Algorithm
2. Simulated Annealing
3. Ant Colony Optimization
4. Particle Swarm Optimization

You can run the application with the following parameters:

 -algorithm [ga | sa | aco | pso | best-algorithm] -configuration [default | best] -search_best_configuration

Please ensure you are in the MetaheuristicsAssignment directory when running the application. (The configuration files are dependant on this)

The parameters can be in any order. Furthermore '-search_best_configuration" supersedes "-configuration" and as a result using both together will just result in the best
configuration being searched. Should no parameters be supplied or some parameters be ommited: "-algorithm" defaults to pso, "-configuration" defaults to default and "-search_best_configuration"
defaults to false (Meaning it won't search). best-algorithm also refers to pso.

Results from the search configuration and configurations in general are stored in the ./Metaheuristics/data/recommenders/ directory and all knapsack data is stored in the ./Metaheuristics/data/ directory.
Should you wish to create your own knapsack please follow the format <id>;<weight>;<value> with each line being a new item that could potentially go into the knapsack.

Should you wish to create your own configuration please rename the current default configuration to something else and create your own in the form <algorithm>_DEFAULT.xml
like the examples already present in that directory.

The program outputs the following:

1. The time it took to complete
2. The seed used for the random number generator
3. The best result
4. The encoding for the best result

Each of the algorithms is capable of beating the current best known optimum of 1013 for the knapsack used in this assignment. The Particle Swarm Optimization was obeserved to be the best
(after 100 runs for each algorithm and comparisons of the mean results) and was selected as the best algorithm. This can be verfied by using -find_best_algorithm as a parameter. NOTE: It supercedes all parameters and takes a long time to complete

PLEASE NOTE: Running the search configurations takes a very very long time (and I didn't have enough time to parallelize it) as each of the variables is manipulated with respect to every other variable. The progress of the search is displayed in the terminal. 
The SA algorithm is the quickest should you wish to verify that it works.

I have also included some python scripts that can visualize the GA and SA algorithms. They can be found in the PythonScripts Directory.
I also added a solution verifyer python script that runs as follows:

	python KnapsackVerifyer.py <path/to/knapsack.csv> <solution_encoding>.

The solution encoding is printed to the console when a run of an algorithm is complete.







