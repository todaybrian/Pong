package todaybrian;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    //dimensions of window
    public static final int GAME_WIDTH = 600;
    public static final int GAME_HEIGHT = 400;

    public Thread gameThread;
    public Image image;
    public Graphics2D g2d;

    private boolean main_menu = true;

    public GamePanel(){
        setFocusable(true);
        addKeyListener(this);
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        requestFocus(); //Make window the active window

        gameThread = new Thread(this);
        gameThread.start();
    }


    public void paint(Graphics g){
        image = createImage(GAME_WIDTH, GAME_HEIGHT);
        g2d = (Graphics2D) image.getGraphics();

        g2d.setColor(Color.WHITE);

        //main menu
        if(main_menu){
            Font font = new Font("Arial", Font.BOLD, 30);
            FontMetrics metrics = g2d.getFontMetrics(font);

            g2d.setFont(font);
            String text = "Press [space] to start";
            g2d.drawString("Press [space] to start", (GAME_WIDTH - metrics.stringWidth(text))/2, GAME_HEIGHT/2);
        }

        //top and bottom lines
        g2d.setStroke(new BasicStroke(5));
        g2d.drawLine(0, 0, GAME_WIDTH, 0);
        g2d.drawLine(0, GAME_HEIGHT, GAME_WIDTH, GAME_HEIGHT);

        //dashed line in center
        g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
        g2d.drawLine(GAME_WIDTH/2, 0, GAME_WIDTH/2, GAME_HEIGHT);

        g.drawImage(image, 0, 0, this);
    }

    //run() method is what makes the game continue running without end. It calls other methods to move objects,  check for collision, and update the screen
    @Override
    public void run() {
        //the CPU runs our game code too quickly - we need to slow it down! The following lines of code "force" the computer to get stuck in a loop for short intervals between calling other methods to update the screen.
        long lastTime = System.nanoTime();
        double amountOfTicks = 60;
        double ns = 1000000000/amountOfTicks;
        double delta = 0;
        long now;

        while(true){ //this is the infinite game loop
            now = System.nanoTime();
            delta = delta + (now-lastTime)/ns;
            lastTime = now;

            //only move objects around and update screen if enough time has passed
            if(delta >= 1){
//                move();
//                checkCollision();
                repaint();
                delta--;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(main_menu) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                main_menu = false;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


}
