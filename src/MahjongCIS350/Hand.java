import java.util.ArrayList;

/**
 * an object of Hand stands for a hand of tiles
 * hands are initially empty
 * any types of tiles can be added or removed
 */
public class Hand {
    private ArrayList<Tile> handTile;

    /**
     * create an empty hand
     */
    public Hand(){
        hand = new ArrayList<Tile>();
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
     * @param position p
     */
    public void removeTile(int p){
        if(p<0 || p>= handTile.size()){
            throw new IllegalArgumentException("Invalid position: " + p);
        }
        handTile.remove(p);
    }

}
