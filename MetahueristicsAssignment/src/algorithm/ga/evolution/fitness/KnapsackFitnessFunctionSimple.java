package algorithm.ga.evolution.fitness;

import main.Knapsack;

import java.util.ArrayList;

//This class calculates the score of a solution given a knapsack
public class KnapsackFitnessFunctionSimple extends FitnessFunction<Boolean> {

    private Knapsack knapsack;

    public KnapsackFitnessFunctionSimple(Knapsack k)
    {
        knapsack = k;
    }

    @Override
    public float CalculateFitness(ArrayList<Boolean> gene) {
        float fitness = 0;
        int totalWeight = 0;
        for(int i = 0; i < gene.size(); i++) //Sum up value and weight
        {
            if(gene.get(i))
            {
                fitness += knapsack.GetTable().get(i).GetValue();
                totalWeight += knapsack.GetTable().get(i).GetWeight();
            }
        }
        float result = totalWeight <= knapsack.GetMaxWeight() ? fitness : -1; //Return -1 if overweight else return total value
        return result;
    }
}
