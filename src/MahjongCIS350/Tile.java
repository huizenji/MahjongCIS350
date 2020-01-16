package MahjongCIS350;

/***********************************************************************
 * This class represent each individual tile in the game of Mahjong.
 *
 * @Author: Jillian Huizenga, Wayne Chen
 * @Versoin: 1/14/2020
 **********************************************************************/
public class Tile {

    /** Type of tile **/
    private String type;

    /** Face up or Face down for the tile */
    private boolean face_up_down;

    /*******************************************************************
     * This is the constructor for the Tile class. Sets the tile to
     * be face down at the start (false).
     *
     * @param type What type of tile is represented
     ******************************************************************/
    public Tile(String type) {
        this.type = type;
        this.face_up_down = false;
    }

    /*******************************************************************
     * This method gets the type of tile.
     *
     * @return What type of mahjong tile it is.
     ******************************************************************/
    public String getType() {
        return type;
    }

    /*******************************************************************
     * This method sets the type of mahjong tile.
     *
     * @param type What type of mahjong tile it represents.
     ******************************************************************/
    public void setType(String type) {
        this.type = type;
    }

    /*******************************************************************
     * This method finds out if a tile is face up or face down.
     *
     * @return Returns true if the tile is face up or false if face
     *         down.
     ******************************************************************/
    public boolean isFace_up_down() {

        return face_up_down;
    }

    /*******************************************************************
     * This method sets which direction the tile is facing.
     *
     * @param face_up_down true for face up and false for face down.
     ******************************************************************/
    public void setFace_up_down(boolean face_up_down) {
        this.face_up_down = face_up_down;
    }
}
