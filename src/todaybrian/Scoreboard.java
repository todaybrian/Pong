//Brian Yan
//May 25, 2022
// This class holds the scoreboards and displays it to the user
package todaybrian;

import java.awt.*;

public class Scoreboard {

    // Store the score
    private int player1Score;
    private int player2Score;

    // Only GAME_WIDTH is needed because the scoreboard is always on the top of the
    // screen near the center
    private static int GAME_WIDTH;

    // Distance of scoreboard from top of screen
    private static final int distanceFromTop = 40;
    // Distance of scores from center of screen
    private static final int distanceFromCenter = 50;

    // Called from GamePanel class to initialize the scoreboard
    // constructor creates scoreboard. This scoreboard is displayed on the top of
    // the screen near the center
    public Scoreboard(int gameWidth) {
        player1Score = 0;
        player2Score = 0;

        GAME_WIDTH = gameWidth;
    }

    // Called from GamePanel class when a player scores
    // Add point to player 1 or player 2
    public void addPoint(int player) {
        if (player == 1) {
            player1Score++;
        } else {
            player2Score++;
        }
    }

    // Called when a game is over
    // Reset the scores to 0
    public void reset() {
        player1Score = 0;
        player2Score = 0;
    }

    // Called from GamePanel to check if a player has won the game
    // Get the score of player 1
    public int getPlayer1Score() {
        return player1Score;
    }

    // Called from GamePanel to check if a player has won the game
    // Get the score of player 2
    public int getPlayer2Score() {
        return player2Score;
    }

    // called frequently from GamePanel class
    // updates the current score and draws it to the screen
    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Consolas", Font.BOLD, 40));
        FontMetrics fm = g.getFontMetrics(); // get font metrics for the current font

        // Draw the scores
        // Player 1 (left)
        int player1ScoreWidth = fm.stringWidth(String.valueOf(player1Score)); // width of player 1 score

        // Move text to center of screen, then move to the left by the distance from
        // center
        int player1ScoreLoc = ((GAME_WIDTH / 2) - (player1ScoreWidth / 2)) - distanceFromCenter;
        g.drawString(String.valueOf(player1Score), player1ScoreLoc, distanceFromTop); // Set score to be on the left of
                                                                                      // the center

        // Player 2 (right)
        int player2ScoreWidth = fm.stringWidth(String.valueOf(player2Score)); // width of player 2 score

        // Move text to center of screen, then move to the right by the distance from
        // center
        int player2ScoreLoc = ((GAME_WIDTH / 2) - (player2ScoreWidth / 2)) + distanceFromCenter;
        g.drawString(String.valueOf(player2Score), player2ScoreLoc, distanceFromTop); // Set score to be on the right of
                                                                                      // the center
    }
}
