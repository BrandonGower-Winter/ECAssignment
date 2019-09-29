import matplotlib.pyplot as plt

import sys
import os

if __name__ == '__main__':
    filename =  sys.argv[1];
    print("Searching File " + filename)

    tempPlot = []
    bestplot = []
    currentPlot = []

    file = open(filename,'r')
    fileLines = file.readlines()
    title = fileLines.pop(0)
    for log in fileLines:
        entry = log.split(',')
        tempPlot.append(float(entry[1]))
        bestplot.append(float(entry[2]))
        currentPlot.append(float(entry[3]))

    plt.plot(tempPlot,bestplot, 'b', label='Best')
    plt.plot(tempPlot, currentPlot, ':r', label='Current')
    plt.title('Simulated Annealing Summary\n' + title)
    plt.xlabel('Temperature')
    plt.ylabel('Score')
    plt.legend(loc='best')  # legend text comes from the plot's label parameter.
    plt.show()