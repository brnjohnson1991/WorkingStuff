# Bradley Johnson, 010, 2/22/16
# Purpose: Generate a random fraction problem based on a difficulty input
# Pre-conditions: Difficulty Input, a "L" or "R" answer
# Post-conditions: Random Fractions, a comparison question, and a correct or incorrect statement


#  display title 
#  ask the user "difficulty?" (1-3)
#
#  input a difficulty value
#  
#  generate 4 random integers using difficulty above
#  if difficulty is less than or equal to 1 generate in range 1-9 and assign as Num1,Den1,Num2,Den2
#  if difficulty is greater than 1 and less than 3 generate in range 5-15 and assign as Num1,Den1,Num2,Den2
#  if difficulty is greater than or equal to 3 generate in range 10-18 and assign as Num1,Den1,Num2,Den2

#  ensure the two fractions don't have the same denominator (that would be too easy)
#  if Den1 equals Den2 regenerate a number over same range (use 3 nested ifs to replicate the ranges) and assign to Den2
#  else execute the following as a print statement
#  

#  display as 2 fractions, one near left side of screen, other one to the right of that
#  
#  calculate which one is less (cross multiply)
#  if Num1*Den2 is greater than Num2*Den1 then variable "correct" is assigned R
#  if Num1*Den2 is less than Num2*Den1 then variable "correct" is assigned L

#  ask the user "which one is less L or R"
#  assign L or R input to variable "answer"

#  tell the user if they got it right or not

#  if "answer" matches "correct" print the correct statement
#  else print the incorrect statement

