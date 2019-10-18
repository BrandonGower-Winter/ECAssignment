package algorithm.pso.main;

import algorithm.ga.evolution.fitness.KnapsackFitnessFunctionSimple;
import main.Knapsack;
import random.MersenneTwisterFast;

import java.util.ArrayList;

public class ParticleSwarmOptimization
{

    private Particle particles[];
    private MersenneTwisterFast randomizer;

    private ArrayList<Boolean> bestSolution;

    private KnapsackFitnessFunctionSimple f;
    private Knapsack k;

    private float maxV,minV;
    private float c1,c2;
    private float innertia;

    public ParticleSwarmOptimization(int count, float maxV, float minV,float c1, float c2, float innertia,Knapsack k, MersenneTwisterFast randomizer)
    {
        particles = new Particle[count];
        this.c1 = c1;
        this.c2 = c2;
        this.maxV = maxV;
        this.minV = minV;
        this.k = k;
        f = new KnapsackFitnessFunctionSimple(k);
        this.randomizer = randomizer;
        this.innertia = innertia;
        bestSolution = null;
        for(int i = 0; i < count; i++) //Create Particles
        {
            particles[i] = new Particle(k.GetTable().size(),maxV,minV,innertia,randomizer,k,f);
            if(bestSolution == null || f.CalculateFitness(bestSolution) < particles[i].getFitness())
            {
                bestSolution = Knapsack.createDeepCopy(particles[i].getBestSolution());
            }
        }
    }

    public void run()
    {
        //Update Particles
        for(int i = 0; i < particles.length; i++)
        {
            particles[i].Update(bestSolution,c1,c2);
        }

        //Update Best
        for(int i = 0; i < particles.length; i++)
        {
            if(f.CalculateFitness(bestSolution) < particles[i].getFitness())
                bestSolution = Knapsack.createDeepCopy(particles[i].getBestSolution());
        }
    }

    public float getBestScore()
    {
        return f.CalculateFitness(bestSolution);
    }

    public float getAverageScore()
    {
        float sum = 0.0f;
        for(Particle p : particles)
            sum += p.getFitness();
        return sum/particles.length;
    }



}
