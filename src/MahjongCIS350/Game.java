package MahjongCIS350;

import java.util.*;

/**********************************************************************
 * This class contains all pieces and rule functionality for the game
 * Mahjong. It is displayed by the Panel and GUI classes.
 *
 * @Authors: Jillian Huizenga, Wayne Chen
 * @Version: 1/15/2020
 *********************************************************************/
public class Game {

    /** All the tiles in Mahjong **/
    private ArrayList<Tile> tiles;

    /** The max amount of tiles in Mahjong **/
    private int maxTile;

    /** The current player ,aka, whose turn **/
    private int currentPlayer; // Change to Player class

    private int startingPlayer; // Change to Player class


    public static void main(String[] args){

        Game test = new Game();
    }

    /******************************************************************
     * This is the constructor for the Game class.
     *****************************************************************/
    public Game(){

        tiles = new ArrayList<>();
        maxTile = 144;
        createTile();
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

    private void assignStartPlayer(){

        Random rand = new Random();
    }

    /** Add to Later Whoever has time **/
    private void isChi(){

    }

    private void isPong(){

    }

    private void isKong(){

    }

    private void mahjongWin(){

    }
}
