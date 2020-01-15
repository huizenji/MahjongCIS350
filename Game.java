/**********************************************************************
 * This class contains all pieces and rule functionality for the game
 * Mahjong. It is displayed by the Panel and GUI classes.
 *
 * @Authors: Jillian Huizenga,
 * @Version: 1/15/2020
 *********************************************************************/
public class Game {

    private Tile[] tiles;

    /******************************************************************
     * This is the constructor for the Game class.
     *****************************************************************/
    public Game(){
        tiles = new Tile[144];

        // create all 144 Tiles
        for (int numTiles = 0; numTiles < 144; numTiles++){

            // create Circle suit (four 1's, four 2's, and so on)
            if (numTiles < 36) {
                if (numTiles < 4) {
                    tiles[numTiles] = new Tile("Circle", 1);
                } else if (numTiles < 8) {
                    tiles[numTiles] = new Tile("Circle", 2);
                } else if (numTiles < 12) {
                    tiles[numTiles] = new Tile("Circle", 3);
                } else if (numTiles < 16) {
                    tiles[numTiles] = new Tile("Circle", 4);
                } else if (numTiles < 20) {
                    tiles[numTiles] = new Tile("Circle", 5);
                } else if (numTiles < 24) {
                    tiles[numTiles] = new Tile("Circle", 6);
                } else if (numTiles < 28) {
                    tiles[numTiles] = new Tile("Circle", 7);
                } else if (numTiles < 32) {
                    tiles[numTiles] = new Tile("Circle", 8);
                } else {
                    tiles[numTiles] = new Tile("Circle", 9);
                }

            // create Character suit (four 1's, four 2's, and so on)
            } else if (numTiles < 72) {
                if (numTiles < 40) {
                    tiles[numTiles] = new Tile("Character", 1);
                } else if (numTiles < 44) {
                    tiles[numTiles] = new Tile("Character", 2);
                } else if (numTiles < 48) {
                    tiles[numTiles] = new Tile("Character", 3);
                } else if (numTiles < 52) {
                    tiles[numTiles] = new Tile("Character", 4);
                } else if (numTiles < 56) {
                    tiles[numTiles] = new Tile("Character", 5);
                } else if (numTiles < 60) {
                    tiles[numTiles] = new Tile("Character", 6);
                } else if (numTiles < 64) {
                    tiles[numTiles] = new Tile("Character", 7);
                } else if (numTiles < 68) {
                    tiles[numTiles] = new Tile("Character", 8);
                } else {
                    tiles[numTiles] = new Tile("Character", 9);
                }

            // create Bamboo suit (four 1's, four 2's, and so on)
            } else if (numTiles < 108) {
                if (numTiles < 76) {
                    tiles[numTiles] = new Tile("Bamboo", 1);
                } else if (numTiles < 80) {
                    tiles[numTiles] = new Tile("Bamboo", 2);
                } else if (numTiles < 84) {
                    tiles[numTiles] = new Tile("Bamboo", 3);
                } else if (numTiles < 88) {
                    tiles[numTiles] = new Tile("Bamboo", 4);
                } else if (numTiles < 92) {
                    tiles[numTiles] = new Tile("Bamboo", 5);
                } else if (numTiles < 96) {
                    tiles[numTiles] = new Tile("Bamboo", 6);
                } else if (numTiles < 100) {
                    tiles[numTiles] = new Tile("Bamboo", 7);
                } else if (numTiles < 104) {
                    tiles[numTiles] = new Tile("Bamboo", 8);
                } else {
                    tiles[numTiles] = new Tile("Bamboo", 9);
                }

            // create Point tiles (12 Dragons, 16 Winds, and 8 Flowers)
            } else if (numTiles < 144){
                if (numTiles < 112) {
                    tiles[numTiles] = new Tile("Red Dragon", 0);
                } else if (numTiles < 116) {
                    tiles[numTiles] = new Tile("Green Dragon", 0);
                } else if (numTiles < 120) {
                    tiles[numTiles] = new Tile("White Dragon", 0);
                } else if (numTiles < 124) {
                    tiles[numTiles] = new Tile("East Wind", 0);
                } else if (numTiles < 128) {
                    tiles[numTiles] = new Tile("North Wind", 0);
                } else if (numTiles < 132) {
                    tiles[numTiles] = new Tile("West Wind", 0);
                } else if (numTiles < 136) {
                    tiles[numTiles] = new Tile("South Wind", 0);
                } else {
                    tiles[numTiles] = new Tile("Flower", 0);
                }
            }

            // not yet tested


            // may have to adjust class location and/or creation
            // method depending on how you plan to create a
            // Player "hand" inside the Player class, Aron

        }

    }

}
