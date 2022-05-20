package todaybrian;

import java.awt.*;

public class Ball extends Rectangle {

    public static final int BALL_DIAMETER = 20;

    public int xVelocity;
    public int yVelocity;

    Ball(int x, int y){
        super(x, y, BALL_DIAMETER, BALL_DIAMETER);
    }

    public void setXVelocity(int xVelocity) {
        this.xVelocity = xVelocity;
    }

    public void setYVelocity(int yVelocity) {
        this.yVelocity = yVelocity;
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
