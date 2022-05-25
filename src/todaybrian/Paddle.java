package todaybrian;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Paddle extends Rectangle {

    public static final int PADDLE_WIDTH = 10;
    public static final int PADDLE_HEIGHT = 80;
    public static final int PADDLE_SPEED = 8;

    public int yVelocity;

    private final int upKey;
    private final int downKey;

    public Paddle(int x, int y, int upKey, int downKey) {
        super(x, y, PADDLE_WIDTH, PADDLE_HEIGHT);

        //Set the keys to move the paddle
        this.upKey = upKey;
        this.downKey = downKey;
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == upKey) {
            setYDirection(-PADDLE_SPEED);
            move();
        } else if (e.getKeyCode() == downKey) {
            setYDirection(PADDLE_SPEED);
            move();
        }
    }

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

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, PADDLE_WIDTH, PADDLE_HEIGHT);
    }
}
