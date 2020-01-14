package MahjongCIS350;

/***********************************************************************
 * This class represent each individual tile in the game of Mahjong.
 *
 * @Author: Wayne Chen
 * @Versoin: 1/14/2020
 **********************************************************************/
public class Tile {

    /** Type of tile **/
    private String type;

    /*******************************************************************
     * This is the constructor for the Tile class.
     *
     * @param type What type of tile is represented
     ******************************************************************/
    public Tile(String type) {
        this.type = type;
    }

    /*******************************************************************
     * This method gets the type of tile.
     *
     * @return What type of mahjong tile it is.
     ******************************************************************/
    public String getType() {
        return type;
    }

    /*******************************************************************
     * This method sets the type of mahjong tile.
     *
     * @param type What type of mahjong tile it represents.
     ******************************************************************/
    public void setType(String type) {
        this.type = type;
    }
}
