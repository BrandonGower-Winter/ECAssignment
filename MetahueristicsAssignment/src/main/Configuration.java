package main;

import random.MersenneTwisterFast;

public enum Configuration {
    instance;

    public String fileSeparator = System.getProperty("file.separator");
    public String userDirectory = System.getProperty("user.dir");
    public String dataDirectory = userDirectory + fileSeparator + "data" + fileSeparator;
    public String dataFilePath = dataDirectory + "knapsack_instance.csv";
    public String dataRDirectory = userDirectory;

    public MersenneTwisterFast randomGenerator = new MersenneTwisterFast(System.nanoTime());

    public int numberOfItems = 150;
    public int maximumCapacity = 822;
    public int bestKnownOptimum = 1013;
}