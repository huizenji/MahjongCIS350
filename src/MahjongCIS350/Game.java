package MahjongCIS350;

import java.util.*;

/**********************************************************************
 * This class contains all pieces and rule functionality for the game
 * Mahjong. It is displayed by the Board and GUI classes.
 *
 * @Authors: Jillian Huizenga, Wayne Chen, Xianghe Zhao(Aaron)
 * @Version: 1/27/2020
 *********************************************************************/
public class Game {

    /**
     * All the tiles in Mahjong
     **/
    private ArrayList<Tile> tiles;

    private ArrayList<Tile> discardPile;

    /**
     * The max amount of tiles in Mahjong
     **/
    private int maxTile;

    /**
     * Index of current player ,aka, whose turn
     **/
    private int currentPlayer;

    /**
     * Index of player who has the "East" direction
     **/
    private int startingPlayer;

    Player[] playerList;

    /**
     * Total number of players per game
     **/
    private int TOTAL_PLAYER = 4;

    public static void main(String[] args) {

        ArrayList<Integer> a = new ArrayList<>();
        ArrayList<Integer> b = new ArrayList<>();

        a.add(1);
        a.add(2);
        b.add(3);

        System.out.println(a);
        a = b;
        System.out.println(a);
    }

    /******************************************************************
     * This is the constructor for the Game class.
     *****************************************************************/
    public Game() {

        tiles = new ArrayList<>();
        discardPile = new ArrayList<>();
        maxTile = 144;
        createTile();
        setupPlayer();
        shuffle();
        dealTile_13();
    }

    /*******************************************************************
     * This method creates every tile in Mahjong.
     ******************************************************************/
    private void createTile() {

        createSuiteTile();
        createPointTile();
    }

    /*******************************************************************
     * This method creates all the suites tiles in Mahjong.
     ******************************************************************/
    private void createSuiteTile() {

        String[] design = {"Circle", "Bamboo", "Character"};

        for (int index = 0; index < 3; index++) {
            for (int numtile = 1; numtile <= 9; numtile++) {
                for (int i = 0; i < 4; i++) {

                    tiles.add(new Suite(design[index], numtile));

                }
            }
        }
    }

    /*******************************************************************
     * This method creates all the point tiles.
     ******************************************************************/
    private void createPointTile() {

        String[] dragonColor = {"Red", "Green", "White"};

        String[] windDir = {"North", "East", "South", "West"};

        // creating Dragon Tiles
        for (int index = 0; index < 3; index++) {
            for (int i = 0; i < 4; i++) {

                tiles.add(new Dragon(dragonColor[index]));
            }
        }

        // Creating Wind Tiles
        for (int index = 0; index < 4; index++) {
            for (int i = 0; i < 4; i++) {

                tiles.add(new Wind(windDir[index]));
            }
        }

        // Creating Flower tiles
        for (int i = 1; i < 9; i++) {

            tiles.add(new Flower(i));
        }
    }

    /******************************************************************
     * A getter function for individual tiles
     *
     * @param index
     * @return tile at indicated index
     *****************************************************************/
    public Tile getTile(int index) {
        return tiles.get(index);
    }

    /*******************************************************************
     * This method creates all the players in the game and randomly
     * assigns one of the player to be the starting player.
     ******************************************************************/
    private void setupPlayer() {

        playerList = new Player[TOTAL_PLAYER];

        // Creating 4 players
        playerList[0] = new Player("East");
        playerList[1] = new Player("South");
        playerList[2] = new Player("West");
        playerList[3] = new Player("North");

        Random rand = new Random();
        int randVal = rand.nextInt(4) + 1;

        // Rotate the winds of the player to randomly set a starting
        // player
        for (int i = 0; i < randVal; i++) {
            for (int j = 0; j < TOTAL_PLAYER; j++) {

                rotatePlayerDir(playerList[j]);
            }
        }

        // Find the starting player index
        startingPlayer = randVal % TOTAL_PLAYER;
        currentPlayer = startingPlayer;
    }

    /*******************************************************************
     * This method rotates the wind direction of each player.
     ******************************************************************/
    private void rotatePlayerDir(Player player) {

        if (player.getDirection().equals("East")) {

            player.setDirection("South");
        } else if (player.getDirection().equals("South")) {

            player.setDirection("West");
        } else if (player.getDirection().equals("West")) {

            player.setDirection("North");
        } else if (player.getDirection().equals("North")) {

            player.setDirection("East");
        }
    }

