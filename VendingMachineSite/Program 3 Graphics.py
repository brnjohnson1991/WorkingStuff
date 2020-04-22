from graphics import *
from random import *


def instructions():
    wininst = GraphWin("Instructions for Don't Roll One!",600,600)
    
    title = Text(Point(300, 50),"Instructions for Don't Roll One!")
    title.draw(wininst)    
    
    text1 = Text(Point(300,100),"Player 1 goes first, then player 2")
    text1.draw(wininst)
    
    text2 = Text(Point(300,150),"The player rolls a die and gets points as long as they dont roll a one")
    text2.draw(wininst)
    
    text3 = Text(Point(300,200),"if they roll a one, they lose all points that round")
    text3.draw(wininst)
    
    text4 = Text(Point(300,250),"first player to 100 wins")
    text4.draw(wininst)
    
    text5 = Text(Point(300,300),"click to continue")
    text5.draw(wininst)
    
    wininst.getMouse()
    wininst.close()
    
      

def main():
    
    instructions()
    
    win = GraphWin("Don't Roll a One!", 600,600)
    
    Title = Text(Point(300,50), "Don't Roll ONE!")
    Title.draw(win)
    PlayerN = ("Player1") 

    def click_yes_or_no():
    
    
        YesBox = Rectangle(Point(100,100),Point(200,200))
        YesBox.draw(win)
        YesTxt = Text(Point(150,150), "YES")
        YesTxt.draw(win)
        TLC = Point(100,100)
        BRC = Point(200,200)
        
        NoBox = Rectangle(Point(100,300),Point(200,400))
        NoBox.draw(win)
        NoTxt = Text(Point(150,350), "NO")
        NoTxt.draw(win)
        
        Click = win.getMouse()
        ClickX = Click.getX()
        ClickY = Click.getY()
        
   
        
        def in_box(TLC,BRC,Click):
            ClickX = Click.getX()
            ClickY = Click.getY()            
            TLCX = TLC.getX()
            TLCY = TLC.getY()
            BRCX = BRC.getX()
            BRCY = BRC.getY()
            if TLCX < ClickX < BRCX and TLCY < ClickY < BRCY:
                return True
            else:
                return False
        
        while ClickX < 100 or ClickX > 200:
            Click = win.getMouse()
            ClickX = Click.getX()
        while (100 > ClickY < 200) and (300 > ClickY < 400):
            Click = win.getMouse()
            ClickY = Click.getY()
            
        return in_box(TLC,BRC,Click)
    
    
    click_yes_or_no()
    
    def play_a_round():
        Player1 = True
        Player2 = False
        RoundScore = 0
        Player1Score = 0
        Player2Score = 0
        Roll = 0
        Player = ("Player1")
        Die = Text(Point(150,550),0)
        
        def draw_die(Roll):
            if Roll == 1:
                Die = Text(Point(150,550),1)
                Die.draw(win)
            elif Roll == 2:
                Die = Text(Point(150,550),2)
                Die.draw(win)
            elif Roll == 3:
                Die = Text(Point(150,550),3)
                Die.draw(win)
            elif Roll == 4:
                Die = Text(Point(150,550),4)
                Die.draw(win)
            elif Roll == 5:
                Die = Text(Point(150,550),5)
                Die.draw(win)              
            elif Roll == 6:
                Die = Text(Point(150,550),6)
                Die.draw(win)
            
    
        while Player1Score < 100 and Player2Score < 100:
            
            P1ScoreTxt = Text(Point(450,450),("Player 1's score is ", Player1Score))
            P1ScoreTxt.draw(win)
            P2ScoreTxt = Text(Point(450,500),("Player 2's score is ", Player2Score))
            P2ScoreTxt.draw(win)
            RollAgainText = Text(Point(300,100),("Would you like to roll again", Player))
            RollAgainText.draw(win)
            RollAgain = click_yes_or_no()
            
            if RollAgain == True:
                Roll = randint(1,6)
                if Roll != 1:
                    RoundScore += Roll
                    RollTxt = Text(Point(350,250),("You Rolled a", Roll))
                    RollTxt.draw(win)
                    RScoreTxt = Text(Point(350,300),("Your round score so far is", RoundScore))
                    RScoreTxt.draw(win)
                    draw_die(Roll)
                    RollAgain = click_yes_or_no()
                    
                elif Roll == 1:
                    RoundScore = 0
                    RollTxt = Text(Point(350,250),("You Rolled a", Roll))
                    RollTxt.draw(win)
                    RScoreTxt = Text(Point(350,300),("Your round score so far is", RoundScore))
                    RScoreTxt.draw(win)
                    draw_die(Roll)
                    win.getMouse()
                    RollAgain = False
                
            RollTxt.undraw()
            RScoreTxt.undraw()
            P1ScoreTxt.undraw()
            P2ScoreTxt.undraw()
            RollAgainText.undraw()
            
                
                
            
            if Player1 == True and RollAgain == False:
                Player1Score += RoundScore
                P1ScoreTxt = Text(Point(450,500),("Player 1's score is ", Player1Score))
                
               
                if RollAgain == False or Roll == 1:
                    Player1 = False
                    Player2 = True
                    Player = ("Player2")
                    RoundScore = 0
            elif Player2 == True and RollAgain == False:
                Player2Score += RoundScore
                P2ScoreTxt = Text(Point(500,500),("Player 2's score is ", Player2Score))                
                if RollAgain == False or Roll == 1:
                    Player2 = False
                    Player1 = True
                    Player = ("Player1")
                    RoundScore = 0
        
    play_a_round()
    
    
    
    
    
    win.getMouse()
    win.close()
    
    
main()