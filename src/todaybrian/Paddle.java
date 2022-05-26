//Brian Yan
//May 25, 2022
//Class for the paddles used in the game
package todaybrian;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Paddle extends Rectangle {

    //Paddle dimension and speed constants
    public static final int PADDLE_WIDTH = 10;
    public static final int PADDLE_HEIGHT = 80;
    public static final int PADDLE_SPEED = 8;

    //Y velocity of the paddle
    public int yVelocity;

    //Player Keys for Paddle (up and down)
    private final int upKey;
    private final int downKey;

    // Constructor creates paddle at given location and sets player keys
    public Paddle(int x, int y, int upKey, int downKey) {
        super(x, y, PADDLE_WIDTH, PADDLE_HEIGHT);

        //Set the keys to move the paddle
        this.upKey = upKey;
        this.downKey = downKey;
    }

    //Called from GamePanel when a key is pressed
    //Moves the paddle up or down
    //If the paddle is at the top or bottom of the screen, it will not move (checkCollision() will prevent this)
    //If none of the keys below are pressed, the paddle will not move
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == upKey) {
            setYDirection(-PADDLE_SPEED);
            move();
        } else if (e.getKeyCode() == downKey) {
            setYDirection(PADDLE_SPEED);
            move();
        }
    }

    //Called from GamePanel when a key is released
    //Stops the paddle from moving
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == upKey || e.getKeyCode() == downKey){
            setYDirection(0);
        }
    }

    //called whenever the movement of the paddle changes in the y-direction (up/down)
    public void setYDirection(int yDirection){
        yVelocity = yDirection;
    }

    //called frequently from both PlayerBall class and GamePanel class
    //updates the current location of the paddle
    public void move(){
        y = y + yVelocity;
    }

    //called frequently from the GamePanel class
    //draws the current location of the paddle to the screen
    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, PADDLE_WIDTH, PADDLE_HEIGHT);
    }
}
