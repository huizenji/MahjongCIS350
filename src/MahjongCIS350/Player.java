package MahjongCIS350;

import java.util.ArrayList;

/**
 * an object of Hand stands for a hand of tiles
 * hands are initially empty
 * any types of tiles can be added or removed
 */
public class Player{

    /** Player tiles that is in his or her hand **/
    private ArrayList<Tile> handTile;

    /** Player tiles that is in his or her hand **/
    private ArrayList<Tile> setPile;

    /** The direction of the Player **/
    private String direction;

    /**
     * create an empty hand
     */
    public Player(){

        handTile = new ArrayList<Tile>();
        this.setPile = new ArrayList<Tile>();
        this.direction = null;
    }

    public Player(String direction) {
        this.handTile = new ArrayList<Tile>();
        this.setPile = new ArrayList<Tile>();
        this.direction = direction;
    }
    
    /**
     * clear the hand
     */
    public void clear(){
        handTile.clear();
    }

    /**
     * add Tile t to the end of the handTile
     * @param t
     */
    public void addTile(Tile t){
        if(t == null){
            throw new NullPointerException("No such type of tile, can't be added");
        }
        handTile.add(t);
    }

    /**
     * remove the Tile t
     * @param t
     */
    public void removeTile(Tile t){
        if(t == null){
            throw new NullPointerException("No such type of tile, can't be added");
        }
        handTile.remove(t);
    }

    /**
     * remove a tile from a specific position
     * @param p
     */
    public void removeTile(int p){
        if(p<0 || p>= handTile.size()){
            throw new IllegalArgumentException("Invalid position: " + p);
        }
        handTile.remove(p);
    }



    public ArrayList<Tile> getHandTile() {
        return handTile;
    }

    public void setHandTile(ArrayList<Tile> handTile) {
        this.handTile = handTile;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
