package MahjongCIS350;

/***********************************************************************
 * This class represent each individual tile in the game of Mahjong.
 *
 * @Author: Wayne Chen
 * @Versoin: 1/14/2020
 **********************************************************************/
public class Tile {


    String type;

    public Tile(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
