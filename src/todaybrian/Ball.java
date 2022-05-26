//Brian Yan
//May 25, 2022
//This class represents the ball in the game.
package todaybrian;

import java.awt.*;

public class Ball extends Rectangle {

    //Ball dimension and speed constants
    public static final int BALL_DIAMETER = 20;
    public static final int INIT_BALL_SPEED = 6;

    //Ball x and y velocity
    public int xVelocity;
    public int yVelocity;

    //constructor creates ball at given location with given dimensions
    Ball(int x, int y){
        super(x, y, BALL_DIAMETER, BALL_DIAMETER);
    }

    //called whenever the movement of the ball changes in the x-direction (left/right)
    public void setXVelocity(int xVelocity) {
        this.xVelocity = xVelocity;
    }

    //called whenever the movement of the ball changes in the y-direction (up/down)
    public void setYVelocity(int yVelocity) {
        this.yVelocity = yVelocity;
    }

    //called frequently from the GamePanel class
    //updates the current location of the ball
    public void move(){
        y += yVelocity;
        x += xVelocity;
    }

    //called from the GamePanel class
    //Resets the ball to the center of the screen (like when a player scores) and sets the ball's x and y velocity to 0
    public void reset(){
        //Set ball to be in the center of the screen
        x = GamePanel.GAME_WIDTH / 2 - BALL_DIAMETER / 2;
        y = GamePanel.GAME_HEIGHT / 2 - BALL_DIAMETER / 2;

        //Set ball's x and y velocity to 0
        xVelocity = 0;
        yVelocity = 0;
    }

    //called from the GamePanel class
    //Chooses a random direction for the ball to move in at the start of a match
    public void startBall(){
        if(Math.random() < 0.5){
            xVelocity = INIT_BALL_SPEED;
        } else {
            xVelocity = -INIT_BALL_SPEED;
        }
    }

    //called frequently from the GamePanel class
    //draws the current location of the ball to the screen
    public void draw(Graphics g){
        g.setColor(Color.WHITE);
        g.fillOval(x, y, BALL_DIAMETER, BALL_DIAMETER);
    }
}
