package MahjongCIS350;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.util.concurrent.TimeUnit;
import java.util.*;

public class Board extends JPanel {

    private Game game;
    private ArrayList<JButton> drawPile, discardPile, p1Hand, p2Hand,
            p3Hand, p4Hand, p1Sets, p2Sets, p3Sets, p4Sets;
    private JPanel drawPilePanel, discardPilePanel, p1HandPanel,
            p2HandPanel, p3HandPanel, p4HandPanel, p1SetPanel,
            p2SetPanel, p3SetPanel, p4SetPanel;
    private JLayeredPane gameBoard;
    private JLabel playerTurn, east, south, west, north;

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

    private Timer timer;


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
        p1Sets = new ArrayList<>();
        p2Sets = new ArrayList<>();
        p3Sets = new ArrayList<>();
        p4Sets = new ArrayList<>();

        // create Panels
        drawPilePanel = new JPanel();
        discardPilePanel = new JPanel();
        p1HandPanel = new JPanel();
        p2HandPanel = new JPanel();
        p3HandPanel = new JPanel();
        p4HandPanel = new JPanel();
        p1SetPanel = new JPanel();
        p2SetPanel = new JPanel();
        p3SetPanel = new JPanel();
        p4SetPanel = new JPanel();
        gameBoard = new JLayeredPane();

        playerTurn = new JLabel(game.getPlayerList(game.getCurrentPlayer()).getDirection() + "'s Turn");
//        east = new JLabel("East");
//        south = new JLabel("South");
//        west = new JLabel("West");
//        north = new JLabel("North");

        Color darkGreen = new Color(0, 150, 100);
        drawPilePanel.setBackground(darkGreen);
        discardPilePanel.setBackground(darkGreen);
        p1HandPanel.setBackground(darkGreen);
        p2HandPanel.setBackground(darkGreen);
        p3HandPanel.setBackground(darkGreen);
        p4HandPanel.setBackground(darkGreen);
        p1SetPanel.setBackground(darkGreen);
        p2SetPanel.setBackground(darkGreen);
        p3SetPanel.setBackground(darkGreen);
        p4SetPanel.setBackground(darkGreen);
        gameBoard.setBackground(darkGreen);
        gameBoard.setOpaque(true);


        // set Layouts
        drawPilePanel.setLayout(new GridBagLayout());
        discardPilePanel.setLayout(new FlowLayout());
        p1HandPanel.setLayout(new GridBagLayout());
        p2HandPanel.setLayout(new GridBagLayout());
        p3HandPanel.setLayout(new GridBagLayout());
        p4HandPanel.setLayout(new GridBagLayout());
        gameBoard.setLayout(new GridBagLayout());

        discardPilePanel.setBorder(BorderFactory.createBevelBorder(1));
        discardPilePanel.setPreferredSize(new Dimension(325, 200));

        // create Icons
        createIcons();

        // set listeners for JButtons
        listener = new listener();

        // create and place JButton tiles into drawPile and then deal
        placeDrawPile();
        dealPlayerTiles();

        // place Panels
        GridBagConstraints c = new GridBagConstraints();

