package MahjongCIS350;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.util.*;

public class Board extends JPanel {

    private Game game;
    private ArrayList<JButton> drawPile, discardPile, p1Hand, p2Hand, p3Hand, p4Hand;
    private JPanel drawPilePanel, discardPilePanel, player1Panel, player2Panel, player3Panel, player4Panel;
    private JLayeredPane gameBoard;

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

    private ImageIcon tileBack;

    private listener listener;


    public Board() {

        // create Game
        game = new Game();

        // create piles and hands
        drawPile = new ArrayList<>();
        discardPile = new ArrayList<>();
        p1Hand = new ArrayList<>();
        p2Hand = new ArrayList<>();
        p3Hand = new ArrayList<>();
        p4Hand = new ArrayList<>();

        // create Panels
        drawPilePanel = new JPanel();
        discardPilePanel = new JPanel();
        player1Panel = new JPanel();
        player2Panel = new JPanel();
        player3Panel = new JPanel();
        player4Panel = new JPanel();
        gameBoard = new JLayeredPane();

        Color darkGreen = new Color(0, 150, 100);
        drawPilePanel.setBackground(darkGreen);
        discardPilePanel.setBackground(darkGreen);
        player1Panel.setBackground(darkGreen);
        player2Panel.setBackground(darkGreen);
        player3Panel.setBackground(darkGreen);
        player4Panel.setBackground(darkGreen);
        gameBoard.setBackground(darkGreen);


        // set Layouts
        drawPilePanel.setLayout(new GridBagLayout());
        discardPilePanel.setLayout(new GridLayout(10, 10,1, 1));
        player1Panel.setLayout(new GridBagLayout());
        player2Panel.setLayout(new GridBagLayout());
        player3Panel.setLayout(new GridBagLayout());
        player4Panel.setLayout(new GridBagLayout());
        gameBoard.setLayout(new GridBagLayout());

        // create Icons
        createIcons();

        // set listeners for JButtons
        listener = new listener();

        // create and place JButton tiles into drawPile and then deal
        placeDrawPile();
        dealPlayerTiles();

        // place Panels
        GridBagConstraints c = new GridBagConstraints();

        c.ipadx = 75;
        c.ipady = 75;

        c.gridx = 1;
        c.gridy = 1;
        gameBoard.add(drawPilePanel, c);
        gameBoard.setLayer(drawPilePanel, 1);
        gameBoard.add(discardPilePanel, c);
        gameBoard.setLayer(discardPilePanel, 0);

        c.ipady = 25;
        c.gridy = 2;
        gameBoard.add(player1Panel, c);

        c.ipady = 75;
        c.gridx = 0;
        c.gridy = 1;
        gameBoard.add(player2Panel, c);

        c.gridx = 1;
        c.gridy = 0;
        gameBoard.add(player3Panel, c);

        c.gridx = 2;
        c.gridy = 1;
        gameBoard.add(player4Panel, c);

        add(gameBoard, BorderLayout.CENTER);
    }



    private void dealPlayerTiles(){

        GridBagConstraints c = new GridBagConstraints();

        for (int i = 0; i < 13; i++){
            p1Hand.add(drawPile.get(143 - i));
            drawPile.remove(143 - i);

            p1Hand.get(i).setIcon(updatedImage(game.getPlayerList
                    (game.getStartingPlayer()).getTileFromHand(i)));

            c.gridx = i;
            c.gridy = 0;
            p1Hand.get(i).setPreferredSize(new Dimension(50,50));
            player1Panel.add(p1Hand.get(i), c);
        }

        for (int i = 0; i < 13; i++){
            p2Hand.add(drawPile.get(130 - i));
            drawPile.remove(130 - i);

            c.gridx = 0;
            c.gridy = i;
            player2Panel.add(p2Hand.get(i), c);
        }

        for (int i = 0; i < 13; i++){
            p3Hand.add(drawPile.get(117 - i));
            drawPile.remove(117 - i);

            c.gridx = i;
            c.gridy = 0;
            player3Panel.add(p3Hand.get(i), c);
        }

        for (int i = 0; i < 13; i++){
            p4Hand.add(drawPile.get(104 - i));
            drawPile.remove(104 - i);

            c.gridx = 0;
            c.gridy = i;
            player4Panel.add(p4Hand.get(i), c);
        }

        // give starting Player one more Tile
        for (int i = 0; i < 4; i++){
            if (game.getStartingPlayer() == i){
                switch (i){
                    case 0:
                        p1Hand.add(drawPile.get(91));
                        drawPile.remove(91);
                        break;
                    case 1:
                        p2Hand.add(drawPile.get(91));
                        drawPile.remove(91);
                        break;
                    case 2:
                        p3Hand.add(drawPile.get(91));
                        drawPile.remove(91);
                        break;
                    case 3:
                        p4Hand.add(drawPile.get(91));
                        drawPile.remove(91);
                        break;
                }
            }
        }
    }

