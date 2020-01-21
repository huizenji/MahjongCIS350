package MahjongCIS350;

import java.util.ArrayList;

/**
 * an object of Hand stands for a hand of tiles
 * hands are initially empty
 * any types of tiles can be added or removed
 */
public class Player{

    /** Player tiles that is in his or her hand **/
    private ArrayList<Suite> handTile;

    /** The direction of the Player **/
    private String direction;

    /**
     * create an empty hand
     */
    public Player(){

        handTile = new ArrayList<Suite>();
    }

    public Player(String direction) {
        this.handTile = new ArrayList<Suite>();
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
    public void addTile(Suite t){
        if(t == null){
            throw new NullPointerException("No such type of tile, can't be added");
        }
        handTile.add(t);
    }

    /**
     * remove the Tile t
     * @param t
     */
    public void removeTile(Suite t){
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

    public ArrayList<Suite> getHandTile() {
        return handTile;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
