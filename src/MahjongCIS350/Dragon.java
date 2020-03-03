package MahjongCIS350;

/***********************************************************************
 * This class contains the information on the Dragon Tile in Mahjong.
 *
 * @Authors: Wayne Chen, Jillian Huizenga, Chris Paul, Xianghe Zhao
 * @Version: 2/28/2020
 **********************************************************************/
public class Dragon extends Tile {

    /** Color of the Dragon Tile. **/
    private String color;

    /*******************************************************************`
     * This ia a constructor that creates for the Dragon class. This
     * only ask for the color of the Dragon.
     *
     * @param color The color of the Dragon tile.
     ******************************************************************/
    public Dragon(final String color) {

        super("Dragon");
        this.color = color;
    }

    /*******************************************************************
     * This is the getter method of the Dragon Tile.
     *
     * @return the color of the Dragon Tile.
     ******************************************************************/
    public String getColor() {

        return color;
    }

    /*******************************************************************
     * This method sets the color of the Dragon Tile.
     *
     * @param color color of the Dragon Tile.
     ******************************************************************/
    public void setColor(final String color) {

        this.color = color;
    }
}
