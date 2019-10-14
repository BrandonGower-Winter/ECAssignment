package algorithm.aco.main;

import algorithm.ga.evolution.fitness.KnapsackFitnessFunctionSimple;
import main.Knapsack;
import main.KnapsackItem;
import random.MersenneTwisterFast;

import java.util.*;

public class AntColonyOptimization
{
    private Knapsack k;
    private KnapsackFitnessFunctionSimple f;
    private MersenneTwisterFast randomizer;

    private float alpha;
    private float beta;
    private float pheromoneDecay;
    private float exploreRate;
    private int numAnts;

    private HashMap<Integer,Float> pheromoneMap;

    private ArrayList<Boolean> origin;
    private ArrayList<Boolean> best;

    private float average = 0.0f;

    public AntColonyOptimization(Knapsack k, MersenneTwisterFast randomizer, int numAnts, float pheromoneDecay, float exploreRate, float alpha, float beta)
    {
        this.k = k;
        f = new KnapsackFitnessFunctionSimple(k);
        this.randomizer = randomizer;
        this.numAnts = numAnts;
        this.pheromoneDecay = pheromoneDecay;
        this.exploreRate = exploreRate;
        this.alpha = alpha;
        this.beta = beta;
        pheromoneMap = new HashMap<>();
        this.origin = k.getEmptyKnapsack();
        best = this.origin;

        for(int i = 0; i < k.GetTable().size(); i++)
        {
            pheromoneMap.put(i,0.0f);
        }
    }

    public void doGeneration()
    {
        for(int id : pheromoneMap.keySet())
        {
            pheromoneMap.put(id,pheromoneMap.get(id) * pheromoneDecay);
        }
        Ant ants[] = new Ant[numAnts];
        //Generate ants and let them find a solution
        for(int i = 0; i < ants.length; i++)
        {
            //Make ant
            ants[i] = new Ant(k);
            //Find Solution
            int nextItem = getNextLoc(ants[i]);
            while (nextItem != -1)
            {
                ants[i].add(nextItem);
                nextItem = getNextLoc(ants[i]);
            }

            if(f.CalculateFitness(best) < f.CalculateFitness(ants[i].getSolution()))
            {
                best = ants[i].getSolution();
            }

        }
        average = 0;
        //Lay Pheromones
        for(Ant a : ants)
        {
            average += f.CalculateFitness(a.getSolution());
            a.layPheromone(f.CalculateFitness(a.getSolution()),pheromoneMap);
        }
        average/=numAnts;
    }

    private int getNextLoc(Ant ant)
    {
        ArrayList<Integer> validOptions = new ArrayList<>();
        float antWeight = k.GetWeight(ant.getSolution());
        for(int i = 0; i < ant.getSolution().size(); i++)
        {
            //If item is not in the current solution and does not make the solution overweight, add it to validOptions
            if(!ant.getSolution().get(i) && antWeight + k.GetTable().get(i).GetWeight() <= k.GetMaxWeight())
            {
                validOptions.add(i);
            }
        }
        if(validOptions.size() == 0)
            return -1;

        if(randomizer.nextFloat() < 1 - exploreRate)
        {
            //Follow Pheromone
            float totalPheromone = 0.0f;
            for(int i : validOptions)
            {
                totalPheromone += pheromoneMap.get(i);
            }
            float totalProb = 0.0f;
            double[] values = new double[validOptions.size()];
            for(int i = 0; i < validOptions.size(); i++)
            {
                KnapsackItem item = k.GetTable().get(validOptions.get(i));
                values[i] = Math.pow(pheromoneMap.get(validOptions.get(i))/totalPheromone,alpha) * Math.pow(item.GetValue()/Math.pow(item.GetWeight(),2),beta);
                totalProb+=values[i];
            }
            float totalProbProb = 0.0f;
            float[] probabilitySum = new float[validOptions.size()];
            for(int i = 0; i < validOptions.size(); i++)
            {
                totalProbProb += values[i]/totalProb;
                probabilitySum[i] = totalProbProb;
            }
            float randomNum = randomizer.nextFloat();
            for(int i = 0; i < validOptions.size(); i++)
            {
                if(randomNum < probabilitySum[i])
                    return validOptions.get(i);
            }
        }

        //Explore
        return validOptions.get(randomizer.nextInt(validOptions.size()));

    }

    public float getBestScore()
    {
        return f.CalculateFitness(best);
    }

    public float getAverageScore()
    {
        return  average;
    }

    public ArrayList<Boolean> getBest()
    {
        return best;
    }

}
