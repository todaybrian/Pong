package todaybrian;

import java.awt.*;

public class Scoreboard {
    private int player1Score, player2Score;

    public Scoreboard() {
        player1Score = 0;
        player2Score = 0;
    }

    public void addPoint(int player) {
        if (player == 1) {
            player1Score++;
        } else {
            player2Score++;
        }
    }

    public void draw(Graphics g){
        g.setFont(new Font("Inconsolata", Font.BOLD, 40));
        FontMetrics fm = g.getFontMetrics();

        g.drawString(player1Score + "", GamePanel.GAME_WIDTH/2-fm.stringWidth(String.valueOf(player1Score))-50, 40);
        g.drawString(player2Score + "", GamePanel.GAME_WIDTH/2+50, 40);
    }
}
