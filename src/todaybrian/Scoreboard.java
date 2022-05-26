//Brian Yan
//May 25, 2022
// This class holds the scoreboards and displays it to the user
package todaybrian;

import java.awt.*;

public class Scoreboard {

    //Store the score
    private int player1Score;
    private int player2Score;

    private static int GAME_WIDTH;

    public Scoreboard(int gameWidth) {
        player1Score = 0;
        player2Score = 0;

        GAME_WIDTH = gameWidth;
    }

    //Add point to player 1 or player 2
    public void addPoint(int player) {
        if (player == 1) {
            player1Score++;
        } else {
            player2Score++;
        }
    }

    //Reset the scores to 0
    public void reset(){
        player1Score = 0;
        player2Score = 0;
    }

    //Get the score of player 1
    public int getPlayer1Score() {
        return player1Score;
    }

    //Get the score of player 2
    public int getPlayer2Score() {
        return player2Score;
    }

    //called frequently from GamePanel class
    //updates the current score and draws it to the screen
    public void draw(Graphics g){
        g.setColor(Color.WHITE);
        g.setFont(new Font("Inconsolata", Font.BOLD, 40));
        FontMetrics fm = g.getFontMetrics();

        //Draw the scores

        //Player 1
        g.drawString(player1Score + "", GAME_WIDTH/2-fm.stringWidth(String.valueOf(player1Score))-50, 40); //Set score to be on the left of the center
        //Player 2
        g.drawString(player2Score + "", GAME_WIDTH/2+50, 40); //Set score to be on the right of the center
    }
}
