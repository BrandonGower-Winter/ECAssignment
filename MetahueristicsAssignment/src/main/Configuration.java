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

    public HeuristicMode mode = HeuristicMode.PSO; //Program defaults to PSO (Best solution)
    public DebugMode dMode = DebugMode.NONE; //Defaults to no debug mode
    public Config configuration = Config.DEFAULT; //Defaults to default configuration

    public boolean searchBest = false; //Used to determine whether configuration search was selected
    public boolean findBestAlgo = false; //Used to find best algorithm

    public void Configure(String args[], long seed)
    {
        randomGenerator.setSeed(seed); //Set Random Number Generator

        for(int i = 0; i < args.length; i++) //Parse Parameters
        {
            switch (args[i])
            {
                case "-algorithm":
                    i++;
                    mode = parseAlgorithm(args[i]);
                    break;
                case "-mode":
                    i++;
                    dMode = parseMode(args[i]);
                    break;
                case "-configuration":
                    i++;
                    configuration = parseConfig(args[i]);
                    break;
                case "-search_best_configuration":
                    searchBest = true;
                    break;
                case "-find_best_algorithm":
                    findBestAlgo = true;
                    break;
                default:
                    System.err.println("Unrecognized Option: " + args[i]);
            }
        }
    }

    private Config parseConfig(String arg)
    {
        if(arg.compareTo("best") == 0)
            return Config.BEST;
        else
            return Config.DEFAULT;
    }

    private DebugMode parseMode(String arg) {
        switch (arg)
        {
            case "file":
                return DebugMode.FILE;
            case "console":
                return DebugMode.CONSOLE;
            case "fileconsole":
                return DebugMode.FILECONSOLE;
        }
        return DebugMode.NONE;
    }

    private HeuristicMode parseAlgorithm(String arg)
    {
        switch (arg)
        {
            case "ga":
                return HeuristicMode.GA;
            case "sa":
                return HeuristicMode.SA;
            case "aco":
                return HeuristicMode.ACO;
            case "pso":
            case "best-algorithm":
                return HeuristicMode.PSO;
        }
        return null;
    }


}

enum DebugMode //An enum used to determine the output conditions
{
    NONE,
    FILE,
    CONSOLE,
    FILECONSOLE
}

enum HeuristicMode //Enum  of the Algorithms implemented in this assignment
{
    GA,
    SA,
    ACO,
    PSO
}

enum Config //Enum for keeping track of which Configuration to use
{
    DEFAULT,
    BEST
}
