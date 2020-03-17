package MahjongCIS350;

public class Flower extends Tile {

    // interact with GUI
    int number;
    /*******************************************************************
     * This is the constructor for the Tile class. Sets the tile to
     * be face down at the start (false).
     *******************************************************************/
    public Flower(int num) {
        super("Flower");
        number = num;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
