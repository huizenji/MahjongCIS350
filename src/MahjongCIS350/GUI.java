package MahjongCIS350;

import java.awt.Dimension;

import javax.swing.JFrame;

public class GUI {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Chess Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Board panel = new Board();
       // frame.getContentPane().add(panel);

        frame.setResizable(true);
        frame.setPreferredSize(new Dimension(800, 650));
        frame.pack();
        frame.setVisible(true);
    }
}