package algorithm.ga.evolution.mutation;

import random.MersenneTwisterFast;

import java.util.ArrayList;

//I assumed this mean flipping bits from a certain point. The slides didn't say what this was.

public class Inversion extends MutateFunction<Boolean> {
    public Inversion(MersenneTwisterFast randomizer) {
        super(randomizer);
    }

    @Override
    public void Mutate(ArrayList<Boolean> gene) {
        int index = randomizer.nextInt(gene.size());

        for(int i = index; i < gene.size(); i++)
        {
            gene.set(i,!gene.get(i));
        }
    }
}