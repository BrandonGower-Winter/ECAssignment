package algorithm.ga.evolution.randomizer;

import algorithm.ga.base.GeneticAgent;
import random.MersenneTwisterFast;

import java.util.ArrayList;

public abstract class GeneRandomizer <T> {

    protected MersenneTwisterFast randomizer;

    public abstract ArrayList<T> NextGene(int length);

    public abstract void MakeGeneValid(GeneticAgent<T> gene);

    public GeneRandomizer(MersenneTwisterFast randomizer)
    {
        this.randomizer = randomizer;
    }

}
