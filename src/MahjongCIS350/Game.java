package MahjongCIS350;

import java.util.*;

/***********************************************************************
 * This class contains all pieces and rule functionality for the game
 * Mahjong. It is displayed by the Board and GUI classes.
 *
 * @Authors: Jillian Huizenga, Wayne Chen, Xianghe Zhao(Aaron)
 * @Version: 1/27/2020
 **********************************************************************/
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

        Game a = new Game();
        System.out.println(a.ruleBook());
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
        removeKongHand();
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
         playerList[startingPlayer].addTile(tiles.remove(0));

        replacePointTile();
    }

    private void replacePointTile() {

        // Looping through each players hand
        for (int index = 0; index < 4; index++) {

            // Keep replacing tile until no point tile is in hand
            for (int i = 0; i < playerList[index].
                    getHandTile().size(); i++) {

                // Check tile if it is a point tile
                if (!(playerList[index].getHandTile().get(i)
                        instanceof Suite)) {

                    playerList[index].getSetPile().add(playerList[index].getHandTile().remove(i));
                    playerList[index].getHandTile().add(tiles.remove(0));
                    i--;
                }
            }

            autoSort(playerList[index]);
        }
    }

    private void removeKongHand(){

        int kongIndex;

        for (int i = 0; i < playerList.length; i++){

            while(isKongHand(playerList[i]) != -1) {

                kongIndex = isKongHand(playerList[i]);

                if (kongIndex != -1) {

                    for (int k = 0; k < 4; k++) {

                        playerList[i].removeTileSet(kongIndex);
                    }

                    draw(playerList[i]);
                    autoSort(playerList[i]);
                }
            }
        }
    }

    /*******************************************************************
     * This method determines if the player has a kong in their hand.
     *
     * @param pl The player hands that is being checked.
     * @return It will return -1 if there is no Kong, but it will return
     *         the starting index of where the Kong starts if there is
     *         a Kong.
     ******************************************************************/
    private int isKongHand(Player pl){

        ArrayList<Tile> hand = pl.getHandTile();
        boolean temp = true;
        for (int i = 0; i < pl.getHandTile().size() - 4; i++){
            for (int k = 1; k < 4; k++){
                if(!(compareSuite((Suite)(hand.get(i)), (Suite)(hand.get(i+k))))){

                    temp = false;
                }
            }

            // Checking if there is actually a kong
            if (temp) {
                return i;
            }

            temp = true;
        }
        return -1;
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
                for (int j = i + 1; j < handTile.size(); ++j) {
                    if (compareConsecutive(handTile.get(i), handTile.get(j), check)) {
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

        ArrayList<Integer> loc = findTile(pl.getHandTile(), desired);

        for (int i = loc.size() - 1; i >= 0; i--) {

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


        for (int value = 1; value <= 9; value++) {

            for (int i = 0; i < player.getHandTile().size(); i++) {

                playerTile = (Suite) player.getHandTile().get(i);
                comp.setValue(value);
                if (compareSuite(comp, playerTile)) {
                    temp.add(playerTile);
                }
            }
        }

        // Adding Bamboo Tiles
        comp.setDesign("Bamboo");

        for (int value = 1; value <= 9; value++) {
            for (int i = 0; i < player.getHandTile().size(); i++) {

                playerTile = (Suite) player.getHandTile().get(i);
                comp.setValue(value);
                if (compareSuite(comp, playerTile)) {

                    temp.add(playerTile);
                }
            }
        }

        // Adding Character Tiles
        comp.setDesign("Character");

        for (int value = 1; value <= 9; value++) {
            for (int i = 0; i < player.getHandTile().size(); i++) {

                playerTile = (Suite) player.getHandTile().get(i);
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

            throw new NullPointerException("Argument 1 - tile 1 has a" +
                    "null object.\n");
        }

        if (tile2 == null) {

            throw new NullPointerException("Argument 2 - tile 2 has a" +
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
     *
     * @param tile1
     * @param tile2
     * @param tile3
     * @return
     */
    private boolean compareConsecutive(Suite tile1, Suite tile2, Suite tile3) {
        //make an array to hold the three values of the three tiles
        int[] array = {tile1.getValue(), tile2.getValue(), tile3.getValue()};
        //find min and max value
        int min = tile1.getValue();
        int max = tile1.getValue();
        for (int i = 0; i < 3; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        for (int i = 0; i < 3; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        //check if they are consecutive and values are not equal to each other
        boolean[] visited = new boolean[3];
        if (tile1.getDesign().equals(tile2.getDesign()) && tile1.getDesign().equals(tile3.getDesign())) {
            if (max - min + 1 == 3) {
                if (array[0] != array[1] && array[1] != array[2] && array[0] != array[2]) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean pointTile(Tile tile){

        if (tile instanceof Suite)
            return false;
        else
            return true;
    }

    /*******************************************************************
     * This method draws a tile from the main wall/tiles. It will then
     * draw another tile if the original tile draw is a point tile.
     *
     * @param pl The current players turn.
     ******************************************************************/
    public void draw(Player pl){

        Tile drawn = tiles.remove(0);

        while(pointTile(drawn)){

            pl.getSetPile().add(drawn);
            drawn = tiles.remove(0);
        }

        pl.getHandTile().add(drawn);
    }

    /*******************************************************************
     * This methods removes a tile from hand based on the player and
     * the tile index in their hand.
     *
     * @param pl The player that is discarding the tile.
     * @param tileIndex The tile index in the selected players hand.
     ******************************************************************/
    public void discard(Player pl, int tileIndex){

        if (tileIndex >= pl.getHandTile().size() || tileIndex < 0){

            throw new IndexOutOfBoundsException("tile index is out of" +
                    "bounds.");
        }

        discardPile.add(pl.getHandTile().remove(tileIndex));
    }

    /*******************************************************************
     * AI design to get rid of a tiles and draw tiles. This is basically
     * an AI design to loses and allow the user to feel good.
     * @param pl The player whose action will be determined by an AI.
     ******************************************************************/
    public void dumbAI(Player pl){

        Random rand = new Random();

        draw(pl);
        discard(pl,rand.nextInt(pl.getHandTile().size()));
    }

    public String ruleBook(){

        String rules = starting() + setRule() + claimChiRule() +
                claimPongRule() + claimKongRule() + declareMahjong()
                + scoring();

        return rules;
    }

    private String starting(){

        String msg = "Players start with 13 tiles each. Each player " +
                "is assigned a wind (East-South-West-North). " +
                "East starts the game by picking a tile from the " +
                "wall and discarding a tile. Players then take" +
                " clockwise turns picking a tile from " +
                "the wall and then discarding one tile from the hand. " +
                "It is also possible to claim a tile discarded by " +
                "another player under certain circumstances. " +
                "In such cases, the player to the left of the " +
                "claiming player becomes next in turn. So some " +
                "players may lose their turn in a go-around.\n\n";

        return msg;
    }

    private String setRule(){

        String msg = "A set of tiles consist of 3 tiles. This 3 tiles" +
                "may all be the same, thus forming a 3 of a kind or " +
                "they all form a 3 tile straight of the same " +
                "suit.\n\nAll sets that are formed in the hand by " +
                "drawing do not have to be revealed to the opposing " +
                "players. However, any set or kong that are claimed" +
                "must be revealed to all opposing players.\n\n" +
                "It is important to know that a kong is not consider " +
                "a set but it can be claimed.\n\n Once a player " +
                "declares that they can from a set, they move the" +
                "tiles that they used to from a set along with the " +
                "recently discarded tile to the set pile.";

        return msg;
    }

    private String claimChiRule(){

        String msg = "A chi can only be claimed when the opposing " +
                "player directly to right discards a " +
                "tile. In addition, the discarded the tile must be " +
                " able to use to form a 3 tile straight in the " +
                "players hand using the same suit. Also, the straight" +
                "can not extend from 9 to 1 or vise versa.\n\n";

        return msg;
    }

    private String claimPongRule(){

        String msg = "A pong can be claimed when any opposing player" +
                " discards a tile and you have a 2 tiles in your hand" +
                " that can be used to form a 3 of a kind with the " +
                "discarded tile.\n\n";

        return msg;
    }

    private String claimKongRule(){

        String msg = "A kong can be claimed when any opposing player" +
                " discards a tile and you have a 3 tiles in your hand" +
                " that can be used to form a 4 of a kind with the " +
                "discarded tile. In addition, the player must draw a " +
                "new tile from the main deck/wall before discarding a" +
                " tile.\n\nAny kong that is formed by drawing a tile " +
                "is does not have to be revealed to the opposing" +
                " players. This is optional. However, it is to " +
                "your advantage to not reveal the tiles because it " +
                "could possibly hinder your opponents game. In " +
                "addition, a kong can not remain in the hand, instead" +
                "the kong that forms by drawing must be moved to the" +
                "set pile or discard a tile to break up the kong.";

        return msg;
    }

    private String declareMahjong(){

        String msg = "A mahjong can be claimed when a player has " +
                "all sets(chi,pong,kong), any point tiles and a " +
                "single pair in their hand or set pile combined." +
                "In addition, there can not be a kong that " +
                "in the declared players hand. At this point, the " +
                "player that declares Mahjong wins and their score" +
                "will be calculated based on the scoring rules.\n\n";

        return msg;
    }

    private String scoring(){

        String msg = "Once a player has declared mahjong, they win. " +
                "The winning player will receive of a score of 1 " +
                "point and 1 additional point for every dragon, wind" +
                "and flower tile that is in the set pile.";

        return msg;
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
