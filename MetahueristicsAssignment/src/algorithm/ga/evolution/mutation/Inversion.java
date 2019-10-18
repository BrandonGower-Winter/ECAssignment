package algorithm.ga.evolution.mutation;

import random.MersenneTwisterFast;

import java.util.ArrayList;

//I assumed this mean flipping bits up until a certain points. The slides didn't say what this was.

public class Inversion extends MutateFunction<Boolean> {
    public Inversion(MersenneTwisterFast randomizer) {
        super(randomizer);
    }

    @Override
    public void Mutate(ArrayList<Boolean> gene) {
        //Get two indices
        int firstIndex = randomizer.nextInt(gene.size());
        int secondIndex = randomizer.nextInt(gene.size());
        while (firstIndex == secondIndex) //ensure they are unequal
        {
            secondIndex = randomizer.nextInt(gene.size());
        }

        if(secondIndex < firstIndex) //and that firstIndex is < secondIndex
        {
            int temp = firstIndex;
            firstIndex = secondIndex;
            secondIndex = temp;
        }
        for(int i = firstIndex; i <= secondIndex; i++) //Invert the allele values
        {
            gene.set(i,!gene.get(i));
        }
    }
}