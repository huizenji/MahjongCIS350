package mahjongCIS350;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.util.*;

/***********************************************************************
 * This class contains the JPanel of the game along with the JButtons,
 * that interact with the GUI class.
 *
 * @Authors: Wayne Chen, Jillian Huizenga, Chris Paul, Xianghe Zhao
 * @Version: 4/08/2020
 **********************************************************************/
public class Board extends JPanel {

    /** Holds and gives access to all game information. **/
    private Game game;

    /** Represents all Tiles that are not in play. **/
    private ArrayList<JButton> drawPile;

    /** Represents all Tiles that have been discarded. **/
    private ArrayList<JButton> discardPile;

    /** Represents all Tiles in the Player's hand. **/
    private ArrayList<JButton> p1Hand, p2Hand, p3Hand, p4Hand;

    /** Represents all Tiles in the Player's set pile. **/
    private ArrayList<JButton> p1Sets, p2Sets, p3Sets, p4Sets;

    /** The JButton for the reset button. **/
    private JButton resetBtn;

    /** Displays all JButtons in drawPile. **/
    private JPanel drawPilePanel;

    /** Displays all JButtons in discardPile. **/
    private JPanel discardPilePanel;

    /** Displays all JButtons in the Player's hand. **/
    private JPanel p1HandPanel, p2HandPanel, p3HandPanel, p4HandPanel;

    /** Displays all JButtons in the Player's set pile. **/
    private JPanel p1SetPanel, p2SetPanel, p3SetPanel, p4SetPanel;

    /** Displays all JPanels. **/
    private JLayeredPane gameBoard;

    /** Displays which Player is currently taking their turn. **/
    private JLabel playerTurn;

    /** Displays the direction of the human Player. **/
    private JLabel p1Direction;

    /** Displays the current score of the game. **/
    private JLabel scoreBoard;

    /** Tile image of type Suit with Circle design. **/
    private ImageIcon circle1, circle2, circle3, circle4, circle5,
            circle6, circle7, circle8, circle9;

    /** Tile image of type Suit with Bamboo design. **/
    private ImageIcon bamboo1, bamboo2, bamboo3, bamboo4, bamboo5,
            bamboo6, bamboo7, bamboo8, bamboo9;

    /** Tile image of type Suit with Character design. **/
    private ImageIcon character1, character2, character3, character4,
            character5, character6, character7, character8, character9;

    /** Tile image of type Flower. **/
    private ImageIcon flower1, flower2, flower3, flower4, flower5,
            flower6, flower7, flower8;

    /** Tile image of type Dragon. **/
    private ImageIcon redDragon, greenDragon, whiteDragon;

    /** Tile image of type Wind. **/
    private ImageIcon eastWind, southWind, westWind, northWind;

    /** Tile image for face-down Tiles. **/
    private ImageIcon tileBack;

    /** Action Listener for JButton. **/
    private Listener listener;

    /** AI turn duration. **/
    private Timer timer;

    /** Determines if a Player should draw a Tile from drawPile. **/
    private boolean drawFlag = true;

    /** Determine if the program should hide tiles. **/
    private boolean removeImage;

    /** Number of tiles in display. **/
    private int pileNum;

    /** Background Color. **/
    private Color color;

    /** Default Background color of Red Shade. **/
    private final int defaultR = 0;

    /** Default Background color of Green Shade. **/
    private final int defaultG = 150;

    /** Default Background color of Blue Shade. **/
    private final int defaultB = 100;

    /** Default discard pile width **/
    private final int disWid = 350;

    /** Default discard pile height **/
    private final int disHei = 225;

    /*******************************************************************
     * This is the Board class constructor. This implements all global
     * variables, sets up the board display, deals the Tiles, and
     * starts the AI turn timer.
     ******************************************************************/
    public Board() {

        // create Game
        game = new Game();

        // Set shade of rgb to default dark green color
        color = new Color(defaultR, defaultG, defaultB);

        scoreBoard = new JLabel();

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

        playerTurn = new JLabel(game.getCurrentPlayer().
                getDirection() + "'s Turn");
        p1Direction = new JLabel(game.getPlayerList(0).
                getDirection());

        // Set background
        updateBackground();

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

        discardPilePanel.setBorder(BorderFactory.
                createBevelBorder(1));
        discardPilePanel.setPreferredSize(
                new Dimension(disWid, disHei));
        gameBoard.setBorder(BorderFactory.createLineBorder(Color.BLACK,
                2));

        createIcons();

        // set listener for JButtons
        listener = new Listener();

        // create reset button
        resetBtn = new JButton("Reset");
        resetBtn.addActionListener(listener);
        gameBoard.add(resetBtn);
        resetBtn.setEnabled(false);

        add(scoreBoard, BorderLayout.NORTH);
        placeDrawPile();
        dealPlayerTiles();
        placePanels();

        // disable human Player hand at start unless it's their turn
        if (game.getCurrentPlayerIndex() != 0) {

            enablePlayer1Hand(false);
        }

        displayBoard();
        removeImage = true;
        pileNum = drawPile.size();

        // AI turn timer and general turn actions
        letsPlay();

        // Ask if user wants to player more traditional game mode
        // Switch mode if applicable
        gameModeSwap();
        updateScore();
        timer.start();
    }


