package algorithm.ga.base;

import algorithm.ga.evolution.crossover.CrossOverFunction;
import algorithm.ga.evolution.fitness.FitnessFunction;
import algorithm.ga.evolution.mutation.MutateFunction;
import algorithm.ga.evolution.randomizer.GeneRandomizer;
import algorithm.ga.evolution.selection.SelectionFunction;
import random.MersenneTwisterFast;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GAManager<T>
{
    protected int capacity;
    protected int geneLength;
    protected ArrayList<GeneticAgent<T>> population;

    protected float mutationRate;
    protected float crossoverRate;
    protected float elitismRatio;
    protected int generation = 0;

    protected MersenneTwisterFast randomizer;


    protected FitnessFunction fitnessFunc;
    protected CrossOverFunction crossoverFunc;
    protected MutateFunction mutateFunc;
    protected SelectionFunction selectionFunc;
    protected GeneRandomizer geneRandomizer;

    protected GAMODE mode;
    protected ArrayList<String> log;

    public GAManager(int capacity, int geneLength, MersenneTwisterFast randomizer, GeneRandomizer geneRandomizer,
                     FitnessFunction fFunc, float mutationRate, MutateFunction mFunc,
                     float elitismRatio, SelectionFunction sFunc,
                     float crossoverRate, CrossOverFunction cFunc,
                     GAMODE mode)
    {
        this.capacity = capacity;
        population = new ArrayList<GeneticAgent<T>>();
        this.geneRandomizer = geneRandomizer;
        this.randomizer = randomizer;
        this.geneLength = geneLength;
        this.mode = mode;
        log = new ArrayList<>();
        this.mutationRate = mutationRate;
        this.elitismRatio = elitismRatio;
        this.crossoverRate = crossoverRate;
        SetFunctors(fFunc,cFunc,mFunc,sFunc);
        InitPopulation();
    }
    protected void InitPopulation()
    {
        for(int i = 0; i < capacity; i++)
        {
            population.add(new GeneticAgent<T>(geneRandomizer.NextGene(geneLength),fitnessFunc,mutateFunc,crossoverFunc));
        }
        if(mode == GAMODE.DEBUG)
        {
            AddToLog();
        }
    }

    public void SetFunctors(FitnessFunction fFunc, CrossOverFunction cFunc, MutateFunction mFunc, SelectionFunction sFunc)
    {
        fitnessFunc = fFunc;
        crossoverFunc = cFunc;
        mutateFunc = mFunc;
        selectionFunc = sFunc;
    }

    public void DoCylce()
    {
        population.sort(GeneticAgent::compareTo);
        int numChildren = population.size() - Math.round(population.size() * elitismRatio);
        ArrayList<GeneticAgent<T>> newPopulation = new ArrayList<>(population.subList(0, population.size() - numChildren));
        //Selection
        ArrayList<GeneticAgent<T>> parents = selectionFunc.Select(population,numChildren/2);
        for(int i = 0; i < parents.size(); i+=2)
        {
            //Crossover
            if(randomizer.nextFloat() <= crossoverRate)
            {
                ArrayList<GeneticAgent<T>> children = parents.get(i).Crossover(parents.get(i+1));
                for (GeneticAgent<T> child : children) {
                    //Mutation
                    if (randomizer.nextFloat() <= mutationRate) {
                        child.Mutate();
                    }
                    if(child.GetFitness() < 0)
                    {
                        geneRandomizer.MakeGeneValid(child);
                    }
                    newPopulation.add(child);
                }
            }
            else //Randomize when parents do not produce offspring
            {
                GeneticAgent<T> newAgent = new GeneticAgent<T>(geneRandomizer.NextGene(geneLength),fitnessFunc,mutateFunc,crossoverFunc);
                newPopulation.add(newAgent);
                newAgent = new GeneticAgent<T>(geneRandomizer.NextGene(geneLength),fitnessFunc,mutateFunc,crossoverFunc);
                newPopulation.add(newAgent);
            }
            //System.out.println("-----------------------------------");
        }
        generation++;
        population = newPopulation;

        if(mode == GAMODE.DEBUG)
        {
            AddToLog();
        }
    }

    public GeneticAgent<T> GetBestAgent()
    {
        GeneticAgent<T> best = null;
        for(GeneticAgent<T> agent : population)
        {
            if(best == null)
            {
                best = agent;
            }
            if(agent.GetFitness() > best.GetFitness())
            {
                best = agent;
            }
        }
        return best;
    }

    public float GetAverageFitness()
    {
        float sum = 0;
        for (GeneticAgent<T> agent: population) {
            sum += agent.GetFitness();
        }
        return sum / population.size();
    }

    public float GetLowestFitness()
    {
        float worst = -1;
        for(GeneticAgent<T> agent : population)
        {
            if(worst < 0 || agent.GetFitness() < worst)
            {
                worst = agent.GetFitness();
            }
        }
        return worst;
    }

    public void AddToLog()
    {
        String logEntry = generation + ",";
        GeneticAgent<T> best = GetBestAgent();
        logEntry += best.GetFitness() + ",";
        logEntry += GetLowestFitness() + ",";
        logEntry += GetAverageFitness() + ",";
        logEntry += best.toString() + "\n";
        log.add(logEntry);
    }
    public void writePopulationReport(String path)
    {
        try (FileWriter fw = new FileWriter(path)) {
            fw.append("n=" + capacity + " m=" + mutationRate + " c=" + crossoverRate + " e=" + elitismRatio + '\n');
            for (String s : log)
            {
                fw.append(s);
            }
        } catch (IOException e) {
            System.err.println("ERROR: Attempted to write to file: " + path);
        }
    }

    public ArrayList<GeneticAgent<T>> getPopulation()
    {
        return population;
    }

    public FitnessFunction getFitnessFunc() {
        return fitnessFunc;
    }

    public void setFitnessFunc(FitnessFunction fitnessFunc) {
        this.fitnessFunc = fitnessFunc;
    }

    public CrossOverFunction getCrossoverFunc() {
        return crossoverFunc;
    }

    public void setCrossoverFunc(CrossOverFunction crossoverFunc) {
        this.crossoverFunc = crossoverFunc;
    }

    public MutateFunction getMutateFunc() {
        return mutateFunc;
    }

    public void setMutateFunc(MutateFunction mutateFunc) {
        this.mutateFunc = mutateFunc;
    }

    public SelectionFunction getSelectionFunc() {
        return selectionFunc;
    }

    public void setSelectionFunc(SelectionFunction selectionFunc) {
        this.selectionFunc = selectionFunc;
    }

    public float getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(float mutationRate) {
        this.mutationRate = mutationRate;
    }

    public float getCrossoverRate() {
        return crossoverRate;
    }

    public void setCrossoverRate(float crossoverRate) {
        this.crossoverRate = crossoverRate;
    }

    public float getElitismRatio() {
        return elitismRatio;
    }

    public void setElitismRatio(float elitismRatio) {
        this.elitismRatio = elitismRatio;
    }

    public enum GAMODE
    {
        STD,
        DEBUG,
        DEEPDEBUG
    }
}