package main;

import algorithm.ga.base.GAManager;
import algorithm.ga.base.GeneticAgent;
import algorithm.ga.base.KnapsackGAManager;
import algorithm.ga.evolution.crossover.OnePointCrossover;
import algorithm.ga.evolution.mutation.BitFlip;
import random.MersenneTwisterFast;

import java.util.ArrayList;

public class Application {
    // --- command line ---
    // -algorithm [ga | sa | aco | pso | best-algorithm] -configuration [default | best] -search_best_configuration
    public static void main(String... args)
    {
        long seed = System.currentTimeMillis();
        int capacity = 1000;
        int geneLength = Configuration.instance.numberOfItems;
        int generations = 500;
        Knapsack k = new Knapsack(Configuration.instance.maximumCapacity,"./data/knapsack_instance.csv");

        System.out.println("Knapsack Generated:");
        System.out.println("Max Weight: " + k.GetMaxWeight());
        System.out.println("# of Items: " + k.GetTable().size());

        KnapsackGAManager ga = KnapsackGAManager.KnapsackCreator(capacity,geneLength,k,new MersenneTwisterFast(seed),
                0.05f, KnapsackGAManager.MutationOperator.BITFLIP,0.1f,KnapsackGAManager.SelectionOperator.ROULETTE,
                1f,KnapsackGAManager.CrossoverOperator.ONEPOINT, GAManager.GAMODE.DEBUG);

        long genStart;
        for(int i = 0; i < generations; i++)
        {
            genStart = System.currentTimeMillis();
            ga.DoCylce();
            System.out.println("Generation " + (i+1) + " complete. (" + (System.currentTimeMillis() - genStart)/1000f + "s) --> Best: " + ga.GetBestAgent().GetFitness());
        }

        ga.writePopulationReport("./data/Testing/" + System.currentTimeMillis() + ".dat");
        System.out.println("Completed in " + (System.currentTimeMillis() - seed)/1000f + " seconds.");
    }
}