package main;

import algorithm.aco.main.AntColonyOptimization;
import algorithm.aco.recommender.ACORecommender;
import algorithm.ga.base.GAManager;
import algorithm.ga.base.KnapsackGAManager;
import algorithm.ga.evolution.fitness.KnapsackFitnessFunctionSimple;
import algorithm.ga.evolution.selection.SelectionFunction;
import algorithm.ga.recommender.GARecommender;
import algorithm.pso.main.ParticleSwarmOptimization;
import algorithm.pso.recommender.PSORecommender;
import algorithm.sa.main.SimulatedAnnealing;
import algorithm.sa.recommender.SARecommender;
import random.MersenneTwisterFast;

public class Application {
    // --- command line ---
    // -algorithm [ga | sa | aco | pso | best-algorithm] -configuration [default | best] -search_best_configuration
    public static void main(String... args)
    {
        //Get and set seed for running
        long seed = System.currentTimeMillis();
        Configuration.instance.Configure(args,seed); //Parse Arguments

        //Load Knapsack
        int geneLength = Configuration.instance.numberOfItems;
        Knapsack k = new Knapsack(Configuration.instance.maximumCapacity,"./data/knapsack_instance.csv");

        //Write Knapsack details
        if(Configuration.instance.dMode == DebugMode.CONSOLE || Configuration.instance.dMode == DebugMode.FILECONSOLE)
        {
            System.out.println("Knapsack Generated:");
            System.out.println("Max Weight: " + k.GetMaxWeight());
            System.out.println("# of Items: " + k.GetTable().size());
        }

        //Use Algorithm
        if(!Configuration.instance.searchBest)
        {
            long start = System.currentTimeMillis();
            float bestResult = 0;
            if(Configuration.instance.mode == HeuristicMode.GA)
            {
                //Load Configuration
                GARecommender gaRecommender = new GARecommender(Configuration.instance.configuration == Config.BEST);
                System.out.println("Genetic Algorithm:\nCapacity:" + gaRecommender.capacity + "\nGenerations:" + gaRecommender.generations  + "\nElitism RAte:" + gaRecommender.elitismRatio
                        + "\nMutationRate:" + gaRecommender.mutationRate + "\nCrossoverRate:" + gaRecommender.crossoverRate
                        + "\nSelectionFunc:" + gaRecommender.selectionFunc + "\nMutationFunc:" + gaRecommender.mutateFunc + "\nCrossOverFunc:" + gaRecommender.crossoverFunc + "\n-------------------------------------------------");

                //Identify Selection Function
                KnapsackGAManager.SelectionOperator sOp;
                switch (gaRecommender.selectionFunc)
                {
                    case "tournament":
                        sOp = KnapsackGAManager.SelectionOperator.TOURNAMENT;
                        break;
                    default:
                        sOp = KnapsackGAManager.SelectionOperator.ROULETTE;
                }

                //Identify Mutate Function
                KnapsackGAManager.MutationOperator mOp;
                switch (gaRecommender.mutateFunc)
                {
                    case "bitflip":
                        mOp = KnapsackGAManager.MutationOperator.BITFLIP;
                        break;
                    case "displacement":
                        mOp = KnapsackGAManager.MutationOperator.DISPLACEMENT;
                        break;
                    case "exchange":
                        mOp = KnapsackGAManager.MutationOperator.EXCHANGE;
                        break;
                    case "insertion":
                        mOp = KnapsackGAManager.MutationOperator.INSERTION;
                        break;
                    case "inversion":
                        mOp = KnapsackGAManager.MutationOperator.INVERSION;
                        break;
                    default:
                        mOp = KnapsackGAManager.MutationOperator.REVERSE;
                }

                //Identify Crossover Function
                KnapsackGAManager.CrossoverOperator cOP;
                switch (gaRecommender.crossoverFunc)
                {
                    case "twopoint":
                        cOP = KnapsackGAManager.CrossoverOperator.TWOPOINT;
                        break;
                    default:
                        cOP = KnapsackGAManager.CrossoverOperator.ONEPOINT;
                }

                //Load Genetic Algorithm Manager
                KnapsackGAManager ga = KnapsackGAManager.KnapsackCreator(gaRecommender.capacity,geneLength,k,Configuration.instance.randomGenerator,
                        gaRecommender.mutationRate, mOp,gaRecommender.elitismRatio,sOp,
                        gaRecommender.crossoverRate,cOP, GAManager.GAMODE.DEBUG);

                long genStart;
                //Simulate n generations
                for(int i = 0; i < gaRecommender.generations; i++)
                {
                    genStart = System.currentTimeMillis();
                    ga.DoCylce();
                    if(Configuration.instance.dMode == DebugMode.CONSOLE || Configuration.instance.dMode == DebugMode.FILECONSOLE)
                        System.out.println("Generation " + (i+1) + " complete. (" + (System.currentTimeMillis() - genStart)/1000f + "s) --> Best: " + ga.GetBestAgent().GetFitness() +
                                " Average: " + ga.GetAverageFitness() + " Lowest: " + ga.GetLowestFitness());
                }

                //Write Summary to file
                if(Configuration.instance.dMode == DebugMode.FILE || Configuration.instance.dMode == DebugMode.FILECONSOLE)
                    ga.writePopulationReport("./data/Testing/GA_" + seed + ".dat");

                //set best result
                bestResult = ga.GetBestAgent().GetFitness();
            }
            else if(Configuration.instance.mode == HeuristicMode.SA)
            {
                //Load Configuration
                SARecommender saRecommender = new SARecommender(Configuration.instance.configuration == Config.BEST);

                System.out.println("Simulated Annealing:\nTemperature:" + saRecommender.temperature + "\nCooling Rate:" + saRecommender.coolingRate + "\n-------------------------------------------------");
                long cycleStart = System.currentTimeMillis();
                //Create Simulated Annealing Instance
                SimulatedAnnealing sa = new SimulatedAnnealing(saRecommender.temperature,saRecommender.coolingRate, k, Configuration.instance.randomGenerator,SimulatedAnnealing.AnnealMode.DEBUG);
                int cycle = 0;
                if(Configuration.instance.dMode == DebugMode.CONSOLE || Configuration.instance.dMode == DebugMode.FILECONSOLE)
                    System.out.println("Cycle " + cycle + " complete. (" + (System.currentTimeMillis() - cycleStart)/1000f +
                            "s) --> Best: " + sa.getBestScore() + " Current: " + sa.getCurrentScore() +
                            " Temperature: " + sa.getTemperature());

                //Run the simulated annealing algorithm
                while (sa.getTemperature() > 1f)
                {
                    cycleStart = System.currentTimeMillis();
                    sa.doCycle();
                    cycle++;

                    if(Configuration.instance.dMode == DebugMode.CONSOLE || Configuration.instance.dMode == DebugMode.FILECONSOLE)
                        System.out.println("Cycle " + cycle + " complete. (" + (System.currentTimeMillis() - cycleStart)/1000f +
                                "s) --> Best: " + sa.getBestScore() + " Current: " + sa.getCurrentScore() +
                                " Temperature: " + sa.getTemperature());

                }
                //Write Summary to file
                if(Configuration.instance.dMode == DebugMode.FILE || Configuration.instance.dMode == DebugMode.FILECONSOLE)
                    sa.writeReport("./data/Testing/SA_" + seed + ".dat");

                //Set best score
                bestResult = sa.getBestScore();
            }
            else if(Configuration.instance.mode == HeuristicMode.ACO)
            {
                //Load Configuration
                ACORecommender acoRecommender = new ACORecommender(Configuration.instance.configuration == Config.BEST);
                System.out.println("Ant Colony Optimization:\nNumAnts:" + acoRecommender.numAnts + "\nGenerations:" + acoRecommender.generations + "\nalpha:" + acoRecommender.alpha +
                        "\nbeta:" + acoRecommender.beta + "\npheromoneDecay:" + acoRecommender.pheromoneDecay + "\nexploreRate:" + acoRecommender.exploreRate + "\n-------------------------------------------------");

                //Create Ant Colony Optimization
                AntColonyOptimization aco = new AntColonyOptimization(k,Configuration.instance.randomGenerator,acoRecommender.numAnts,acoRecommender.pheromoneDecay,
                        acoRecommender.exploreRate,acoRecommender.alpha,acoRecommender.beta);

                //Run algorithm
                for(int i = 0; i < acoRecommender.generations; i++)
                {
                    long genStart = System.currentTimeMillis();
                    aco.doGeneration();
                    if(Configuration.instance.dMode == DebugMode.CONSOLE || Configuration.instance.dMode == DebugMode.FILECONSOLE)
                        System.out.println("Generation " + (i+1) + " complete. (" + (System.currentTimeMillis() - genStart)/1000f + "s) --> Best: " + aco.getBestScore() + " Average: " + aco.getAverageScore());
                }

                //Store Best Result
                bestResult = aco.getBestScore();
            }
            else if(Configuration.instance.mode == HeuristicMode.PSO)
            {
                //Load Configuration
                PSORecommender psoRecommender = new PSORecommender(Configuration.instance.configuration == Config.BEST);
                System.out.println("Particle Swarm Optimization:\nNumParticles:" + psoRecommender.numParticles + "\nGenerations:" + psoRecommender.generations + "\nminV:" + psoRecommender.minV +
                        "\nmaxV:" + psoRecommender.maxV + "\nc1:" + psoRecommender.c1 + "\nc2:" + psoRecommender.c2 + "\ninertia:" + psoRecommender.inertia + "\n-------------------------------------------------");

                //Create Particle Swarm Optimization
                ParticleSwarmOptimization pso = new ParticleSwarmOptimization(psoRecommender.numParticles,psoRecommender.maxV,psoRecommender.minV,
                        psoRecommender.c1,psoRecommender.c2,psoRecommender.inertia,k,Configuration.instance.randomGenerator);
                //Run Algorithm
                for(int i = 0; i < psoRecommender.generations; i++)
                {
                    long genStart = System.currentTimeMillis();
                    pso.run();
                    if(Configuration.instance.dMode == DebugMode.CONSOLE || Configuration.instance.dMode == DebugMode.FILECONSOLE)
                        System.out.println("Generation " + (i+1) + " complete. (" + (System.currentTimeMillis() - genStart)/1000f + "s) --> Best: " + pso.getBestScore() + " Average: " + pso.getAverageScore());
                }
                //Store best result
                bestResult = pso.getBestScore();
            }

            //Print out best result
            System.out.println("Completed in " + (System.currentTimeMillis() - start)/1000f + " seconds.\nSeed:" + seed + "\nBest Result --> " + bestResult);
        }
        else //Search for best configuration
        {
            if(Configuration.instance.mode == HeuristicMode.SA)
            {
                System.out.println("Starting search for best SA configuration...");
                searchBestSA(k);
                System.out.println("Done.");
            }
            else if(Configuration.instance.mode == HeuristicMode.ACO)
            {
                System.out.println("Starting search for best ACO configuration...");
                searchBestACO(k);
                System.out.println("Done.");
            }
            else if(Configuration.instance.mode == HeuristicMode.PSO)
            {
                System.out.println("Starting search for best PSO configuration...");
                searchBestPSO(k);
                System.out.println("Done.");
            }
            else if(Configuration.instance.mode == HeuristicMode.GA)
            {
                System.out.println("Starting search for best GA configuration...");
                searchBestGA(k);
                System.out.println("Done.");
            }
        }


    }

