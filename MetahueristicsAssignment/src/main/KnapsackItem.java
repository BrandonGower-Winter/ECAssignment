package main;

public class KnapsackItem {

    private int weight;
    private int value;

    public KnapsackItem(int w, int v)
    {
        weight = w;
        value = v;
    }

    public int GetWeight()
    {
        return weight;
    }

    public int GetValue()
    {
        return value;
    }
}
