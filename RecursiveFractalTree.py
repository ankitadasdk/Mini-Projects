import turtle

def draw_tree(branch_len, angle, level):
    if level == 0:
        return
    turtle.pensize(level * 0.8)
    if level < 4:
        turtle.color("magenta")       
    elif level < 7:
        turtle.color("magenta")    
    else:
        turtle.color("magenta")         
    turtle.forward(branch_len)
    turtle.right(angle)
    draw_tree(branch_len * 0.75, angle, level - 1)
    turtle.left(angle * 2)
    draw_tree(branch_len * 0.75, angle, level - 1)
    turtle.right(angle)
    if level < 4:
        turtle.color("magenta")
    elif level < 7:
        turtle.color("magenta")
    else:
        turtle.color("magenta")
        
    turtle.backward(branch_len)

turtle.hideturtle()
turtle.left(90)
turtle.penup()
turtle.goto(0, -250) 
turtle.pendown()
turtle.tracer(8, 1)
draw_tree(110, 25, 10)
turtle.update()
turtle.exitonclick()
