package todaybrian;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    //dimensions of window
    public static final int GAME_WIDTH = 600;
    public static final int GAME_HEIGHT = 400;


    public Image image;
    public Graphics2D g2d;
    public GamePanel(){

        setFocusable(true);
        addKeyListener(this);
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
    }


    public void paint(Graphics g){
        image = createImage(GAME_WIDTH, GAME_HEIGHT);
        g2d = (Graphics2D) image.getGraphics();

        g2d.setColor(Color.WHITE);

        //top and bottom lines
        g2d.setStroke(new BasicStroke(5));
        g2d.drawLine(0, 0, GAME_WIDTH, 0);
        g2d.drawLine(0, GAME_HEIGHT, GAME_WIDTH, GAME_HEIGHT);

        //dashed line in center
        g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
        g2d.drawLine(GAME_WIDTH/2, 0, GAME_WIDTH/2, GAME_HEIGHT);

        g.drawImage(image, 0, 0, this);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void run() {

    }
}
