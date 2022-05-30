//Brian Yan
//May 25, 2022
// GamePanel class which holds the game logic and the main game loop.
// It listens to keyboard input, updates positions of objects, and draws the game to the screen.
// It uses Runnable and Thread to run the game loop.
/* GamePanel class acts as the main "game loop" - continuously runs the game and calls whatever needs to be called

Child of JPanel because JPanel contains methods for drawing to the screen

Implements KeyListener interface to listen for keyboard input

Implements Runnable interface to use "threading" - let the game do two things at once

*/
package todaybrian;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    // dimensions of window
    public static final int GAME_WIDTH = 600;
    public static final int GAME_HEIGHT = 400;

    public Thread gameThread;
    public Image image;
    public Graphics2D graphics2D;

    public Paddle player1Paddle;
    public Paddle player2Paddle;
    public Ball ball;
    public Scoreboard scoreboard;

    private CollisionType lastCollision; // Store the last collision so we can check for a double collision

    // Score that a player must reach to win
    public static final int WIN_CONDITION = 5;

    // Sound System
    private SoundPlayer soundPlayer;

    // Main Menu
    private boolean main_menu = true;

    // Pause After Score
    private boolean pause_after_score = false;

    // Game Over
    private boolean game_over = false;
    private boolean player_1_wins = false;

    public GamePanel() {
        // Init objects in game

        // Player 1's left side paddle. Moved using W and S keys. Initial location is at
        // middle of screen on the left.
        player1Paddle = new Paddle(0, GAME_HEIGHT / 2 - Paddle.PADDLE_HEIGHT / 2, KeyEvent.VK_W, KeyEvent.VK_S);

        // Player 2's right side paddle. Moved using Up and Down keys. Initial location
        // is at middle of screen on the right.
        player2Paddle = new Paddle(GAME_WIDTH - Paddle.PADDLE_WIDTH, GAME_HEIGHT / 2 - Paddle.PADDLE_HEIGHT / 2,
                KeyEvent.VK_UP, KeyEvent.VK_DOWN);

        // create a ball object at the center of the screen which will bounce off the
        // paddles and walls in the game
        ball = new Ball(GAME_WIDTH / 2 - Ball.BALL_DIAMETER / 2, GAME_HEIGHT / 2 - Ball.BALL_DIAMETER / 2);

        // Create scoreboard, initial location is at top of screen
        scoreboard = new Scoreboard(GAME_WIDTH); // start counting the score

        // Sound System
        soundPlayer = new SoundPlayer();

        this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT)); // set the size of the window
        this.setFocusable(true); // make everything in this class appear on the screen
        this.addKeyListener(this); // start listening for keyboard input

        this.requestFocus(); // Make window the active window

        soundPlayer.playSound(Assets.BOUNCE_FILE); // Play the bounce sound to remove music lag

        // make this class run at the same time as other classes (without this each
        // class would "pause" while another class runs). By using threading we can
        // remove lag, and also allows us to do features like display timers in real
        // time!
        gameThread = new Thread(this);
        gameThread.start();
    }

    // paint is a method in java.awt library that we are overriding. It is a special
    // method - it is called automatically in the background in order to update what
    // appears in the window. You NEVER call paint() yourself
    public void paint(Graphics g) {
        // we are using "double buffering" here - if we draw images directly onto the
        // screen, it takes time and the human eye can actually notice flashes of lag as
        // each pixel on the screen is drawn one at a time. Instead, we are going to
        // draw images OFF the screen (outside dimensions of the frame), then simply
        // move the image on screen as needed.
        image = createImage(GAME_WIDTH, GAME_HEIGHT); // draw off screen
        this.graphics2D = (Graphics2D) image.getGraphics();

        // Menus are drawn first, so that players can see the paddles and interact with
        // them before the game starts
        drawMenus(this.graphics2D); // draw the menus

        // update the positions of everything on the screen
        draw(this.graphics2D);

        g.drawImage(image, 0, 0, this); // redraw everything on the screen
    }

    // drawMenus is a method that draws the menus on the screen
    public void drawMenus(Graphics2D g2d) {
        // MENUS
        if (main_menu) { // Main menu

            ImageIcon main_menu_image = new ImageIcon(Assets.MAIN_MENU_IMAGE);
            g2d.drawImage(main_menu_image.getImage(), 0, 0, null); // Draw the main menu image on screen

        } else if (pause_after_score) {// pause after score until players are ready

            ImageIcon pause_after_score_image = new ImageIcon(Assets.PAUSE_AFTER_SCORE_IMAGE);
            g2d.drawImage(pause_after_score_image.getImage(), 0, 0, null); // Draw the pause after score image on screen

        } else if (game_over) { // Game over
            if (player_1_wins) { // If player 1 wins

                ImageIcon player_1_wins_image = new ImageIcon(Assets.PLAYER_1_WINS_IMAGE);
                g2d.drawImage(player_1_wins_image.getImage(), 0, 0, null); // Draw the player 1 wins image on screen

            } else { // Player 1 did not win, so player 2 wins

                ImageIcon player_2_wins_image = new ImageIcon(Assets.PLAYER_2_WINS_IMAGE);
                g2d.drawImage(player_2_wins_image.getImage(), 0, 0, null); // Draw the player 2 wins image on screen

            }
        }
    }

    // call the draw methods in each class to update positions as things move
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.WHITE); // Set initial color to white

        // draw objects
        player1Paddle.draw(g2d);
        player2Paddle.draw(g2d);
        scoreboard.draw(g2d);
        if (!main_menu && !pause_after_score && !game_over) { // Don't draw ball if there is a menu or game over
            ball.draw(g2d);
        }

        // draw dashed line in center
        g2d.setColor(Color.white);
        // set stroke to be dashed
        g2d.setStroke(new BasicStroke(3,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_BEVEL,
                0,
                new float[] { 10 }, 0));
        g2d.drawLine(GAME_WIDTH / 2, 0, GAME_WIDTH / 2, GAME_HEIGHT); // draw line from top to bottom of screen
    }

    // call the move methods in other classes to update positions
    // this method is constantly called from run(). By doing this, movements appear
    // fluid and natural. If we take this out the movements appear sluggish and
    // laggy
    public void move() {
        player1Paddle.move();
        player2Paddle.move();
        ball.move();
    }

    // handles all collision detection and responds accordingly
    public void checkCollision() {
        // check for collision with top and bottom walls
        // collisions are done like this to avoid the ball from bouncing off the wall
        // multiple times

        if (ball.y < 0) { // top wall
            ball.setYVelocity(Math.abs(ball.yVelocity)); // make the ball bounce off the wall downwards
            ball.setY(0); // set the ball's y position to the top of the wall to avoid object clipping

            if (ball.yVelocity == 0) { // ball is glitched, so set it to a random direction. This glitch is super rare.
                ball.setYVelocity(1);
            }

            if (lastCollision != CollisionType.TOP_WALL) { // check if collision doesn't happen on top wall twice in a
                                                           // row. If it does, don't play sound since it's a glitch

                soundPlayer.playSound(Assets.BOUNCE_FILE); // Bounce sound
            }

            lastCollision = CollisionType.TOP_WALL;
        } else if (ball.y > GAME_HEIGHT - Ball.BALL_DIAMETER) { // bottom wall
            ball.setYVelocity(-Math.abs(ball.yVelocity)); // make the ball bounce off the wall upwards
            ball.setY(GAME_HEIGHT - Ball.BALL_DIAMETER); // set the ball's y position to the bottom of the wall to avoid
                                                         // object clipping

            if (ball.yVelocity == 0) { // ball is glitched, so set it to a random direction. This glitch is super rare.
                ball.setYVelocity(-1);
            }

            if (lastCollision != CollisionType.BOTTOM_WALL) { // check if collision doesn't happen on bottom wall twice
                                                              // in a row. If it does, don't play sound since it's a
                                                              // glitch
                soundPlayer.playSound(Assets.BOUNCE_FILE); // Bounce sound
            }

            lastCollision = CollisionType.BOTTOM_WALL;
        }

        // check for collision with paddles
        // collisions are done like this to avoid the ball from bouncing off the paddle
        // multiple times
        if (ball.intersects(player1Paddle)) { // Bounced of player 1's paddle
            ball.setXVelocity(Math.abs(ball.xVelocity)); // Set going to the right
            ball.setX(player1Paddle.x + Paddle.PADDLE_WIDTH); // Set ball to be right of player 1's paddle

            if (lastCollision != CollisionType.PADDLE1) { // If this is the first time the ball has hit the paddle, play
                                                          // the bounce sound
                ball.setYVelocity(ball.yVelocity + player1Paddle.yVelocity / 2); // Add y velocity based on player
                                                                                 // paddle's y velocity
                soundPlayer.playSound(Assets.BOUNCE_FILE); // Bounce sound
            }

            lastCollision = CollisionType.PADDLE1;
        } else if (ball.intersects(player2Paddle)) { // Bounced of player 2's paddle
            ball.setXVelocity(-Math.abs(ball.xVelocity)); // Set going to the left
            ball.setX(player2Paddle.x - Ball.BALL_DIAMETER); // Set ball to be left of player 2's paddle

            if (lastCollision != CollisionType.PADDLE2) { // if we haven't already bounced off the paddle
                ball.setYVelocity(ball.yVelocity + player2Paddle.yVelocity / 2); // Add y velocity based on player
                                                                                 // paddle's y velocity
                soundPlayer.playSound(Assets.BOUNCE_FILE); // Bounce sound
            }

            lastCollision = CollisionType.PADDLE2;
        }

        // stop paddles from going off screen
        if (player1Paddle.y < 0) { // Player 1 paddle is above the screen
            player1Paddle.y = 0;
        }
        if (player1Paddle.y + Paddle.PADDLE_HEIGHT > GAME_HEIGHT) { // Player 1 paddle is below the screen
            player1Paddle.y = GAME_HEIGHT - Paddle.PADDLE_HEIGHT;
        }

        if (player2Paddle.y < 0) { // Player 2 paddle is above the screen
            player2Paddle.y = 0;
        }
        if (player2Paddle.y + Paddle.PADDLE_HEIGHT > GAME_HEIGHT) { // Player 2 paddle is below the screen
            player2Paddle.y = GAME_HEIGHT - Paddle.PADDLE_HEIGHT;
        }

        // Check for collision with walls, which means a player has scored
        if (ball.x +Ball.BALL_DIAMETER< 0) { // Gone off the left side of screen
            scoreboard.addPoint(2); // Player 2 gets a point

            ball.reset(); // Reset position of ball
            lastCollision = null; // Reset last collision

            if (scoreboard.getPlayer2Score() == WIN_CONDITION) { // Check if player 2 has won
                player_1_wins = false; // Player 2 wins

                game_over = true; // Game over

                soundPlayer.playSound(Assets.GAME_OVER_FILE);
            } else {
                soundPlayer.playSound(Assets.WIN_MATCH_FILE); // otherwise play sound of match win

                pause_after_score = true; // Pause the game until players are ready
            }
        } else if (ball.x > GAME_WIDTH + Ball.BALL_DIAMETER) { // Gone off the right side of screen
            scoreboard.addPoint(1); // Player 1 gets a point

            ball.reset(); // Reset position of ball
            lastCollision = null; // Reset last collision

            if (scoreboard.getPlayer1Score() == WIN_CONDITION) { // Check if player 1 has won
                player_1_wins = true; // Player 1 wins

                game_over = true; // Game over

                soundPlayer.playSound(Assets.GAME_OVER_FILE);
            } else {
                soundPlayer.playSound(Assets.WIN_MATCH_FILE); // otherwise play sound of match win

                pause_after_score = true; // Pause the game until players are ready
            }
        }
    }

    // run() method is what makes the game continue running without end. It calls
    // other methods to move objects, check for collision, and update the screen
    @Override
    public void run() {
        // the CPU runs our game code too quickly - we need to slow it down! The
        // following lines of code "force" the computer to get stuck in a loop for short
        // intervals between calling other methods to update the screen.
        long lastTime = System.nanoTime();
        double amountOfTicks = 120;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long now;

        while (true) { // this is the infinite game loop
            now = System.nanoTime();
            delta = delta + (now - lastTime) / ns;
            lastTime = now;

            // only move objects around and update screen if enough time has passed
            if (delta >= 1) {
                move();
                checkCollision();
                repaint();
                delta--;
            }
        }
    }

    // if a key is pressed, we will process it.
    //First check menu input if menus are open
    // If no menus are open, process game input
    @Override
    public void keyPressed(KeyEvent e) {
        // Check MENUS
        if (game_over) {// Game over screen
            // Check for enter to restart game
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                main_menu = true; // go to main menu

                pause_after_score = false; // Game is over, so don't pause
                game_over = false; // no longer game over

                scoreboard.reset(); // Reset scoreboard

                soundPlayer.stopSound(); // Stop game ending music if it is playing
            }
        } else if (main_menu || pause_after_score) { // Both pause after score and main menu have the same actions,
                                                     // except main menu plays a sound
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                if (main_menu) {
                    soundPlayer.playSound(Assets.START_FILE); // Play start music if after main menu
                }
                main_menu = false; // no longer in main menu
                pause_after_score = false; // no longer in pause after score screen, we are in game

                // reset and start the ball
                ball.reset();
                ball.startBall();
            }
        }

        // Send to paddles for processing
        player1Paddle.keyPressed(e);
        player2Paddle.keyPressed(e);
    }

    // if a key is released, send it to the paddles for processing
    @Override
    public void keyReleased(KeyEvent e) {
        player1Paddle.keyReleased(e);
        player2Paddle.keyReleased(e);
    }

    // left empty because we don't need it; must be here because it is required to
    // be overridded by the KeyListener interface
    @Override
    public void keyTyped(KeyEvent e) {
    }

}