        JButton b1 = new JButton("Game Reset");//button for the game reset
        b1.setBounds(40,0,30,20);
        gameBoard.add(b1);
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource()==b1){
                    game.reset();
                }
                displayBoard();
            }
        });
        c.ipadx = 50;
        c.ipady = 50;

        c.gridx = 2;
        c.gridy = 2;
        gameBoard.add(drawPilePanel, c);
        gameBoard.setLayer(drawPilePanel, 0);
        gameBoard.add(discardPilePanel, c);
        gameBoard.setLayer(discardPilePanel, 1);

        c.ipady = 10;
        c.gridy = 4;
        gameBoard.add(p1HandPanel, c);
        c.gridy = 3;
        gameBoard.add(p1SetPanel, c);
        c.gridx = 1;
        c.gridy = 4;
        gameBoard.add(new JLabel(game.getPlayerList(0).getDirection()), c);

        c.ipady = 10;
        c.gridx = 0;
        c.gridy = 2;
        gameBoard.add(p2HandPanel, c);
        c.gridx = 1;
        gameBoard.add(p2SetPanel, c);

        c.gridx = 2;
        c.gridy = 0;
        gameBoard.add(p3HandPanel, c);
        c.gridy = 1;
        gameBoard.add((p3SetPanel), c);

        c.gridx = 4;
        c.gridy = 2;
        gameBoard.add(p4HandPanel, c);
        c.gridx = 3;
        gameBoard.add(p4SetPanel, c);

        c.gridx = 4;
        c.gridy = 0;
        gameBoard.add(playerTurn);

        add(gameBoard, BorderLayout.CENTER);

        if (game.getCurrentPlayer() != 0) {
            setJButton(false);
        }

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (game.getCurrentPlayer() != 0) {

                    game.dumbAI(game.getCuurentPlayer());
                    game.setNextCurrentPlayer();

                    displayBoard();

                    if (game.getCurrentPlayer() == 0) {
                        setJButton(true);
                    }else{
                        setJButton(false);
                    }

                    if (game.getCurrentPlayer() == 0 && game
                            .getPlayerList(0).getHandTile().size()
                            != 14){
                        game.draw(game.getPlayerList(0));
                    }

                    displayBoard();

                }
            }
        });

        timer.start();

    }

    private void dealPlayerTiles() {

        GridBagConstraints c = new GridBagConstraints();
        int p1HandSize = game.getPlayerList(0).getHandTile().size();
        int p2HandSize = game.getPlayerList(1).getHandTile().size();
        int p3HandSize = game.getPlayerList(2).getHandTile().size();
        int p4HandSize = game.getPlayerList(3).getHandTile().size();


        for (int i = 0; i < p1HandSize; i++) {
            JButton temp = new JButton();
            temp.addActionListener(listener);
            p1Hand.add(temp);
            drawPilePanel.remove(drawPile.size() - 1);
            drawPile.remove(drawPile.size() - 1);

            p1Hand.get(i).setIcon(updatedImage(game.getPlayerList
                    (0).getTileFromHand(i)));

            c.gridx = i;
            c.gridy = 0;
            p1Hand.get(i).setPreferredSize(new Dimension(50, 50));
            p1HandPanel.add(p1Hand.get(i), c);
        }

        for (int i = 0; i < p2HandSize; i++) {
            JButton temp = new JButton(tileBack);
            temp.setPreferredSize(new Dimension(25, 25));
            p2Hand.add(temp);
            drawPilePanel.remove(drawPile.size() - 1);
            drawPile.remove(drawPile.size() - 1);

            c.gridx = 0;
            c.gridy = i;
            p2HandPanel.add(p2Hand.get(i), c);
        }

        for (int i = 0; i < p3HandSize; i++) {
            JButton temp = new JButton(tileBack);
            temp.setPreferredSize(new Dimension(25, 25));
            p3Hand.add(temp);
            drawPilePanel.remove(drawPile.size() - 1);
            drawPile.remove(drawPile.size() - 1);

            c.gridx = i;
            c.gridy = 0;
            p3HandPanel.add(p3Hand.get(i), c);
        }

        for (int i = 0; i < p4HandSize; i++) {
            JButton temp = new JButton(tileBack);
            temp.setPreferredSize(new Dimension(25, 25));
            p4Hand.add(temp);
            drawPilePanel.remove(drawPile.size() - 1);
            drawPile.remove(drawPile.size() - 1);

            c.gridx = 0;
            c.gridy = i;
            p4HandPanel.add(p4Hand.get(i), c);
        }
    }

    // need to figure out how to sync this with GridBagLayout
    private void drawTile(ArrayList<JButton> hand, JPanel panel) {
        hand.add(drawPile.get(drawPile.size() - 1));
        panel.add(hand.get(hand.size() - 1));
        drawPile.remove(drawPile.size() - 1);
    }

    private ImageIcon updatedImage(Tile tile) {

        if (tile instanceof Suite) {
            switch (((Suite) tile).getDesign()) {
                case "Circle":
                    return getCircleImage(((Suite) tile).getValue());
                case "Bamboo":
                    return getBambooImage((((Suite) tile).getValue()));
                case "Character":
                    return getCharacterImage(((Suite) tile).getValue());
            }

        } else {
            switch (tile.getType()) {
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
            drawPile.add(temp);
        }

        // add to drawPilePanel to make visible
        GridBagConstraints c = new GridBagConstraints();
        int index = 0;
        for (int row = 0; row < 22; row++) {
            for (int col = 0; col < 22; col++) {
                if (row < 2 || row > 19) {
                    if (col > 1 && col < 20) {
                        c.gridx = col;
                        c.gridy = row;
                        drawPilePanel.add(drawPile.get((index)), c);
                        index++;
                    }
                } else if (col < 2 || col > 19) {
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

    private ImageIcon getCircleImage(int value) {
        switch (value) {
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

    private ImageIcon getBambooImage(int value) {
        switch (value) {
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

    private ImageIcon getCharacterImage(int value) {
        switch (value) {
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

    private ImageIcon getDragonImage(String color) {
        switch (color) {
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

    private ImageIcon getWindImage(String direction) {
        switch (direction) {
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

    private ImageIcon getFlowerImage(int number) {
        switch (number) {
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

        int p1HandSize = game.getPlayerList(0).getHandTile().size();
        int discardPileSize = game.getDiscardPile().size();
        int drawPileSize = 144 - discardPileSize;

        // update Player1 hand Tiles
        if (p1Hand.size() < p1HandSize){
            JButton temp = new JButton();
            temp.setPreferredSize(new Dimension(50, 50));
            temp.addActionListener(listener);
            p1Hand.add(temp);
            p1HandPanel.add(p1Hand.get(p1Hand.size() - 1));
            p1Hand.get(p1Hand.size() - 1).setBorder(BorderFactory.createLineBorder(Color.BLUE, 5));
        }else if(p1Hand.size() > p1HandSize){
            while (p1Hand.size() > p1HandSize){
                p1Hand.remove(p1Hand.size() - 1);
            }
        }

        for (int i = 0; i < p1HandSize; i++) {
            p1Hand.get(i).setIcon(updatedImage(game.getPlayerList(0).getTileFromHand(i)));
        }

        // update drawPile when Player or AI draws a Tile (is something still wrong with this?)
        if (drawPile.size() != drawPileSize){
            drawPilePanel.remove(drawPile.get(drawPile.size() - 1));
            drawPile.remove(drawPile.get(drawPile.size() - 1));
        }

        // update discardPile when AI discards
        if (discardPile.size() != discardPileSize) {
            JButton temp = new JButton();
            temp.setPreferredSize(new Dimension(30, 35));
            discardPile.add(temp);
            discardPile.get(discardPile.size() - 1).setIcon
                    (updatedImage(game.getDiscardPile().get(game
                            .getDiscardPile().size() - 1)));
            discardPilePanel.add(temp);
        }

        // update label for Player turn
        playerTurn.setText(game.getPlayerList(game.getCurrentPlayer())
                .getDirection() + "'s Turn");

        // display all of the updates
        repaint();

    }


    // inner class that represents action listener for buttons
    private class listener implements ActionListener {
        public void actionPerformed(ActionEvent event) {

            int p1HandSize = game.getPlayerList(0).getHandTile().size();

            // when Tile is selected for discard
            // is it the Player's turn?
            // if (game.getCurrentPlayer() == 0) {
            for (int i = 0; i < p1HandSize; i++) {

                // find out which Tile they selected
                if (p1Hand.get(i) == event.getSource()) {

                    // are you sure you want to discard?
                    int discard = JOptionPane.showConfirmDialog(null,
                            "Discard Tile?", "Discard", JOptionPane.YES_NO_OPTION);
                    if (discard == JOptionPane.YES_OPTION) {
                        game.discard(game.getPlayerList(0), i);

                        JButton temp = new JButton(null, p1Hand.get(i).getIcon());
                        temp.setPreferredSize(new Dimension(30, 35));
                        p1HandPanel.remove(p1Hand.size() - 1);
                        p1Hand.remove(p1Hand.size() - 1);
                        discardPile.add(temp);
                        discardPilePanel.add(discardPile.get(discardPile.size() - 1));

                        game.setNextCurrentPlayer();
                        break;
                    } else {
                        break;
                    }
                }
            }


            // claim Tile
            // will need to sync with Game, Player can only add
            // Tile to their hand after selecting in the GUI

            // }

            displayBoard();
        }
    }


    private void setJButton(boolean condition) {

        for (int i = 0; i < p1Hand.size(); i++) {
            p1Hand.get(i).setEnabled(condition);
        }
    }
}



