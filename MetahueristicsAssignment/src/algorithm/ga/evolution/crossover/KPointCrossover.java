package algorithm.ga.evolution.crossover;

import random.MersenneTwisterFast;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;

//KPoint CrossOver function
public class KPointCrossover<T> extends CrossOverFunction<T>
{
    private MersenneTwisterFast randomizer;
    private int k = 2;
    public KPointCrossover(int k, MersenneTwisterFast randomizer)
    {
        this.k = k;
        this.randomizer = randomizer;
    }

    @Override
    public ArrayList<ArrayList<T>> CrossOver(ArrayList<T> parent, ArrayList<T> otherParent)
    {
        Queue<Integer> queue = new ArrayDeque<>();
        for(int i = 0; i < k; i++) //Create k crossover points
        {
            if(i == 0)
                queue.add(randomizer.nextInt(parent.size()-(k) - 1) + 1);
            else
                queue.add(randomizer.nextInt(parent.size()-(k-queue.size()) - queue.peek())+queue.peek() + queue.size());
        }

        ArrayList<ArrayList<T>> children = new ArrayList<>();

        children.add(new ArrayList<T>());
        children.add(new ArrayList<T>());

        //look over parents
        for(int i = 0; i < parent.size(); i++)
        {
            if(queue.size() > 0 && i >= queue.peek()) //pop of of queue when solution is found and swap children position in list
            {
                    queue.remove();
                    ArrayList<T> tempChild = children.get(0);
                    children.set(0, children.get(1));
                    children.set(1, tempChild);
            }

            //Add alleles to children
            children.get(0).add(parent.get(i));
            children.get(1).add(otherParent.get(i));


        }
        return children;
    }
}
