package MahjongCIS350;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.util.concurrent.TimeUnit;
import java.util.*;

public class Board extends JPanel {

    /** Holds and gives access to all game information **/
    private Game game;

    /** Represents all Tiles that are not in play **/
    private ArrayList<JButton> drawPile;

    /** Represents all Tiles that have been discarded **/
    private ArrayList<JButton> discardPile;

    /** Represents all Tiles in the Player's hand **/
    private ArrayList<JButton> p1Hand, p2Hand, p3Hand, p4Hand;

    /** Represents all Tiles in the Player's set pile **/
    private ArrayList<JButton> p1Sets, p2Sets, p3Sets, p4Sets;

    private JButton resetBtn;

    /** Displays all JButtons in drawPile **/
    private JPanel drawPilePanel;

    /** Displays all JButtons in discardPile **/
    private JPanel discardPilePanel;

    /** Displays all JButtons in the Player's hand **/
    private JPanel p1HandPanel, p2HandPanel, p3HandPanel, p4HandPanel;

    /** Displays all JButtons in the Player's set pile **/
    private JPanel p1SetPanel, p2SetPanel, p3SetPanel, p4SetPanel;

    /** Displays all JPanels **/
    private JLayeredPane gameBoard;

    /** Displays which Player is currently taking their turn **/
    private JLabel playerTurn;

    private JLabel p1Direction;

    /** Tile image of type Suite with Circle design **/
    private ImageIcon circle1, circle2, circle3, circle4, circle5,
            circle6, circle7, circle8, circle9;

    /** Tile image of type Suite with Bamboo design **/
    private ImageIcon bamboo1, bamboo2, bamboo3, bamboo4, bamboo5,
            bamboo6, bamboo7, bamboo8, bamboo9;

    /** Tile image of type Suite with Character design **/
    private ImageIcon character1, character2, character3, character4,
            character5, character6, character7, character8, character9;

    /** Tile image of type Flower **/
    private ImageIcon flower1, flower2, flower3, flower4, flower5,
            flower6, flower7, flower8;

    /** Tile image of type Dragon **/
    private ImageIcon redDragon, greenDragon, whiteDragon;

    /** Tile image of type Wind **/
    private ImageIcon eastWind, southWind, westWind, northWind;

    /** Tile image for face-down Tiles **/
    private ImageIcon tileBack;

    /** Action Listener **/
    private listener listener;

    /** AI turn duration **/
    private Timer timer;

    private boolean drawFlag = true;

    /******************************************************************
     * This is the Board class constructor. This implements all global
     * variables, sets up the board display, deals the Tiles, and
     * starts the AI turn timer.
     *****************************************************************/
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

        playerTurn = new JLabel(game.getCuurentPlayer().getDirection() + "'s Turn");
        p1Direction = new JLabel(game.getPlayerList(0).getDirection());

        // set background
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
        p1SetPanel.setLayout(new GridBagLayout());
        p2SetPanel.setLayout(new GridBagLayout());
        p3SetPanel.setLayout(new GridBagLayout());
        p4SetPanel.setLayout(new GridBagLayout());
        gameBoard.setLayout(new GridBagLayout());

        discardPilePanel.setBorder(BorderFactory.createBevelBorder(1));
        discardPilePanel.setPreferredSize(new Dimension(325, 200));

        createIcons();

        // set listeners for JButtons
        listener = new listener();

        // create reset button
        resetBtn = new JButton("Reset Game");
        resetBtn.addActionListener(listener);
        gameBoard.add(resetBtn);

        placeDrawPile();
        dealPlayerTiles();
        placePanels();

        // disable Player hand unless it's their turn
        if (game.getCurrentPlayer() != 0) {
            setJButton(false);
        }

        displayBoard();

        // AI turn timer and general turn actions
        letsPlay();

