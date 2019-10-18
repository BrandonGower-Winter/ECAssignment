package algorithm.pso.recommender;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class PSORecommender
{
    public int numParticles;
    public int generations;
    public float minV, maxV;
    public float c1, c2;
    public float inertia;

    public PSORecommender(boolean best)
    {
        if(best)
            readFile("PSO_BEST.xml");
        else
            readFile("PSO_DEFAULT.xml");
    }

    public PSORecommender(int numParticles, int generations, float minV, float maxV, float c1, float c2, float inertia) {
        this.numParticles = numParticles;
        this.generations = generations;
        this.minV = minV;
        this.maxV = maxV;
        this.c1 = c1;
        this.c2 = c2;
        this.inertia = inertia;
    }

    public void readFile(String file)
    {
        try {
            Scanner scFile = new Scanner(new File("./data/Recommenders/" + file));
            numParticles = Integer.parseInt(scFile.nextLine().replaceAll("<numParticles>","").replaceAll("</numParticles>",""));
            generations = Integer.parseInt(scFile.nextLine().replaceAll("<generations>","").replaceAll("</generations>",""));
            minV = Float.parseFloat(scFile.nextLine().replaceAll("<minV>","").replaceAll("</minV>",""));
            maxV = Float.parseFloat(scFile.nextLine().replaceAll("<maxV>","").replaceAll("</maxV>",""));
            c1 = Float.parseFloat(scFile.nextLine().replaceAll("<c1>","").replaceAll("</c1>",""));
            c2 = Float.parseFloat(scFile.nextLine().replaceAll("<c2>","").replaceAll("</c2>",""));
            inertia = Float.parseFloat(scFile.nextLine().replaceAll("<inertia>","").replaceAll("</inertia>",""));
            scFile.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " +  file + " not found. Could not load recommender.");
        }
    }

    public void writeFile(boolean best)
    {
        String file = best ? "PSO_BEST.xml" : "PSO_DEFAULT.xml";
        try {
            FileWriter fw = new FileWriter("./data/Recommenders/" + file);
            fw.write("<numParticles>" + numParticles + "</numParticles>\n");
            fw.write("<generations>" + generations + "</generations>\n");
            fw.write("<minV>" + minV + "</minV>\n");
            fw.write("<maxV>" + maxV + "</maxV>\n");
            fw.write("<c1>" + c1 + "</c1>\n");
            fw.write("<c2>" + c2 + "</c2>\n");
            fw.write("<inertia>" + inertia + "</inertia>");

            fw.close();
        } catch (IOException e) {
            System.out.println("File " +  file + " not found. Could not load recommender.");
        }
    }
}
