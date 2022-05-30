//Brian Yan
//May 25, 2022
//This class represents the ball in the game.
/* Ball class defines behaviours for the ball

child of Rectangle because that makes it easy to draw and check for collision

In 2D GUI, basically everything is a rectangle even if it doesn't look like it!
*/
package todaybrian;

import java.awt.*;

public class Ball extends Rectangle {

    //Ball dimension and speed constants
    public static final int BALL_DIAMETER = 10;
    public static final int INIT_BALL_SPEED = 3;
    public static final int MAX_Y_BALL_SPEED = 3;

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
        //if the ball is moving too fast in the y-direction, limit it to a maximum speed
        this.yVelocity = Math.min(this.yVelocity, MAX_Y_BALL_SPEED);
        this.yVelocity = Math.max(this.yVelocity, -MAX_Y_BALL_SPEED);
    }

    //called frequently from the GamePanel class
    //updates the current location of the ball
    public void move(){
        y += yVelocity;
        x += xVelocity;
    }


    //Set X coordinate of ball
    //called when ball hits a paddle to avoid ball getting stuck in paddle
    //Setter method used here because in the future, it would be easier to incorporate multiplayer
    public void setX(int x){
        this.x = x;
    }

    //Set Y coordinate of ball
    //called when ball hits a wall to avoid ball getting stuck in wall
    public void setY(int y){
        this.y = y;
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
        g.fillRect(x, y, BALL_DIAMETER, BALL_DIAMETER);
    }
}
