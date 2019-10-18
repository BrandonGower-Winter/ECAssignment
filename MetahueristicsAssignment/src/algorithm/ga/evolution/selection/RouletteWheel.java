package algorithm.ga.evolution.selection;

import algorithm.ga.base.GeneticAgent;
import random.MersenneTwisterFast;

import java.util.ArrayList;

public class RouletteWheel<T> extends SelectionFunction<T> {

    public RouletteWheel(MersenneTwisterFast randomizer) {
        super(randomizer);
    }

    @Override
    public ArrayList<GeneticAgent<T>> Select(ArrayList<GeneticAgent<T>> population, int numParents) {

        ArrayList<GeneticAgent<T>> parents = new ArrayList<>();


        //Create the roulette wheel
        float totalFitness = 0;
        for (GeneticAgent<T> individual : population)
        {
            totalFitness += individual.GetFitness();
        }

        float sumOfProbabilities = 0;
        float[] probabilities = new float[population.size()];
        for (int i = 0; i < population.size(); i++)
        {
            probabilities[i] = sumOfProbabilities + (population.get(i).GetFitness()/totalFitness);
            sumOfProbabilities = probabilities[i];
        }

        for(int i = 0; i < numParents; i++)
        {
            int parentOne = SpinWheel(population,probabilities);
            int parentTwo = SpinWheel(population,probabilities);
            while(parentOne == parentTwo)
            {
                parentTwo = SpinWheel(population,probabilities);
            }
            parents.add(population.get(parentOne));
            parents.add(population.get(parentTwo));
        }
        return parents;
    }

    private int SpinWheel(ArrayList<GeneticAgent<T>> population, float[] probabilities) //Spins the wheel
    {

        float random = randomizer.nextFloat();
        int index = population.size() - 1;
        for (int i = 0; i < population.size(); i++)
        {
            if(random < probabilities[i])
            {
                index = i;
                break;
            }
        }
        return index;
    }
}