        timer.start();

    }

    private void letsPlay() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //if (game.isStalemate())

                if (game.getCurrentPlayer() != 0) {

                    game.dumbAI(game.getCuurentPlayer());
                    displayBoard();
                    // set claims go here

                    // test code
                    Suite disTile = (Suite)(game.getRecentDiscard());

                    if (game.isPong(game.getPlayerHand(0),
                            disTile)){

                        String message = "Claim pong of tile " +
                                disTile.getValue() + " " +
                                disTile.getDesign() + "?";

                        int takePong = JOptionPane.showConfirmDialog(null,
                                message, "Claim Message", JOptionPane.YES_NO_OPTION);

                        if (takePong == JOptionPane.YES_OPTION){

                            game.takePong(game.getPlayerList(0),game.getRecentDiscard());
                            displayBoard();
                            drawFlag = false;
                        }

                        game.setNextCurrentPlayer(0);
                    }

                    else {
                        //
                        game.setNextCurrentPlayer();
                    }

                    if (game.getCurrentPlayer() == 0) {
                        setJButton(true);
                    }else{
                        setJButton(false);
                    }

                    // Player draws and then discards
                    if (game.getCurrentPlayer() == 0 && game
                            .getPlayerList(0).getHandTile().size()
                            != 14 && drawFlag){
                        game.draw(game.getPlayerList(0));
                    }


                    displayBoard();
                }
            }
        });
    }

    private void placePanels() {
        GridBagConstraints c = new GridBagConstraints();

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
        gameBoard.add(p1Direction, c);

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
    }

    /******************************************************************
     * Creates 144 JButtons to represent all 144 Tiles in the game.
     * Places these JButtons into drawPile and displays drawPilePanel
     * in a 2-layer box format with a hollow center.
     *****************************************************************/
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

    /******************************************************************
     * Fills p1Hand, p2Hand, p3Hand, and p4Hand and places their
     * contents into p1HandPanel, p2HandPanel, p3HandPanel, and
     * p4HandPanel.
     *****************************************************************/
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

    /******************************************************************
     * This method takes a Tile as a parameter and returns the image
     * type that correctly represents the Tile. This method is used to
     * update JButtons across the Board to correctly represent the game
     * as it proceeds.
     * @param tile a Tile object
     * @return an image that correctly represents the Tile
     *****************************************************************/
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

    /******************************************************************
     * Defines all global ImageIcons.
     *****************************************************************/
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
        southWind = new ImageIcon("./src/MahjongCIS350/Images/southWind.jpg");
        westWind = new ImageIcon("./src/MahjongCIS350/Images/westWind.jpg");
        northWind = new ImageIcon("./src/MahjongCIS350/Images/northWind.jpg");

        tileBack = new ImageIcon("./src/MahjongCIS350/Images/tileBack.jpg");
    }

    /******************************************************************
     * This method returns an image that represents a Suite Tile with a
     * Circle design of the indicated value.
     * @param value the numerical value of a Suite Tile (1-9)
     * @return an image that correctly matches the indicated value,
     *         will return tileBack if there is an error
     *****************************************************************/
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
        return tileBack;
    }

    /******************************************************************
     * This method returns an image that represents a Suite Tile with a
     * Bamboo design of the indicated value.
     * @param value the numerical value of a Suite Tile (1-9)
     * @return an image that correctly matches the indicated value,
     *         will return tileBack if there is an error
     *****************************************************************/
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
        return tileBack;
    }

    /******************************************************************
     * This method returns an image that represents a Suite Tile with a
     * Character design of the indicated value.
     * @param value the numerical value of a Suite Tile (1-9)
     * @return an image that correctly matches the indicated value,
     *         will return tileBack if there is an error
     *****************************************************************/
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
        return tileBack;
    }

    /******************************************************************
     * This method returns an image that represents a Dragon Tile
     * of the indicated color.
     * @param color the color of a Dragon Tile (Red, Green, or White)
     * @return an image that correctly matches the indicated color,
     *         will return tileBack if there is an error
     *****************************************************************/
    private ImageIcon getDragonImage(String color) {
        switch (color) {
            case "Red":
                return redDragon;
            case "Green":
                return greenDragon;
            case "White":
                return whiteDragon;
        }
        return tileBack;
    }

    /******************************************************************
     * This method returns an image that represents a Wind Tile with a
     * of the indicated direction.
     * @param direction the cardinal direction of a Wind Tile (east,
     *                  south, west, or north)
     * @return an image that correctly matches the indicated direction,
     *         will return tileBack if there is an error
     *****************************************************************/
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
        return tileBack;
    }

    /******************************************************************
     * This method returns an image that represents a Flower Tile with
     * the indicated number.
     * @param number the number of a Flower Tile (1-8)
     * @return an image that correctly matches the indicated number,
     *         will return tileBack if there is an error
     *****************************************************************/
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
        return tileBack;
    }

    /******************************************************************
     * This method sets p1Hand JButtons to be enabled or disabled
     * depending on whether it is p1's turn or not.
     * @param condition true if it is p1's turn, otherwise false
     *****************************************************************/
    private void setJButton(boolean condition) {

        for (int i = 0; i < p1Hand.size(); i++) {
            p1Hand.get(i).setEnabled(condition);
        }
    }

    /******************************************************************
     * Updates the Board to match the current state of the game.
     *****************************************************************/
    private void displayBoard() {

        int p1HandSize = game.getPlayerList(0).getHandTile().size();
        int discardPileSize = game.getDiscardPile().size();
        int drawPileSize = game.getDrawPile().size();

        int p1SetSize = game.getPlayerList(0).getSetPile().size();
        int p2SetSize = game.getPlayerList(1).getSetPile().size();
        int p3SetSize = game.getPlayerList(2).getSetPile().size();
        int p4SetSize = game.getPlayerList(3).getSetPile().size();

        updateP1Hand(p1HandSize);

        updateP1SetPile(p1SetSize);

        updateP2SetPile(p2SetSize);

        updateP3SetPile(p3SetSize);

        updateP4SetPile(p4SetSize);

        updateDrawPile(drawPileSize);

        updateDiscardPile(discardPileSize);

        // update label for Player turn or game reset
        playerTurn.setText(game.getCuurentPlayer()
                .getDirection() + "'s Turn");
        p1Direction.setText(game.getPlayerList(0).getDirection());

        // display all of the updates
        repaint();
    }

    private void updateDiscardPile(int discardPileSize) {
        while (discardPile.size() < discardPileSize) {
            JButton temp = new JButton();
            temp.setPreferredSize(new Dimension(30, 35));
            discardPile.add(temp);
            discardPile.get(discardPile.size() - 1).setIcon
                    (updatedImage(game.getDiscardPile().get(game
                            .getDiscardPile().size() - 1)));
            discardPilePanel.add(temp);
        }

        while (discardPile.size() > discardPileSize){
            discardPilePanel.remove(discardPile.size() - 1);
            discardPile.remove(discardPile.size() - 1);
        }
    }

    private void updateDrawPile(int drawPileSize) {
        if (drawPile.size() > drawPileSize){
            drawPilePanel.remove(drawPile.get(drawPile.size() - 1));
            drawPile.remove(drawPile.get(drawPile.size() - 1));
        }else if (drawPile.size() < drawPileSize){
            while(drawPile.size() < drawPileSize){
                JButton temp = new JButton(tileBack);
                drawPile.add(temp);
                drawPilePanel.add(temp);
            }
        }
    }

    private void updateP4SetPile(int p4SetSize) {
        while (p4Sets.size() < p4SetSize) {
            JButton temp = new JButton();
            temp.setPreferredSize(new Dimension(25, 25));
            p4Sets.add(temp);

            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = p4Sets.size() - 1;
            p4SetPanel.add(p4Sets.get(p4Sets.size() - 1), c);
        }

        while (p4Sets.size() > p4SetSize){
            p4SetPanel.remove(p4Sets.size() - 1);
            p4Sets.remove(p4Sets.size() - 1);
        }

        for (int i = 0; i < p4SetSize; i++) {
            p4Sets.get(i).setIcon(updatedImage(game.getPlayerList(3).getSetTile(i)));
        }
    }

    private void updateP3SetPile(int p3SetSize) {
        while (p3Sets.size() < p3SetSize) {
            JButton temp = new JButton();
            temp.setPreferredSize(new Dimension(25, 25));
            p3Sets.add(temp);
            p3SetPanel.add(p3Sets.get(p3Sets.size() - 1));
        }

        while (p3Sets.size() > p3SetSize){
            p3SetPanel.remove(p3Sets.size() - 1);
            p3Sets.remove(p3Sets.size() - 1);
        }

        for (int i = 0; i < p3SetSize; i++) {
            p3Sets.get(i).setIcon(updatedImage(game.getPlayerList(2).getSetTile(i)));
        }
    }

    private void updateP2SetPile(int p2SetSize) {
        while (p2Sets.size() < p2SetSize) {
            JButton temp = new JButton();
            temp.setPreferredSize(new Dimension(25, 25));
            p2Sets.add(temp);

            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = p2Sets.size() - 1;
            p2SetPanel.add(p2Sets.get(p2Sets.size() - 1), c);
        }

        while (p2Sets.size() > p2SetSize){
            p2SetPanel.remove(p2Sets.size() - 1);
            p2Sets.remove(p2Sets.size() - 1);
        }

        for (int i = 0; i < p2SetSize; i++) {
            p2Sets.get(i).setIcon(updatedImage(game.getPlayerList(1).getSetTile(i)));
        }
    }

    private void updateP1SetPile(int p1SetSize) {
        while (p1Sets.size() < p1SetSize) {
            JButton temp = new JButton();
            temp.setPreferredSize(new Dimension(25, 25));
            p1Sets.add(temp);
            p1SetPanel.add(p1Sets.get(p1Sets.size() - 1));
        }

        while (p1Sets.size() > p1SetSize){
            p1SetPanel.remove(p1Sets.size() - 1);
            p1Sets.remove(p1Sets.size() - 1);
        }

        for (int i = 0; i < p1SetSize; i++) {
            p1Sets.get(i).setIcon(updatedImage(game.getPlayerList(0).getSetTile(i)));
        }
    }

    private void updateP1Hand(int p1HandSize) {
        while (p1Hand.size() < p1HandSize){
            JButton temp = new JButton();
            temp.setPreferredSize(new Dimension(50, 50));
            temp.addActionListener(listener);
            p1Hand.add(temp);
            p1HandPanel.add(p1Hand.get(p1Hand.size() - 1));
            p1Hand.get(p1Hand.size() - 1).setBorder(BorderFactory.createLineBorder(Color.BLUE, 5));
        }

        while(p1Hand.size() > p1HandSize){
            p1HandPanel.remove(p1Hand.size() - 1);
            p1Hand.remove(p1Hand.size() - 1);
        }

        for (int i = 0; i < p1HandSize; i++) {
            p1Hand.get(i).setIcon(updatedImage(game.getPlayerList(0).getTileFromHand(i)));
        }
    }

    /******************************************************************
     * A class within Board that handles action listeners
     *****************************************************************/
    private class listener implements ActionListener {
        public void actionPerformed(ActionEvent event) {

            int p1HandSize = game.getPlayerList(0).getHandTile().size();

            // when Tile is selected for discard, only p1 can do this
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
                        drawFlag = true;
                        break;
                    } else {
                        break;
                    }
                }
            }

            if (event.getSource() == resetBtn){
                game.reset();
            }

            displayBoard();
        }
    }


}



