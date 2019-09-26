import os
import sys

if __name__ == '__main__':
    for file in os.listdir(sys.argv[1]):
        print sys.argv[1] + file
