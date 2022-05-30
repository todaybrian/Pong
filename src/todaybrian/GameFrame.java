//Brian Yan
//May 25, 2022
//Game Frame class which creates window and starts the game
package todaybrian;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    public GamePanel gamePanel;

    public GameFrame() {
        gamePanel = new GamePanel(); //run GamePanel constructor

        this.add(gamePanel);
        this.setTitle("Pong!"); //set title for frame
        this.setResizable(false); //frame can't change size
        this.setBackground(Color.BLACK);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //X button will stop program execution
        this.pack(); //makes components fit in window - don't need to set JFrame size, as it will adjust accordingly
        this.setVisible(true); //makes window visible to user
        this.setLocationRelativeTo(null);//set window in middle of screen

    }
}
