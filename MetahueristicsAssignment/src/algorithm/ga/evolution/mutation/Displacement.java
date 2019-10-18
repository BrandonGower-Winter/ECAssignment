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
        //Get first and second index
        int firstIndex = randomizer.nextInt(gene.size());
        int secondIndex = randomizer.nextInt(gene.size());
        while (firstIndex == secondIndex) //ensure they are unequal
        {
            secondIndex = randomizer.nextInt(gene.size());
        }

        if(secondIndex < firstIndex) //ensure first index is less than second index
        {
            int temp = firstIndex;
            firstIndex = secondIndex;
            secondIndex = temp;
        }

        List<T> alleleSublist = gene.subList(firstIndex,secondIndex); //Create a sublist of alleles
        List<T> newGene = new ArrayList<>();

        //Fill new gene with remaining alleles not in alleleSublist

        if(firstIndex != 0)
            newGene.addAll(gene.subList(0, firstIndex));

        for(int i = secondIndex; i < gene.size(); i++)
        {
            newGene.add(gene.get(i));
        }

        int insertIndex = randomizer.nextInt(newGene.size()); //Pick a random value to insert sublist
        newGene.addAll(insertIndex,alleleSublist); //Add all the elements in alleleSublist in the new gene at position insertIndex
        gene.clear(); //Clear current gene
        gene.addAll(newGene); //set gene to the new gene

    }
}