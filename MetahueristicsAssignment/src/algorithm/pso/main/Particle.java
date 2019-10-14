package algorithm.pso.main;

import algorithm.ga.evolution.fitness.KnapsackFitnessFunctionSimple;
import main.Knapsack;
import random.MersenneTwisterFast;

import java.util.ArrayList;

public class Particle
{
    private ArrayList<Boolean> bestSolution;
    private ArrayList<Boolean> solution;
    private ArrayList<Float> velocity;

    private MersenneTwisterFast randomizer;
    private KnapsackFitnessFunctionSimple f;
    private Knapsack k;

    private float maxV = 4;
    private float minV = -4;
    private float innertia = 1;

    public Particle(int solutionLength, float maxV, float minV, float innertia, MersenneTwisterFast randomizer,Knapsack k, KnapsackFitnessFunctionSimple f)
    {
        this.maxV = maxV;
        this.minV = minV;
        this.randomizer = randomizer;
        this.f = f;
        this.innertia = innertia;
        this.k = k;
        init(solutionLength);
    }

    private void init(int length)
    {
        solution = new ArrayList<>();
        velocity = new ArrayList<>();

        for(int i = 0; i < length; i++)
        {
            solution.add(false);
            velocity.add((randomizer.nextFloat()*(maxV - minV))+minV);
            solution.set(i, randomizer.nextFloat() < calcPositionUpdateProbability(i));
        }

        bestSolution = Knapsack.createDeepCopy(solution);

    }

    private float calcPositionUpdateProbability(int index)
    {
        if(!solution.get(index) && k.GetWeight(solution) + k.GetTable().get(index).GetWeight() > k.GetMaxWeight())
            return 0.0f;

        float x = (solution.get(index)) ? 1 : 0;
        return (x + velocity.get(index)+maxV)/(1 + 2*maxV);
    }

    public float getFitness()
    {
        return f.CalculateFitness(bestSolution);
    }

    public ArrayList<Boolean> getBestSolution()
    {
        return  bestSolution;
    }

    public void Update(ArrayList<Boolean> bestSolution, float c1, float c2)
    {
        //Update Velocity
        for(int i = 0; i < velocity.size(); i++)
        {
            float v = velocity.get(i);
            float pos = (solution.get(i)) ? 1 : 0;
            float bestPos = (this.bestSolution.get(i)) ? 1 : 0;
            float globalPos =  (bestSolution.get(i)) ? 1 : 0;
            v = innertia * v + c1*randomizer.nextFloat()*(bestPos-pos) + c2*randomizer.nextFloat()*(globalPos-pos);
            v = Math.max(minV, Math.min(maxV, v));
            velocity.set(i,v);
        }

        //Update Position
        for(int i = 0; i < solution.size(); i++)
        {
            solution.set(i, randomizer.nextFloat() < calcPositionUpdateProbability(i));
        }

        //Update Best

        if(f.CalculateFitness(this.bestSolution) < f.CalculateFitness(solution))
        {
            this.bestSolution = Knapsack.createDeepCopy(solution);
        }

    }

}
