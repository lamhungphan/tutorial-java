package module_7_gui._50_labels;

import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {
    MyFrame(){
        this.setTitle("my program");
        this.setResizable(false); //prevent frame from being resized
        this.setSize(420,420);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true); //make frame visible

        ImageIcon image = new ImageIcon("logo.png");
        this.setIconImage(image.getImage()); //change icon of frame
        this.getContentPane().setBackground(Color.gray);
    }
}
