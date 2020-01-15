/**********************************************************************
 * This class contains all pieces and rule functionality for the game
 * Mahjong. It is displayed by the Panel and GUI classes.
 *
 * @Authors: Jillian Huizenga,
 * @Versoin: 1/15/2020
 *********************************************************************/
public class Game {

    private Tile[] tiles;

    /******************************************************************
     * This is the constructor for the Game class.
     *****************************************************************/
    public Game(){
        tiles = new Tile[144];
        int tileVal = 1;

        // create all 144 Tiles
        for (int numTiles = 0; numTiles < 144; numTiles++){

            // handle Tile values (should loop 1-9)
            if (tileVal == 10)
                tileVal = 1;

            // create Circle suit (four 1's, four 2's, and so on)
            if (numTiles < 36) {
                if (numTiles < 4) {
                    tiles[numTiles] = new Tile("Circle", tileVal);
                } else if (numTiles < 8) {
                    tileVal++;
                    tiles[numTiles] = new Tile("Circle", tileVal);
                } else if (numTiles < 12) {
                    tileVal++;
                    tiles[numTiles] = new Tile("Circle", tileVal);
                } else if (numTiles < 16) {
                    tileVal++;
                    tiles[numTiles] = new Tile("Circle", tileVal);
                } else if (numTiles < 20) {
                    tileVal++;
                    tiles[numTiles] = new Tile("Circle", tileVal);
                } else if (numTiles < 24) {
                    tileVal++;
                    tiles[numTiles] = new Tile("Circle", tileVal);
                } else if (numTiles < 28) {
                    tileVal++;
                    tiles[numTiles] = new Tile("Circle", tileVal);
                } else if (numTiles < 32) {
                    tileVal++;
                    tiles[numTiles] = new Tile("Circle", tileVal);
                } else {
                    tileVal++;
                    tiles[numTiles] = new Tile("Circle", tileVal);
                }

            // create Character suit (four 1's, four 2's, and so on)
            } else if (numTiles < 72) {
                if (numTiles < 40) {
                    tiles[numTiles] = new Tile("Character", tileVal);
                } else if (numTiles < 44) {
                    tileVal++;
                    tiles[numTiles] = new Tile("Character", tileVal);
                } else if (numTiles < 48) {
                    tileVal++;
                    tiles[numTiles] = new Tile("Character", tileVal);
                } else if (numTiles < 52) {
                    tileVal++;
                    tiles[numTiles] = new Tile("Character", tileVal);
                } else if (numTiles < 56) {
                    tileVal++;
                    tiles[numTiles] = new Tile("Character", tileVal);
                } else if (numTiles < 60) {
                    tileVal++;
                    tiles[numTiles] = new Tile("Character", tileVal);
                } else if (numTiles < 64) {
                    tileVal++;
                    tiles[numTiles] = new Tile("Character", tileVal);
                } else if (numTiles < 68) {
                    tileVal++;
                    tiles[numTiles] = new Tile("Character", tileVal);
                } else {
                    tileVal++;
                    tiles[numTiles] = new Tile("Character", tileVal);
                }

            // create Bamboo suit (four 1's, four 2's, and so on)
            } else if (numTiles < 108) {
                if (numTiles < 76) {
                    tiles[numTiles] = new Tile("Bamboo", tileVal);
                } else if (numTiles < 80) {
                    tileVal++;
                    tiles[numTiles] = new Tile("Bamboo", tileVal);
                } else if (numTiles < 84) {
                    tileVal++;
                    tiles[numTiles] = new Tile("Bamboo", tileVal);
                } else if (numTiles < 88) {
                    tileVal++;
                    tiles[numTiles] = new Tile("Bamboo", tileVal);
                } else if (numTiles < 92) {
                    tileVal++;
                    tiles[numTiles] = new Tile("Bamboo", tileVal);
                } else if (numTiles < 96) {
                    tileVal++;
                    tiles[numTiles] = new Tile("Bamboo", tileVal);
                } else if (numTiles < 100) {
                    tileVal++;
                    tiles[numTiles] = new Tile("Bamboo", tileVal);
                } else if (numTiles < 104) {
                    tileVal++;
                    tiles[numTiles] = new Tile("Bamboo", tileVal);
                } else {
                    tileVal++;
                    tiles[numTiles] = new Tile("Bamboo", tileVal);
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