    private ImageIcon updatedImage(Tile tile){

        if (tile instanceof Suite){
            switch (((Suite) tile).getDesign()){
                case "Circle":
                    return getCircleImage(((Suite) tile).getValue());
                case "Bamboo":
                    return getBambooImage((((Suite) tile).getValue()));
                case "Character":
                    return getCharacterImage(((Suite) tile).getValue());
            }

        } else{
            switch (tile.getType()){
                case "Dragon":
                    return getDragonImage(((Dragon) tile).getColor());
                case "Wind":
                    return getWindImage(((Wind) tile).getDirection());
                case "Flower":
                    return getFlowerImage(((Flower) tile).getNumber());
            }
        }
        System.out.println("Error in updatedImage()");
        return tileBack;
    }

    private void placeDrawPile() {

        for (int i = 0; i < 144; i++) {
            JButton temp = new JButton(null, tileBack);
            temp.setPreferredSize(new Dimension(25, 25));
            temp.addActionListener(listener);
            drawPile.add(temp);
        }

        // add to drawPilePanel to make visible
        GridBagConstraints c = new GridBagConstraints();
        int index = 0;
        for (int row = 0; row < 22; row++){
            for (int col = 0; col < 22; col++) {
                if (row < 2 || row > 19) {
                    if(col > 1 && col < 20) {
                        c.gridx = col;
                        c.gridy = row;
                        drawPilePanel.add(drawPile.get((index)), c);
                        index++;
                    }
                }else if (col < 2 || col > 19){
                    c.gridx = col;
                    c.gridy = row;
                    drawPilePanel.add(drawPile.get((index)), c);
                    index++;
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

        tileBack = new ImageIcon("./src/MahjongCIS350/Images/tileBack.jpg");
    }

    private ImageIcon getCircleImage(int value){
        switch (value){
            case 1:
                return circle1;
            case 2:
                return circle2;
            case 3:
                return circle3;
            case 4:
                return circle4;
            case 5:
                return circle5;
            case 6:
                return circle6;
            case 7:
                return circle7;
            case 8:
                return circle8;
            case 9:
                return circle9;
        }
        System.out.println("Error in circleImage");
        return tileBack;
    }

    private ImageIcon getBambooImage(int value){
        switch (value){
            case 1:
                return bamboo1;
            case 2:
                return bamboo2;
            case 3:
                return bamboo3;
            case 4:
                return bamboo4;
            case 5:
                return bamboo5;
            case 6:
                return bamboo6;
            case 7:
                return bamboo7;
            case 8:
                return bamboo8;
            case 9:
                return bamboo9;
        }
        System.out.println("Error in bambooImage");
        return tileBack;
    }

    private ImageIcon getCharacterImage(int value){
        switch (value){
            case 1:
                return character1;
            case 2:
                return character2;
            case 3:
                return character3;
            case 4:
                return character4;
            case 5:
                return character5;
            case 6:
                return character6;
            case 7:
                return character7;
            case 8:
                return character8;
            case 9:
                return character9;
        }
        System.out.println("Error in characterImage");
        return tileBack;
    }

    private ImageIcon getDragonImage(String color){
        switch (color){
            case "Red":
                return redDragon;
            case "Green":
                return greenDragon;
            case "White":
                return whiteDragon;
        }
        System.out.println("Error in dragonImage");
        return tileBack;
    }

    private ImageIcon getWindImage(String direction){
        switch (direction){
            case "East":
                return eastWind;
            case "South":
                return southWind;
            case "West":
                return westWind;
            case "North":
                return northWind;
        }
        System.out.println("Error in windImage");
        return tileBack;
    }

    private ImageIcon getFlowerImage(int number){
        switch (number){
            case 1:
                return flower1;
            case 2:
                return flower2;
            case 3:
                return flower3;
            case 4:
                return flower4;
            case 5:
                return flower5;
            case 6:
                return flower6;
            case 7:
                return flower7;
            case 8:
                return flower8;
        }
        System.out.println("Error in flowerImage");
        return tileBack;
    }

    // updates images and JButtons based on ArrayLists in Game
    private void displayBoard() {



    }


    // inner class that represents action listener for buttons
    private class listener implements ActionListener {
        public void actionPerformed(ActionEvent event) {

            // when Tile is selected for discard
            // currently errors when clicking on non-Suite Tiles
            for (int i = 0; i < 14; i++) {
                if (p1Hand.get(i) == event.getSource()) {
                    discardPile.add(p1Hand.get(i));
                    discardPilePanel.add(discardPile.get(discardPile.size() - 1));
                    p1Hand.remove(i);
                    game.getPlayerList(game.getCurrentPlayer()).removeTile(i);
                    break;
                    //call autosort
                }
            }

            repaint();

        }

    }


}


