package main;

import algorithm.ga.base.GAManager;
import algorithm.ga.base.KnapsackGAManager;
import algorithm.ga.evolution.mutation.Inversion;
import algorithm.ga.evolution.mutation.Reverse;
import random.MersenneTwisterFast;

import java.util.ArrayList;

public class Application {
    // --- command line ---
    // -algorithm [ga | sa | aco | pso | best-algorithm] -configuration [default | best] -search_best_configuration
    public static void main(String... args)
    {

        DebugMode mode = DebugMode.CONSOLE;
        long seed = System.currentTimeMillis();
        int capacity = 100;
        int geneLength = Configuration.instance.numberOfItems;
        int generations = 150;
        Knapsack k = new Knapsack(Configuration.instance.maximumCapacity,"./data/knapsack_instance.csv");

        if(mode == DebugMode.CONSOLE || mode == DebugMode.FILECONSOLE)
        {
            System.out.println("Knapsack Generated:");
            System.out.println("Max Weight: " + k.GetMaxWeight());
            System.out.println("# of Items: " + k.GetTable().size());
        }

        KnapsackGAManager ga = KnapsackGAManager.KnapsackCreator(capacity,geneLength,k,new MersenneTwisterFast(seed),
                0.05f, KnapsackGAManager.MutationOperator.INVERSION,0.01f,KnapsackGAManager.SelectionOperator.TOURNAMENT,
                1f,KnapsackGAManager.CrossoverOperator.TWOPOINT, GAManager.GAMODE.DEBUG);

        long genStart;
        for(int i = 0; i < generations; i++)
        {
            genStart = System.currentTimeMillis();
            ga.DoCylce();
            if(mode == DebugMode.CONSOLE || mode == DebugMode.FILECONSOLE)
                System.out.println("Generation " + (i+1) + " complete. (" + (System.currentTimeMillis() - genStart)/1000f + "s) --> Best: " + ga.GetBestAgent().GetFitness() +
                    " Average: " + ga.GetAverageFitness() + " Lowest: " + ga.GetLowestFitness());
        }

        if(mode == DebugMode.FILE || mode == DebugMode.FILECONSOLE)
            ga.writePopulationReport("./data/Testing/" + System.currentTimeMillis() + ".dat");

        System.out.println("Completed in " + (System.currentTimeMillis() - seed)/1000f + " seconds.\n" + "Best Agent --> " + ga.GetBestAgent().GetFitness());
    }

    enum DebugMode
    {
        NONE,
        FILE,
        CONSOLE,
        FILECONSOLE
    }
}