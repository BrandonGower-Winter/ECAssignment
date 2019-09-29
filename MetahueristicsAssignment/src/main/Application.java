package main;

import algorithm.ga.base.GAManager;
import algorithm.ga.base.KnapsackGAManager;
import algorithm.ga.evolution.mutation.Displacement;
import algorithm.ga.evolution.mutation.Insertion;
import algorithm.ga.evolution.mutation.Inversion;
import algorithm.ga.evolution.mutation.Reverse;
import algorithm.sa.main.SimulatedAnnealing;
import random.MersenneTwisterFast;

import java.util.ArrayList;

public class Application {
    // --- command line ---
    // -algorithm [ga | sa | aco | pso | best-algorithm] -configuration [default | best] -search_best_configuration
    public static void main(String... args)
    {

        HeuristicMode mode = HeuristicMode.SA;
        DebugMode debugMode = DebugMode.CONSOLE;
        long seed = System.currentTimeMillis();
        int capacity = 100;
        int geneLength = Configuration.instance.numberOfItems;
        int generations = 150;
        Knapsack k = new Knapsack(Configuration.instance.maximumCapacity,"./data/knapsack_instance.csv");

        float bestResult = 0;

        if(debugMode == DebugMode.CONSOLE || debugMode == DebugMode.FILECONSOLE)
        {
            System.out.println("Knapsack Generated:");
            System.out.println("Max Weight: " + k.GetMaxWeight());
            System.out.println("# of Items: " + k.GetTable().size());
        }

        long start = System.currentTimeMillis();

        if(mode == HeuristicMode.GA)
        {
            KnapsackGAManager ga = KnapsackGAManager.KnapsackCreator(capacity,geneLength,k,new MersenneTwisterFast(seed),
                    0.05f, KnapsackGAManager.MutationOperator.INSERTION,0.01f,KnapsackGAManager.SelectionOperator.TOURNAMENT,
                    1f,KnapsackGAManager.CrossoverOperator.TWOPOINT, GAManager.GAMODE.DEBUG);

            long genStart;
            for(int i = 0; i < generations; i++)
            {
                genStart = System.currentTimeMillis();
                ga.DoCylce();
                if(debugMode == DebugMode.CONSOLE || debugMode == DebugMode.FILECONSOLE)
                    System.out.println("Generation " + (i+1) + " complete. (" + (System.currentTimeMillis() - genStart)/1000f + "s) --> Best: " + ga.GetBestAgent().GetFitness() +
                            " Average: " + ga.GetAverageFitness() + " Lowest: " + ga.GetLowestFitness());
            }

            if(debugMode == DebugMode.FILE || debugMode == DebugMode.FILECONSOLE)
                ga.writePopulationReport("./data/Testing/GA_" + seed + ".dat");

            bestResult = ga.GetBestAgent().GetFitness();
        }
        else if(mode == HeuristicMode.SA)
        {
            long cycleStart = System.currentTimeMillis();
            SimulatedAnnealing sa = new SimulatedAnnealing(capacity,0.001f, k, new MersenneTwisterFast(seed),SimulatedAnnealing.AnnealMode.DEBUG);
            int cycle = 0;
            if(debugMode == DebugMode.CONSOLE || debugMode == DebugMode.FILECONSOLE)
                System.out.println("Cycle " + cycle + " complete. (" + (System.currentTimeMillis() - cycleStart)/1000f +
                    "s) --> Best: " + sa.getBestScore() + " Current: " + sa.getCurrentScore() +
                        " Temperature: " + sa.getTemperature());
            while (sa.getTemperature() > 1f)
            {
                cycleStart = System.currentTimeMillis();
                sa.doCycle();
                cycle++;

                if(debugMode == DebugMode.CONSOLE || debugMode == DebugMode.FILECONSOLE)
                    System.out.println("Cycle " + cycle + " complete. (" + (System.currentTimeMillis() - cycleStart)/1000f +
                            "s) --> Best: " + sa.getBestScore() + " Current: " + sa.getCurrentScore() +
                            " Temperature: " + sa.getTemperature());

            }

            if(debugMode == DebugMode.FILE || debugMode == DebugMode.FILECONSOLE)
                sa.writeReport("./data/Testing/SA_" + seed + ".dat");

            bestResult = sa.getBestScore();
        }

        System.out.println("Completed in " + (System.currentTimeMillis() - start)/1000f + " seconds.\n" + "Best Result --> " + bestResult);

    }


    enum DebugMode
    {
        NONE,
        FILE,
        CONSOLE,
        FILECONSOLE
    }

    enum HeuristicMode
    {
        GA,
        SA
    }
}