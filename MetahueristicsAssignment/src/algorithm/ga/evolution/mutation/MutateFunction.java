package algorithm.ga.evolution.mutation;

import random.MersenneTwisterFast;

import java.util.ArrayList;

public abstract class MutateFunction <T> {
    protected MersenneTwisterFast randomizer;

    public MutateFunction(MersenneTwisterFast randomizer)
    {
        this.randomizer = randomizer;
    }

    public abstract void Mutate(ArrayList<T> gene);
}
