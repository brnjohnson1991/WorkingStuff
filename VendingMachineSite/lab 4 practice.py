from graphics import *
def main():
    win = GraphWin("Triangles", 500, 500)
    click_prompt = Text(Point(250, 250), "Click three times")
    click_prompt.draw(win)
    click_prompt.setSize(20)
    click_prompt.setFill("blue")
    
    pt1 = win.getMouse()
    circ1 = Circle (pt1, 5)
    circ1.draw(win)
    
    pt2 = win.getMouse()
    circ2 = Circle (pt2, 5)
    circ2.draw(win)
    
    pt3 = win.getMouse()
    circ3 = Circle(pt3, 5)
    circ3.draw(win)
    
    poly1 = Polygon (pt1, pt2, pt3)
    poly1.draw(win)
    poly1.setWidth(3)
    
    # pause program so user can see window
    win.getMouse()
    win.close()
main()