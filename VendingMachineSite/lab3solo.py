import math
from math import sin, radians

def main():
    Repeat = 1
    while Repeat == 1 :
        Height = float(input("Please enter the height in meters "))
        AngleD = float(input("Please enter the height in degrees "))
        AngleR = radians(AngleD)
        PathLength = Height / (sin(AngleR))
        print ("the path is", round(PathLength,1), "meters long.\n\n")
                
main()
