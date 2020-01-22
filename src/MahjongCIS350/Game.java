package MahjongCIS350;

import java.util.*;

/**********************************************************************
 * This class contains all pieces and rule functionality for the game
 * Mahjong. It is displayed by the Board and GUI classes.
 *
 * @Authors: Jillian Huizenga, Wayne Chen
 * @Version: 1/21/2020
 *********************************************************************/
public class Game {

    /** All the tiles in Mahjong **/
    private ArrayList<Tile> tiles;

    /** The max amount of tiles in Mahjong **/
    private int maxTile;

    /** Index of current player ,aka, whose turn **/
    private int currentPlayer;

    /** Index of player who has the "East" direction **/
    private int startingPlayer;

    Player []playerList;

    /** Total number of players per game **/
    private int TOTAL_PLAYER = 4;

    public static void main(String[] args){

        Suite a = new Suite("a",2);
        Suite b = new Suite("a",2);

        if (a.equals(b)){

            System.out.println("Objects are Equal");
        }

        else{
            System.out.println("No");
        }
    }

    /******************************************************************
     * This is the constructor for the Game class.
     *****************************************************************/
    public Game(){

        tiles = new ArrayList<>();
        maxTile = 144;
        createTile();
        setupPlayer();
        shuffle();
        dealTile();
    }

    /*******************************************************************
     * This method creates every tile in Mahjong.
     ******************************************************************/
    private void createTile(){

        createSuiteTile();
        createPointTile();
    }

    /*******************************************************************
     * This method creates all the suites tiles in Mahjong.
     ******************************************************************/
    private void createSuiteTile(){

        String []design = {"Circle","Character","Bamboo"};

        for (int index = 0; index < 3; index++) {
            for (int numtile = 1; numtile <= 9; numtile++) {
                for (int i = 0; i < 4; i++) {

                    tiles.add(new Suite(design[index], numtile));
                }
            }
        }
    }

    /*******************************************************************
     * This method creates all the point tiles.
     ******************************************************************/
    private void createPointTile(){

        String []dragonColor = {"Red","Green","White"};

        String []windDir = {"North","East","South","West"};

        // creating Dragon Tiles
        for (int index = 0; index < 3; index++){
            for (int i = 0; i < 4; i++){

                tiles.add(new Dragon(dragonColor[index]));
            }
        }

        // Creating Wind Tiles
        for (int index = 4; index < 4; index++){
            for (int i = 0; i < 4; i++){

                tiles.add(new Wind(windDir[index]));
            }
        }

        // Creating Flower tiles
        for (int i = 0; i < 8; i++){

            tiles.add(new Tile("Flower"));
        }
    }

    /*******************************************************************
     * This method creates all the players in the game and randomly
     * assigns one of the player to be the starting player.
     ******************************************************************/
    private void setupPlayer(){

        playerList = new Player[TOTAL_PLAYER];

        // Creating 4 players
        playerList[0] = new Player("East");
        playerList[1] = new Player("South");
        playerList[2] = new Player("West");
        playerList[3] = new Player("North");

        Random rand = new Random();
        int randVal = rand.nextInt(4) + 1;

        // Rotate the winds of the player to randomly set a starting
        // player
        for (int i = 0; i < randVal; i++){
            for (int j = 0; j < TOTAL_PLAYER; j++){

                rotatePlayer(playerList[j]);
            }
        }

        // Find the starting player index
        startingPlayer = randVal % TOTAL_PLAYER;
        currentPlayer = startingPlayer;
    }

    /*******************************************************************
     * This method rotates the wind direction of each player.
     ******************************************************************/
    private void rotatePlayer(Player player){

        if (player.getDirection().equals("East")){

            player.setDirection("South");
        }

        else if (player.getDirection().equals("South")){

            player.setDirection("West");
        }

        else if (player.getDirection().equals("West")){

            player.setDirection("North");
        }

        else if (player.getDirection().equals("North")){

            player.setDirection("East");
        }
    }

    /*******************************************************************
     * This method shuffles all the tiles that are not in the players
     * hands.
     ******************************************************************/
    private void shuffle(){

        Random rand = new Random();
        int randNum1;
        int randNum2;

        for (int i = 0; i < 100000; i++){

            randNum1 = rand.nextInt(maxTile);
            randNum2 = rand.nextInt(maxTile);
            swapTile(tiles.get(randNum1),tiles.get(randNum2));
        }
    }

    /*******************************************************************
     * This method swap the position of 2 tiles.
     *
     * @param a The first Tile.
     * @param b The second Tile.
     ******************************************************************/
    private void swapTile(Tile a, Tile b){

        Tile temp = a;
        a = b;
        b = temp;
    }

    /*******************************************************************
     * This method deals out 14 suite tiles to all players
     ******************************************************************/
    private void dealTile(){

        for (int i = 0; i < 4 * 14; i++) {
            playerList[i].addTile(tiles.remove(0));
        }

        // Giving starting player 1 extra tile
        playerList[startingPlayer].addTile(tiles.remove(0));

        /** Finish later with discussion about point tiles **/
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getStartingPlayer() {
        return startingPlayer;
    }

    public void setStartingPlayer(int startingPlayer) {
        this.startingPlayer = startingPlayer;
    }

    /** Add to Later Whoever has time **/
    private void isChi(){

    }

    /*******************************************************************
     * This method checks if there is a pong for a specific player.
     *
     * @param handTile The hand of the player that is being checked
     *                 for a pong.
     * @param discard The tile that was last discarded.
     ******************************************************************/
    private boolean isPong(ArrayList<Suite> handTile, Suite discard){

        // Number of Tiles in player hand that can used for a pong
        int matchTile = 0;

        // Scan through the hand and determine if there is a matching
        // tile
        for(int i = 0; i < handTile.size(); i++){

            if (compareSuite(handTile.get(i),discard)){

                matchTile++;
            }
        }

        // If there is 2 or more matching tile, then there is a pong
        if (matchTile >= 2){

            return true;
        }

        else{

            return false;
        }
    }

    private void isKong(){

    }

    private void mahjongWin(){

    }

    /*******************************************************************
     * This method compares 2 suite tiles and determines if they are
     * the same tile. Only works with Suites
     *
     * @param tile1 The first tile that is being compared.
     * @param tile2 The second tile that is being compared.
     * @return true if both tiles are the same.
     ******************************************************************/
    private boolean compareSuite(Suite tile1, Suite tile2){

        if (tile1 == null){

            throw new NullPointerException("Argument 1 - tile1 has a" +
                    "null object.\n");
        }

        if (tile2 == null){

            throw new NullPointerException("Argument 2 - tile1 has a" +
                    "null object.\n");
        }

        if (tile1.getDesign().equals(tile2.getDesign()) &&
                tile1.getValue() == tile2.getValue()){

            return true;
        }

        return false;
    }
}
