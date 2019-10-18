import sys

if __name__ == '__main__':

    knapsack_filename = sys.argv[1]
    encoding = list(sys.argv[2])
    maxWeight = 822
    optimum = 1013

    knapsack = {}

    #Read Knapsack
    knapsack_file = open(knapsack_filename,'r')
    for line in knapsack_file.readlines():
        values = line.split(';')
        knapsack[int(values[0])] = [int(values[1]),int(values[2])]

    knapsack_file.close()

    weight = 0;
    value = 0;

    for i in range(0,len(encoding)):
        if encoding[i] == '1':
            weight += knapsack[i+1][0]
            value += knapsack[i+1][1]

    print("Total Value: " + str(value) + " Total Weight: " + str(weight) + " Is Valid: " + str(weight<=maxWeight) + " Optima %: " + str(value/float(optimum) * 100.0))