package algorithm.ga.evolution.selection;

import algorithm.ga.base.GeneticAgent;
import random.MersenneTwisterFast;

import java.util.ArrayList;

public abstract class SelectionFunction <T> {
    protected MersenneTwisterFast randomizer;

    public SelectionFunction(MersenneTwisterFast randomizer)
    {
        this.randomizer = randomizer;
    }

    public abstract ArrayList<GeneticAgent<T>> Select(ArrayList<GeneticAgent<T>> population, int numParents);
}
