package mahjongCIS350;

/***********************************************************************
 * This is the class for any Wind Tile in the game of Mahjong.
 *
 * @Authors: Wayne Chen, Jillian Huizenga, Chris Paul, Aron Zhao
 * @Version: 3/27/2020
 **********************************************************************/
public class Wind extends Tile {

    /** The direction of the Wind Tile **/
    private String direction;

    /*******************************************************************
     * This is an empty constructor that sets the direction of the
     * Wind tile to null;
     ******************************************************************/
    public Wind() {
        super("Wind");
        direction = null;
    }

    /*******************************************************************
     * Constructor to create the Wind Tile based on direction.
     *
     * @param direction Direction of the wind tile.
     ******************************************************************/
    public Wind(final String direction) {

        super("Wind");
        this.direction = direction;
    }

    /*******************************************************************
     * This method gets the direction of the Wind Tile.
     *
     * @return The direction of the Wind Tile.
     ******************************************8***********************/
    public String getDirection() {

        return direction;
    }

    /*******************************************************************
     * This method sets the direction of the wind tile.
     * @param direction Direction of the wind tile.
     ******************************************************************/
    public void setDirection(final String direction) {

        this.direction = direction;
    }
}
