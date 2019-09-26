import matplotlib.pyplot as plt

import sys
import os

if __name__ == '__main__':
    filename =  sys.argv[1];
    print("Searching File " + filename)

    bestplot = []
    worstplot = []
    averageplot = []
    benchmark = []

    file = open(filename,'r')
    fileLines = file.readlines()
    title = fileLines.pop(0)
    for log in fileLines:
        entry = log.split(',')
        bestplot.append(float(entry[1]))
        worstplot.append(float(entry[2]))
        averageplot.append(float(entry[3]))
        benchmark.append(1013)

    plt.plot(bestplot, 'g', label='Best')
    plt.plot(worstplot, 'r', label='Worst')
    plt.plot(averageplot, 'b', label="Average")
    plt.plot(benchmark,'c',label='Benchmark')
    plt.title('Fitness Summary\n' + title)
    plt.xlabel('Generation')
    plt.ylabel('Fitness')
    plt.legend(loc='best')  # legend text comes from the plot's label parameter.
    plt.show()