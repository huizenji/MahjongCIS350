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
    private int point;// add point if draw a flower

    public Player(){

        handTile = new ArrayList<Tile>();
        this.setPile = new ArrayList<Tile>();
        this.direction = null;
        point = 0;
    }

    public Player(String direction) {
        this.handTile = new ArrayList<Tile>();
        this.setPile = new ArrayList<Tile>();
        this.direction = direction;
        point = 0;
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

        Suite s = (Suite)t;
        if(t == null){
            throw new NullPointerException("No such type of tile, can't be added");
        }
        handTile.remove(t);
        setPile.add(t);
        if(s.getType().equals("Flower")){
            point+=s.getValue();
        }
    }

    /**
     * remove a tile from a specific position
     * @param p
     */
    public void removeTile(int p){

        if(p<0 || p>= handTile.size()){
            throw new IllegalArgumentException("Invalid position: " + p);
        }
        setPile.add(handTile.get(p));// add removed_tile to the pile
        Suite s = (Suite)handTile.get(p);
        if(s.getType().equals("Flower")){
            point+=s.getValue();
        }
        handTile.remove(p);
    }

    /**
     * get the last tile of the handtile or pile
     * @param handTile
     * @return  last Tile
     */
    public Tile lastTile(ArrayList<Suite> handTile){
        return handTile.get(handTile.size() - 1);
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

    public ArrayList<Tile> getSetPile(){return setPile;}

    public void setSetPile(ArrayList<Tile> setPile){this.setPile = setPile;};
}
