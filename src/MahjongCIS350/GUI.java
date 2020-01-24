package MahjongCIS350;

import java.awt.Dimension;
import java.awt.Color;
import javax.swing.JFrame;

public class GUI {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Mahjong");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // doesn't work?
        //frame.getContentPane().setBackground(Color.GREEN);

        Board board = new Board();
        frame.getContentPane().add(board);

        frame.setResizable(true);
        frame.setPreferredSize(new Dimension(2000, 1000));
        frame.pack();
        frame.setVisible(true);
    }
}