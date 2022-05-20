package todaybrian;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Paddle extends Rectangle {

    public static final int PADDLE_WIDTH = 10;
    public static final int PADDLE_HEIGHT = 100;
    public static final int PADDLE_SPEED = 10;


    public Paddle() {
        setSize(PADDLE_WIDTH, PADDLE_HEIGHT);
    }

    public void keyPressed(KeyEvent e) {

    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, PADDLE_WIDTH, PADDLE_HEIGHT);
    }
}
