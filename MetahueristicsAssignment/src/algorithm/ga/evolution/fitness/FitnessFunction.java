package algorithm.ga.evolution.fitness;

import java.util.ArrayList;

//Abstract Fitness Function class
public abstract class FitnessFunction<T>
{
    public abstract float CalculateFitness(ArrayList<T> gene);
}
