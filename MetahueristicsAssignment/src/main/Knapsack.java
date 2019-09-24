package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Scanner;

public class Knapsack
{

    private Dictionary<Integer,KnapsackItem> lookupTable;
    private int maxWeight;

    public Knapsack(int maxWeight)
    {
        this.maxWeight = maxWeight;
        lookupTable = new Hashtable<>();
    }

    public Knapsack(int maxWeight, String dataPath)
    {
        this.maxWeight = maxWeight;
        lookupTable = new Hashtable<>();

        try
        {
            Scanner sc = new Scanner(new File(dataPath));
            while (sc.hasNextLine())
            {
                Scanner scLine = new Scanner(sc.nextLine()).useDelimiter(";");
                lookupTable.put(scLine.nextInt() - 1, new KnapsackItem(scLine.nextInt(),scLine.nextInt()));
                scLine.close();
            }
            sc.close();
        }
        catch (FileNotFoundException e)
        {
            System.err.println("File " + dataPath + " not found");
        }

    }

    public Dictionary<Integer,KnapsackItem> GetTable()
    {
        return  lookupTable;
    }
    public int GetMaxWeight()
    {
        return  maxWeight;
    }


    public void Add(int key, KnapsackItem item)
    {
        lookupTable.put(key,item);
    }

    public int GetWeight(ArrayList<Boolean> encoding)
    {
        int weight = 0;
        for (int i = 0; i < encoding.size(); i++)
        {
            if (encoding.get(i))
            {
                weight+= lookupTable.get(i).GetWeight();
            }
        }
        return weight;
    }
}
