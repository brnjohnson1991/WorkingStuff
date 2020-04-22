# Bradley Johnson, brnjohnson1991@gmail.com, 010, 2/6/2016
# Program purpose- calculate the number of chips of a given size that can be cut from a  waifer of a given diameter
# pre-conditions - area of the die and diameter of the wafer
# post conditions - outputs area of the wafer and number of chips that can be cut from it along with a disclaimer.

import math
from math import pi, sqrt

def main():
    
    # This section prints an introductory statement about wafers
    print ("     +++ Slicing Wafers+++     \n")
    #This section gathers input from the user
    Diameter =  float(input("What is the diameter of the wafer to slice? (in mm) "))
    D_Area = float(input("What is the area of a single die? (in mm^2) "))
    
   
    #This section calculates the area of the wafer from the given diameter
    Radius = Diameter/2
    W_Area = (Radius**2)*pi
    
    #This section calculates the dies per wafer (DPW) with the given formula
    
    DPW = Diameter * pi  * (Diameter/(4 * D_Area) - 1/sqrt(2 * D_Area))
    
    #This section outputs the area of the wafer rounded to two decimals and number of dies
    print ("From a wafer with area", round(W_Area,2), "square millimeters you can cut", (int(DPW)), "dies.")
    
    #This section outputs a disclaimer
    print ("This does not take into account defective dies, alignment markings, and test sites \non the wafer's surface")
main()
