package algorithm.ga.evolution.fitness;

import main.Knapsack;

import java.util.ArrayList;

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
        for(int i = 0; i < gene.size(); i++)
        {
            if(gene.get(i))
            {
                fitness += knapsack.GetTable().get(i).GetValue();
                totalWeight += knapsack.GetTable().get(i).GetWeight();
            }
        }
        float result = totalWeight <= knapsack.GetMaxWeight() ? fitness : -1;
        return result;
    }
}
