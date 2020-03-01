package MahjongCIS350;

/***********************************************************************
 * This is the class for all Suit Tiles in Mahjong (Bamboo, Circle,
 * Character)
 *
 * @Authors: Wayne Chen, Jillian Huizenga, Chris Paul, Aron Zhao
 * @Version: 2/28/2020
 **********************************************************************/
public class Suit extends Tile {

    /** Type of Suit Tile design **/
    private String design;

    /** Numerical value of the Suit Tile **/
    private int value;

    /*******************************************************************
     * This is the base constructor of the Suit class.
     ******************************************************************/
    public Suit() {

        super("Suit");
        this.design = null;
        this.value = 0;
    }

    /*******************************************************************
     * This is the constructor for a Suit Tile with parameters.
     *
     * @param design design of the tile (Circle, Bamboo
     *               Character).
     * @param value the value of the Tile.
     ******************************************************************/
    public Suit(String design, int value) {

        super("Suit");
        this.design = design;
        this.value = value;
    }

    /*******************************************************************
     * Method to return the design of the Tile.
     *
     * @return design of the Tile (Circle, Character, or Bamboo)
     ******************************************************************/
    public String getDesign() {

        return design;
    }

    /*******************************************************************
     * Method that sets the design of the Tile.
     *
     * @param design Type of design for the Tile.
     ******************************************************************/
    public void setDesign(String design) {

        this.design = design;
    }

    /******************************************************************
     * This method gets the value of the Suit Tile.
     *
     * @return Value of the Suit Tile.
     ******************************************************************/
    public int getValue() {

        return value;
    }

    /*******************************************************************
     * This method sets the value of the Suit Tile.
     *
     * @param value Value of the Suit Tile.
     ******************************************************************/
    public void setValue(int value) {

        this.value = value;
    }
}
