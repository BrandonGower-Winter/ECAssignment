package algorithm.ga.evolution.crossover;

import random.MersenneTwisterFast;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;

public class TwoPointCrossover<T> extends CrossOverFunction<T>
{
    private MersenneTwisterFast randomizer;

    public TwoPointCrossover(MersenneTwisterFast randomizer)
    {
        this.randomizer = randomizer;
    }

    @Override
    public ArrayList<ArrayList<T>> CrossOver(ArrayList<T> parent, ArrayList<T> otherParent)
    {
        Queue<Integer> queue = new ArrayDeque<>();
        for(int i = 0; i < 2; i++)
        {
            if(i == 0)
                queue.add(randomizer.nextInt(parent.size()-(2) - 1) + 1);
            else
                queue.add(randomizer.nextInt(parent.size()-(2-queue.size()) - queue.peek())+queue.peek() + queue.size());
        }

        ArrayList<ArrayList<T>> children = new ArrayList<>();

        children.add(new ArrayList<T>());
        children.add(new ArrayList<T>());

        for(int i = 0; i < parent.size(); i++)
        {
            if(queue.size() > 0 && i >= queue.peek())
            {
                    queue.remove();
                    ArrayList<T> tempChild = children.get(0);
                    children.set(0, children.get(1));
                    children.set(1, tempChild);
            }


            children.get(0).add(parent.get(i));
            children.get(1).add(otherParent.get(i));


        }
        return children;
    }
}
