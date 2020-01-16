package MahjongCIS350;

public class Suite extends Tile {

    /** Type of suite tile design it is **/
    private String design;

    /** Numerical value of the tile **/
    private int value;

    /*******************************************************
     * Constructor for a suite tile.
     *
     * @param design Design of the tile, Circle, Bamboo
     *               Char.
     * @param value What is the value of the Tile.
     ******************************************************/
    public Suite(String design, int value) {

        super("Suite");
        this.design = design;
        this.value = value;
    }

    /*******************************************************
     * Method gets the design of the tile.
     *
     * @return design of the tile, circle, char, or bamboo
     ******************************************************/
    public String getDesign() {
        return design;
    }

    /*******************************************************
     * Method sets the design of the tile.
     *
     * @param design Tupe of design for the tile.
     ******************************************************/
    public void setDesign(String design) {
        this.design = design;
    }

    /*******************************************************
     * This method gets the value of the suite tile.
     *
     * @return Value of the suite tile.
     ******************************************************/
    public int getValue() {
        return value;
    }

    /*******************************************************
     * This method sets the value of the suite tile.
     *
     * @param value Value of the suite tiles.
     ******************************************************/
    public void setValue(int value) {
        this.value = value;
    }
}
