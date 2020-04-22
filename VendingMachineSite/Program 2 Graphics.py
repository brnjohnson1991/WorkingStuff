#Bradley Johnson, 010, 2/27/16, brnjohnson1991@gmail.com
#This program generates a fraction comparison graphic game
#Preconditions: Difficulty input, a click answer
#Postconditions: Fractions to be compared, difficulty readout, correct/incorrect statement 


from random import randint
from graphics import *

def main():
    win = GraphWin("Comparing Fractions", 600, 600)
    
#This section creates a window and introductory text
    title = Text(Point(300, 50),"Can you compare fractions")
    title.draw(win)
    
    diftxt = Text (Point (300, 75), "difficulty ranges from 1 to 3, 1 is easy")
    diftxt.draw(win)

#This section lets the user click in the input box and select a difficulty

    input = Entry(Point(300, 150),5)
    input.setText("    ")
    input.draw(win)

    win.getMouse()

    Difficulty = float(input.getText())  
    Difficulty = int(Difficulty)


#This section converts non 1-3 difficulties into 1,2, or 3    
    if Difficulty <= 1:
        Difficulty = 1
    if Difficulty >= 3:
        Difficulty = 3
          


#This section visually divides the window into left and right with colors  
    Red = Rectangle(Point(0,0), Point (300,600))
    Red.draw(win)
    Red.setFill("Red")
    
    Red = Rectangle(Point(300,0), Point (600,600))
    Red.draw(win)
    Red.setFill("Blue")    

#This section randomly generates the numbers used in the fractions
    
    if Difficulty == 1:
        Num1 = randint(1,9)
        Num2 = randint(1,9)
        Den1 = randint(1,9)
        Den2 = randint(1,9)
    elif Difficulty == 2:
        Num1 = randint(5,15)
        Num2 = randint(5,15)
        Den1 = randint(5,15)
        Den2 = randint(5,15)
    else:
        Num1 = randint(10,18)
        Num2 = randint(10,18)
        Den1 = randint(10,18)
        Den2 = randint(10,18)    

#This section checks if the denominators are equal and changes one if so
        
    while Den1 == Den2:
        if Difficulty <=1:
            Den2 = randint(1,9)
        elif Difficulty == 2:
            Den2 = randint(5,15)
        elif Difficulty >= 3:
            Den2 = randint(5,15)

# This section checks if the fractions are euivalent and changes one of the numerators if so
    while (Num1 / Den1) == (Num2 / Den2):
        if Difficulty <=1:
            Num1 = randint(1,9)
        elif Difficulty == 2:
            Num1 = randint(5,15)
        elif Difficulty >= 3:
            Num1 = randint(10,18)

#this section displays the question, difficulty,the fractions, and division symbols    

    Question = Text(Point(300,200), "Which is less: Left or Right?")       
    Question.draw(win)
    
    DifDisplay1 = Text(Point (55,75), "The difficulty is")       
    DifDisplay1.draw(win)
    
    DifDisplay2 = Text(Point (115,75), str(Difficulty))
    DifDisplay2.draw(win)
    
    
    Dis_Num1 = Text(Point(150,250),Num1)
    Dis_Num1.draw(win)
    
    Dis_Num2 = Text(Point(450,250),Num2)
    Dis_Num2.draw(win)
    
    Dis_Den1 = Text(Point(150,300),Den1)
    Dis_Den1.draw(win) 
    
    Dis_Den2 = Text(Point(450,300),Den2)
    Dis_Den2.draw(win)    
    
    Divisor1 = Rectangle(Point(125,270), Point(175,272))
    Divisor1.draw(win)
    Divisor1.setFill("Black")
    
    Divisor2 = Rectangle(Point(425,270), Point(475,272))
    Divisor2.draw(win)
    Divisor2.setFill("Black")

#This section inputs a click and converts it into either a "L" or "R" 
    
    Click = win.getMouse()
    Answer = Click.getX()
    if Answer < 300:
        Answer = str("L")
    elif Answer > 300:
        Answer = str("R")
        
    Click = Circle(Click,5)
    Click.draw(win)
    Click.setFill("Yellow")
        
#This section creates a correct variable and assigns it either an "L" or "R" based on the numbers generated above
        
    Correct = ("abg")
    if (Num1 * Den2) < (Num2 * Den1):
        Correct = str("L")
    elif(Num1 * Den2) > (Num2 * Den1):
        Correct = str("R")

#This section compares the answer and correct variables and displays text based on their equivalence
        
    if Answer == Correct:
        Dis_Cor = Text(Point(300,500),"you're right!")
        Dis_Cor.draw(win)
    if Answer != Correct:
        Dis_Wrong = Text(Point(300,500), "sorry!, you're wrong!")
        Dis_Wrong.draw(win)
        
        
    
#this section lets the user click to close the program   
    prompt = Text(Point(300, 550),"Click to end")
    prompt.draw(win)
    win.getMouse()
    win.close()
    
    
main()