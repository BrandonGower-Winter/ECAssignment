package algorithm.ga.evolution.mutation;

import random.MersenneTwisterFast;

import java.util.ArrayList;

public class BitFlip extends MutateFunction<Boolean>
{

    public BitFlip(MersenneTwisterFast randomizer)
    {
        super(randomizer);
    }

    @Override
    public void Mutate(ArrayList<Boolean> gene)
    {
        int index = randomizer.nextInt(gene.size()); //Pick a random index in the gene
        gene.set(index,!gene.get(index)); //Flip it
    }
}