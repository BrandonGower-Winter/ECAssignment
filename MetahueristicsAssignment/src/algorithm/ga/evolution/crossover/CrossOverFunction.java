package algorithm.ga.evolution.crossover;

import algorithm.ga.base.GeneticAgent;

import java.util.ArrayList;

//Abstract class for CrossOver Function
public abstract class CrossOverFunction<T>{
    public abstract ArrayList<ArrayList<T>> CrossOver(ArrayList<T> parent, ArrayList<T> otherParent);
}
