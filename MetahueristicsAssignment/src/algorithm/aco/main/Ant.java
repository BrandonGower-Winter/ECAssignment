package algorithm.aco.main;

import main.Knapsack;

import java.util.ArrayList;
import java.util.HashMap;

public class Ant
{

    private ArrayList<Boolean> path;
    private Knapsack k;
    private int noOfItems;

    public Ant(Knapsack k)
    {

        this.k = k;
        noOfItems = 0;
        path = k.getEmptyKnapsack();
    }

    public ArrayList<Boolean> getSolution()
    {
        return path;
    }

    //Add destination
    public void add(int i)
    {
        path.set(i,true);
        noOfItems ++;
    }

    public int numOfItems()
    {
        return noOfItems;
    }

    public void layPheromone(float pheromone, HashMap<Integer,Float> pheromoneTable)
    {
        for(int i = 0; i < path.size(); i++)
        {
            if(path.get(i))
            {
                pheromoneTable.put(i,pheromoneTable.get(i) + pheromone);
            }
        }
    }

}
