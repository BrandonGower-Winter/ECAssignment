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

    public HeuristicMode mode = HeuristicMode.GA;
    public DebugMode dMode = DebugMode.NONE;
    public Config configuration = Config.DEFAULT;

    public boolean searchBest = false;


    public void Configure(String args[], long seed)
    {
        randomGenerator.setSeed(seed);

        for(int i = 0; i < args.length; i++)
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
                return HeuristicMode.PSO;
        }
        return null;
    }


}

enum DebugMode
{
    NONE,
    FILE,
    CONSOLE,
    FILECONSOLE
}

enum HeuristicMode
{
    GA,
    SA,
    ACO,
    PSO
}

enum Config
{
    DEFAULT,
    BEST
}
