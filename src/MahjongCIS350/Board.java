package MahjongCIS350;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Board extends JPanel{

    private Game game;
    private JButton[][][] drawPile;
            //discardPile, playerHand1, playerHand2, playerHand3, playerHand4;
    private JPanel drawPilePanel;

    private ImageIcon circle1;
    private ImageIcon circle2;
    private ImageIcon circle3;
    private ImageIcon circle4;
    private ImageIcon circle5;
    private ImageIcon circle6;
    private ImageIcon circle7;
    private ImageIcon circle8;
    private ImageIcon circle9;

    private ImageIcon bamboo1;
    private ImageIcon bamboo2;
    private ImageIcon bamboo3;
    private ImageIcon bamboo4;
    private ImageIcon bamboo5;
    private ImageIcon bamboo6;
    private ImageIcon bamboo7;
    private ImageIcon bamboo8;
    private ImageIcon bamboo9;

    private ImageIcon character1;
    private ImageIcon character2;
    private ImageIcon character3;
    private ImageIcon character4;
    private ImageIcon character5;
    private ImageIcon character6;
    private ImageIcon character7;
    private ImageIcon character8;
    private ImageIcon character9;

    private ImageIcon flower1;
    private ImageIcon flower2;
    private ImageIcon flower3;
    private ImageIcon flower4;
    private ImageIcon flower5;
    private ImageIcon flower6;
    private ImageIcon flower7;
    private ImageIcon flower8;

    private ImageIcon redDragon;
    private ImageIcon greenDragon;
    private ImageIcon whiteDragon;

    private ImageIcon eastWind;
    private ImageIcon southWind;
    private ImageIcon westWind;
    private ImageIcon northWind;

    private listener listener;


    public Board(){
        game = new Game();

        drawPile = new JButton[18][18][2];
        drawPilePanel = new JPanel();
        drawPilePanel.setLayout(new GridLayout(18, 18, 1, 1));

        listener = new listener();

        int row = 0, col = 0, depth = 0;
        drawPilePanel.add(drawPile[row][col][depth]);

        createIcons();

    }


    private void placeDrawPile(int r, int c, int d){
        Tile tile = game.getTile(r*c);
        if (tile instanceof Suite) {
            if (((Suite) tile).getDesign().equals("Circle")) {
                if (((Suite) tile).getValue() == 1) {
                    drawPile[r][c][d] = new JButton(null, circle1);
                    drawPile[r][c][d].addActionListener(listener);
                }
                if (((Suite) tile).getValue() == 2) {
                    drawPile[r][c][d] = new JButton(null, circle2);
                    drawPile[r][c][d].addActionListener(listener);
                }
                if (((Suite) tile).getValue() == 3) {
                    drawPile[r][c][d] = new JButton(null, circle3);
                    drawPile[r][c][d].addActionListener(listener);
                }
                if (((Suite) tile).getValue() == 4) {
                    drawPile[r][c][d] = new JButton(null, circle4);
                    drawPile[r][c][d].addActionListener(listener);
                }
            }

            if (((Suite) tile).getDesign().equals("Bamboo")) {
                if (((Suite) tile).getValue() == 1) {
                    drawPile[r][c][d] = new JButton(null, bamboo1);
                    drawPile[r][c][d].addActionListener(listener);
                }
                if (((Suite) tile).getValue() == 2) {
                    drawPile[r][c][0] = new JButton(null, bamboo2);
                    drawPile[r][c][0].addActionListener(listener);
                }
                if (((Suite) tile).getValue() == 3) {
                    drawPile[r][c][d] = new JButton(null, bamboo3);
                    drawPile[r][c][d].addActionListener(listener);
                }
                if (((Suite) tile).getValue() == 4) {
                    drawPile[r][c][d] = new JButton(null, bamboo4);
                    drawPile[r][c][d].addActionListener(listener);
                }
            }
        }


    }

    private void createIcons() {
        // Sets the Image for circle tiles
        circle1 = new ImageIcon("./src/MahjongCIS350/Images/circle1.jpg");
        circle2 = new ImageIcon("./src/MahjongCIS350/Images/circle2.jpg");
        circle3 = new ImageIcon("./src/MahjongCIS350/Images/circle3.jpg");
        circle4 = new ImageIcon("./src/MahjongCIS350/Images/circle4.jpg");
        circle5 = new ImageIcon("./src/MahjongCIS350/Images/circle5.jpg");
        circle6 = new ImageIcon("./src/MahjongCIS350/Images/circle6.jpg");
        circle7 = new ImageIcon("./src/MahjongCIS350/Images/circle7.jpg");
        circle8 = new ImageIcon("./src/MahjongCIS350/Images/circle8.jpg");
        circle9 = new ImageIcon("./src/MahjongCIS350/Images/circle9.jpg");

        // Sets the Image for bamboo tiles
        bamboo1 = new ImageIcon("./src/MahjongCIS350/Images/bamboo1.jpg");
        bamboo2 = new ImageIcon("./src/MahjongCIS350/Images/bamboo2.jpg");
        bamboo3 = new ImageIcon("./src/MahjongCIS350/Images/bamboo3.jpg");
        bamboo4 = new ImageIcon("./src/MahjongCIS350/Images/bamboo4.jpg");
        bamboo5 = new ImageIcon("./src/MahjongCIS350/Images/bamboo5.jpg");
        bamboo6 = new ImageIcon("./src/MahjongCIS350/Images/bamboo6.jpg");
        bamboo7 = new ImageIcon("./src/MahjongCIS350/Images/bamboo7.jpg");
        bamboo8 = new ImageIcon("./src/MahjongCIS350/Images/bamboo8.jpg");
        bamboo9 = new ImageIcon("./src/MahjongCIS350/Images/bamboo9.jpg");

        // Sets the Image for character tiles
        character1 = new ImageIcon("./src/MahjongCIS350/Images/character1.jpg");
        character2 = new ImageIcon("./src/MahjongCIS350/Images/character2.jpg");
        character3 = new ImageIcon("./src/MahjongCIS350/Images/character3.jpg");
        character4 = new ImageIcon("./src/MahjongCIS350/Images/character4.jpg");
        character5 = new ImageIcon("./src/MahjongCIS350/Images/character5.jpg");
        character6 = new ImageIcon("./src/MahjongCIS350/Images/character6.jpg");
        character7 = new ImageIcon("./src/MahjongCIS350/Images/character7.jpg");
        character8 = new ImageIcon("./src/MahjongCIS350/Images/character8.jpg");
        character9 = new ImageIcon("./src/MahjongCIS350/Images/character9.jpg");

        // Sets the Image for flower tiles
        flower1 = new ImageIcon("./src/MahjongCIS350/Images/flower1.jpg");
        flower2 = new ImageIcon("./src/MahjongCIS350/Images/flower2.jpg");
        flower3 = new ImageIcon("./src/MahjongCIS350/Images/flower3.jpg");
        flower4 = new ImageIcon("./src/MahjongCIS350/Images/flower4.jpg");
        flower5 = new ImageIcon("./src/MahjongCIS350/Images/flower5.jpg");
        flower6 = new ImageIcon("./src/MahjongCIS350/Images/flower6.jpg");
        flower7 = new ImageIcon("./src/MahjongCIS350/Images/flower7.jpg");
        flower8 = new ImageIcon("./src/MahjongCIS350/Images/flower8.jpg");

        // Sets the Image for dragon tiles
        redDragon = new ImageIcon("./src/MahjongCIS350/Images/redDragon.jpg");
        greenDragon = new ImageIcon("./src/MahjongCIS350/Images/greenDragon.jpg");
        whiteDragon = new ImageIcon("./src/MahjongCIS350/Images/whiteDragon.jpg");

        // Sets the Image for the wind tiles
        eastWind = new ImageIcon("./src/MahjongCIS350/Images/eastWind.jpg");
        southWind = new ImageIcon("./src/MahjongCIS350/Images/eastWind.jpg");
        westWind = new ImageIcon("./src/MahjongCIS350/Images/eastWind.jpg");
        northWind = new ImageIcon("./src/MahjongCIS350/Images/eastWind.jpg");

    }


    private void displayBoard(){

    }


    // inner class that represents action listener for buttons
    private class listener implements ActionListener {
        public void actionPerformed(ActionEvent event) {

        }

    }


}


