package algorithm.ga.evolution.mutation;

import random.MersenneTwisterFast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Reverse<T> extends MutateFunction<T>
{
    public Reverse(MersenneTwisterFast randomizer) {
        super(randomizer);
    }

    @Override
    public void Mutate(ArrayList<T> gene)
    {
        int index = randomizer.nextInt(gene.size());
        List<T> sublist = gene.subList(index,gene.size());

        Collections.reverse(sublist);
        for(int i = 0; i < sublist.size(); i++)
        {
            gene.set(i + index,sublist.get(i));
        }
    }
}
