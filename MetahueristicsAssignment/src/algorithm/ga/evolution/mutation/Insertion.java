package algorithm.ga.evolution.mutation;

//based on this video:
// https://www.youtube.com/watch?v=IwpeNnScDEk

import random.MersenneTwisterFast;

import java.util.ArrayList;

public class Insertion<T>  extends MutateFunction<T> {

    public Insertion(MersenneTwisterFast randomizer) {
        super(randomizer);
    }

    @Override
    public void Mutate(ArrayList<T> gene)
    {
        int firstIndex = randomizer.nextInt(gene.size());
        int secondIndex = randomizer.nextInt(gene.size());
        while (firstIndex == secondIndex)
        {
            secondIndex = randomizer.nextInt(gene.size());
        }

        if(secondIndex < firstIndex)
        {
            int temp = firstIndex;
            firstIndex = secondIndex;
            secondIndex = temp;
        }

        T temp = gene.get(firstIndex);
        gene.remove(firstIndex);
        gene.add(secondIndex - 1, temp);
    }
}