    //I took the average of 10 trials to determine the best score

    private static void searchBestACO(Knapsack k)
    {
        float bestScore = 0.0f;

        int bestNumAnts = 0;
        int generation = 200; //Normally Converges much faster so I saw no need to waste time playing with the number of generations.
        float bestAlpha = 0.0f;
        float bestBeta = 0.0f;
        float bestPhDecay = 0.0f;
        float bestExploreRate = 0.0f;

        for(int i = 10; i <= 100; i+=10) { //Number of ants
            for (float alpha = 0.5f; alpha <= 1.0f; alpha += 0.05f) { //Alpha
                for (float beta = 0.5f; beta <= 1.0f; beta += 0.05) { //Beta
                    for (float phDecay = 0.05f; phDecay <= 0.8f; phDecay += 0.05) { //Pheromone Decay
                        for(float expRate = 0.01f; expRate <= 0.1f; expRate += 0.01f) //Explore Rate
                        {
                            System.out.println("Ants= " + i + " alpha=" + alpha + " beta=" + beta + " phDecay=" + phDecay + " exploreRate= " + expRate);
                            float sum = 0.0f;
                            for (int j = 0; j < 10; j++) { //Average Results Over 10 Iterations
                                AntColonyOptimization aco = new AntColonyOptimization(k, Configuration.instance.randomGenerator, i, phDecay, expRate, alpha, beta);
                                for (int gen = 0; gen < generation; gen++) {
                                    aco.doGeneration();
                                }
                                sum += aco.getBestScore();
                            }
                            //Update Best Score
                            if (bestScore < sum / 10) {
                                bestScore = sum;
                                bestNumAnts = i;
                                bestAlpha = alpha;
                                bestBeta = beta;
                                bestPhDecay = phDecay;
                                bestExploreRate = expRate;
                            }
                        }

                    }
                }
            }
        }
        //Write Configuration to file
        new ACORecommender(bestNumAnts,generation,bestAlpha,bestBeta,bestPhDecay,bestExploreRate).writeFile(true);

    }

