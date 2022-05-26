//Brian Yan
//May 25, 2022
// GamePanel class which holds the game logic
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

    //Sound System
    private SoundPlayer soundPlayer;

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

    //Sound Files
    private static final String BOUNCE_FILE = "src/assets/bounce.wav";
    private static final String START_FILE = "src/assets/start_game.wav";
    private static final String WIN_MATCH_FILE = "src/assets/win_sound.wav";
    private static final String END_FILE = "src/assets/end_music.wav";

    public GamePanel(){
        //Init objects in game
        player1Paddle = new Paddle(8, GAME_HEIGHT/2 - Paddle.PADDLE_HEIGHT/2,KeyEvent.VK_W, KeyEvent.VK_S); //Create Player 1's controlled paddle, set location to left side of screen
        player2Paddle = new Paddle(GAME_WIDTH-8-Paddle.PADDLE_WIDTH, GAME_HEIGHT/2 - Paddle.PADDLE_HEIGHT/2,KeyEvent.VK_UP, KeyEvent.VK_DOWN); //Create Player 2's controlled paddle, set location to right side of screen
        ball = new Ball(GAME_WIDTH/2 - Ball.BALL_DIAMETER/2, GAME_HEIGHT/2 - Ball.BALL_DIAMETER/2); //create a ball object at the center of the screen which will bounce off the paddles and walls
        scoreboard = new Scoreboard(GAME_WIDTH); //start counting the score

        soundPlayer = new SoundPlayer(); //Sound System

        setFocusable(true); //make everything in this class appear on the screen
        addKeyListener(this); //start listening for keyboard input

        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        requestFocus(); //Make window the active window

        soundPlayer.playSound(BOUNCE_FILE); //Play the bounce sound to remove music lag

        //make this class run at the same time as other classes (without this each class would "pause" while another class runs). By using threading we can remove lag, and also allows us to do features like display timers in real time!
        gameThread = new Thread(this);
        gameThread.start();
    }

    //paint is a method in java.awt library that we are overriding. It is a special method - it is called automatically in the background in order to update what appears in the window. You NEVER call paint() yourself
    public void paint(Graphics g){
        //we are using "double buffering" here - if we draw images directly onto the screen, it takes time and the human eye can actually notice flashes of lag as each pixel on the screen is drawn one at a time. Instead, we are going to draw images OFF the screen (outside dimensions of the frame), then simply move the image on screen as needed.
        image = createImage(GAME_WIDTH, GAME_HEIGHT); //draw off screen
        g2d = (Graphics2D) image.getGraphics();

        g2d.setColor(Color.WHITE);

        // MENUS
        //main menu
        if(main_menu){
            ImageIcon main_menu_image = new ImageIcon(MAIN_MENU_IMAGE);
            g2d.drawImage(main_menu_image.getImage(), 0, 0, null); //Draw the main menu image on screen
        } else if(pause_after_score){//pause after score until players are ready
            ImageIcon pause_after_score_image = new ImageIcon(PAUSE_AFTER_SCORE_IMAGE);
            g2d.drawImage(pause_after_score_image.getImage(), 0, 0, null); //Draw the pause after score image on screen
        } else if(game_over){ //Game over
            if(player_1_wins){
                ImageIcon player_1_wins_image = new ImageIcon(PLAYER_1_WINS_IMAGE);
                g2d.drawImage(player_1_wins_image.getImage(), 0, 0, null); //Draw the player 1 wins image on screen
            }  else{
                ImageIcon player_2_wins_image = new ImageIcon(PLAYER_2_WINS_IMAGE);
                g2d.drawImage(player_2_wins_image.getImage(), 0, 0, null); //Draw the player 2 wins image on screen
            }
        }

        draw(g2d); //update the positions of everything on the screen

        //dashed line in center
        g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
        g2d.drawLine(GAME_WIDTH/2, 0, GAME_WIDTH/2, GAME_HEIGHT);

        g.drawImage(image, 0, 0, this); //redraw everything on the screen
    }

    //call the draw methods in each class to update positions as things move
    public void draw(Graphics g){
        player1Paddle.draw(g);
        player2Paddle.draw(g);
        scoreboard.draw(g);
        if(!main_menu && !pause_after_score && !game_over){ //Don't draw ball if there is a menu or game over
            ball.draw(g);
        }
    }

    //call the move methods in other classes to update positions
    //this method is constantly called from run(). By doing this, movements appear fluid and natural. If we take this out the movements appear sluggish and laggy
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
        if(ball.intersects(player1Paddle)){ //Bounced of player 1's paddle
            ball.setXVelocity(Math.abs(ball.xVelocity)); //Set going to the right

            ball.yVelocity += player1Paddle.yVelocity/2; //Add y velocity based on player paddle's y velocity

            soundPlayer.playSound(BOUNCE_FILE); //Bounce sound

        } else if(ball.intersects(player2Paddle)){ //Bounced of player 2's paddle
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

            ball.reset(); //Reset position of ball

            if(scoreboard.getPlayer2Score() == 5){ //Check if player 2 has won
                player_1_wins = false; //Player 2 wins

                game_over = true; //Game over

                soundPlayer.playSound(END_FILE);
            } else{
                soundPlayer.playSound(WIN_MATCH_FILE); //otherwise play sound of match win
                pause_after_score = true; //Pause the game until players are ready
            }
        } else if(ball.x > GAME_WIDTH - Ball.BALL_DIAMETER){
            scoreboard.addPoint(1); //Player 1 gets a point

            ball.reset(); //Reset position of ball
            if(scoreboard.getPlayer1Score() == 5){ //Check if player 1 has won
                player_1_wins = true; //Player 1 wins

                game_over = true; //Game over

                soundPlayer.playSound(END_FILE);
            } else{
                soundPlayer.playSound(WIN_MATCH_FILE); //otherwise play sound of match win
                pause_after_score = true; //Pause the game until players are ready
            }
        }
    }

        //run() method is what makes the game continue running without end. It calls other methods to move objects,  check for collision, and update the screen
    @Override
    public void run() {
        //the CPU runs our game code too quickly - we need to slow it down! The following lines of code "force" the computer to get stuck in a loop for short intervals between calling other methods to update the screen.
        long lastTime = System.nanoTime();
        double amountOfTicks = 65;
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

    //if a key is pressed, we will process it
    @Override
    public void keyPressed(KeyEvent e) {
        //MENUS
        //Game over screen
        if(game_over) {
            //Check for enter to restart game
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                main_menu = true; // go to main menu

                pause_after_score = false; // Game is over, so don't pause
                game_over = false; // no longer game over

                scoreboard.reset(); //Reset scoreboard

                soundPlayer.stopSound(); //Stop game ending music if it is playing to avoid overlapping sounds
            }
        } else if(main_menu || pause_after_score) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                if(main_menu){
                    soundPlayer.playSound(START_FILE); //Play start music if after main menu
                }
                main_menu = false; // no longer in main menu
                pause_after_score = false; // no longer in pause after score screen, we are in game

                //reset and start the ball
                ball.reset();
                ball.startBall();
            }
        }

        // Send to paddles for processing
        player1Paddle.keyPressed(e);
        player2Paddle.keyPressed(e);
    }

    //if a key is released, send it to the paddles for processing
    @Override
    public void keyReleased(KeyEvent e) {
        player1Paddle.keyReleased(e);
        player2Paddle.keyReleased(e);
    }

    //left empty because we don't need it; must be here because it is required to be overridded by the KeyListener interface
    @Override
    public void keyTyped(KeyEvent e) {}

}