    /*******************************************************************
     * This method shuffles all the tiles that are not in the players
     * hands.
     ******************************************************************/
    private void shuffle() {

        Random rand = new Random();
        int randNum1;
        int randNum2;

        for (int i = 0; i < 100000; i++) {

            randNum1 = rand.nextInt(maxTile);
            randNum2 = rand.nextInt(maxTile);

            Tile temp = tiles.get(randNum2);
            tiles.set(randNum2, tiles.get(randNum1));
            tiles.set(randNum1, temp);
        }
    }

    /*******************************************************************
     * This method deals out 13 suite tiles to three players, and 14
     * tiles to the East player
     ******************************************************************/
    private void dealTile_13() {

        // Give out 12 Tiles
        for (int index = 0; index < 4; index++) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 4; j++) {
                    playerList[index].addTile(tiles.remove(0));
                }
            }
        }

        // Give out the 13 Tile
        for (int index = 0; index < 4; index++) {
                playerList[index].addTile(tiles.remove(0));
        }
        // Giving starting player 1 extra tile
        //playerList[startingPlayer].addTile(tiles.remove(0));

        replacePointTile();
    }

    private void replacePointTile(){

        // Looping through each players hand
        for (int index = 0; index < 4; index++){

            // Keep replacing tile until no point tile is in hand
            for (int i = 0; i < playerList[index].
                    getHandTile().size(); i++){

                // Check tile if it is a point tile
                if (!(playerList[index].getHandTile().get(i)
                        instanceof Suite)){

                    /** change to a function later **/
                    playerList[index].getSetPile().add(playerList[index].getHandTile().remove(i));
                    i--;
                }
            }

            //autoSort(playerList[index]);
        }
    }


    /**
     * To check if there is chi for a specific player
     *
     * @param handTile
     * @param check    the checked_Suite should belongs to the last player of the current player
     * @return
     */
    private boolean isChi(ArrayList<Suite> handTile, Suite check) {
        //set up lastPlayer of the currentPlayer
        int lastPlayer = currentPlayer - 1;


        int matchdesign = 0;
        //check if there are 3 same design of suite including the check_Suite
        for (int i = 0; i < handTile.size(); ++i) {
            if (handTile.get(i).getDesign().equals(check.getDesign())) {
                matchdesign++;
            }
        }
        //check if their values are consecutive
        if (matchdesign >= 2) {
            for (int i = 0; i < handTile.size() - 1; ++i) {
                for (int j = i+1; j < handTile.size(); ++j){
                    if(compareConsecutive(handTile.get(i),handTile.get(j),check)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /*******************************************************************
     * This method checks if there is a pong for a specific player.
     *
     * @param handTile The hand of the player that is being checked
     *                 for a pong.
     * @param check The tile checked if it can be made into a pong
     ******************************************************************/
    private boolean isPong(ArrayList<Suite> handTile, Suite check) {

        // Number of Tiles in player hand that can used for a pong
        int matchTile = 0;

        // Scan through the hand and determine if there is a matching
        // tile
        for (int i = 0; i < handTile.size(); i++) {

            if (compareSuite(handTile.get(i), check)) {

                matchTile++;
            }
        }

        // If there is 2 or more matching tile, then there is a pong
        if (matchTile >= 2) {

            return true;
        } else {

            return false;
        }
    }

    private void isKongDiscard() {

    }

    private void isKongDraw() {

    }

    private void isMahjong() {

    }

    private void takePong(Player pl, Suite tile) {

        ArrayList<Tile> desired = new ArrayList<>();
        desired.add(tile);
        desired.add(tile);

        ArrayList<Integer> loc = findTile(pl.getHandTile(),desired);

        for (int i = loc.size() - 1; i >= 0; i--){

            pl.removeTile(loc.get(i));
        }
    }

    /*******************************************************************
     * This method finds the desired tiles of an arraylist and returns
     * the index from the players hand that they are located at.
     *
     * @param playerHand
     * @param desired
     * @return
     */
    private ArrayList<Integer> findTile(ArrayList<Tile> playerHand,
                                        ArrayList<Tile> desired) {

        ArrayList index_loc = new ArrayList();
        for (int i = 0; i < desired.size(); i++) {

            for (int hand_index = 0; hand_index < playerHand.size();
                 hand_index++) {


                if (compareSuite((Suite) desired.get(i),
                        (Suite) playerHand.get(hand_index))) {

                }
            }
        }

        return index_loc;
    }


    /*******************************************************************
     * This method sorts the players hand with by circle, bamboo, and
     * character with the value incremented.
     *
     * @param player The player hand that is being sorted.
     ******************************************************************/
    private void autoSort(Player player) {

        ArrayList<Tile> temp = new ArrayList<>();
        Suite comp = new Suite(); // Suite that will be placed first
        Suite playerTile; // Tile from player that is being checked

        // Adding Circle Tiles
        comp.setDesign("Circle");
        for (int i = 0; i < player.getHandTile().size(); i++) {

            playerTile = (Suite) player.getHandTile().get(i);
            for (int value = 1; value <= 9; value++) {

                comp.setValue(value);
                if (compareSuite(comp, playerTile)) {
                    temp.add(playerTile);
                }
            }
        }

        // Adding Bamboo Tiles
        comp.setDesign("Bamboo");
        for (int i = 0; i < player.getHandTile().size(); i++) {

            playerTile = (Suite) player.getHandTile().get(i);
            for (int value = 1; value <= 9; value++) {

                comp.setValue(value);
                if (compareSuite(comp, playerTile)) {

                    temp.add(playerTile);
                }
            }
        }

        // Adding Character Tiles
        comp.setDesign("Character");
        for (int i = 0; i < player.getHandTile().size(); i++) {

            playerTile = (Suite) player.getHandTile().get(i);
            for (int value = 1; value <= 9; value++) {

                comp.setValue(value);
                if (compareSuite(comp, playerTile)) {
                    temp.add(playerTile);
                }
            }
        }

        // Setting the sorted hand to player
        player.setHandTile(temp);
    }

    /*******************************************************************
     * This method compares 2 suite tiles and determines if they are
     * the same tile. Only works with Suites
     *
     * @param tile1 The first tile that is being compared.
     * @param tile2 The second tile that is being compared.
     * @return true if both tiles are the same.
     ******************************************************************/
    private boolean compareSuite(Suite tile1, Suite tile2) {

        if (tile1 == null) {

            throw new NullPointerException("Argument 1 - tile1 has a" +
                    "null object.\n");
        }

        if (tile2 == null) {

            throw new NullPointerException("Argument 2 - tile1 has a" +
                    "null object.\n");
        }

        if (tile1.getDesign().equals(tile2.getDesign()) &&
                tile1.getValue() == tile2.getValue()) {

            return true;
        }

        return false;
    }

    /**
     * This method compares 3 suite tiles and determine if they are consecutive and belong to the same design of suite
     * @param tile1
     * @param tile2
     * @param tile3
     * @return
     */
    private boolean compareConsecutive(Suite tile1, Suite tile2, Suite tile3){
        //make an array to hold the three values of the three tiles
        int[] array = {tile1.getValue(),tile2.getValue(),tile3.getValue()};
        //find min and max value
        int min = tile1.getValue();int max = tile1.getValue();
        for(int i =0; i < 3; i++){
            if(array[i]<min){
                min = array[i];
            }
        }
        for(int i = 0; i<3; i++){
            if(array[i]>max){
                max = array[i];
            }
        }
        //check if they are consecutive and values are not equal to each other
        boolean[] visited = new boolean[3];
        if(tile1.getDesign().equals(tile2.getDesign())&&tile1.getDesign().equals(tile3.getDesign())){
            if(max-min+1 == 3){
                if(array[0]!=array[1]&&array[1]!=array[2]&&array[0]!=array[2]){
                    return true;
                }
            }
        }
        return false;
    }


    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getStartingPlayer() {
        return startingPlayer;
    }

    public void setStartingPlayer(int startingPlayer) {
        this.startingPlayer = startingPlayer;
    }

    private void setNextStartingPlayer() {

        startingPlayer = (startingPlayer + 1) % 4;
    }

    private void setNextCurrentPlayer() {

        currentPlayer = (currentPlayer + 1) % 4;
    }

    public Player getPlayerList(int playerNum) {
        return playerList[playerNum];
    }

    public ArrayList<Tile> getDiscardPile() {
        return discardPile;
    }

    public void setDiscardPile(ArrayList<Tile> discardPile) {
        this.discardPile = discardPile;
    }
}
