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
    private int point;

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
    public void clearHandPile(){
        handTile.clear();
    }

    public void clearSetPile(){
        setPile.clear();
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

    public void addTileSet(Tile t){

        if (t == null){

            throw new NullPointerException("No such type of tile, can't be added");
        }
        setPile.add(t);
    }

    public void removeTileSet(int index){

        if(index < 0 || index >= handTile.size()){
            throw new IllegalArgumentException("Invalid position: " +
                    index);
        }

        setPile.add(handTile.remove(index));
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

    public Tile getTileFromHand(int index){
        return handTile.get(index);
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

    public Tile getSetTile(int index){
        return setPile.get(index);
    }

    public void setSetPile(ArrayList<Tile> setPile){this.setPile = setPile;};

    public void setPoint(int point) {
        this.point = point;
    }

    public int getPoint() {
        return point;
    }
}
