package algorithm.ga.evolution.selection;

import algorithm.ga.base.GeneticAgent;
import random.MersenneTwisterFast;

import java.util.ArrayList;

public class Tournament<T> extends SelectionFunction<T>
{
    private int k;

    public Tournament(int k, MersenneTwisterFast randomizer)
    {
        super(randomizer);
        this.k = k;
    }

    @Override
    public ArrayList<GeneticAgent<T>> Select(ArrayList<GeneticAgent<T>> population, int numParents) {
        ArrayList<GeneticAgent<T>> parents = new ArrayList<>();
        for(int i = 0; i < numParents; i++)
        {
            GeneticAgent<T> parentOne = Fight(population);
            GeneticAgent<T> parentTwo = Fight(population);
            while(parentOne == parentTwo) //Could be cause for concern.
            {
                parentTwo = Fight(population);
            }
            parents.add(parentOne);
            parents.add(parentTwo);
        }
        return parents;
    }

    private GeneticAgent<T> Fight(ArrayList<GeneticAgent<T>> population)
    {
        ArrayList<GeneticAgent<T>> combatants = new ArrayList<>();
        for(int i = 0; i < k; i++)
        {
            GeneticAgent<T> potentialCombatant = population.get(randomizer.nextInt(population.size()));
            while(combatants.contains(potentialCombatant))
            {
                potentialCombatant = population.get(randomizer.nextInt(population.size()));
            }
            combatants.add(potentialCombatant);
        }

        GeneticAgent<T> champion = null;
        for(GeneticAgent<T> combatant : combatants)
        {
            if(champion == null || champion.GetFitness() < combatant.GetFitness())
                champion = combatant;
        }
        return champion;
    }
}
