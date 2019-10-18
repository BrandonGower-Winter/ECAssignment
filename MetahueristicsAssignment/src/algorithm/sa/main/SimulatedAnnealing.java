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


    //Calculates the probability of accepting a new solution given its fitness relative to the current fitness
    protected boolean acceptByProbability(float oldFitness, float newFitness)
    {
        if(newFitness > oldFitness) //If it is better then accept immediately
        {
            return true;
        }
        else //If it is worse
        {
            float delta = oldFitness - newFitness;
            double probability  = Math.exp(-delta /temperature); //Boltzman Distribution function
            return probability > randomizer.nextDouble();
        }
    }


    //Gets a new solution to compare to the current solution
    public ArrayList<Boolean> getNewSolution()
    {
        ArrayList<Boolean> newSol = Knapsack.createDeepCopy(currentResult);
        // Add/Remove random item from the bad and ensure that it does not make an invalid solution.
        int index = randomizer.nextInt(newSol.size());
        if(!newSol.get(index)) //Item not in knapsack
        {
            while (knapsack.GetWeight(newSol) + knapsack.GetTable().get(index).GetWeight() > knapsack.GetMaxWeight())
            {
                int remIndex = randomizer.nextInt(newSol.size());
                if(newSol.get(remIndex))
                    newSol.set(remIndex,false);
            }
            newSol.set(index, true);
        }
        else //Item in knapsack
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

    public void doCycle()
    {
        //Create a new solution
        ArrayList<Boolean> newSolution = getNewSolution();
        //Calculate their energies
        float currentEnergy = fitnessFunc.CalculateFitness(currentResult);
        float newEnergy = fitnessFunc.CalculateFitness(newSolution);
        //Accept by the probability function
        if(acceptByProbability(currentEnergy,newEnergy))
        {
            currentResult = newSolution;
        }
        //Compare fitness to current best
        if(fitnessFunc.CalculateFitness(currentResult) > fitnessFunc.CalculateFitness(bestResult))
        {
            bestResult = currentResult;
        }

        temperature *= (1-coolingRate); //Cool down temperature
        cycle++;

        if(mode == AnnealMode.DEBUG)
        {
            logs.add(new AnnealedLog(cycle,temperature,fitnessFunc.CalculateFitness(bestResult),fitnessFunc.CalculateFitness(currentResult),bestResult));
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

        protected String items = "";

        public AnnealedLog(int cycle, float temperature, float bestScore, float currentScore, ArrayList<Boolean> items) {
            this.cycle = cycle;
            this.temperature = temperature;
            this.bestScore = bestScore;
            this.currentScore = currentScore;

            for(Boolean b : items) {
                this.items +=  b + " ";
            }
            this.items.stripTrailing();
        }

        public String toString()
        {
            return cycle + "," + temperature + "," + bestScore + "," + currentScore + "," + items + "\n";
        }
    }

    public enum AnnealMode
    {
        DEBUG,
        STD
    }
}
