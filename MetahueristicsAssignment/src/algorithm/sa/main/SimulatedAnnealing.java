package algorithm.sa.main;


import algorithm.ga.evolution.fitness.KnapsackFitnessFunctionSimple;
import algorithm.ga.evolution.randomizer.KnapsackGeneRandomizer;
import main.Knapsack;
import random.MersenneTwisterFast;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SimulatedAnnealing {
    private float temperature;
    private float coolingRate;

    private ArrayList<Boolean> currentResult;
    private ArrayList<Boolean> bestResult;

    private Knapsack knapsack;

    private MersenneTwisterFast randomizer;
    private KnapsackGeneRandomizer geneRandomizer;
    private KnapsackFitnessFunctionSimple fitnessFunc;

    protected int cycle = 0;

    protected AnnealMode mode;
    protected ArrayList<AnnealedLog> logs = new ArrayList<>();

    public SimulatedAnnealing(float temperature, float coolingRate, Knapsack knapsack, MersenneTwisterFast randomizer, AnnealMode mode)
    {
        this.temperature = temperature;
        this.coolingRate = coolingRate;
        this.randomizer = randomizer;
        this.knapsack = knapsack;
        this.mode = mode;
        geneRandomizer = new KnapsackGeneRandomizer(randomizer,knapsack);
        fitnessFunc = new KnapsackFitnessFunctionSimple(knapsack);

        currentResult = geneRandomizer.NextGene(knapsack.GetTable().size());
        bestResult = currentResult;
    }


    protected boolean acceptByProbability(float oldFitness, float newFitness)
    {
        if(newFitness > oldFitness)
        {
            return true;
        }
        else
        {
            float delta = oldFitness - newFitness;
            double probability  = Math.exp(-delta /temperature);
            return probability > randomizer.nextDouble();
        }
    }


    public ArrayList<Boolean> getNewSolution()
    {
        ArrayList<Boolean> newSol = createDeepCopy(currentResult);
        // Add/Remove random item.
        int index = randomizer.nextInt(newSol.size());
        if(!newSol.get(index))
        {
            while (knapsack.GetWeight(newSol) + knapsack.GetTable().get(index).GetWeight() > knapsack.GetMaxWeight())
            {
                int remIndex = randomizer.nextInt(newSol.size());
                if(newSol.get(remIndex))
                    newSol.set(remIndex,false);
            }

            newSol.set(index, true);
        }
        else
        {
             newSol.set(index,false);
             index = randomizer.nextInt(newSol.size());
             while (!newSol.get(index))
             {
                 index = randomizer.nextInt(newSol.size());
             }
             newSol.set(index,true);

             while (knapsack.GetWeight(newSol) > knapsack.GetMaxWeight())
             {
                 index = randomizer.nextInt(newSol.size());
                 newSol.set(index,false);
             }
        }

        return  newSol;
    }

    private ArrayList<Boolean> createDeepCopy(ArrayList<Boolean> array)
    {
        ArrayList<Boolean> deepCopy = new ArrayList<>();
        for(Boolean b : array)
        {
            if(b)
                deepCopy.add(true);
            else
                deepCopy.add(false);
        }
        return deepCopy;
    }

    public void doCycle()
    {
        ArrayList<Boolean> newSolution = getNewSolution();
        float currentEnergy = fitnessFunc.CalculateFitness(currentResult);
        float newEnergy = fitnessFunc.CalculateFitness(newSolution);

        if(acceptByProbability(currentEnergy,newEnergy))
        {
            currentResult = newSolution;
        }

        if(fitnessFunc.CalculateFitness(currentResult) > fitnessFunc.CalculateFitness(bestResult))
        {
            bestResult = currentResult;
        }

        temperature *= (1-coolingRate);
        cycle++;

        if(mode == AnnealMode.DEBUG)
        {
            logs.add(new AnnealedLog(cycle,temperature,fitnessFunc.CalculateFitness(bestResult),fitnessFunc.CalculateFitness(currentResult)));
        }

    }

    public ArrayList<Boolean> getBestResult()
    {
        return bestResult;
    }

    public ArrayList<Boolean> getCurrentResult()
    {
        return currentResult;
    }

    public float getBestScore()
    {
        return fitnessFunc.CalculateFitness(bestResult);
    }

    public float getCurrentScore()
    {
        return fitnessFunc.CalculateFitness(currentResult);
    }

    public float getTemperature()
    {
        return temperature;
    }


    public void writeReport(String path)
    {
        try (FileWriter fw = new FileWriter(path)) {
            fw.append("cooling=" + coolingRate + '\n');
            for (AnnealedLog log : logs)
            {
                fw.append(log.toString());
            }
        } catch (IOException e) {
            System.err.println("ERROR: Attempted to write to file: " + path);
        }
    }

    protected static class AnnealedLog
    {
        protected int cycle;
        protected float temperature;
        protected float bestScore;
        protected float currentScore;

        public AnnealedLog(int cycle, float temperature, float bestScore, float currentScore) {
            this.cycle = cycle;
            this.temperature = temperature;
            this.bestScore = bestScore;
            this.currentScore = currentScore;
        }

        public String toString()
        {
            return cycle + "," + temperature + "," + bestScore + "," + currentScore  + "\n";
        }
    }

    public enum AnnealMode
    {
        DEBUG,
        STD
    }
}
