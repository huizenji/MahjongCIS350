package mahjongCIS350;

import java.util.ArrayList;

/***********************************************************************
 * This class represents each individual player. Each player will
 * have a hand and a setpile where sets are moved to when claimed.
 *
 * @Authors: Wayne Chen, Jillian Huizenga, Chris Paul, Aron Zhao
 * @Version: 2/28/2020
 **********************************************************************/
public class Player {

    /** Player tiles that is in his or her hand. **/
    private ArrayList<Tile> handTile;

    /** Player tiles that is in his or her hand. **/
    private ArrayList<Tile> setPile;

    /** The direction of the Player. **/
    private String direction;

    /** The total number of points for the player. **/
    private int point;

    /*******************************************************************
     * This is a constructor that instantiates the hand, setpile , and
     * the direction of the player as well as set the point total to 0.
     ******************************************************************/
    public Player() {

        handTile = new ArrayList<>();
        this.setPile = new ArrayList<>();
        this.direction = null;
        point = 0;
    }

    /*******************************************************************
     * This is a constructor that instantiates the hand, setpile , but
     * sets direction of the player based on the parameter direction,
     *  as well as set the point total to 0.
     *
     * @param direction The direction of the player.
     ******************************************************************/
    public Player(final String direction) {
        this.handTile = new ArrayList<>();
        this.setPile = new ArrayList<>();
        this.direction = direction;
        point = 0;
    }

    /*******************************************************************
     * This method clears the hand of all tiles.
     ******************************************************************/
    public void clearHandPile() {

        handTile.clear();
    }

    /*******************************************************************
     * This method clears the setpile of all tiles.
     ******************************************************************/
    public void clearSetPile() {

        setPile.clear();
    }

    /*******************************************************************
     * This method adds the Tile t to the end of the handTile
     *
     * @param tile The tile that is being added to the player's hand.
     ******************************************************************/
    public void addTile(final Tile tile) {

        if (tile == null) {
            throw new NullPointerException("No such type of tile, "
                    + "can't be added");
        }
        handTile.add(tile);
    }

    /*******************************************************************
     * This method adds the Tile t to the end of the setPile
     *
     * @param tile The tile that is being added to the player's setPile.
     ******************************************************************/
    public void addTileSet(final Tile tile) {

        if (tile == null) {

            throw new NullPointerException("No such type of tile," +
                    " can't be added");
        }
        setPile.add(tile);
    }

    /*******************************************************************
     * This method remove a tile from the hand and moves it to the
     * set pile.
     *
     * @param index The index of which the tile is located at.
     ******************************************************************/
    public void removeTileSet(final int index){

        if (index < 0 || index >= handTile.size()){

            throw new IllegalArgumentException("Invalid position: " +
                    index);
        }

        setPile.add(handTile.remove(index));
    }

    /*******************************************************************
     * This method gets a players hand consisting of tiles.
     *
     * @return The players hand.
     ******************************************************************/
    public ArrayList<Tile> getHandTile() {
        return handTile;
    }

    /*******************************************************************
     * This method gets a specific tile based on index from a player's
     * hand.
     *
     * @param index The index where the tile is located at
     * @return The tile of located at the specific index.
     ******************************************************************/
    public Tile getTileFromHand(final int index){

        return handTile.get(index);
    }

    /*******************************************************************
     * This method set a players hand.
     *
     * @param handTile The hand that is given to the player.
     ******************************************************************/
    public void setHandTile(final ArrayList<Tile> handTile) {
        this.handTile = handTile;
    }

    /*******************************************************************
     * This method gets the direction of the player.
     *
     * @return The direction of the player.
     ******************************************************************/
    public String getDirection() {
        return direction;
    }

    /*******************************************************************
     * This method sets the direction of the player.
     *
     * @param direction The direction of the player.
     ******************************************************************/
    public void setDirection(final String direction) {
        this.direction = direction;
    }

    /*******************************************************************
     * This method gets a players entire set pile.
     *
     * @return An arraylist containing the tiels in the players setpile.
     ******************************************************************/
    public ArrayList<Tile> getSetPile(){return setPile;}

    /*******************************************************************
     * This method gets a tile in the player's set pile at a specific
     * index.
     *
     * @param index The index of the tile.
     * @return The tile in the set pile at the specfic index.
     ******************************************************************/
    public Tile getSetTile(final int index){
        return setPile.get(index);
    }

    /*******************************************************************
     * This method sets the number of points for the player.
     *
     * @param point The number of points that player will have.
     ******************************************************************/
    public void setPoint(final int point) {
        this.point = point;
    }

    /*******************************************************************
     * This method gets the number of points the player has.
     *
     * @return The number of points the player has.
     ******************************************************************/
    public int getPoint() {
        return point;
    }
}
