package todaybrian;

import java.awt.*;

public class Ball extends Rectangle {

    public int xVelocity;
    public int yVelocity;
    public static final int BALL_DIAMETER = 20;

    Ball(){

    }

    public void move(){
        y += yVelocity;
        x += xVelocity;
    }

    //called frequently from the GamePanel class
    //draws the current location of the ball to the screen
    public void draw(Graphics g){
        g.setColor(Color.WHITE);
        g.fillOval(x, y, BALL_DIAMETER, BALL_DIAMETER);
    }
}
