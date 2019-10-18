package algorithm.ga.evolution.mutation;

//My assumption is that this is the interchanging/swapping mutation from the notes.

import random.MersenneTwisterFast;

import java.util.ArrayList;

public class Exchange<T> extends MutateFunction<T>
{
    public Exchange(MersenneTwisterFast randomizer)
    {
        super(randomizer);
    }

    @Override
    public void Mutate(ArrayList<T> gene)
    {
        //Get two indices
        int firstIndex = randomizer.nextInt(gene.size());
        int secondIndex = randomizer.nextInt(gene.size());

        while(firstIndex == secondIndex) //ensure they are unequal
        {
            secondIndex = randomizer.nextInt(gene.size());
        }

        //Swap the two alleles
        T tempAllele = gene.get(firstIndex);
        gene.set(firstIndex,gene.get(secondIndex));
        gene.set(secondIndex,tempAllele);
    }
}