package algorithm.ga.evolution.mutation;

//Not in the notes so I implemented what I saw on youtube:
// https://www.youtube.com/watch?v=UgXDhdPe72M

import random.MersenneTwisterFast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Displacement<T> extends MutateFunction<T> {


    public Displacement(MersenneTwisterFast randomizer) {
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

        List<T> alleleSublist = gene.subList(firstIndex,secondIndex);
        List<T> newGene = new ArrayList<>();

        if(firstIndex != 0)
            newGene.addAll(gene.subList(0, firstIndex));

        for(int i = secondIndex; i < gene.size(); i++)
        {
            newGene.add(gene.get(i));
        }

        int insertIndex = randomizer.nextInt(newGene.size());
        newGene.addAll(insertIndex,alleleSublist);
        gene.clear();
        gene.addAll(newGene);

    }
}