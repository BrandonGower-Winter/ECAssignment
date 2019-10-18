package algorithm.sa.recommender;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class SARecommender
{
    public float temperature;
    public float coolingRate;

    public SARecommender(boolean best)
    {
        if(best)
            readFile("SA_BEST.xml");
        else
            readFile("SA_DEFAULT.xml");
    }

    public SARecommender(float temperature, float coolingRate)
    {
        this.temperature = temperature;
        this.coolingRate = coolingRate;
    }

    //Read SA Configurations
    public void readFile(String file)
    {
        try {
            Scanner scFile = new Scanner(new File("./data/Recommenders/" + file));
            temperature = Float.parseFloat(scFile.nextLine().replaceAll("<temperature>","").replaceAll("</temperature>",""));
            coolingRate = Float.parseFloat(scFile.nextLine().replaceAll("<cRate>","").replaceAll("</cRate>",""));
            scFile.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " +  file + " not found. Could not load recommender.");
        }
    }
    //Write SA Configurations
    public void writeFile(boolean best)
    {
        String file = best ? "SA_BEST.xml" : "SA_DEFAULT.xml";
        try {
            FileWriter fw = new FileWriter("./data/Recommenders/" + file);
            fw.write("<temperature>" + temperature + "</temperature>\n");
            fw.write("<cRate>" + coolingRate + "</cRate>");

            fw.close();
        } catch (IOException e) {
            System.out.println("File " +  file + " not found. Could not load recommender.");
        }
    }
}
