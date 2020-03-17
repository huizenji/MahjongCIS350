package mahjongCIS350;

/***********************************************************************
 * This is the class for any Wind Tile in the game of Mahjong.
 *
 * @Authors: Wayne Chen, Jillian Huizenga, Chris Paul, Aron Zhao
 * @Version: 2/28/2020
 **********************************************************************/
public class Wind extends Tile{

    /** The direction of the Wind Tile **/
    private String direction;

    /******************************************************************
     * Constructor to create the Wind Tile based on direction.
     *
     * @param direction Direction of the wind tile.
     *****************************************************************/
    public Wind(String direction) {

        super("Wind");
        this.direction = direction;
    }

    /******************************************************************
     * This method gets the direction of the Wind Tile.
     *
     * @return The direction of the Wind Tile.
     *****************************************************************/
    public String getDirection() {

        return direction;
    }
}
