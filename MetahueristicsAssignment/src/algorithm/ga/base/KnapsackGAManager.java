package algorithm.ga.base;

import algorithm.ga.evolution.crossover.CrossOverFunction;
import algorithm.ga.evolution.crossover.OnePointCrossover;
import algorithm.ga.evolution.crossover.KPointCrossover;
import algorithm.ga.evolution.fitness.FitnessFunction;
import algorithm.ga.evolution.fitness.KnapsackFitnessFunctionSimple;
import algorithm.ga.evolution.mutation.*;
import algorithm.ga.evolution.randomizer.GeneRandomizer;
import algorithm.ga.evolution.randomizer.KnapsackGeneRandomizer;
import algorithm.ga.evolution.selection.RouletteWheel;
import algorithm.ga.evolution.selection.SelectionFunction;
import algorithm.ga.evolution.selection.Tournament;
import main.Knapsack;
import random.MersenneTwisterFast;

//This is the GA for Knapsack problem it extends the GAManager
public class KnapsackGAManager extends GAManager<Boolean> {

    private Knapsack knapsack;

    public KnapsackGAManager(int capacity, int geneLength, Knapsack knapsack, MersenneTwisterFast randomizer,
                             GeneRandomizer geneRandomizer, FitnessFunction fFunc,
                             float mutationRate, MutateFunction mFunc,
                             float elitismRate, SelectionFunction sFunc,
                             float crossoverRate, CrossOverFunction cFunc,
                             GAMODE mode) {
        super(capacity, geneLength, randomizer, geneRandomizer, fFunc, mutationRate,mFunc, elitismRate,sFunc,crossoverRate,cFunc, mode);
        this.knapsack = knapsack;
    }

    //This function makes use of enums to make it easier to create a KnapsackGA outside of this class
    public static KnapsackGAManager KnapsackCreator(int capacity, int geneLength, Knapsack knapsack,
                                                      MersenneTwisterFast randomizer,
                                                      float mutationRate , MutationOperator mOp,
                                                      float elitismRatio, SelectionOperator sOp,
                                                      float crossoverRate, CrossoverOperator cOp, GAMODE mode)
    {
        //Create Mutator *
        MutateFunction<Boolean> mFunc;
        switch (mOp)
        {
            case DISPLACEMENT:
                mFunc = new Displacement<>(randomizer);
                break;
            case EXCHANGE:
                mFunc = new Exchange<>(randomizer);
                break;
            case REVERSE:
                mFunc = new Reverse<>(randomizer);
                break;
            case INVERSION:
                mFunc = new Inversion(randomizer);
                break;
            default:
                mFunc = new BitFlip(randomizer);

        }
        //Create CrossOver
        CrossOverFunction<Boolean> cFunc;
        switch (cOp)
        {
            case TWOPOINT:
                cFunc = new KPointCrossover<>(2,randomizer);
                break;
            default:
                cFunc = new OnePointCrossover<>(randomizer);
        }
        //Create Selection
        SelectionFunction<Boolean> sFunc;
        switch (sOp)
        {
            case TOURNAMENT:
                sFunc = new Tournament<>(10,randomizer); //Cause for concern
                break;
            default:
                sFunc = new RouletteWheel<>(randomizer);
        }
        return new KnapsackGAManager(capacity,geneLength,knapsack,randomizer, new KnapsackGeneRandomizer(randomizer,knapsack),
                new KnapsackFitnessFunctionSimple(knapsack),
                mutationRate,mFunc,elitismRatio,sFunc,crossoverRate,cFunc,mode);
    }

    public String GetPopulationSummary()
    {
        String result = "";
        for (GeneticAgent<Boolean> agent : population)
        {
            result += agent.toString() + " F: " + agent.GetFitness() + " W: " + knapsack.GetWeight(agent.GetGene()) + "\n";
        }
        return  result;
    }

    public enum CrossoverOperator
    {
        ONEPOINT,
        TWOPOINT
    }

    public enum MutationOperator
    {
        BITFLIP,
        EXCHANGE,
        INVERSION,
        INSERTION,
        REVERSE,
        DISPLACEMENT
    }

    public enum SelectionOperator
    {
        ROULETTE,
        TOURNAMENT
    }
}
