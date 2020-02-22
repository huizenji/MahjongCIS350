package MahjongCIS350;

/***********************************************************************
 * This is the class for any Suit tiles in mahjong. This class covers
 * all the Suit tiles, bamboo, circle, character.
 *
 * @Authors: Jillian Huizenga, Wayne Chen, Aron Zhao
 * @Version: 1/21/2020
 **********************************************************************/
public class Suit extends Tile {

    /** Type of Suit tile design it is **/
    private String design;

    /** Numerical value of the tile **/
    private int value;

    public Suit() {

        super("Suit");
        this.design = null;
        this.value = 0;
    }



    /*******************************************************************
     * Constructor for a Suit tile.
     *
     * @param design Design of the tile, Circle, Bamboo
     *               Char.
     * @param value What is the value of the Tile.
     ******************************************************************/
    public Suit(String design, int value) {

        super("Suit");
        this.design = design;
        this.value = value;
    }

    /*******************************************************************
     * Method gets the design of the tile.
     *
     * @return design of the tile, circle, char, or bamboo
     ******************************************************************/
    public String getDesign() {
        return design;
    }

    /*******************************************************************
     * Method sets the design of the tile.
     *
     * @param design Tupe of design for the tile.
     ******************************************************************/
    public void setDesign(String design) {
        this.design = design;
    }

    /*******************************************************************
     * This method gets the value of the Suit tile.
     *
     * @return Value of the Suit tile.
     ******************************************************************/
    public int getValue() {
        return value;
    }

    /*******************************************************************
     * This method sets the value of the Suit tile.
     *
     * @param value Value of the Suit tiles.
     ******************************************************************/
    public void setValue(int value) {
        this.value = value;
    }
}
