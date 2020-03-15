package MahjongCIS350;

/***********************************************************************
 * This class contains the information on the Dragon Tile in Mahjong.
 *
 * @Authors: Wayne Chen, Jillian Huizenga, Chris Paul, Xianghe Zhao
 * @Version: 2/28/2020
 **********************************************************************/
public class Flower extends Tile {

    /** The number of a Flower Tile to determine Image. **/
    private int number;

    /*******************************************************************
     * This is the constructor for the Tile class.
     ******************************************************************/
    public Flower(final int num) {

        super("Flower");
        number = num;
    }

    /*******************************************************************
     * A method that returns the Flower Tile's number.
     *
     * @return number of Flower Tile.
     ******************************************************************/
    public int getNumber() {

        return number;
    }
}
