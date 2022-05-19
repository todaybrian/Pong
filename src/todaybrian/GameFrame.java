package todaybrian;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    public GamePanel gamePanel;

    public GameFrame() {
        gamePanel = new GamePanel();
        add(gamePanel);
        setTitle("Pong!");
        setBackground(Color.BLACK);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        requestFocus();
    }


}
