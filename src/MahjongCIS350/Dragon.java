package MahjongCIS350;

/***********************************************************************
 * This class contains the information on the dragon tile in mahjong.
 *
 * @Authors: Jillian Huizenga, Wayne Chen, Aron Zhao
 * @Version: 1/21/2020
 **********************************************************************/
public class Dragon extends Tile{

    /** Color of the Dragon Tile **/
    private String color;

    /*******************************************************************
     * This ia a constructor that creates for the Dragon class. This
     * only ask for the color of the Dragon.
     *
     * @param color The color of the Dragon tile.
     ******************************************************************/
    public Dragon(String color) {
        super("Dragon");
        this.color = color;
    }

    /*******************************************************************
     * This is the getter method of the dragon tile.
     *
     * @return Returns the color of the dragon tile.
     ******************************************************************/
    public String getColor() {
        return color;
    }

    /*******************************************************************
     * This method sets the color of the dragon tile.
     *
     * @param color Color of the dragon tile.
     ******************************************************************/
    public void setColor(String color) {
        this.color = color;
    }
}
