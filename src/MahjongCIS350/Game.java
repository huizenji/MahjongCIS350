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

    private ArrayList<Tile> tiles;

    /******************************************************************
     * This is the constructor for the Game class.
     *****************************************************************/
    public Game(){

        tiles = new ArrayList<>();

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
}
