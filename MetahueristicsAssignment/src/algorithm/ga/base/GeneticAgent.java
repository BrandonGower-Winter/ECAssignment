package algorithm.ga.base;

import algorithm.ga.evolution.crossover.CrossOverFunction;
import algorithm.ga.evolution.fitness.FitnessFunction;
import algorithm.ga.evolution.mutation.MutateFunction;
import algorithm.ga.evolution.randomizer.GeneRandomizer;

import java.lang.reflect.Array;
import java.util.ArrayList;

//Generic Implementation of a solution in the population
public class GeneticAgent<T> implements Comparable
{
    private ArrayList<T> gene;

    private FitnessFunction fitnessFunction;
    private MutateFunction mutateFunction;
    private CrossOverFunction crossOverFunction;

    public GeneticAgent(ArrayList<T> gene, FitnessFunction fFunc, MutateFunction mFunc, CrossOverFunction cFunc)
    {
        this.gene = gene;
        this.fitnessFunction = fFunc;
        this.mutateFunction = mFunc;
        this.crossOverFunction = cFunc;
    }

    public float GetFitness()
    {
        return fitnessFunction.CalculateFitness(gene);
    }

    public ArrayList<GeneticAgent<T>> Crossover(GeneticAgent<T> parent) //Apply crossover function to this gene and another parent
    {
        ArrayList<GeneticAgent<T>> children = new ArrayList<>();
        ArrayList<ArrayList<T>> genes = crossOverFunction.CrossOver(gene,parent.GetGene());
        for (ArrayList<T> gene : genes) {
            children.add(new GeneticAgent<T>(gene,fitnessFunction,mutateFunction,crossOverFunction));
        }
        return children;
    }
    //Mutate this solution
    public void Mutate()
    {
        mutateFunction.Mutate(gene);
    }

    public ArrayList<T> GetGene()
    {
        return gene;
    }

    public String toString()
    {
        String toRet = "";

        for(T g : gene) {
            toRet +=  g.toString() + " ";
        }
        return  toRet.stripTrailing();
    }


    @Override
    public int compareTo(Object o) {
        @SuppressWarnings("unchecked")
        GeneticAgent<T> other = (GeneticAgent<T>) o;
        if(GetFitness() < other.GetFitness()) return 1;
        else if(GetFitness() > other.GetFitness()) return -1;
        else return 0;
    }
}