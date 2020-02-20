package MahjongCIS350;

import java.awt.*;
import javax.swing.*;

public class GUI {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Mahjong");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Board board = new Board();
        Game game = new Game();

        JPanel ruleBook = new JPanel();
        TextArea rules = new TextArea();
        rules.append(game.ruleBook());
        ruleBook.add(rules, BorderLayout.CENTER);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Board", board);
        tabbedPane.add("Rules", ruleBook);
        frame.getContentPane().add(tabbedPane);

        frame.setResizable(true);
        frame.setPreferredSize(new Dimension(1000, 1000));
        frame.pack();
        frame.setVisible(true);

    }
}