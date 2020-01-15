import java.util.*;

/**********************************************************************
 * This class contains all pieces and rule functionality for the game
 * Mahjong. It is displayed by the Panel and GUI classes.
 *
 * @Authors: Jillian Huizenga,
 * @Version: 1/15/2020
 *********************************************************************/
public class Game {

    private ArrayList<Tile> tiles;

    /******************************************************************
     * This is the constructor for the Game class.
     *****************************************************************/
    public Game(){
        tiles = new ArrayList<>();

        // create all 144 Tiles
        for (int numTiles = 0; numTiles < 144; numTiles++){

            // create Circle suit (four 1's, four 2's, and so on)
            if (numTiles < 36) {
                if (numTiles < 4) {
                    tiles.add(new Tile("Circle", 1));
                } else if (numTiles < 8) {
                    tiles.add(new Tile("Circle", 2));
                } else if (numTiles < 12) {
                    tiles.add(new Tile("Circle", 3));
                } else if (numTiles < 16) {
                    tiles.add(new Tile("Circle", 4));
                } else if (numTiles < 20) {
                    tiles.add(new Tile("Circle", 5));
                } else if (numTiles < 24) {
                    tiles.add(new Tile("Circle", 6));
                } else if (numTiles < 28) {
                    tiles.add(new Tile("Circle", 7));
                } else if (numTiles < 32) {
                    tiles.add(new Tile("Circle", 8));
                } else {
                    tiles.add(new Tile("Circle", 9));
                }

            // create Character suit (four 1's, four 2's, and so on)
            } else if (numTiles < 72) {
                if (numTiles < 40) {
                    tiles.add(new Tile("Character", 1));
                } else if (numTiles < 44) {
                    tiles.add(new Tile("Character", 2));
                } else if (numTiles < 48) {
                    tiles.add(new Tile("Character", 3));
                } else if (numTiles < 52) {
                    tiles.add(new Tile("Character", 4));
                } else if (numTiles < 56) {
                    tiles.add(new Tile("Character", 5));
                } else if (numTiles < 60) {
                    tiles.add(new Tile("Character", 6));
                } else if (numTiles < 64) {
                    tiles.add(new Tile("Character", 7));
                } else if (numTiles < 68) {
                    tiles.add(new Tile("Character", 8));
                } else {
                    tiles.add(new Tile("Character", 9));
                }

            // create Bamboo suit (four 1's, four 2's, and so on)
            } else if (numTiles < 108) {
                if (numTiles < 76) {
                    tiles.add(new Tile("Bamboo", 1));
                } else if (numTiles < 80) {
                    tiles.add(new Tile("Bamboo", 2));
                } else if (numTiles < 84) {
                    tiles.add(new Tile("Bamboo", 3));
                } else if (numTiles < 88) {
                    tiles.add(new Tile("Bamboo", 4));
                } else if (numTiles < 92) {
                    tiles.add(new Tile("Bamboo", 5));
                } else if (numTiles < 96) {
                    tiles.add(new Tile("Bamboo", 6));
                } else if (numTiles < 100) {
                    tiles.add(new Tile("Bamboo", 7));
                } else if (numTiles < 104) {
                    tiles.add(new Tile("Bamboo", 8));
                } else {
                    tiles.add(new Tile("Bamboo", 9));
                }

            // create Point tiles (12 Dragons, 16 Winds, and 8 Flowers)
            } else if (numTiles < 144){
                if (numTiles < 112) {
                    tiles.add(new Tile("Red Dragon", 0));
                } else if (numTiles < 116) {
                    tiles.add(new Tile("Green Dragon", 0));
                } else if (numTiles < 120) {
                    tiles.add(new Tile("White Dragon", 0));
                } else if (numTiles < 124) {
                    tiles.add(new Tile("East Wind", 0));
                } else if (numTiles < 128) {
                    tiles.add(new Tile("South Wind", 0));
                } else if (numTiles < 132) {
                    tiles.add(new Tile("West Wind", 0));
                } else if (numTiles < 136) {
                    tiles.add(new Tile("North Wind", 0));
                    
                // create 8 separate Flower Tiles (different images)    
                } else if (numTiles == 136){
                    tiles.add(new Tile("Flower", 0));
                } else if (numTiles == 137){
                    tiles.add(new Tile("Flower", 1));
                } else if (numTiles == 138){
                    tiles.add(new Tile("Flower", 2));
                } else if (numTiles == 139){
                    tiles.add(new Tile("Flower", 3));
                } else if (numTiles == 140){
                    tiles.add(new Tile("Flower", 4));
                } else if (numTiles == 141){
                    tiles.add(new Tile("Flower", 5));
                } else if (numTiles == 142){
                    tiles.add(new Tile("Flower", 6));
                } else {
                    tiles.add(new Tile("Flower", 7));
                }
            }

            // not yet tested

            // ArrayList so that tiles can be moved between ArrayLists (add then delete)

        }

    }

}
