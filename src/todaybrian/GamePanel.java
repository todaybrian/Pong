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

    public Paddle player1Paddle;
    public Paddle player2Paddle;
    public Ball ball;

    // Main Menu
    private static final String MAIN_MENU_IMAGE = "src/assets/main_menu.png";
    private boolean main_menu = true;

    public GamePanel(){
        player1Paddle = new Paddle(5, GAME_HEIGHT/2 - Paddle.PADDLE_HEIGHT/2,KeyEvent.VK_W, KeyEvent.VK_S);
        player2Paddle = new Paddle(GAME_WIDTH-5-Paddle.PADDLE_WIDTH, GAME_HEIGHT/2 - Paddle.PADDLE_HEIGHT/2,KeyEvent.VK_UP, KeyEvent.VK_DOWN);
        ball = new Ball(GAME_WIDTH/2 - Ball.BALL_DIAMETER/2, GAME_HEIGHT/2 - Ball.BALL_DIAMETER/2);

        setFocusable(true);
        addKeyListener(this);
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        requestFocus(); //Make window the active window

        gameThread = new Thread(this);
        gameThread.start();
    }

    //paint is a method in java.awt library that we are overriding. It is a special method - it is called automatically in the background in order to update what appears in the window. You NEVER call paint() yourself
    public void paint(Graphics g){
        //we are using "double buffering" here - if we draw images directly onto the screen, it takes time and the human eye can actually notice flashes of lag as each pixel on the screen is drawn one at a time. Instead, we are going to draw images OFF the screen (outside dimensions of the frame), then simply move the image on screen as needed.
        image = createImage(GAME_WIDTH, GAME_HEIGHT); //draw off screen
        g2d = (Graphics2D) image.getGraphics();

        g2d.setColor(Color.WHITE);

        //main menu
        if(main_menu){
            ImageIcon main_menu_image = new ImageIcon(MAIN_MENU_IMAGE);
            g2d.drawImage(main_menu_image.getImage(), 0, 0, null);
        }

        draw(g2d);

        //dashed line in center
        g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
        g2d.drawLine(GAME_WIDTH/2, 0, GAME_WIDTH/2, GAME_HEIGHT);

        g.drawImage(image, 0, 0, this);
    }

    public void draw(Graphics g){
        player1Paddle.draw(g);
        player2Paddle.draw(g);
        if(!main_menu){
            ball.draw(g);
        }
    }

    public void move(){
        player1Paddle.move();
        player2Paddle.move();
        ball.move();
    }

    //handles all collision detection and responds accordingly
    public void checkCollision() {
        //check for collision with top and bottom walls
        if(ball.y < 0 || ball.y > GAME_HEIGHT - Ball.BALL_DIAMETER){
            ball.yVelocity *= -1;
        }

        //check for collision with paddles
        if(ball.intersects(player1Paddle)){
            ball.setXVelocity(Math.abs(ball.xVelocity));
        }

        if(ball.intersects(player2Paddle)){
            ball.setXVelocity(-Math.abs(ball.xVelocity));
        }

        //stop paddles from going off screen
        if(player1Paddle.y < 0){
            player1Paddle.y = 0;
        }
        if(player1Paddle.y + Paddle.PADDLE_HEIGHT > GAME_HEIGHT){
            player1Paddle.y = GAME_HEIGHT - Paddle.PADDLE_HEIGHT;
        }

        if(player2Paddle.y < 0){
            player2Paddle.y = 0;
        }
        if(player2Paddle.y + Paddle.PADDLE_HEIGHT > GAME_HEIGHT){
            player2Paddle.y = GAME_HEIGHT - Paddle.PADDLE_HEIGHT;
        }
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
                move();
                checkCollision();
                repaint();
                delta--;
            }
        }
    }


    @Override
    public void keyPressed(KeyEvent e) {
        if(main_menu) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                main_menu = false;
                ball.setXVelocity(5);
                ball.setYVelocity(5);
            }
        }
        player1Paddle.keyPressed(e);
        player2Paddle.keyPressed(e);
    }

    //if a key is released,
    @Override
    public void keyReleased(KeyEvent e) {
        player1Paddle.keyReleased(e);
        player2Paddle.keyReleased(e);
    }

    //left empty because we don't need it; must be here because it is required to be overridded by the KeyListener interface
    @Override
    public void keyTyped(KeyEvent e) {}
}
