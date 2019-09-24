package algorithm.ga.evolution.fitness;

import java.util.ArrayList;

public abstract class FitnessFunction<T>
{
    public abstract float CalculateFitness(ArrayList<T> gene);
}
