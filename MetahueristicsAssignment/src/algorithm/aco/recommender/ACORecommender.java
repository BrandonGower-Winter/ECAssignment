package algorithm.aco.recommender;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ACORecommender {
    public int numAnts;
    public int generations;
    public float alpha;
    public float beta;
    public float pheromoneDecay;
    public float exploreRate;


    public ACORecommender(boolean best)
    {
        if(best)
            readFile("ACO_BEST.xml");
        else
            readFile("ACO_DEFAULT.xml");
    }

    public ACORecommender(int numAnts, int generations, float alpha, float beta, float pheromoneDecay, float exploreRate) {
        this.numAnts = numAnts;
        this.generations = generations;
        this.alpha = alpha;
        this.beta = beta;
        this.pheromoneDecay = pheromoneDecay;
        this.exploreRate = exploreRate;
    }

    public void readFile(String file)
    {
        try {
            Scanner scFile = new Scanner(new File("./data/Recommenders/" + file));
            numAnts = Integer.parseInt(scFile.nextLine().replaceAll("<numAnts>","").replaceAll("</numAnts>",""));
            generations = Integer.parseInt(scFile.nextLine().replaceAll("<generations>","").replaceAll("</generations>",""));
            alpha = Float.parseFloat(scFile.nextLine().replaceAll("<alpha>","").replaceAll("</alpha>",""));
            beta = Float.parseFloat(scFile.nextLine().replaceAll("<beta>","").replaceAll("</beta>",""));
            pheromoneDecay = Float.parseFloat(scFile.nextLine().replaceAll("<pheromone>","").replaceAll("</pheromone>",""));
            exploreRate = Float.parseFloat(scFile.nextLine().replaceAll("<explore>","").replaceAll("</explore>",""));
            scFile.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " +  file + " not found. Could not load recommender.");
        }
    }

    public void writeFile(boolean best)
    {
        String file = best ? "ACO_BEST.xml" : "ACO_DEFAULT.xml";
        try {
            FileWriter fw = new FileWriter("./data/Recommenders/" + file);
            fw.write("<numAnts>" + numAnts + "</numAnts>\n");
            fw.write("<generations>" + generations + "</generations>\n");
            fw.write("<alpha>" + alpha + "</alpha>\n");
            fw.write("<beta>" + beta + "</beta>\n");
            fw.write("<pheromone>" + pheromoneDecay + "</pheromone>\n");
            fw.write("<explore>" + exploreRate + "</explore>");
            fw.close();
        } catch (IOException e) {
            System.out.println("File " +  file + " not found. Could not load recommender.");
        }
    }

}
