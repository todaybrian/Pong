package todaybrian;

import java.awt.*;

public class Scoreboard {
    private int player1Score, player2Score;

    public Scoreboard() {
        player1Score = 0;
        player2Score = 0;
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
    public void draw(Graphics g){
        g.setFont(new Font("Inconsolata", Font.BOLD, 40));
        FontMetrics fm = g.getFontMetrics();

        g.drawString(player1Score + "", GamePanel.GAME_WIDTH/2-fm.stringWidth(String.valueOf(player1Score))-50, 40);
        g.drawString(player2Score + "", GamePanel.GAME_WIDTH/2+50, 40);
    }
}
