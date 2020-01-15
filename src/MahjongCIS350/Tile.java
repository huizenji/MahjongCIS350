package MahjongCIS350;

/**********************************************************************
 * This class represent each individual tile in the game of Mahjong.
 *
 * @Author: Wayne Chen
 * @Versoin: 1/14/2020
 *********************************************************************/
public class Tile {

    /** Type of tile **/
    private String type;
    
    /** Value of tile **/
    private int value;

    /******************************************************************
     * This is the constructor for the Tile class.
     *
     * @param type What type of tile is represented
     * @param value The numerical value of the tile           
     *****************************************************************/
    public Tile(String type, int value) {
        this.type = type;
        this.value = value;
    }

    /******************************************************************
     * This method gets the type of tile.
     *
     * @return What type of mahjong tile it is.
     *****************************************************************/
    public String getType() {
        return type;
    }

    /******************************************************************
     * This method sets the type of mahjong tile.
     *
     * @param type What type of mahjong tile it represents.
     *****************************************************************/
    public void setType(String type) {
        this.type = type;
    }


    /******************************************************************
     * This method gets the value of the tile.
     *
     * @return The value of the mahjong tile.
     *****************************************************************/
    public int getValue() {
        return value;
    }

    /******************************************************************
     * This method sets the type of mahjong tile.
     *
     * @param value The value of the mahjong tile it represents.
     *****************************************************************/
    public void setValue(int value) {
        this.value = value;
    }
}
