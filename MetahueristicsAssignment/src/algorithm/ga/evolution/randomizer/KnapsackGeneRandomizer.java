package algorithm.ga.evolution.randomizer;

import algorithm.ga.base.GeneticAgent;
import main.Knapsack;
import random.MersenneTwisterFast;

import java.util.ArrayList;

public class KnapsackGeneRandomizer extends GeneRandomizer<Boolean> {

    private Knapsack knapsack;

    public KnapsackGeneRandomizer(MersenneTwisterFast randomizer, Knapsack k) {
        super(randomizer);
        knapsack = k;
    }

    @Override
    public ArrayList<Boolean> NextGene(int length) //Create a random encoding
    {
        ArrayList<Boolean> newGene = new ArrayList<>();
        int geneWeight = 0;
        for(int i = 0; i < length; i++)
        {
            if(randomizer.nextBoolean() && geneWeight + knapsack.GetTable().get(i).GetWeight() <= knapsack.GetMaxWeight()) //Is the next boolean true and does it keep the knapsack underweight
            {
                geneWeight += knapsack.GetTable().get(i).GetWeight();
                newGene.add(true);
            }
            else
            {
                newGene.add(false);
            }
        }
        return newGene;
    }

    @Override
    public void MakeGeneValid(GeneticAgent<Boolean> gene) //Makes an invalid encoding valid
    {
        while(gene.GetFitness() < 0)
        {
            for(int i = 0; i < gene.GetGene().size(); i++)
            {
                if(gene.GetGene().get(i) && randomizer.nextBoolean()) //Randomly choose to remove items until the solution is no longer overweight
                {
                    gene.GetGene().set(i,false);
                    if(gene.GetFitness() >= 0)
                    {
                        return;
                    }
                }
            }
        }
    }
}
