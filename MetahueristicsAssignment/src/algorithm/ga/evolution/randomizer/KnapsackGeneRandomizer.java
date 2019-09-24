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
    public ArrayList<Boolean> NextGene(int length)
    {
        ArrayList<Boolean> newGene = new ArrayList<>();
        int geneWeight = 0;
        for(int i = 0; i < length; i++)
        {
            if(randomizer.nextBoolean() && geneWeight + knapsack.GetTable().get(i).GetWeight() <= knapsack.GetMaxWeight())
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
    public void MakeGeneValid(GeneticAgent<Boolean> gene)
    {
        while(gene.GetFitness() < 0)
        {
            for(int i = 0; i < gene.GetGene().size(); i++)
            {
                if(gene.GetGene().get(i) && randomizer.nextBoolean())
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
