import random
from random import randint

def main():
    Choice = input("Do You want to display individual rolls? (Y or N) ")
    while 1==1:
        Dice = int(input("\n\nHow many Dice do you want to roll? "))
        
        Hits = 0
        One = 0
        Count = 0
        
        while Count != Dice:
            Roll = randint(1,6) 
            if Choice == ("Y") or Choice == ("y"):
                    print(Roll)
            if Roll == 5 or Roll == 6:
                    Hits = Hits + 1
                    Count = Count + 1
            if Roll == 1:
                    One = One + 1
                    Count = Count + 1
            elif Roll == 2 or Roll == 3 or Roll == 4:
                    Count = Count + 1
                    
        print(Hits, "hits!")
        
        if One > Hits:
            print ("Glitch! (",One,"ones! )")
            if Hits == 0:
                print("!!! Critical Glitch !!!")
            
main()
               
    
               