    public static void searchBestPSO(Knapsack k)
    {
        float bestScore = 0.0f;
        int bestNumParticles = 0;
        int generations = 200;
        float bestMinV  = 0.0f;
        float bestMaxV = 0.0f;
        float bestc1 = 0.0f;
        float bestc2 = 0.0f;
        float bestInertia = 0.0f;

        for(int particles = 10; particles < 100; particles+=10) //Number of Particles
        {
            for(float minV = -10.0f; minV < 0; minV += 0.5f) //MinV
            {
                for(float maxV = 10.0f; maxV > 0; maxV-=0.5f) //MaxV
                {
                    for(float c1 = 0.05f; c1 <= 1.0f; c1 += 0.05) //C1
                    {
                        for(float c2 = 0.05f; c2 <= 1.0f; c2 += 0.05) //C2
                        {
                            for(float inertia = 0.8f; inertia <= 1.2f; inertia +=0.05) //Inertia
                            {
                                System.out.println("Particles= " + particles + " minV= " + minV + " maxV= " + maxV + " c1=" + c1 + " c2=" + c2 + " inertia=" + inertia);
                                float sum = 0.0f;
                                for(int i = 0; i < 10; i++) //Average score over 10 iterations
                                {
                                    ParticleSwarmOptimization pso = new ParticleSwarmOptimization(particles,maxV,minV,c1,c2,inertia,k,Configuration.instance.randomGenerator);
                                    for(int j = 0; j < generations; j++)
                                    {
                                        pso.run();
                                    }
                                    sum += pso.getBestScore();
                                }
                                //Update best score
                                if(bestScore < sum/10)
                                {
                                    bestScore = sum/10;
                                    bestNumParticles = particles;
                                    bestMinV = minV;
                                    bestMaxV = maxV;
                                    bestc1 = c1;
                                    bestc2 = c2;
                                    bestInertia = inertia;
                                }

                            }
                        }
                    }
                }
            }
        }
        //Write Configuration to file
        new PSORecommender(bestNumParticles,generations,bestMinV,bestMaxV,bestc1,bestc2,bestInertia).writeFile(true);

    }

