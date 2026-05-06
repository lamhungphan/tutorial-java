package module_7_gui._50_labels;


import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        //JLabel = a GUI display area for a string of text, an image, or both
        ImageIcon image = new ImageIcon("icon.png");
        Border border = BorderFactory.createLineBorder(Color.red,3);

        JLabel label = new JLabel(); //create a label
        label.setText("Hello World"); //set text of label
        label.setIcon(image);
        label.setHorizontalTextPosition(JLabel.CENTER); //set text LEFT, CENTER, RIGHT of image icon
        label.setVerticalTextPosition(JLabel.TOP); //set text TOP, CENTER, BOTTOM of image icon
        label.setForeground(new Color(0x00FF00)); //set font color of text
        label.setFont(new Font("Serif", Font.PLAIN, 20)); //set font of text
        label.setIconTextGap(25); //set gap of text to image
        label.setBackground(Color.BLACK); //set background color
        label.setOpaque(true); //display background color
        label.setBorder(border);
        label.setVerticalAlignment(JLabel.TOP); //set vertical position of icon + text within label
        label.setHorizontalAlignment(JLabel.CENTER); //set horizontal position of icon + text within label
        //label.setBounds(0,0,150,150);//set x,y position within frame as well as dimensions

        JFrame frame = new JFrame();
//        frame.setSize(420,420);
//        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.add(label);
        frame.pack();
    }
}
