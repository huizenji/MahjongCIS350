package MahjongCIS350;

/***********************************************************************
 * This class represents each individual tile in the game of Mahjong.
 *
 * @Author: Wayne Chen, Jillian Huizenga, Chris Paul, Aron Zhao
 * @Versoin: 2/28/2020
 **********************************************************************/
public class Tile {

    /** Type of tile **/
    private String type;

    /*******************************************************************
     * This is the constructor for the Tile class.
     *
     * @param type What type of tile is represented.
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
}
