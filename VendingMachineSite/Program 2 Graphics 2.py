#Bradley Johnson, 010, 2/27/16, brnjohnson1991@gmail.com
#This program generates a fraction comparison graphic game
#Preconditions: Difficulty input, left or right answer answer
#Postconditions: Fractions to be compared, difficulty readout, correct/incorrect statement 



import random
import graphics
from random import randint
from graphics import *

def main():
   
        
    win = GraphWin("Comparing Fractions", 300, 300)
        
    title = Text (Point(150, 50), "Compare Fractions")
    title.draw(win)
        
    diftxt = Text (Point (150, 75), "difficulty ranges from 1 to 3, 1 is easy")
    diftxt.draw(win)
        
    input = Entry(Point(150, 150),5)
    input.setText("    ")
    input.draw(win)
    
    win.getMouse()
    
    number = str(input.getText())  # the very line that does the input!
    
    win.getmouse()
    
main()