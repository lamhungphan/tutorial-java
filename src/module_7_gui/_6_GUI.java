package module_7_gui;

import javax.swing.JOptionPane;

public class _6_GUI {
    public static void main(String[] args) {

     /*  GRAPHICAL USER INTERFACE   */
        String name = JOptionPane.showInputDialog("Enter your name");
        JOptionPane.showMessageDialog(null,"Hello "+name);  //null is the first input character

        int age = Integer.parseInt(JOptionPane.showInputDialog("Enter your age"));
        JOptionPane.showMessageDialog(null,"Your are "+age+" years old");

        double height= Double.parseDouble(JOptionPane.showInputDialog("Enter your height"));
        JOptionPane.showMessageDialog(null,"You are "+height+" cm tall");
    }
}