    public static void searchBestSA(Knapsack k)
    {
        float bestScore = 0.0f;
        float bestTemperature = 0.0f;
        float bestCoolingRate = 0.0f;
        //For Temperature
        for(float temp = 100; temp <= 10000.0f; temp +=100) //Temperature
        {
            for(float coolingRate = 0.001f; coolingRate <= 0.2f; coolingRate += 0.001f) //Cooling Rate
            {
                System.out.println("Temp= " + temp + " Cooling rate= " + coolingRate);
                float sum = 0.0f;
                for(int i = 0; i < 10; i ++) //Average results over 10 iterations
                {
                    SimulatedAnnealing sa = new SimulatedAnnealing(temp,coolingRate, k, Configuration.instance.randomGenerator,SimulatedAnnealing.AnnealMode.STD);
                    while (sa.getTemperature() > 1f) {
                        sa.doCycle();
                    }
                    sum += sa.getBestScore();
                }
                if(bestScore < sum/10) //Update best score
                {
                    bestScore = sum/10;
                    bestTemperature = temp;
                    bestCoolingRate = coolingRate;
                }
            }
        }
        //Write best configuration
        new SARecommender(bestTemperature,bestCoolingRate).writeFile(true);

    }

    public static void searchBestGA(Knapsack k)
    {
        float bestScore = 0.0f;

        int bestCapacity = 0;
        int generations = 200;
        float bestMutationRate = 0.0f;
        float bestCrossoverRate = 0.0f;
        float bestElitismRatio = 0.0f;

        String[] crossOverFuncs = {"onepoint","twopoint"};
        KnapsackGAManager.CrossoverOperator cOps[] = {KnapsackGAManager.CrossoverOperator.ONEPOINT,KnapsackGAManager.CrossoverOperator.TWOPOINT};
        String bestCrossOverFunc = "";

        String[] selectionFuncs = {"roulette","tournament"};
        KnapsackGAManager.SelectionOperator sOps[] = {KnapsackGAManager.SelectionOperator.ROULETTE,KnapsackGAManager.SelectionOperator.TOURNAMENT};
        String bestSelectionFunc = "";

        String[] mutationFunc = {"bitflip","displacement","exchange","insertion","inversion","reverse"};
        KnapsackGAManager.MutationOperator mOps[] = {KnapsackGAManager.MutationOperator.BITFLIP,KnapsackGAManager.MutationOperator.DISPLACEMENT,KnapsackGAManager.MutationOperator.EXCHANGE,
                KnapsackGAManager.MutationOperator.INSERTION,KnapsackGAManager.MutationOperator.INVERSION,KnapsackGAManager.MutationOperator.REVERSE};
        String bestMutateFunc = "";


        for(int cap = 100; cap < 1000; cap+=50) //Population Capacity
        {
            for(float mutationRate = 0.01f; mutationRate <= 0.1f; mutationRate +=0.01) //Mutation Rate
            {
                for(float crossOverRate = 0.6f; crossOverRate<=1.0f; crossOverRate += 0.05) //CrossOver rate
                {
                    for(float elitism = 0; elitism <= 0.1f; elitism +=0.01) //Elitism
                    {
                        for(int c = 0; c < crossOverFuncs.length; c++) //CrossOver Function
                        {
                            for(int s = 0; s < selectionFuncs.length; s++) //Selection Function
                            {
                                for(int m = 0; m < mutationFunc.length; m++) //Mutation Function
                                {
                                    System.out.println("Capacity= " + cap + " mutatationRate= " + mutationRate + " crossOverRate=" + crossOverRate + " elitism= " +elitism + " CrossOver Operator= " + crossOverFuncs[c]
                                    + " Selection Function= " + selectionFuncs[s] + " Mutation Operator= " + mutationFunc[m]);
                                    float sum = 0.0f;
                                    for(int i = 0; i < 10; i++) //Average results over 10 iterations
                                    {
                                        KnapsackGAManager ga = KnapsackGAManager.KnapsackCreator(cap,k.GetTable().size(),k,Configuration.instance.randomGenerator,mutationRate,mOps[m],
                                                elitism,sOps[s],crossOverRate,cOps[c],KnapsackGAManager.GAMODE.STD);
                                        for(int g = 0; g < generations; g++)
                                        {
                                            ga.DoCylce();
                                        }
                                        sum += ga.GetBestAgent().GetFitness();
                                    }
                                    if(bestScore < sum/10) //Update best score
                                    {
                                        bestScore = sum/10;
                                        bestCapacity = cap;
                                        bestMutationRate = mutationRate;
                                        bestCrossoverRate = crossOverRate;
                                        bestElitismRatio = elitism;
                                        bestCrossOverFunc = crossOverFuncs[c];
                                        bestSelectionFunc = selectionFuncs[s];
                                        bestMutateFunc = mutationFunc[m];
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //Write Configuration to file
        new GARecommender(bestCapacity,generations,bestMutationRate,bestCrossoverRate,bestElitismRatio,bestCrossOverFunc,bestMutateFunc,bestSelectionFunc).writeFile(true);


    }

}