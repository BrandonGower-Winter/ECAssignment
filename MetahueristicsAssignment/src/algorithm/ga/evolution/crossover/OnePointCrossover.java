package algorithm.ga.evolution.crossover;

import algorithm.ga.base.GeneticAgent;
import random.MersenneTwisterFast;

import java.util.ArrayList;

//Generic Implementation of OnePointCrossOver
public class OnePointCrossover<T> extends CrossOverFunction<T> {

    private MersenneTwisterFast randomizer;

    public OnePointCrossover(MersenneTwisterFast random)
    {
            this.randomizer = random;
    }

    @Override
    public ArrayList<ArrayList<T>> CrossOver(ArrayList<T> parent, ArrayList<T> otherParent)
    {
        ArrayList<ArrayList<T>> children = new ArrayList<>();
        int crossover = randomizer.nextInt(parent.size()-1); //Choose CrossOver

        children.add(new ArrayList<T>()); //First Child
        children.add(new ArrayList<T>()); //Second Child

        for(int i = 0; i < parent.size(); i++) //WHen i = crossover swap allocation of alleles from one parent to another
        {
            if(i < crossover)
            {
                children.get(0).add(parent.get(i));
                children.get(1).add(otherParent.get(i));
            }
            else
            {
                children.get(1).add(parent.get(i));
                children.get(0).add(otherParent.get(i));
            }
        }
        return children;
    }
}