    /*******************************************************************
     * This method uses a timer to go through each players turn.
     ******************************************************************/
    private void letsPlay() {

        timer = new Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {

                String msgAIMahjong = "An opponent has declared "
                        + "Mahjong. Sorry, you lose.";

                // Player winning off of Mahjong
                String msgWin = "Do you wish to declare "
                        + "Mahjong and win?";

                String msgWin2 = "Congratulations, "
                        + "you won";

                // Check for Stalemate
                if (game.isStalemate()) {

                    stalemateSeq();
                }

                // AI turn
                if (game.getCurrentPlayerIndex() != 0) {

                    // AI draws Tile
                    game.dumbAIDraw(game.getCurrentPlayer());
                    drawFlag = true;

                    // Check if AI can declare Mahjong
                    if (game.isMahjong(game.getPlayerHand(game
                            .getCurrentPlayerIndex()), null)) {

                        JOptionPane.showMessageDialog(
                                null, msgAIMahjong);
                        enablePlayer1Hand(false);

                        enablePlayer1Hand(false);
                        displayWinningHand(
                                game.getCurrentPlayerIndex());
                        timer.stop();
                        drawFlag = false;
                        resetBtn.setEnabled(true);

                        revealWinningAIHand(game
                                .getCurrentPlayerIndex());

                        game.increaseScore(
                                game.getCurrentPlayerIndex(),
                                false);
                        gameModeSwap();

                        if (game.getCurrentPlayerIndex()
                                != game.getStartingPlayer()) {

                            game.setNextStartingPlayer();
                        }
                    }

                    // Checking if kong is drawn into using set Pile
                    if (game.isKongDraw(game.getPlayerList(
                            game.getCurrentPlayerIndex()))) {

                        game.takeKongDraw(game.getCurrentPlayer());
                    }

                    else {

                        game.generalAIDiscard(game.getCurrentPlayer());
                    }

                    // AI discards Tile
                    displayBoard();
                    checkSeq();

                    if (game.getCurrentPlayerIndex() == 0) {

                        enablePlayer1Hand(true);

                    } else {

                        enablePlayer1Hand(false);
                    }

                    // Check for Stalemate
                    if (game.isStalemate()) {

                        stalemateSeq();
                    }

                    // Player draws and then discards and see if they
                    // win
                    if (game.getCurrentPlayerIndex() == 0 && game
                            .getPlayerList(0).
                                    getHandTile().size()
                            != 14 && drawFlag) {

                        game.draw(game.getPlayerList(0));
                        displayBoard();

                        if (game.isMahjong(game.getPlayerHand(game
                                        .getCurrentPlayerIndex()),
                                null)) {

                            int mahjong = JOptionPane.showConfirmDialog(
                                    null, msgWin,
                                    "Claim Message",
                                    JOptionPane.YES_NO_OPTION);

                            if (mahjong == JOptionPane.YES_OPTION) {

                                JOptionPane.showMessageDialog(
                                        null, msgWin2);

                                enablePlayer1Hand(false);
                                displayWinningHand(0);
                                timer.stop();
                                drawFlag = false;
                                resetBtn.setEnabled(true);
                                game.increaseScore(
                                        game.getCurrentPlayerIndex(),
                                        false);
                                gameModeSwap();

                                if (0 != game.getStartingPlayer()) {

                                    game.setNextStartingPlayer();
                                }
                            }
                        }

                        if (game.isKongDraw(game.getPlayerList(
                                0))) {

                            kongDrawSeq();
                        }
                    }

                    displayBoard();
                }
            }
        });
    }

    /*******************************************************************
     * This method places the individual panels in the overall JPanel
     * that represents the game board.
     ******************************************************************/
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

    /*******************************************************************
     * Creates 144 JButtons to represent all 144 Tiles in the game.
     * Places these JButtons into drawPile and displays drawPilePanel
     * in a 2-layer box format with a hollow center.
     ******************************************************************/
    private void placeDrawPile() {

        for (int i = 0; i < 144; i++) {

            JButton temp = new JButton(null, tileBack);
            temp.setPreferredSize(new Dimension(25, 25));
            drawPile.add(temp);
        }

        // add JButton to drawPilePanel to make visible
        GridBagConstraints c = new GridBagConstraints();
        int index = 0;

        for (int row = 0; row < 22; row++) {

            for (int col = 0; col < 22; col++) {

                if (row < 2 || row > 19) {

                    if (col > 1 && col < 20) {

                        c.gridx = col;
                        c.gridy = row;
                        drawPilePanel.add(drawPile.get(index), c);
                        index++;
                    }

                } else if (col < 2 || col > 19) {

                    c.gridx = col;
                    c.gridy = row;
                    drawPilePanel.add(drawPile.get(index), c);
                    index++;
                }
            }
        }
    }

    /*******************************************************************
     * Fills p1Hand, p2Hand, p3Hand, and p4Hand and places their
     * contents into p1HandPanel, p2HandPanel, p3HandPanel, and
     * p4HandPanel.
     ******************************************************************/
    private void dealPlayerTiles() {

        GridBagConstraints c = new GridBagConstraints();
        int p1HandSize = game.getPlayerList(
                0).getHandTile().size();
        int p2HandSize = game.getPlayerList(
                1).getHandTile().size();
        int p3HandSize = game.getPlayerList(
                2).getHandTile().size();
        int p4HandSize = game.getPlayerList(
                3).getHandTile().size();

        for (int i = 0; i < p1HandSize; i++) {

            JButton temp = new JButton();
            temp.addActionListener(listener);
            p1Hand.add(temp);
            drawPilePanel.remove(drawPile.size() - 1);
            drawPile.remove(drawPile.size() - 1);

            p1Hand.get(i).setIcon(updatedImage(game.getPlayerList(
                    0).getTileFromHand(i)));

            c.gridx = i;
            c.gridy = 0;
            p1Hand.get(i).setPreferredSize(new Dimension(
                    50, 50));
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

    /*******************************************************************
     * Helper method that reveals the winning player's hand.
     * @param playerIndex index of winning player
     ******************************************************************/
    private void revealWinningAIHand(final int playerIndex) {

         for (int i = 0; i < game.getCurrentPlayer().getHandTile()
                 .size(); i++) {

             if (playerIndex == 1) {

                 p2Hand.get(i).setIcon(updatedImage(game.getPlayerList(
                         0).getTileFromHand(i)));
                 p2Hand.get(i).setPreferredSize(new Dimension(
                         50, 50));

             } else if (playerIndex == 2) {

                 p3Hand.get(i).setIcon(updatedImage(game.getPlayerList(
                         0).getTileFromHand(i)));
                 p3Hand.get(i).setPreferredSize(new Dimension(
                         50, 50));

             } else if (playerIndex == 3) {

                 p4Hand.get(i).setIcon(updatedImage(game.getPlayerList(
                         0).getTileFromHand(i)));
                 p4Hand.get(i).setPreferredSize(new Dimension(
                         50, 50));
             } else {

                 throw new IllegalArgumentException("Player 1 Hand "
                         + "already revealed");
             }
         }
    }

    /*******************************************************************
     * This method takes a Tile as a parameter and returns the image
     * type that correctly represents the Tile. This method is used to
     * update JButtons across the Board to correctly represent the game
     * as it proceeds.
     *
     * @param tile a Tile object.
     * @return an image that correctly represents the Tile.
     ******************************************************************/
    private ImageIcon updatedImage(final Tile tile) {

        if (tile instanceof Suit) {
            switch (((Suit) tile).getDesign()) {
                case "Circle":
                    return getCircleImage(((Suit) tile).getValue());
                case "Bamboo":
                    return getBambooImage((((Suit) tile).getValue()));
                case "Character":
                    return getCharacterImage(((Suit) tile).getValue());
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

        return tileBack;
    }

    /*******************************************************************
     * Defines all global ImageIcons.
     ******************************************************************/
    private void createIcons() {

        // Sets the Image for circle Suit Tiles
        circle1 = new ImageIcon("./src/mahjongCIS350/Images/"
                + "circle1.jpg");
        circle2 = new ImageIcon("./src/mahjongCIS350/Images/"
                + "circle2.jpg");
        circle3 = new ImageIcon("./src/mahjongCIS350/Images/"
                + "circle3.jpg");
        circle4 = new ImageIcon("./src/mahjongCIS350/Images/"
                + "circle4.jpg");
        circle5 = new ImageIcon("./src/mahjongCIS350/Images/"
                + "circle5.jpg");
        circle6 = new ImageIcon("./src/mahjongCIS350/Images/"
                + "circle6.jpg");
        circle7 = new ImageIcon("./src/mahjongCIS350/Images/"
                + "circle7.jpg");
        circle8 = new ImageIcon("./src/mahjongCIS350/Images/"
                + "circle8.jpg");
        circle9 = new ImageIcon("./src/mahjongCIS350/Images/"
                + "circle9.jpg");

        // Sets the Image for bamboo Suit Tiles
        bamboo1 = new ImageIcon("./src/mahjongCIS350/Images/"
                + "bamboo1.jpg");
        bamboo2 = new ImageIcon("./src/mahjongCIS350/Images/"
                + "bamboo2.jpg");
        bamboo3 = new ImageIcon("./src/mahjongCIS350/Images/"
                + "bamboo3.jpg");
        bamboo4 = new ImageIcon("./src/mahjongCIS350/Images/"
                + "bamboo4.jpg");
        bamboo5 = new ImageIcon("./src/mahjongCIS350/Images/"
                + "bamboo5.jpg");
        bamboo6 = new ImageIcon("./src/mahjongCIS350/Images/"
                + "bamboo6.jpg");
        bamboo7 = new ImageIcon("./src/mahjongCIS350/Images/"
                + "bamboo7.jpg");
        bamboo8 = new ImageIcon("./src/mahjongCIS350/Images/"
                + "bamboo8.jpg");
        bamboo9 = new ImageIcon("./src/mahjongCIS350/Images/"
                + "bamboo9.jpg");

        // Sets the Image for character Suit Tiles
        character1 = new ImageIcon(
                "./src/mahjongCIS350/Images/character1.jpg");
        character2 = new ImageIcon(
                "./src/mahjongCIS350/Images/character2.jpg");
        character3 = new ImageIcon(
                "./src/mahjongCIS350/Images/character3.jpg");
        character4 = new ImageIcon(
                "./src/mahjongCIS350/Images/character4.jpg");
        character5 = new ImageIcon(
                "./src/mahjongCIS350/Images/character5.jpg");
        character6 = new ImageIcon(
                "./src/mahjongCIS350/Images/character6.jpg");
        character7 = new ImageIcon(
                "./src/mahjongCIS350/Images/character7.jpg");
        character8 = new ImageIcon(
                "./src/mahjongCIS350/Images/character8.jpg");
        character9 = new ImageIcon(
                "./src/mahjongCIS350/Images/character9.jpg");

        // Sets the Image for Flower Tiles
        flower1 = new ImageIcon(
                "./src/mahjongCIS350/Images/flower1.jpg");
        flower2 = new ImageIcon(
                "./src/mahjongCIS350/Images/flower2.jpg");
        flower3 = new ImageIcon(
                "./src/mahjongCIS350/Images/flower3.jpg");
        flower4 = new ImageIcon(
                "./src/mahjongCIS350/Images/flower4.jpg");
        flower5 = new ImageIcon(
                "./src/mahjongCIS350/Images/flower5.jpg");
        flower6 = new ImageIcon(
                "./src/mahjongCIS350/Images/flower6.jpg");
        flower7 = new ImageIcon(
                "./src/mahjongCIS350/Images/flower7.jpg");
        flower8 = new ImageIcon(
                "./src/mahjongCIS350/Images/flower8.jpg");

        // Sets the Image for Dragon Tiles
        redDragon = new ImageIcon(
                "./src/mahjongCIS350/Images/redDragon.jpg");
        greenDragon = new ImageIcon(
                "./src/mahjongCIS350/Images/greenDragon.jpg");
        whiteDragon = new ImageIcon(
                "./src/mahjongCIS350/Images/whiteDragon.jpg");

        // Sets the Image for the Wind Tiles
        eastWind = new ImageIcon(
                "./src/mahjongCIS350/Images/eastWind.jpg");
        southWind = new ImageIcon(
                "./src/mahjongCIS350/Images/southWind.jpg");
        westWind = new ImageIcon(
                "./src/mahjongCIS350/Images/westWind.jpg");
        northWind = new ImageIcon(
                "./src/mahjongCIS350/Images/northWind.jpg");

        // Sets the Image for the back of a Tile
        tileBack = new ImageIcon(
                "./src/mahjongCIS350/Images/tileBack.jpg");
    }

    /*******************************************************************
     * This method returns an image that represents a Suit Tile with a
     * Circle design of the indicated value.
     *
     * @param value the numerical value of a Suit Tile (1-9).
     * @return an Image that correctly matches the indicated value,
     *         will return tileBack if there is an error.
     ******************************************************************/
    private ImageIcon getCircleImage(final int value) {

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

    /*******************************************************************
     * This method returns an image that represents a Suit Tile with a
     * Bamboo design of the indicated value.
     *
     * @param value the numerical value of a Suit Tile (1-9).
     * @return an Image that correctly matches the indicated value,
     *         will return tileBack if there is an error.
     ******************************************************************/
    private ImageIcon getBambooImage(final int value) {

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

    /*******************************************************************
     * This method returns an Image that represents a Suit Tile with a
     * Character design of the indicated value.
     *
     * @param value the numerical value of a Suit Tile (1-9).
     * @return an Image that correctly matches the indicated value,
     *         will return tileBack if there is an error.
     ******************************************************************/
    private ImageIcon getCharacterImage(final int value) {

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

    /*******************************************************************
     * This method returns an image that represents a Dragon Tile
     * of the indicated color.
     *
     * @param color the color of a Dragon Tile (Red, Green, or White).
     * @return an Image that correctly matches the indicated color,
     *         will return tileBack if there is an error.
     ******************************************************************/
    private ImageIcon getDragonImage(final String color) {

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

    /*******************************************************************
     * This method returns an Image that represents a Wind Tile
     * of the indicated direction.
     *
     * @param direction the cardinal direction of a Wind Tile (east,
     *                  south, west, or north).
     * @return an Image that correctly matches the indicated direction,
     *         will return tileBack if there is an error.
     ******************************************************************/
    private ImageIcon getWindImage(final String direction) {

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

    /*******************************************************************
     * This method returns an Image that represents a Flower Tile with
     * the indicated number.
     *
     * @param number the number of a Flower Tile (1-8).
     * @return an Image that correctly matches the indicated number,
     *         will return tileBack if there is an error.
     ******************************************************************/
    private ImageIcon getFlowerImage(final int number) {

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

    /*******************************************************************
     * This method sets p1Hand JButtons to be enabled or disabled
     * depending on whether it is p1's turn or not.
     *
     * @param condition true if it is p1's turn, otherwise false.
     ******************************************************************/
    private void enablePlayer1Hand(final boolean condition) {

        for (int i = 0; i < p1Hand.size(); i++) {

            p1Hand.get(i).setEnabled(condition);
        }
    }

    /******************************************************************
     * Updates the Board to match the current state of the game.
     *****************************************************************/
    private void displayBoard() {

        int p1HandSize = game.getPlayerList(
                0).getHandTile().size();
        
        int discardPileSize = game.getDiscardPile().size();
        int drawPileSize = game.getDrawPile().size();

        int p1SetSize = game.getPlayerList(
                0).getSetPile().size();
        int p2SetSize = game.getPlayerList(
                1).getSetPile().size();
        int p3SetSize = game.getPlayerList(
                2).getSetPile().size();
        int p4SetSize = game.getPlayerList(
                3).getSetPile().size();

        updateP1Hand(p1HandSize);

        updateP1SetPile(p1SetSize);
        updateP2SetPile(p2SetSize);
        updateP3SetPile(p3SetSize);
        updateP4SetPile(p4SetSize);

        updateDrawPile(drawPileSize);

        updateDiscardPile(discardPileSize);

        // update Label for Player turn or game reset
        playerTurn.setText(game.getCurrentPlayer()
                .getDirection() + "'s Turn");
        p1Direction.setText(game.getPlayerList(
                0).getDirection());

        // highlight current Player
        if (game.getCurrentPlayerIndex() == 0) {

            p1HandPanel.setBackground(Color.GREEN);
            p4HandPanel.setBackground(color);

        } else if (game.getCurrentPlayerIndex() == 1) {

            p2HandPanel.setBackground(Color.GREEN);
            p1HandPanel.setBackground(color);

        } else if (game.getCurrentPlayerIndex() == 2) {

            p3HandPanel.setBackground(Color.GREEN);
            p2HandPanel.setBackground(color);

        } else if (game.getCurrentPlayerIndex() == 3) {

            p4HandPanel.setBackground(Color.GREEN);
            p3HandPanel.setBackground(color);
        }

        // display all of the updates
        repaint();
    }

    /*******************************************************************
     * Updates the background of all panels.
     ******************************************************************/
    private void updateBackground() {

        // set background
        drawPilePanel.setBackground(color);
        discardPilePanel.setBackground(color);
        p1HandPanel.setBackground(color);
        p2HandPanel.setBackground(color);
        p3HandPanel.setBackground(color);
        p4HandPanel.setBackground(color);
        p1SetPanel.setBackground(color);
        p2SetPanel.setBackground(color);
        p3SetPanel.setBackground(color);
        p4SetPanel.setBackground(color);
        gameBoard.setBackground(color);
        gameBoard.setOpaque(true);
    }

    /*******************************************************************
     * This method resets the color of all the remove tiles from the
     * draw pile.
     ******************************************************************/
    private void updateRemovedTile() {

        for (int i = pileNum - 1; i < drawPile.size(); i++) {

            drawPile.get(i).setBackground(color);
        }
    }

    /*******************************************************************
     * This method updates the discard pile.
     *
     * @param discardPileSize The amount of tiles in the discard pile.
     ******************************************************************/
    private void updateDiscardPile(final int discardPileSize) {

        while (discardPile.size() < discardPileSize) {

            JButton temp = new JButton();
            temp.setPreferredSize(new Dimension(30, 35));
            discardPile.add(temp);
            discardPile.get(discardPile.size() - 1).setIcon(
                    updatedImage(game.getDiscardPile().get(game
                            .getDiscardPile().size() - 1)));
            discardPilePanel.add(temp);
        }

        while (discardPile.size() > discardPileSize) {

            discardPilePanel.remove(discardPile.size() - 1);
            discardPile.remove(discardPile.size() - 1);
        }
    }

    /*******************************************************************
     * This method updates the draw pile.
     *
     * @param drawPileSize The amount of tiles in the draw pile.
     ******************************************************************/
    private void updateDrawPile(final int drawPileSize) {

        if (pileNum > drawPileSize && removeImage) {

            drawPile.get(pileNum - 1).setIcon(null);
            drawPile.get(pileNum - 1).setBackground(color);
            drawPile.get(pileNum - 1).setBorder(
                    BorderFactory.createEmptyBorder());
            pileNum--;
        }

        if (drawPile.size() > drawPileSize && !removeImage) {

            drawPilePanel.remove(drawPile.get(drawPile.size() - 1));
            drawPile.remove(drawPile.get(drawPile.size() - 1));

        } else if (drawPile.size() < drawPileSize) {

            while (drawPile.size() < drawPileSize) {

                JButton temp = new JButton(tileBack);
                drawPile.add(temp);
                drawPilePanel.add(temp);
            }
        }
    }

    /*******************************************************************
     * This method updates set the set pile of player 4.
     *
     * @param p4SetSize The size of the set pile of player 4.
     ******************************************************************/
    private void updateP4SetPile(final int p4SetSize) {

        while (p4Sets.size() < p4SetSize) {

            JButton temp = new JButton();
            temp.setPreferredSize(new Dimension(25, 25));
            p4Sets.add(temp);

            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = p4Sets.size() - 1;
            p4SetPanel.add(p4Sets.get(p4Sets.size() - 1), c);
        }

        while (p4Sets.size() > p4SetSize) {

            p4SetPanel.remove(p4Sets.size() - 1);
            p4Sets.remove(p4Sets.size() - 1);
        }

        for (int i = 0; i < p4SetSize; i++) {

            p4Sets.get(i).setIcon(updatedImage(
                    game.getPlayerList(3)
                            .getSetTile(i)));
        }
    }

    /*******************************************************************
     * This method updates set the set pile of player 3.
     *
     * @param p3SetSize The size of the set pile of player 3.
     ******************************************************************/
    private void updateP3SetPile(final int p3SetSize) {

        while (p3Sets.size() < p3SetSize) {

            JButton temp = new JButton();
            temp.setPreferredSize(new Dimension(25, 25));
            p3Sets.add(temp);
            p3SetPanel.add(p3Sets.get(p3Sets.size() - 1));
        }

        while (p3Sets.size() > p3SetSize) {

            p3SetPanel.remove(p3Sets.size() - 1);
            p3Sets.remove(p3Sets.size() - 1);
        }

        for (int i = 0; i < p3SetSize; i++) {

            p3Sets.get(i).setIcon(updatedImage(
                    game.getPlayerList(2)
                            .getSetTile(i)));
        }
    }

    /*******************************************************************
     * This method updates set the set pile of player 2.
     *
     * @param p2SetSize The size of the set pile of player 2.
     ******************************************************************/
    private void updateP2SetPile(final int p2SetSize) {

        while (p2Sets.size() < p2SetSize) {

            JButton temp = new JButton();
            temp.setPreferredSize(new Dimension(25, 25));
            p2Sets.add(temp);

            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = p2Sets.size() - 1;
            p2SetPanel.add(p2Sets.get(p2Sets.size() - 1), c);
        }

        while (p2Sets.size() > p2SetSize) {

            p2SetPanel.remove(p2Sets.size() - 1);
            p2Sets.remove(p2Sets.size() - 1);
        }

        for (int i = 0; i < p2SetSize; i++) {

            p2Sets.get(i).setIcon(updatedImage(
                    game.getPlayerList(1)
                            .getSetTile(i)));
        }
    }

    /*******************************************************************
     * This method updates set  the set pile of player 1.
     *
     * @param p1SetSize The size of the set pile of player 1.
     ******************************************************************/
    private void updateP1SetPile(final int p1SetSize) {

        while (p1Sets.size() < p1SetSize) {

            JButton temp = new JButton();
            temp.setPreferredSize(new Dimension(25, 25));
            p1Sets.add(temp);
            p1SetPanel.add(p1Sets.get(p1Sets.size() - 1));
        }

        while (p1Sets.size() > p1SetSize) {

            p1SetPanel.remove(p1Sets.size() - 1);
            p1Sets.remove(p1Sets.size() - 1);
        }

        for (int i = 0; i < p1SetSize; i++) {

            p1Sets.get(i).setIcon(updatedImage(
                    game.getPlayerList(0)
                            .getSetTile(i)));
        }
    }

    /******************************************************************
     * This method updates the player's hand.
     *
     * @param p1HandSize The hand of the first player.
     *****************************************************************/
    private void updateP1Hand(final int p1HandSize) {

        while (p1Hand.size() < p1HandSize) {

            JButton temp = new JButton();
            temp.setPreferredSize(new Dimension(50, 50));
            temp.addActionListener(listener);
            p1Hand.add(temp);
            p1HandPanel.add(p1Hand.get(p1Hand.size() - 1));

            // give new Tile a border
            p1Hand.get(p1Hand.size() - 1).setBorder(BorderFactory
                    .createLineBorder(Color.BLUE, 5));
        }

        while (p1Hand.size() > p1HandSize) {

            p1HandPanel.remove(p1Hand.size() - 1);
            p1Hand.remove(p1Hand.size() - 1);
        }

        for (int i = 0; i < p1HandSize; i++) {

            p1Hand.get(i).setIcon(updatedImage(
                    game.getPlayerList(0)
                            .getTileFromHand(i)));
        }
    }

    /******************************************************************
     * This method updates the player's hand.
     *
     * @param p2HandSize The hand of the second player.
     *****************************************************************/
    private void updateP2Hand(final int p2HandSize) {

        while (p2Hand.size() < p2HandSize) {

            JButton temp = new JButton();
            temp.setPreferredSize(new Dimension(25, 25));
            p2Hand.add(temp);
            p2HandPanel.add(p2Hand.get(p2Hand.size() - 1));

        }

        while (p2Hand.size() > p2HandSize) {

            p2HandPanel.remove(p2Hand.size() - 1);
            p2Hand.remove(p2Hand.size() - 1);
        }
    }

    /******************************************************************
     * This method updates the player's hand.
     *
     * @param p3HandSize The hand of the third player.
     *****************************************************************/
    private void updateP3Hand(final int p3HandSize) {

        while (p3Hand.size() < p3HandSize) {

            JButton temp = new JButton();
            temp.setPreferredSize(new Dimension(25, 25));
            p3Hand.add(temp);
            p3HandPanel.add(p3Hand.get(p3Hand.size() - 1));

        }

        while (p3Hand.size() > p3HandSize) {

            p3HandPanel.remove(p3Hand.size() - 1);
            p3Hand.remove(p3Hand.size() - 1);
        }
    }

    /******************************************************************
     * This method updates the player's hand.
     *
     * @param p4HandSize The hand size of the fourth player.
     *****************************************************************/
    private void updateP4Hand(final int p4HandSize) {

        while (p4Hand.size() < p4HandSize) {

            JButton temp = new JButton();
            temp.setPreferredSize(new Dimension(25, 25));
            p4Hand.add(temp);
            p4HandPanel.add(p4Hand.get(p4Hand.size() - 1));

        }

        while (p4Hand.size() > p4HandSize) {

            p4HandPanel.remove(p4Hand.size() - 1);
            p4Hand.remove(p4Hand.size() - 1);
        }
    }

    /*******************************************************************
     * This method ask the user if they want to switch game modes.
     ******************************************************************/
    private void gameModeSwap() {

        String message = "Would you like to play ";
        String message2;

        if (!game.getGameOptionSimple()) {

            message2 = "Simple Mode?";
        } else {

            message2 = "Traditional Mode?";
        }

        int swap = JOptionPane.showConfirmDialog(
                null, message + message2,
                "Mode Switch",
                JOptionPane.YES_NO_OPTION);

        if (swap == JOptionPane.YES_OPTION) {

            game.setGameOptionSimple(!game.getGameOptionSimple());
            game.reset();
            resetBoard();
            resetBtn.setEnabled(false);
        }
    }

    /*******************************************************************
     * This method checks all the Players and returns a Player who has
     * Mahjong.
     *
     * @return Player's index in Game's playerList if a player has a
     *         Mahjong, else it returns a -1 for when a Tile is
     *         discarded.
     ******************************************************************/
    private int isMahjongPlayerDiscard() {

        if (game.getDiscardPile().size() == 0) {

            return -1;
        }

        for (int i = game.getCurrentPlayerIndex() + 1;
             i < (game.getCurrentPlayerIndex() + game.TOTALPLAYER);
             i++) {

            if (game.isMahjong(game.getPlayerHand(
                    (i % game.TOTALPLAYER)), game.getRecentDiscard())) {

                return i % game.TOTALPLAYER;
            }
        }

        return -1;
    }

    /*******************************************************************
     * This performs the sequence of actions if a stalemate occurs.
     ******************************************************************/
    private void stalemateSeq() {


        JOptionPane.showMessageDialog(null,
                "There are no possible ways to "
                        + "win, therefore the game is a "
                        + "stalemate.");

        enablePlayer1Hand(false);
        timer.stop();
        drawFlag = false;
        resetBtn.setEnabled(true);
        gameModeSwap();
    }

    /*******************************************************************
     * This method performs a sequence of action when a chi can be
     * claimed.
     *
     * @param disTile The recent discarded tile.
     ******************************************************************/
    private void chiSeq(final Suit disTile) {

        String message = "Claim chi of tile "
                + disTile.getValue() + " "
                + disTile.getDesign() + "?";
        String msgTitle = "Claim Message";

        int takeChi = JOptionPane.showConfirmDialog(null,
                message, msgTitle,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE, updatedImage(disTile));

        if (takeChi == JOptionPane.YES_OPTION) {

            message = "Claim Chi with the displayed tiles?";
            ArrayList<Integer> options = game.getChiTile(
                    game.getPlayerList(0),
                    game.getRecentDiscard());

            // Loop Through Options
            while (options.size() > 0) {

                Suit tile1 = (Suit) game.getPlayerList(0).
                        getHandTile().get(options.get(0));
                Suit tile2 = (Suit) game.getPlayerList(0).
                        getHandTile().get(options.get(1));

                ImageIcon icon1 = updatedImage(tile1);
                ImageIcon icon2 = updatedImage(tile2);

                int claimChi = JOptionPane.showConfirmDialog(
                        null, new JLabel(
                                message, icon2, SwingConstants.LEFT),
                        msgTitle,
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE, icon1);

                if (claimChi == JOptionPane.YES_OPTION) {

                    game.takeChi(game.getPlayerList(0),
                            options.get(0), options.get(1));
                    displayBoard();
                    game.setNextCurrentPlayer(0);
                    drawFlag = false;
                    break;
                }

                else {
                    options.remove(0);
                    options.remove(0);
                }
            }
        }
    }

    /******************************************************************
     * This method performs a sequence of action when a pong can
     * be claimed.
     *
     * @param disTile The recent discarded tile.
     *****************************************************************/
    private void pongSeq(final Tile disTile) {

        String message = "Claim pong of the Displayed Tile.";

        int takePong = JOptionPane.showConfirmDialog(
                null,
                message, "Claim Message",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE, updatedImage(disTile));

        if (takePong == JOptionPane.YES_OPTION) {

            game.takePong(game.getPlayerList(0), game
                    .getRecentDiscard());
            displayBoard();
            game.setNextCurrentPlayer(0);
            drawFlag = false;
        }
    }

    /******************************************************************
     * This method performs a sequence of action when a kong can
     * be claimed.
     *
     * @param disTile The recent discarded tile.
     *****************************************************************/
    private void kongSeq(final Suit disTile) {

        String message = "Claim Kong of tile "
                + disTile.getValue() + " "
                + disTile.getDesign() + "?";

        int takeKong = JOptionPane.showConfirmDialog(
                null, message, "Claim Kong",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE
                ,updatedImage(disTile));

        if (takeKong == JOptionPane.YES_OPTION) {

            game.takeKong(game.getPlayerList(0), game
                    .getRecentDiscard());
            displayBoard();
            game.setNextCurrentPlayer(0);
            drawFlag = false;
        }
    }

    /*******************************************************************
     * This method is for the sequence of actions the program will make
     * if the player draws into a kong.
     ******************************************************************/
    private void kongDrawSeq() {

        ArrayList<Tile> hand = game.getPlayerList(0)
                .getHandTile();
        Tile kongTile = hand.get(hand.size() - 1);

        String message = "Form a kong with the display tile using the"
                + "pong in the set pile?";

        int takeKong = JOptionPane.showConfirmDialog(
                null, message, "Claim Kong",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE
                ,updatedImage(kongTile));

        if (takeKong == JOptionPane.YES_OPTION) {

            game.takeKongDraw(game.getPlayerList(0));
            displayBoard();
            game.setNextCurrentPlayer(0);
            drawFlag = false;
        }
    }


    /*******************************************************************
     * This methods is the sequence of action when a mahjong can
     * be declared.
     ******************************************************************/
    private void mahjongSeq() {

        int winner = isMahjongPlayerDiscard();

        // Player can claim Mahjong
        if (winner == 0) {

            String message = "Do you wish to declare "
                    + "Mahjong and win?";

            String message2 = "Congratulations, you won!";
            int mahjong = JOptionPane.showConfirmDialog(
                    null,
                    message, "Claim Message",
                    JOptionPane.YES_NO_OPTION);

            if (mahjong == JOptionPane.YES_OPTION) {

                JOptionPane.showMessageDialog(
                        null, message2);

                enablePlayer1Hand(false);
                displayWinningHand(winner);
                timer.stop();
                drawFlag = false;
                resetBtn.setEnabled(true);

                if (winner != game.getStartingPlayer()) {

                    game.setNextStartingPlayer();
                }

                game.increaseScore(winner, true);
                gameModeSwap();
            }

        } else {

            String message = "An opponent has declared Mahjong."
                    + " Sorry, you lose.";
            JOptionPane.showMessageDialog(null, message);
            enablePlayer1Hand(false);
            displayWinningHand(winner);
            timer.stop();
            drawFlag = false;
            resetBtn.setEnabled(true);

            if (winner != game.getStartingPlayer()) {

                game.setNextStartingPlayer();
            }

            game.increaseScore(winner, true);
            gameModeSwap();
        }
    }

    /*******************************************************************
     * This method runs through a sequence of checks if a set can be
     * claimed or a mahjong can be declared.
     ******************************************************************/
    private void checkSeq() {

        displayBoard();

        // Check for if AI or human Player Mahjong off of discard
        if (isMahjongPlayerDiscard() != -1) {

            mahjongSeq();
        }


        // Checking for Kong upon discard
        for (int i = game.getCurrentPlayerIndex() + 1;
             i < (game.getCurrentPlayerIndex() + game.TOTALPLAYER)
                     ; i++) {

            if (game.isKong(game.getPlayerHand(i %
                    game.TOTALPLAYER), game.getRecentDiscard())
                    && drawFlag) {

                // Human Player Action
                if (i % game.TOTALPLAYER == 0) {
                    kongSeq((Suit) game.getRecentDiscard());

                } else {

                	game.takeKong(game.getPlayerList(
                	        i % game.TOTALPLAYER),
                            game.getRecentDiscard());
                    displayBoard();
                    game.setNextCurrentPlayer(i % game.TOTALPLAYER);
                    game.generalAIDiscard(game.getPlayerList(
                            i % game.TOTALPLAYER));
                    drawFlag = false;
                }
            }
        }

        // Check for pongs that can be claimed
        // to do: check for all players in order later
        if (drawFlag) {
            for (int i = game.getCurrentPlayerIndex() + 1;
                 i < (game.getCurrentPlayerIndex() + game.TOTALPLAYER);
                 i++) {

                if (game.isPong(game.getPlayerHand(0),
                        game.getRecentDiscard())) {

                    // Human Action
                    if (i % game.TOTALPLAYER == 0) {

                        pongSeq(game.getRecentDiscard());

                    } else {
                              game.takePong(game.getPlayerList(
                                      i % game.TOTALPLAYER),
                                      game.getRecentDiscard());
                              displayBoard();
                              game.setNextCurrentPlayer(
                                      i % game.TOTALPLAYER);
                              drawFlag = false;
                        game.generalAIDiscard(game.getPlayerList(
                                i % game.TOTALPLAYER));
                    }
                }
            }
        }

        // Check for any chi that can be claimed
        // drawFlag used to determine if user has already claimed pong
        int nextPlIndex = (game.getCurrentPlayerIndex() + 1) %
                game.TOTALPLAYER;

        if (drawFlag) {

            if (game.isChi(game.getPlayerList(nextPlIndex),
                     game.getRecentDiscard())) {

                // If Human Player
                if (nextPlIndex == 0) {

                    chiSeq((Suit) game.getRecentDiscard());
                }

                // Goes through and Claims a chi for the AI player
                else {
                        ArrayList<Integer> options = game.getChiTile(
                        		game.getPlayerList(nextPlIndex),
                                game.getRecentDiscard());

                        // Loop Through Options
                        while (options.size() > 0) {

                                game.takeChi(game.getPlayerList(
                                        nextPlIndex),
                                        options.get(0), options.get(1));
                                displayBoard();
                                game.setNextCurrentPlayer(nextPlIndex);
                                drawFlag = false;
                                break;
                    }
                }
            }
        }

        if (drawFlag) {

            game.setNextCurrentPlayer();
        }

        displayBoard();
    }

    /*******************************************************************
     * This method resets the game board in the board class.
     ******************************************************************/
    private void resetBoard() {

        // Removing all tiles.
        removeImage = false;
        removeTileButton();

        // Placing Image of new Game Board
        placeDrawPile();
        dealPlayerTiles();

        // disable human Player hand at start unless it's their turn
        if (game.getCurrentPlayerIndex() != 0) {

            enablePlayer1Hand(false);
        }

        // Reset Flags, Counters and Start Timer
        displayBoard();
        removeImage = true;
        drawFlag = true;
        pileNum = drawPile.size();
        game.setNextCurrentPlayer(game.getStartingPlayer());
        timer.start();
    }

    /*******************************************************************
     * This method removes all JButtons that are connected to tiles that
     * are on the board.
     ******************************************************************/
    private void removeTileButton() {

        // Remove draw and discard
        while (drawPile.size() > 0) {
            drawPilePanel.remove(drawPile.get(drawPile.size() - 1));
            drawPile.remove(drawPile.get(drawPile.size() - 1));
        }

        while (discardPile.size() > 0) {

            discardPilePanel.remove(discardPile.size() - 1);
            discardPile.remove(discardPile.size() - 1);
        }

        // Removing Player 1 Set and Hand Tiles
        while (p1Hand.size() > 0) {

            p1HandPanel.remove(p1Hand.size() - 1);
            p1Hand.remove(p1Hand.size() - 1);
        }

        while (p1Sets.size() > 0) {

            p1SetPanel.remove(p1Sets.size() - 1);
            p1Sets.remove(p1Sets.size() - 1);
        }

        while (p2Sets.size() > 0) {

            p2SetPanel.remove(p2Sets.size() - 1);
            p2Sets.remove(p2Sets.size() - 1);
        }

        while (p3Sets.size() > 0) {

            p3SetPanel.remove(p3Sets.size() - 1);
            p3Sets.remove(p3Sets.size() - 1);
        }

        while (p4Sets.size() > 0) {

            p4SetPanel.remove(p4Sets.size() - 1);
            p4Sets.remove(p4Sets.size() - 1);
        }
    }

    /*******************************************************************
     * This method updates the background color of the board.
     * @param red The shade of the red element.
     * @param green The shade of the green element.
     * @param blue The shade of the blue element.
     ******************************************************************/
    public void updateBgColor(final int red, final int green,
                              final int blue) {

        color = new Color(red, green, blue);
        updateBackground();
        updateRemovedTile();
    }

    /*******************************************************************
     * This method sets the difficulty of the AI.
     * @param difficulty Difficult of the AI.
     * @param playerIndex Which AI.
     ******************************************************************/
    public void setAIDiff(final int difficulty, final int playerIndex) {

        if (difficulty < Game.DUMB || difficulty > Game.ADVANCED) {

            throw new IllegalArgumentException("Difficulty " +
                    "setting not Excepted.");
        }

        if (playerIndex <= 0 || playerIndex > Game.TOTALPLAYER) {

            throw new IllegalArgumentException("Index of Player is" +
                    "not an AI");
        }

        game.setAIDiff(difficulty, playerIndex);
    }

    /*******************************************************************
     * This method updates the score of the player after a win.
     ******************************************************************/
    private void updateScore() {

        int p1score = game.getPlayerList(0).getPoint();
        int p2score = game.getPlayerList(1).getPoint();
        int p3score = game.getPlayerList(2).getPoint();
        int p4score = game.getPlayerList(3).getPoint();

        scoreBoard.setText("Player 1 Score: " + p1score + "     "
            + "Player 2 Score: " + p2score + "     "
            + "Player 3 Score: " + p3score + "     "
            + "Player 4 Score: " + p4score);
    }

    /*******************************************************************
     * This method displays the winning player index in the discard
     * pile.
     * @param plIndex The winning player index.
     ******************************************************************/
    private void displayWinningHand(final int plIndex) {

        int index = 0;
        updateDiscardPile(0);
        game.showWinningHand(plIndex);

        while (discardPile.size() < game.getDiscardPile().size()) {

            JButton temp = new JButton();
            temp.setPreferredSize(new Dimension(30, 35));
            discardPile.add(temp);
            discardPile.get(discardPile.size() - 1).setIcon(
                    updatedImage(game.getDiscardPile().get(index)));
            discardPilePanel.add(temp);

            index++;
        }
    }

    /*******************************************************************
     * A class within Board that handles action listeners of buttons.
     ******************************************************************/
    private class Listener implements ActionListener {

        public void actionPerformed(final ActionEvent event) {

            int p1HandSize = game.getPlayerList(0)
                    .getHandTile()
                    .size();

            // when Tile is selected for discard, only p1 can do this
            for (int i = 0; i < p1HandSize; i++) {

                // find out which Tile they selected
                if (p1Hand.get(i) == event.getSource()) {

                    // are you sure you want to discard?
                    int discard = JOptionPane.showConfirmDialog(
                            null,
                            new JLabel("Discard tile?",
                                    updatedImage(game
                                            .getPlayerList(0)
                                            .getTileFromHand(i)),
                                    SwingConstants.LEFT),
                            "Discard",
                            JOptionPane.YES_NO_OPTION);

                    if (discard == JOptionPane.YES_OPTION) {

                        game.discard(game.getPlayerList(
                                0), i);

                        JButton temp = new JButton(null,
                                p1Hand.get(i).getIcon());
                        temp.setPreferredSize(new Dimension(
                                30, 35));
                        p1HandPanel.remove(p1Hand.size() - 1);
                        p1Hand.remove(p1Hand.size() - 1);
                        discardPile.add(temp);
                        discardPilePanel.add(discardPile.get(
                                discardPile.size() - 1));

                        game.setNextCurrentPlayer();
                        drawFlag = true;
                        displayBoard();
                        break;

                    } else {

                        break;
                    }
                }
            }

            if (event.getSource() == resetBtn) {

                game.reset();
                resetBoard();
                resetBtn.setEnabled(false);
                updateScore();
            }
        }
    }

}


