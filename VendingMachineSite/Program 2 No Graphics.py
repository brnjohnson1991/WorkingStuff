#Bradley Johnson, 010, 2/25/16

import random
from random import randint

def main():
   

#Title statement

    print("Can you compare fractions?\n")
      
#This section inputs an integer difficulty setting
    
    Difficulty = float(input("Difficulty 1-3, 1 easy, 3 hard\nWhat difficulty? (1-3) "))
    Difficulty = int(Difficulty)
    
    if Difficulty <= 1:
        Difficulty = 1
    if Difficulty >= 3:
        Difficulty = 3
    
    print("\nDifficulty ",Difficulty)
    
    #This section generates random numbers based on the difficulty
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
        
    while Den1 == Den2:
        if Difficulty <=1:
            Den2 = randint(1,9)
        elif Difficulty == 2:
            Den2 = randint(5,15)
        elif Difficulty >= 3:
            Den2 = randint(5,15)
    
    
#This section checks to see if the denominators are equivalent and changes them if so            
    while (Num1 / Den1) == (Num2 / Den2):
        if Difficulty <=1:
            Num1 = randint(1,9)
        elif Difficulty == 2:
            Num1 = randint(5,15)
        elif Difficulty >= 3:
            Num1 = randint(10,18)
            
            
#prints the fractions    
    print()
    print(Num1,"/", Den1,"   ", Num2,"/", Den2)
    
    Answer = input("\nWhich is less, the left (L) or the right (R)? ")
    

#This section forces the user to input a valid answer choice    
    while Answer is not "L" and Answer is not "R":
        Answer = input("Please enter a valid answer ('L' or 'R') ")
    
    
#This section performs the calculation and assigns the variable Correct a corresponding value    
    Correct = ("abg")
    if (Num1 * Den2) < (Num2 * Den1):
        Correct = ("L")
    elif(Num1 * Den2) > (Num2 * Den1):
        Correct = ("R")

#this section outputs a statement telling the user if they got the question right        
    if (Answer == Correct):
        print("\nyou're right!!")
    else:
        print("\nsorry! you're wrong")
        
main()




    
    
    
    