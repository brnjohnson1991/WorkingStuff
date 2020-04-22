from graphics import *

def main ():
    win = GraphWin("The Great Intersection Finder", 600, 500)

    msg1 = Text(Point(300, 250), "Click 4 times please")
    msg1.draw(win)

    
    #3.  get four Points from the user

    #4.  get the x's and y's from the Points
    #    naming convention "line1point1x" means Line 1, Point 1, X coordinate
    #    naming convention "line2point1y" means Line 2, Point 1, Y coordinate
    #    you can change this if you want, but it means you will have to rewrite
    #    the formulas below with your own names
    
    #Calculating the slopes of the lines.
    line1slope = (line1point2y - line1point1y) / (line1point2x - line1point1x)
    line2slope = (line2point2y - line2point1y) / (line2point2x - line2point1x)

    #Calculating the y-intercepts of the lines.
    # y1 - m * x1
    line1yint = line1point1y - line1slope * line1point1x
    line2yint = line2point1y - line2slope * line2point1x

    #Calculate where the two lines intersect.
    intersectx = (-line1yint + line2yint) / (line1slope - line2slope)
    intersecty = line1yint + line1slope * intersectx
    

    #5. draw a red circle for one endpoint of Line 1 (radius 25)
    #   draw line to other endpoint of first line
    #   draw other endpoint circle in red (radius 25)
    
    #6. draw endpoint of second line in red (radius 25)
    #   draw line to other endpoint of second line
    #   draw other endpoint circle of second line in red (radius 25)

    #7. draw green circle that is centered on point of intersection (radius 35)

    
    #8. CHANGE this code so that it puts the text on the graphics window (before it closes, of course).
    #   You will need to concatenate all the items on one line of output into a single string.
    #   Don't forget to typecast the numbers into strings.

    #Show what line 1 and 2 points are, their lines, and point of intersection.
    print()
    print("Line 1: (", line1point1x, ",", line1point1y,") and (", line1point2x, ", ", line1point2y,")", sep="")
    print("Equation of Line 1: y = ", line1slope,' x + ', line1yint, sep="")
    print()
    print("Line 2: (", line2point1x, ",", line2point1y,") and (", line2point2x, ", ", line2point2y,")", sep="")
    print("Equation of Line 2  y = ", line2slope,' x + ', line2yint, sep="")
    print()
    print("Point of intersection = (", intersectx,", ", intersecty, ")", sep="")
    print()

    #9. wait for click then close the window
    
main()