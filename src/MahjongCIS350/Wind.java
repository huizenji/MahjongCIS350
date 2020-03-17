package MahjongCIS350;

/***********************************************************************
 * This is the class for any wind tile in the game of mahjong.
 *
 * @Authors: Jillian Huizenga, Wayne Chen, Aron Zhao
 * @Version: 1/21/2020
 **********************************************************************/
public class Wind extends Tile{

    /** The direction of the wind tile. **/
    private String direction;

    /*******************************************************************
     * Constructor to create the wind tile based on direction.
     *
     * @param direction Direction of the wind tile.
     ******************************************************************/
    public Wind(String direction) {
        super("Wind");
        this.direction = direction;
    }

    /*******************************************************************
     * This method gets the direction of the wind tile.
     *
     * @return The direction of the wind tile.
     ******************************************************************/
    public String getDirection() {
        return direction;
    }

    /*******************************************************************
     * This method sets the direction of the wind tile.
     * @param direction The direction of the wind tile.
     ******************************************************************/
    public void setDirection(String direction) {
        this.direction = direction;
    }
}
