package algorithm.ga.recommender;

import random.MersenneTwisterFast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class GARecommender
{
    public int capacity;
    public int generations;
    public float mutationRate;
    public float crossoverRate;
    public float elitismRatio;

    public GARecommender(int capacity, int generations, float mutationRate, float crossoverRate, float elitismRatio, String crossoverFunc, String mutateFunc, String selectionFunc) {
        this.capacity = capacity;
        this.generations = generations;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.elitismRatio = elitismRatio;
        this.crossoverFunc = crossoverFunc;
        this.mutateFunc = mutateFunc;
        this.selectionFunc = selectionFunc;
    }

    public String crossoverFunc;
    public String mutateFunc;
    public String selectionFunc;

    public GARecommender(boolean best)
    {
        if(best)
            readFile("GA_BEST.xml");
        else
            readFile("GA_DEFAULT.xml");
    }

    public void readFile(String file)
    {
        try {
            Scanner scFile = new Scanner(new File("./data/Recommenders/" + file));
            capacity = Integer.parseInt(scFile.nextLine().replaceAll("<capacity>","").replaceAll("</capacity>",""));
            generations = Integer.parseInt(scFile.nextLine().replaceAll("<generations>","").replaceAll("</generations>",""));
            mutationRate = Float.parseFloat(scFile.nextLine().replaceAll("<mutationRate>","").replaceAll("</mutationRate>",""));
            crossoverRate = Float.parseFloat(scFile.nextLine().replaceAll("<crossoverRate>","").replaceAll("</crossoverRate>",""));
            elitismRatio = Float.parseFloat(scFile.nextLine().replaceAll("<elitismRatio>","").replaceAll("</elitismRatio>",""));
            crossoverFunc = scFile.nextLine().replaceAll("<crossoverFunc>","").replaceAll("</crossoverFunc>","");
            mutateFunc = scFile.nextLine().replaceAll("<mutateFunc>","").replaceAll("</mutateFunc>","");
            selectionFunc = scFile.nextLine().replaceAll("<selectionFunc>","").replaceAll("</selectionFunc>","");
            scFile.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " +  file + " not found. Could not load recommender.");
        }
    }

    public void writeFile(boolean best)
    {
        String file = best ? "GA_BEST.xml" : "GA_DEFAULT.xml";
        try {
            FileWriter fw = new FileWriter("./data/Recommenders/" + file);
            fw.write("<capacity>" + capacity + "</capacity>\n");
            fw.write("<generations>" + generations + "</generations>\n");
            fw.write("<mutationRate>" + mutationRate + "</mutationRate>\n");
            fw.write("<crossoverRate>" + crossoverRate + "</crossoverRate>\n");
            fw.write("<elitismRatio>" + elitismRatio + "</elitismRatio>\n");
            fw.write("<crossoverFunc>" + crossoverFunc + "</crossoverFunc>\n");
            fw.write("<mutateFunc>" + mutateFunc + "</mutateFunc>\n");
            fw.write("<selectionFunc>" + selectionFunc + "</selectionFunc>");
            fw.close();
        } catch (IOException e) {
            System.out.println("File " +  file + " not found. Could not load recommender.");
        }
    }

}
