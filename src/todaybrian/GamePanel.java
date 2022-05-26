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
    public Scoreboard scoreboard;

    // Main Menu
    private static final String MAIN_MENU_IMAGE = "src/assets/main_menu.png";
    private boolean main_menu = true;

    // Pause After Score
    private static final String PAUSE_AFTER_SCORE_IMAGE = "src/assets/pause_after_score.png";
    private boolean pause_after_score = false;

    // Game Over
    private static final String PLAYER_1_WINS_IMAGE = "src/assets/player_1_wins.png";
    private static final String PLAYER_2_WINS_IMAGE = "src/assets/player_2_wins.png";
    private boolean game_over = false;
    private boolean player_1_wins = false;

    //Sound System
    private SoundPlayer soundPlayer;

    //Sounds Files
    private static final String BOUNCE_FILE = "src/assets/bounce.wav";
    private static final String START_FILE = "src/assets/start_game.wav";
    private static final String WIN_MATCH_FILE = "src/assets/win_sound.wav";
    private static final String END_FILE = "src/assets/end_music.wav";

    public GamePanel(){
        //Init objects in game
        player1Paddle = new Paddle(8, GAME_HEIGHT/2 - Paddle.PADDLE_HEIGHT/2,KeyEvent.VK_W, KeyEvent.VK_S);
        player2Paddle = new Paddle(GAME_WIDTH-8-Paddle.PADDLE_WIDTH, GAME_HEIGHT/2 - Paddle.PADDLE_HEIGHT/2,KeyEvent.VK_UP, KeyEvent.VK_DOWN);
        ball = new Ball(GAME_WIDTH/2 - Ball.BALL_DIAMETER/2, GAME_HEIGHT/2 - Ball.BALL_DIAMETER/2);
        scoreboard = new Scoreboard();

        soundPlayer = new SoundPlayer();

        setFocusable(true);
        addKeyListener(this);
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        requestFocus(); //Make window the active window

        soundPlayer.playSound(BOUNCE_FILE); //Play first sound to avoid delay

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

        //pause after score until players are ready
        if(pause_after_score){
            ImageIcon pause_after_score_image = new ImageIcon(PAUSE_AFTER_SCORE_IMAGE);
            g2d.drawImage(pause_after_score_image.getImage(), 0, 0, null);
        }

        //Game over
        if(game_over){
            if(player_1_wins){
                ImageIcon player_1_wins_image = new ImageIcon(PLAYER_1_WINS_IMAGE);
                g2d.drawImage(player_1_wins_image.getImage(), 0, 0, null);
            }
            else{
                ImageIcon player_2_wins_image = new ImageIcon(PLAYER_2_WINS_IMAGE);
                g2d.drawImage(player_2_wins_image.getImage(), 0, 0, null);
            }
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
        scoreboard.draw(g);
        if(!main_menu && !pause_after_score && !game_over){
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
        //collisions are done like this to avoid the ball from bouncing off the wall multiple times
        if(ball.y < 0){ //top wall
            ball.yVelocity = Math.abs(ball.yVelocity);

            soundPlayer.playSound(BOUNCE_FILE); //Bounce sound
        } else if (ball.y > GAME_HEIGHT - Ball.BALL_DIAMETER){ //bottom wall
            ball.yVelocity = -Math.abs(ball.yVelocity);

            soundPlayer.playSound(BOUNCE_FILE); //Bounce sound
        }

        //check for collision with paddles
        //collisions are done like this to avoid the ball from bouncing off the paddle multiple times
        if(ball.intersects(player1Paddle)){
            ball.setXVelocity(Math.abs(ball.xVelocity)); //Set going to the right

            ball.yVelocity += player1Paddle.yVelocity/2; //Add y velocity based on player paddle's y velocity

            soundPlayer.playSound(BOUNCE_FILE); //Bounce sound
        } else if(ball.intersects(player2Paddle)){
            ball.setXVelocity(-Math.abs(ball.xVelocity)); //Set going to the left

            ball.yVelocity += player2Paddle.yVelocity/2; //Add y velocity based on player paddle's y velocity

            soundPlayer.playSound(BOUNCE_FILE); //Bounce sound
        }

        //stop paddles from going off screen
        if(player1Paddle.y < 0){ //Player 1 paddle is above the screen
            player1Paddle.y = 0;
        }
        if(player1Paddle.y + Paddle.PADDLE_HEIGHT > GAME_HEIGHT){ //Player 1 paddle is below the screen
            player1Paddle.y = GAME_HEIGHT - Paddle.PADDLE_HEIGHT;
        }

        if(player2Paddle.y < 0){ //Player 2 paddle is above the screen
            player2Paddle.y = 0;
        }
        if(player2Paddle.y + Paddle.PADDLE_HEIGHT > GAME_HEIGHT){ //Player 2 paddle is below the screen
            player2Paddle.y = GAME_HEIGHT - Paddle.PADDLE_HEIGHT;
        }

        //Check for collision with walls, which means a player has scored
        if(ball.x < 0){ //Gone off the left side of screen

            scoreboard.addPoint(2); //Player 2 gets a point
            pause_after_score = true; //Pause the game until players are ready

            ball.reset(); //Reset position of ball

            if(scoreboard.getPlayer2Score() == 5){ //Check if player 2 has won
                player_1_wins = false; //Player 2 wins

                game_over = true;

                soundPlayer.playSound(END_FILE);
            } else{
                soundPlayer.playSound(WIN_MATCH_FILE); //otherwise play sound of match win
            }
        } else if(ball.x > GAME_WIDTH - Ball.BALL_DIAMETER){
            scoreboard.addPoint(1); //Player 1 gets a point
            pause_after_score = true; //Pause the game until players are ready

            ball.reset(); //Reset position of ball
            if(scoreboard.getPlayer1Score() == 5){ //Check if player 1 has won
                player_1_wins = true; //Player 1 wins

                game_over = true;

                soundPlayer.playSound(END_FILE);
            } else{
                soundPlayer.playSound(WIN_MATCH_FILE); //otherwise play sound of match win
            }
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
        //Game over screen
        if(game_over) {
            //Press space to restart game
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                main_menu = true; // go to main menu
                pause_after_score = false;
                game_over = false; // no longer game over

                scoreboard.reset(); //Reset scoreboard

                soundPlayer.stopSound(); //Stop game ending music
            }
        } else if(main_menu || pause_after_score) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                if(main_menu){
                    soundPlayer.playSound(START_FILE);
                }
                main_menu = false; // no longer in main menu
                pause_after_score = false; // no longer in pause after score screen, we are in game

                //reset and start the ball
                ball.reset();
                ball.startBall();
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
