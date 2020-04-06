package mahjongCIS350;

import java.util.*;

/***********************************************************************
 * This class contains all pieces and rule functionality for the game
 * Mahjong. It is displayed by the Board and GUI classes.
 *
 * @Authors: Wayne Chen, Jillian Huizenga, Chris Paul, Xianghe Zhao
 * @Version: 2/28/2020
 **********************************************************************/
public class Game {

    /** All Tiles in Mahjong. **/
    private ArrayList<Tile> tiles;

    /** All Tiles that have been discard by players. **/
    private ArrayList<Tile> discardPile;

    /** The max amount of Tiles in Mahjong. **/
    private int maxTile;

    /** Index of current Player to indicate turn order. **/
    private int currentPlayer;

    /** Index of Player who has the "East" direction. **/
    private int startingPlayer;

    /** List of all Players. **/
    private Player[] playerList;

    /** The Difficulty of the AI, levels 1 - 3. **/
    private int[] AIDiff;

    /** Total number of players per game. **/
    public static final int TOTALPLAYER = 4;

    /** The total amount of turns that have gone by. **/
    private int turnCount = 0;

    /** The 2 game options that the user can set to. **/
    private boolean gameOptionSimple;

    /** The constant for an very dumb AI. **/
    public static final int DUMB = 1;

    /** The constant for a beginner level AI. **/
    public static final int BEGINNER = 2;

    /** The constant for an advance level AI. **/
    public static final int ADVANCE = 3;

    /** The constant for default setting for AI. **/
    public static final int defaultAI = BEGINNER;

    /*******************************************************************
     * This is the constructor for the Game class.
     ******************************************************************/
    public Game() {

        tiles = new ArrayList<>();
        discardPile = new ArrayList<>();
        maxTile = 144;
        gameOptionSimple = true;
        createTile();
        setupPlayer();
        shuffle();
        dealTile13();
        removeKongHand();
        setupAIDiff();
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

        for (int index = 0; index < design.length; index++) {
            for (int numtile = 1; numtile <= 9; numtile++) {

                for (int i = 0; i < 4; i++) {

                    tiles.add(new Suit(design[index], numtile));
                }
            }
        }
    }

    /*******************************************************************
     * This method creates all the point Tiles.
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

    /*******************************************************************
     * This method creates all the players in the game and randomly
     * assigns one of the player to be the starting player.
     ******************************************************************/
    private void setupPlayer() {

        playerList = new Player[TOTALPLAYER];

        // Creating 4 Players
        playerList[0] = new Player("East");
        playerList[1] = new Player("South");
        playerList[2] = new Player("West");
        playerList[3] = new Player("North");

        Random rand = new Random();
        int randVal = rand.nextInt(4) + 1;

        // Rotate the winds of the player to randomly set a starting
        // Player
        for (int i = 0; i < randVal; i++) {

            for (int j = 0; j < TOTALPLAYER; j++) {

                rotatePlayerDir(playerList[j]);
            }
        }

        // Find the starting player index
        startingPlayer = randVal % TOTALPLAYER;
        currentPlayer = startingPlayer;
    }

    /*******************************************************************
     * This method rotates the wind direction of each Player.
     * @param player The player whose direction is rotated.
     ******************************************************************/
    private void rotatePlayerDir(final Player player) {

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
     * This method shuffles all the Tiles that are not in the Players'
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
     * Remove all the Tiles from all hands and regenerate the game.
     ******************************************************************/
    public void reset(){

        for(int i =0; i <4; i++) {

            playerList[i].clearHandPile();
            playerList[i].clearSetPile();
        }

        tiles.clear();
        discardPile.clear();
        createTile();
        setupPlayer();
        shuffle();
        dealTile13();
        removeKongHand();
    }

    /*******************************************************************
     * This method deals out 13 Suit Tiles to three Players, and 14
     * Tiles to the East Player
     ******************************************************************/
    private void dealTile13() {

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

        // Only Appropriate tile depending on game mode
        replacePointTile();
    }

    /*******************************************************************
     * If a Player draws a point Tile, then the Tile is moved to their
     * set pile and they draw a new Tile to replace it. This is done
     * until all Tiles in a Player's hand are Suits. If game mode is
     * set to traditional, only removes flower tile.
     ******************************************************************/
    private void replacePointTile() {

        // Looping through each players hand
        for (int index = 0; index < 4; index++) {

            // Keep replacing tile until no point tile is in hand
            for (int i = 0; i < playerList[index].
                    getHandTile().size(); i++) {

                // Check tile if it is a point tile
                if (isPointTile(playerList[index].getHandTile().
                        get(i))) {

                    playerList[index].getSetPile().
                            add(playerList[index].getHandTile().
                                    remove(i));
                    playerList[index].getHandTile().
                            add(tiles.remove(0));
                    i--;
                }
            }

            autoSort(playerList[index]);
        }
    }

    /*******************************************************************
     * This method goes through each players hand and removes any
     * kongs/4 of kinds that exist.
     ******************************************************************/
    private void removeKongHand() {

        Tile kongTile;

        for (int i = 0; i < playerList.length; i++) {

            kongTile = isKongHand(playerList[i]);
            while (kongTile != null) {

                for (int k = playerList[i].getHandTile().size() - 1;
                     k >= 0 ; k--) {

                    if (compareTile(playerList[i].getHandTile().get(k),
                            kongTile))

                    draw(playerList[i]);
                    autoSort(playerList[i]);

                }

                // reset till
                kongTile = isKongHand(playerList[i]);
            }
        }
    }

    /*******************************************************************
     * This method determines if the Player has a kong in their hand.
     *
     * @param player The player hands that is being checked.
     * @return Returns the tile that forms the kong.
     ******************************************************************/
    private Tile isKongHand(Player player) {

        ArrayList<Tile> hand = player.getHandTile();
        int totalCopy = 4;
        int count = 0;
        Tile kong = null;

        for (int i = 0; i < hand.size() - totalCopy; i++) {
            for (int k = i + 1; k < hand.size(); k++) {

                if ((compareTile((hand.get(i)), (hand.get(k))))) {

                    count++;
                }
            }

            // Checking if there is a kong
            if (count == totalCopy) {

                kong = hand.get(i);
            }

            // reset counter
            count = 0;
        }

        return kong;
    }

    /*******************************************************************
     * This method sets the AI to the basic level (beginner)
     ******************************************************************/
    private void setupAIDiff(){

        AIDiff = new int[TOTALPLAYER - 1];

        for (int i = 0; i < AIDiff.length; i++) {

            AIDiff[i] = BEGINNER;
        }
    }

    /*******************************************************************
     * Whenever Player draws a Tile, if it's a point Tile then score
     * increase by 1. If there's a point Tile in hand, also increase
     * the score by 1.
     * @param player The player.
     ******************************************************************/
    public void pileScore(final Player player) {

        int point = 0;

        for(int i = 0; i <= player.getSetPile().size(); i++) {

            if (isPointTile(player.getSetTile(i))) {

                point++;
            }
        }

        player.setPoint(player.getPoint() + point);
    }

    /*******************************************************************
     * To check if there is chi for a specific Player.
     *
     * @param pl The player that is being checked.
     * @param check the checked_Suite should belongs to the last
     *              player of the current player
     * @return True if the player can claim a chi.
     ******************************************************************/
    public boolean isChi(Player pl, Tile check) {

        ArrayList<Tile> plHand = pl.getHandTile();

        if (check == null){

            throw new IllegalArgumentException("Needs a tile"
                    + " to check");
        }

        if (!(check instanceof Suit)){

            return false;
        }

        for (int i = 0; i < plHand.size(); i++){

            for (int j = i; j < plHand.size(); j++){

                if (i != j){

                    if (plHand.get(i) instanceof Suit
                        && plHand.get(j) instanceof Suit) {

                        Suit suit1 = (Suit) plHand.get(i);
                        Suit suit2 = (Suit) plHand.get(j);

                        if (compareConsecutiveSuits(suit1, suit2,
                                (Suit) check)) {

                            return true;
                        }
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
    public boolean isPong(ArrayList<Tile> handTile, Tile check) {

        // Number of Tiles in Player hand that can used for a pong
        int matchTile = 0;

        if (check == null){

            throw new IllegalArgumentException("Tile that is checked "
                    + "can not be null");
        }

        // Scan through the hand and determine if there is a matching
        // Tile
        for (int i = 0; i < handTile.size(); i++) {

            if (compareTile(handTile.get(i), check)) {

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

    /*******************************************************************
     * This method compares 2 Tiles and determines if they are
     * the same Tile. This method uses compareSuit for checking
     * two Tiles of type Suit
     *
     * @param tile1 The first tile that is being compared.
     * @param tile2 The second tile that is being compared.
     * @return true if both tiles are the same.
     ******************************************************************/
    private boolean compareTile(Tile tile1, Tile tile2) {

        //If we have null tile throw an exception
        if (tile1 == null) {

            throw new NullPointerException("Argument 1 - tile1 has a" +
                    "null object.\n");
        }

        if (tile2 == null) {
            throw new NullPointerException("Argument 2 - tile1 has a" +
                    "null object.\n");
        }

        // First check to see if the types match (e.g. Two tiles of type
        // "Suit",two tiles of type "Dragon", etc...)
        if (tile1.getType().equals(tile2.getType())) {

            //If the tiles are Winds, check direction match
            if (tile1.getType().equals("Wind")) {

                if (((Wind) tile1).getDirection().equals(((Wind) tile2).
                        getDirection())) {

                    return true;
                }
            }

            //If the tiles are Dragons, check color match
            if (tile1.getType().equals("Dragon")) {

                if (((Dragon) tile1).getColor().equals(((Dragon) tile2).
                        getColor())) {

                    return true;
                }
            }

            // If the Tiles are a Suit, use compareSuit
            if (tile1.getType() == "Suit") {

                if (compareSuit((Suit) tile1, (Suit) tile2)) {

                    return true;
                }
            }
        }

        //If we got here, return false as we have no match
        return false;
    }

    /*******************************************************************
     * This method checks if there is a Kong in a Player's setPile or
     * handTile. This method uses compareTile.
     *
     * @param search The handTile or setPile being searched through
     *        for a Kong.
     * @param check The Tile that is checked if it can be made into a
     *              Kong.
     ******************************************************************/
    public boolean isKong(ArrayList<Tile> search, Tile check) {

        int numOfMatch = 0;

        if (check == null){

            throw new IllegalArgumentException("Tile that is checked "
                    + "can not be null");
        }

        //Loop through search looking for matching tiles
        for (int i = 0; i < search.size(); i++) {

            if (compareTile(search.get(i), check)) {
                numOfMatch++;
            }
        }

        // If we have found enough to make 4 of the same, then we can
        // make a Kong
        if (numOfMatch >= 3) {

            return true;

        } else {

            return false;
        }
    }


    /*******************************************************************
     * This methods determines if the player has drawn into a kong that
     * can be formed using a pong in the set pile.
     *
     * @param player The player that is being checked.
     * @return true if the player can form a kong using the set pile.
     ******************************************************************/
    public boolean isKongDraw(Player player) {

        boolean kong = false;
        // Note when a tile is draw, the tile is in the last index in
        // the array.

        // Counter to keep track of the number of tiles that are the
        // same
        int totalTile = 0;

        if (player.getHandTile().size() == 0){

            return false;
        }

        ArrayList<Tile> setPile = player.getSetPile();

        for (int i = 0; i < setPile.size(); i++) {

            if(compareTile(setPile.get(i), player.getHandTile().get(
                    player.getHandTile().size() - 1))) {

                totalTile++;
            }
        }

        if (totalTile == 3) {
            kong = true;
        }

        return kong;
    }

    /*******************************************************************
     * This method checks if a Player can Mahjong. It only checks
     * what is left of the Player's hand. If there are only chi's and
     * pong's along with 1 Tile that matches check, then the Player can
     * Mahjong.
     *
     * @param handTile The Player's current hand.
     * @param check The Tile checked to see if it lets the Player
     *              Mahjong.
     ******************************************************************/
    public boolean isMahjong(ArrayList<Tile> handTile, Tile check) {

        // Make a copy of what is in the Player's hand
        ArrayList<Tile> temp = new ArrayList<>();

        for (Tile t : handTile) {

            temp.add(t);
        }

        if (check != null) {

            temp.add(check);
            temp = autoSort(temp);
        }

//        // This section checks for any chi in the players hand
//        for (int i = 0; i < temp.size(); i++) {
//
//            outer:
//            for (int j = i + 1; j < temp.size(); j++) {
//
//                for (int k = j + 1; k < temp.size(); k++) {
//
//                    if (temp.get(i) instanceof Suit
//                            && temp.get(j) instanceof Suit
//                            && temp.get(k) instanceof Suit) {
//
//                        Suit tile1 = (Suit) temp.get(i);
//                        Suit tile2 = (Suit) temp.get(j);
//                        Suit tile3 = (Suit) temp.get(k);
//
//                        if (compareConsecutiveSuits(tile1, tile2,
//                                tile3)) {
//
//                            temp.remove(k);
//                            temp.remove(j);
//                            temp.remove(i);
//                            i--;
//                            break outer;
//                        }
//                    }
//                }
//            }
//        }
//
//        temp = removeAllPong(temp);
//
//        // If a player has 2 tiles remaining and they are the same,
//        // then they have mahjong.
//        if (temp.size() == 2 ){
//            if (compareTile(temp.get(0), temp.get(1)))
//            return true;
//        }

        return isMahjongBacktrackAlg(handTile);
    }



    /*******************************************************************
     * This is a helper method that assist in the algorithm in
     * determining mahjong.
     * @param hand The hand of the player
     * @return A hand where all pongs are removed
     ******************************************************************/
    private ArrayList<Tile> removeAllPong(ArrayList<Tile> hand){

        // Make a copy of what is in the Player's hand
        ArrayList<Tile> temp = new ArrayList<>();

        for (Tile t : hand) {

            temp.add(t);
        }

        // This section checks for any pongs in the Player's hand
        for (int i = 0; i < temp.size(); i++) {

            outer:
            for (int j = i + 1; j < temp.size(); j++) {

                // Check to see if the following Tile is the same as
                // the current Tile
                if (compareTile(temp.get(i), temp.get(j))) {

                    // Check to see if the Tile two away is the same as
                    // the current Tile
                    for (int k = j + 1; k < temp.size(); k++) {

                        // If so, remove the pong from temp
                        if (compareTile(temp.get(i), temp.get(k))) {
                            temp.remove(k);
                            temp.remove(j);
                            temp.remove(i);

                            // Counteract the increment as we need to
                            // start at the beginning of the ArrayList
                            i--;
                            break outer;
                        }
                    }
                }
            }
        }

        return hand;
    }

    /*******************************************************************
     * This is an recursive Algorithm that determines if the user
     * has a Mahjong. This uses a backtracking algorithm.
     * @param hand The hand that is checked
     * @return True if the hand has a Mahjong, false if not.
     ******************************************************************/
    private boolean isMahjongBacktrackAlg(ArrayList<Tile> hand){

        // Make a copy of what is in the Player's hand
        ArrayList<Tile> temp = new ArrayList<>();
        ArrayList<Tile> copy = new ArrayList<>();

        boolean win = false;

        for (Tile t : hand) {

            temp.add(t);
            copy.add(t);
        }

        if (temp.size() == 2 ){
            if (compareTile(temp.get(0), temp.get(1)))
                return true;

            else {

                return false;
            }
        }

        // Just in Case of an event occuring
        else if (temp.size() % 3 != 2){

            return false;
        }

        else {

            // This section removes the first chi or pong in the players
            // hand, then applies the algorithm
            for (int i = 0; i < temp.size(); i++) {
                for (int j = i + 1; j < temp.size(); j++) {
                    for (int k = j + 1; k < temp.size(); k++) {

                        if (compareTile(copy.get(i),
                                copy.get(j)) && compareTile(
                                        copy.get(i),
                                copy.get(k))) {

                            // If so, remove the pong from temp
                            if (compareTile(copy.get(i),
                                    copy.get(k))) {
                                copy.remove(k);
                                copy.remove(j);
                                copy.remove(i);

                                win =  isMahjongBacktrackAlg(copy);
                            }
                        }

                        // Remove Chi
                        else if (copy.get(i) instanceof Suit
                                && copy.get(j) instanceof Suit
                                && copy.get(k) instanceof Suit) {

                            Suit tile1 = (Suit) copy.get(i);
                            Suit tile2 = (Suit) copy.get(j);
                            Suit tile3 = (Suit) copy.get(k);

                            if (compareConsecutiveSuits(tile1, tile2,
                                    tile3)) {

                                copy.remove(k);
                                copy.remove(j);
                                copy.remove(i);

                                win =  isMahjongBacktrackAlg(copy);

                            }
                        }


                        // Backtracking Algorthim Determine win
                        if (win){

                            return win;
                        }

                        // Not a Winning Combination, So retry with a
                        // different combination
                        else{

                            copy.clear();
                            for (Tile t : temp) {

                                copy.add(t);
                            }
                        }
                    }
                }
            }
        }

        return win;
    }

    /*******************************************************************
     * This method finds the tiles the player can used to claim a chi
     * @param pl The player that claimed the chi.
     * @param discard The tile that is discarded to claim the chi.
     * @return AN array list of integers the chi consisting of index.
     ******************************************************************/
     public ArrayList<Integer> getChiTile(Player pl, Tile discard) {

        ArrayList<Tile> desired = new ArrayList<>();
        ArrayList<Integer> loc;

        for (int i = 0; i < pl.getHandTile().size(); i++) {

            for (int j = i + 1; j < pl.getHandTile().size(); j++) {

                if (i != j) {

                    if (pl.getHandTile().get(i) instanceof Suit
                     && pl.getHandTile().get(j) instanceof Suit) {

                        Suit suit1 = (Suit) pl.getHandTile().get(i);
                        Suit suit2 = (Suit) pl.getHandTile().get(j);

                        if (compareConsecutiveSuits(suit1, suit2,
                                (Suit) discard)) {

                            desired.add(suit1);
                            desired.add(suit2);
                        }
                    }
                }
            }
        }

        loc = findTileVer2(pl.getHandTile(), desired);

        // Remove Repeating Tiles/Indexes
        for (int i = 0; i < loc.size() - 2; i += 2) {

            if (i + 3 > loc.size() || i + 2 > loc.size()) {

                break;
            }

            // Trying to find if tiles are the same
            else if (loc.get(i) == loc.get(i + 2)
                    && loc.get(i + 1) == loc.get(i + 3)) {

                loc.remove(i);
                loc.remove(i);
                i = i - 2;
            }

            else if (loc.get(i) == loc.get(i + 3)
                    && loc.get(i + 1) == loc.get(i + 2)) {

                loc.remove(i + 2);
                loc.remove(i + 2);
                i = i - 2;
            }
        }

        return loc;
    }


    public void takeChi(Player player, int tile1, int tile2){

        player.removeTileSet(tile2);
        player.removeTileSet(tile1);
        player.addTileSet(discardPile.remove(
                discardPile.size() - 1));
    }

    /*******************************************************************
     * This method allows a player to claim a pong.
     *
     * @param pl The player that is claiming the pong.
     * @param tile The discard tile that is being used to claim the
     *             pong.
     ******************************************************************/
    public void takePong(Player pl, Tile tile) {

        ArrayList<Tile> desired = new ArrayList<>();
        desired.add(tile);
        desired.add(tile);

        ArrayList<Integer> loc = findTile(pl.getHandTile(), desired);

        for (int i = loc.size() - 1; i >= 0; i--) {

            pl.removeTileSet(loc.get(i));
        }

        pl.addTileSet(discardPile.remove(discardPile.size() - 1));
    }

    /*******************************************************************
     * This method allows the player to claim a kong.
     *
     * @param player The player that is claiming the kong.
     * @param tile The tile that is being used to claim the kong.
     ******************************************************************/
    public void takeKong(Player player, Tile tile){

        // Add Tile and remove Kong
        player.getHandTile().add(tile);
        removeKongHand();

        // draw Tile and remove any Kong
        draw(player);
        removeKongHand();
    }

    /*******************************************************************
     * This method allows the player to move a tile to the set pile
     * to form a Kong
     *
     * @param player Player who drew into a kong.
     ******************************************************************/
    public void takeKongDraw(Player player){

        // Add the tile from hand to setpile
        int lastLoc = player.getHandTile().size() - 1;
        player.getSetPile().add(player.getHandTile().remove(lastLoc));

        draw(player);
        removeKongHand();
    }

    /*******************************************************************
     * This method finds the desired Tiles of an ArrayList and returns
     * the index from the Player's hand that they are located at.
     *
     * @param playerHand The player's hand that is being searched.
     * @param desired The tiles that are searched in the players hand
     *                as an arraylist.
     * @return The index where the searched tiles are found.
     ******************************************************************/
    private ArrayList<Integer> findTile(ArrayList<Tile> playerHand,
                                        ArrayList<Tile> desired) {

        ArrayList<Integer> index_loc = new ArrayList();

        for (int i = 0; i < desired.size(); i++) {

            search:
            for (int hand_index = 0; hand_index < playerHand.size();
                 hand_index++) {

                if (compareTile(desired.get(i),
                        playerHand.get(hand_index))) {

                    if (!index_loc.contains(hand_index)){

                        index_loc.add(hand_index);
                        break search;
                    }
                }
            }
        }

        return index_loc;
    }

    /*******************************************************************
     * This method finds the desired Tiles of an ArrayList and returns
     * the index from the Player's hand that they are located at. This
     * version of find tiles will take duplicate locations.
     *
     * @param playerHand The player's hand that is being searched.
     * @param desired The tiles that are searched in the players hand
     *                as an arraylist.
     * @return The index where the searched tiles are found.
     ******************************************************************/
    private ArrayList<Integer> findTileVer2(ArrayList<Tile> playerHand,
                                        ArrayList<Tile> desired) {

        ArrayList<Integer> index_loc = new ArrayList();

        for (int i = 0; i < desired.size(); i++) {

            search:
            for (int hand_index = 0; hand_index < playerHand.size();
                 hand_index++) {

                if (compareTile(desired.get(i),
                        playerHand.get(hand_index))) {

                        index_loc.add(hand_index);
                        break search;
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
        String[] dragonColor = {"Red", "Green", "White"};
        String[] windDir = {"North", "East", "South", "West"};

        // Tile from player that is being checked
        Tile playerTile;

        // Tiles created for comparision
        Dragon compD = new Dragon();
        Wind compW = new Wind();
        Suit compS = new Suit();

        for (int i = 0; i < dragonColor.length; i++){

            compD.setColor(dragonColor[i]);

            for (int k = 0; k < player.getHandTile().size(); k++){
                playerTile = player.getHandTile().get(k);
                if (compareTile(compD, playerTile)){

                    temp.add(playerTile);
                }
            }
        }

        for (int i = 0; i < windDir.length; i++){

            compW.setDirection(windDir[i]);

            for (int k = 0; k < player.getHandTile().size(); k++){
                playerTile = player.getHandTile().get(k);
                if (compareTile(compW, playerTile)){

                    temp.add(playerTile);
                }
            }
        }

        // Adding Circle Tiles
        compS.setDesign("Circle");

        for (int value = 1; value <= 9; value++) {
            for (int i = 0; i < player.getHandTile().size(); i++) {

                playerTile = player.getHandTile().get(i);
                compS.setValue(value);

                if (compareTile(compS, playerTile)) {

                    temp.add(playerTile);
                }
            }
        }

        // Adding Bamboo Tiles
        compS.setDesign("Bamboo");

        for (int value = 1; value <= 9; value++) {

            for (int i = 0; i < player.getHandTile().size(); i++) {

                playerTile = player.getHandTile().get(i);
                compS.setValue(value);

                if (compareTile(compS, playerTile)) {

                    temp.add(playerTile);
                }
            }
        }

        // Adding Character Tiles
        compS.setDesign("Character");

        for (int value = 1; value <= 9; value++) {

            for (int i = 0; i < player.getHandTile().size(); i++) {

                playerTile = player.getHandTile().get(i);
                compS.setValue(value);

                if (compareTile(compS, playerTile)) {

                    temp.add(playerTile);
                }
            }
        }

        // Setting the sorted hand to player
        player.setHandTile(temp);
    }

    /*******************************************************************
     * This method sorts the players hand with by circle, bamboo, and
     * character with the value incremented.
     *
     * @param hand The hand the is being autosorted
     ******************************************************************/
    private ArrayList<Tile> autoSort(ArrayList<Tile> hand) {

        ArrayList<Tile> temp = new ArrayList<>();
        String[] dragonColor = {"Red", "Green", "White"};
        String[] windDir = {"North", "East", "South", "West"};

        // Tile from player that is being checked
        Tile playerTile;

        // Tiles created for comparision
        Dragon compD = new Dragon();
        Wind compW = new Wind();
        Suit compS = new Suit();

        for (int i = 0; i < dragonColor.length; i++){
            compD.setColor(dragonColor[i]);

            for (int k = 0; k < hand.size(); k++){
                playerTile = hand.get(k);
                if (compareTile(compD, playerTile)){

                    temp.add(playerTile);
                }
            }
        }

        for (int i = 0; i < windDir.length; i++){

            compW.setDirection(windDir[i]);

            for (int k = 0; k < hand.size(); k++){
                playerTile = hand.get(k);
                if (compareTile(compD, playerTile)){

                    temp.add(playerTile);
                }
            }
        }

        // Adding Circle Tiles
        compS.setDesign("Circle");

        for (int value = 1; value <= 9; value++) {
            for (int i = 0; i < hand.size(); i++) {

                playerTile = hand.get(i);
                compS.setValue(value);

                if (compareTile(compS, playerTile)) {

                    temp.add(playerTile);
                }
            }
        }

        // Adding Bamboo Tiles
        compS.setDesign("Bamboo");

        for (int value = 1; value <= 9; value++) {

            for (int i = 0; i < hand.size(); i++) {

                playerTile = hand.get(i);
                compS.setValue(value);

                if (compareTile(compS, playerTile)) {

                    temp.add(playerTile);
                }
            }
        }

        // Adding Character Tiles
        compS.setDesign("Character");

        for (int value = 1; value <= 9; value++) {

            for (int i = 0; i < hand.size(); i++) {

                playerTile = hand.get(i);
                compS.setValue(value);

                if (compareTile(compS, playerTile)) {

                    temp.add(playerTile);
                }
            }
        }

        // Setting the sorted hand to player
        return temp;
    }

    /*******************************************************************
     * This method compares 2 Suit Tiles and determines if they are
     * the same Tile. Only works with Suits.
     *
     * @param tile1 The first Tile that is being compared.
     * @param tile2 The second Tile that is being compared.
     * @return true if both Tiles are the same.
     ******************************************************************/
    private boolean compareSuit(Suit tile1, Suit tile2) {

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

    /******************************************************************
     * This method compares 3 Suit Tiles and determines if they are
     * consecutive and belong to the same design of Suit.
     * @param tile1 Tile 1.
     * @param tile2 Tile 2.
     * @param tile3 Tile 3.
     * @return True if the tiles are the same suit and are consecutive
     *         with respective to each other.
     *****************************************************************/
    private boolean compareConsecutiveSuits(Suit tile1, Suit tile2,
                                            Suit tile3) {

        // make an array to hold the three values of the three Tiles
        int[] array = {tile1.getValue(), tile2.getValue(),
                tile3.getValue()};

        // find min and max value
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

        // check if they are consecutive and values are not
        // equal to each other
        if (tile1.getDesign().equals(tile2.getDesign()) &&
                tile1.getDesign().equals(tile3.getDesign())) {

            if (max - min + 1 == 3) {

                if (array[0] != array[1] && array[1] != array[2]
                        && array[0] != array[2]) {

                    return true;
                }
            }
        }

        return false;
    }

    /*******************************************************************
     * This method determines if a tile is a point tile.
     *
     * @param tile The tile that is being checked.
     * @return True if the tile is a point tile, false otherwise.
     ******************************************************************/
    private boolean isPointTile(final Tile tile) {

        boolean isPoint;

        if (gameOptionSimple) {
            if (tile instanceof Suit) {

                isPoint = false;

            } else if (tile instanceof Dragon || tile instanceof Wind ||
                    tile instanceof Flower) {

                isPoint = true;

            } else {

                isPoint = false;
            }

        } else {

            if (tile instanceof Flower) {

                isPoint = true;
            } else {

                isPoint = false;
            }
        }

        return isPoint;
    }

    /*******************************************************************
     * Determines if a pair of specific Tiles exists in a player's hand.
     * Returns false if the Tile is alone or already part of a pong.
     * @param plHand the player's hand being searched
     * @param tile the indicated Tile that might be part of a pair
     * @return true if pair is found, otherwise false
     ******************************************************************/
    private boolean isPair(ArrayList<Tile> plHand, Tile tile){

        int counter = 0;
        for (int i = 0; i < plHand.size(); i++){

            if (compareTile(plHand.get(i), tile)){

                counter++;
            }
        }

        if (counter == 2){
            return true;
        }
        return false;
    }

    /*******************************************************************
     * A function that indicates whether a Tile is part of an
     * almost-Chi, or that it and another Tile in the indicated player's
     * hand will be able to make a chi with another Tile that is
     * still in the drawPile.
     * @param pl the player whose hand is being searched.
     * @param tile the Tile that may be part of an almost-Chi.
     * @return true if the Tile is part of an almost-Chi.
     ******************************************************************/
    private boolean isAlmostChi(Player pl, Tile tile){

        ArrayList<Tile> plHand = pl.getHandTile();

        for (int check = 0; check < tiles.size(); check++) {

            if (tiles.get(check) == null) {

                throw new IllegalArgumentException("Needs a tile"
                        + " to check");
            }

            if ((tiles.get(check) instanceof Suit)) {

                for (int i = 0; i < plHand.size(); i++) {

                    for (int j = i; j < plHand.size(); j++) {

                        if (i != j) {

                            if (plHand.get(i) instanceof Suit
                                    && plHand.get(j) instanceof Suit) {

                                Suit suit1 = (Suit) plHand.get(i);
                                Suit suit2 = (Suit) plHand.get(j);

                                if (compareConsecutiveSuits(suit1,
                                        suit2, (Suit) tiles.get(check)))
                                {
                                    if (compareTile(suit1, tile) ||
                                            compareTile(suit2, tile)){
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }

        return false;

    }

    /*******************************************************************
     * This method draws a tile from the main wall/tiles. It will then
     * draw another tile if the original tile draw is a point tile.
     *
     * @param pl The current players turn.
     ******************************************************************/
    public void draw(final Player pl) {

        Tile drawn = tiles.remove(0);

        while (isPointTile(drawn)) {

            pl.getSetPile().add(drawn);
            drawn = tiles.remove(0);
        }

        pl.getHandTile().add(drawn);
        removeKongHand();
    }

    /*******************************************************************
     * This methods removes a tile from hand based on the player and
     * the tile index in their hand.
     *
     * @param player The player that is discarding the tile.
     * @param tileIndex The tile index in the selected players hand.
     ******************************************************************/
    public void discard(Player player, int tileIndex) {

        if (tileIndex >= player.getHandTile().size() || tileIndex < 0) {

            throw new IndexOutOfBoundsException("tile index is out of"
                    + " bounds.");
        }

        discardPile.add(player.getHandTile().remove(tileIndex));
        autoSort(player);
    }

    /*******************************************************************
     * This method determine if the game is a stalemate.
     * @return true if the game is going to result in a stalemate
     ******************************************************************/
    public boolean isStalemate(){

        for (int i = 0; i < tiles.size(); i++){

            if (tiles.get(i) instanceof Suit){

                return false;
            }
        }

        return true;
    }

    /*******************************************************************
     * AI design to get rid of a tiles and draw tiles. This is basically
     * an AI design to lose and allow the user to feel good. This
     * method tells the AI to draw a tile.
     *
     * @param pl The player whose action will be determined by an AI.
     ******************************************************************/
    public void dumbAIDraw(Player pl) {

        if (pl == null){

            throw new IllegalArgumentException("Player can not be "
                    + "Null");
        }

        if (turnCount != 0) {

            draw(pl);
        }
    }

    /*******************************************************************
     * AI design to get rid of a tiles and draw tiles. This is basically
     * an AI design to lose and allow the user to feel good. This
     * method tells the AI to discard a tile.
     *
     * @param player The player whose action will be determined by
     *               an AI.
     ******************************************************************/
    public void dumbAIDiscard(Player player) {

        if (player == null){

            throw new IllegalArgumentException("Player can not be "
                    + "Null");
        }

        Random rand = new Random();
        discard(player, rand.nextInt(player.getHandTile().size()));
    }


    /*******************************************************************
     *
     * @param pl
     ******************************************************************/
    public void beginnerAIDiscard(Player pl){

    }

    /*******************************************************************
     * The discard sequence for the advanced AI option. This takes
     * into account advanced strategy, discarding Tiles that have the
     * least amount of use to the AI player, and takes the discard
     * pile into account.
     * @param pl the AI player that needs to discard.
     ******************************************************************/
    public void advancedAIDiscard(Player pl){

        // check each Tile in hand one by one
        for (int tileIndex = 0; tileIndex < getCurrentPlayer()
                .getHandTile().size(); tileIndex++) {

            // ranked order of importance, current order does not reflect
            // point differences in different kinds of sets

            // assuming Dragon/Wind Tiles are not point-Tiles


                // Tile can no longer be used due to previous discards
                if (numInDiscard(getCurrentPlayer().getHandTile()
                        .get(tileIndex)) == 3){
                    discard(pl, tileIndex);
                    System.out.println("AI discarded useless Tile");
                    break;
                }

                // Tile isn't part of an existing set, pair, or
                // almost-Chi
                else if (!isPong(pl.getHandTile(),
                        pl.getTileFromHand(tileIndex))) {

                    if (!isChi(pl, pl.getTileFromHand(tileIndex))) {

                        if(!isPair(pl.getHandTile(),
                                pl.getTileFromHand(tileIndex))) {

                            if (!isAlmostChi(pl, pl.getTileFromHand(
                                    tileIndex)))
                            {

                                discard(pl, tileIndex);
                                System.out.println("AI discarded " +
                                        "currently useless Tile");
                                break;
                            }
                        }
                    }
                }

                // Tile is part of a near-Chi
                else if (isAlmostChi(pl,
                        pl.getTileFromHand(tileIndex))) {

                    discard(pl, tileIndex);
                    System.out.println("AI discarded almost-Chi Tile");
                    break;
                }

                // Tile is part of a pair that can no longer pong
                else if (isPair(pl.getHandTile(),
                        pl.getTileFromHand(tileIndex)) && (numInDiscard
                        (pl.getTileFromHand(tileIndex)) == 2)) {

                    discard(pl, tileIndex);
                    System.out.println("AI discarded pair Tile");
                    break;
                }
        }
    }

    /*******************************************************************
     * A helper method that finds how many of a certain type of Tile is
     * in the discard pile.
     * @param tile indicated Tile
     * @return the number of the indicated Tile that is in the 
     * discard pile
     ******************************************************************/
    private int numInDiscard(Tile tile){

        int count = 0;

        for (int i = 0; i < discardPile.size(); i++){
            if (compareTile(discardPile.get(i), tile)){
                count++;
            }
        }

        return count;
    }

    /******************************************************************
     * This method explains the rules of the game.
     *
     * @return The rules of the game.
     *****************************************************************/
    public String ruleBook() {

        return starting() + setRule() + claimChiRule()
                + claimPongRule() + claimKongRule() + declareMahjong()
                + scoring() + gameMode();
    }

    /*******************************************************************
     * This method explains the introduction of the rules of the game.
     *
     * @return The introduction of the rules of the game.
     ******************************************************************/
    private String starting() {

        return "General Rules: \n"
                + "Players start with 13 tiles each. Each player "
                + "is assigned a wind (East-South-West-North). "
                + "East starts the game by picking a tile from the "
                + "wall and discarding a tile.\nPlayers then take"
                + " clockwise turns picking a tile from "
                + "the wall and then discarding one tile from the hand."
                + " It is also possible to claim a tile discarded by "
                + "another player\n under certain circumstances. "
                + "In such cases, the player to the left of the "
                + "claiming player becomes next in turn. So some "
                + "players may lose their turn in a go-around.\n\n";
    }

    /*******************************************************************
     * This explains the rules about sets.
     *
     * @return The rules about sets.
     ******************************************************************/
    private String setRule() {

        return "Rules on Sets: \n"
                + "A set of tiles consist of 3 tiles. This 3 tiles "
                + "may all be the same, thus forming a 3 of a kind or "
                + "they all form a 3 tile straight of the same "
                + "suit.\nAll sets that are formed in the hand by "
                + "drawing do not have to be revealed to the opposing "
                + "players. However, any set or kong that are claimed "
                + "must be revealed\nto all opposing players."
                + "It is important to know that a kong is consider as"
                + "1 set. Once a player "
                + "declares that they can from a set, they move the "
                + "tiles that they used\nto from a set along with the "
                + "recently discarded tile to the set pile. Then, the "
                + "player who claimed the set discards a tile. \nThe"
                + "next player turn is the player that is next in "
                + "rotation of the player that discarded the tile.\n\n";
    }

    /*******************************************************************
     * This method explains the rules about claiming Chi.
     *
     * @return The rules about claiming Chi.
     ******************************************************************/
    private String claimChiRule() {

        String msg = "Claiming Chi:\n"
                + "A chi can only be claimed when the opposing "
                + "player directly to right discards a "
                + "tile. In addition, the discarded the tile must be "
                + " able to use to form a 3 tile straight\n in the "
                + "players hand using the same suit. Also, the straight"
                + "can not extend from 9 to 1 or vise versa and"
                + " can not be done with dragon or wind tiles.\n\n";

        return msg;
    }

    /*******************************************************************
     * This method explain the rules about claiming pongs.
     *
     * @return The rules about claiming pongs.
     ******************************************************************/
    private String claimPongRule() {

        return "Claiming Pong:\n"
                + "A pong can be claimed when any opposing player"
                + " discards a tile and you have a 2 tiles in your hand"
                + " that can be used to form a 3 of a kind with the "
                + "discarded tile.\n\n";
    }

    /*******************************************************************
     * This method explain the rules about claiming kongs.
     *
     * @return The rules about claming kongs.
     ******************************************************************/
    private String claimKongRule() {

        return  "A kong can be claimed when any opposing player"
                + " discards a tile and you have a 3 tiles in your hand"
                + " that can be used to form a 4 of a kind with the "
                + "discarded tile.\nIn addition, the player must draw a "
                + "new tile from the main wall before discarding a"
                + " tile. In addition, a kong can not remain in "
                + "the hand.\nInstead the kong that forms by drawing "
                + "must be moved to the"
                + "set pile or discard a tile to break up the kong. "
                + "A special rule with Kong is that you can form a\n"
                + "kong with a pong in your set pile.\n\n";
    }

    /*******************************************************************
     * This method explains the rules on how to declare Mahjong.
     *
     * @return The rules on how to declare Mahjong.
     ******************************************************************/
    private String declareMahjong() {

        return  "Declaring Mahjong: \n"
                + "A mahjong can be claimed when a player has "
                + "all sets(chi,pong,kong), any point tiles and a "
                + "single pair in their hand or set pile combined. "
                + "\nIn addition, there can not be a kong that "
                + "in the declared players hand. At this point, the "
                + "player that declares Mahjong wins and their score\n"
                + "will be calculated based on the scoring rules.\n\n";
    }

    /*******************************************************************
     * This method explains the rules about scoring.
     *
     * @return The rules about scoring.
     ******************************************************************/
    private String scoring() {

        return "Scoring: \n"
                + "Once a player has declared mahjong, they win. "
                + "The winning player will receive of a score of 1 "
                + "point and 1 additional point for every dragon, wind"
                + " and flower tile\nthat is in the set pile for"
                + "game option 1. Game option 2 sets scores based"
                + "on the players hand and set pile when the "
                + "declare Mahjong.";
    }

    /*******************************************************************
     * This method explains the rules about the 2 different game modes.
     *
     * @return The rules about the game modes.
     ******************************************************************/
    private String gameMode() {

        return  "Game Mode: \n"
                + "The simple game mode automatically moves all non"
                + "suit tiles to the set pile and is treated as a "
                + "point.\nThe more traditional game mode only moves"
                + "flower tiles to the set pile. This will allow "
                + "for pongs to be made with dragon and wind tiles";
    }

    /*******************************************************************
     * This method gets the index of the current player.
     *
     * @return THe index of the current player
     ******************************************************************/
    public int getCurrentPlayerIndex() {

        return currentPlayer;
    }

    /*******************************************************************
     * This method get the current player.
     *
     * @return The current player.
     ******************************************************************/
    public Player getCurrentPlayer(){

        return playerList[currentPlayer];
    }

    /*******************************************************************
     * This method gets the starting player.
     * @return The starting player index.
     ******************************************************************/
    public int getStartingPlayer() {
        return startingPlayer;
    }

    /*******************************************************************
     * This method gets the player based off the player list.
     *
     * @param playerNum THe index of the player.
     * @return The player based of the index.
     ******************************************************************/
    public Player getPlayerList(int playerNum) {

        if (playerNum < 0 || playerNum > 4){

            throw new IndexOutOfBoundsException("Index is out of "
                    + "bounds. Must be from 0 - 3");
        }

        return playerList[playerNum];
    }

    /*******************************************************************
     * This method gets the player hand.
     *
     * @param playerNum The index of the player.
     * @return The players hand based off the players index.
     ******************************************************************/
    public ArrayList<Tile> getPlayerHand(int playerNum){

        if (playerNum < 0 || playerNum >= 4){

            throw new IndexOutOfBoundsException("Index is out of "
                    + "bounds. Must be from 0 - 3");
        }

        return playerList[playerNum].getHandTile();
    }

    /*******************************************************************
     * This method gets the entire discard pile.
     *
     * @return The discard pile.
     ******************************************************************/
    public ArrayList<Tile> getDiscardPile() {

        return discardPile;
    }

    /*******************************************************************
     * This method gets the most recently discarded tile.
     *
     * @return The most recently discarded tile.
     ******************************************************************/
    public Tile getRecentDiscard(){

        if (discardPile.size() == 0){

            throw new NullPointerException("There are "
                    + "no tiles in the discard pile.");
        }

        return discardPile.get(discardPile.size() - 1);
    }

    /*******************************************************************
     * This method gets the draw pile/main deck.
     *
     * @return The tiles that are in the draw pile/ main deck.
     ******************************************************************/
    public ArrayList<Tile> getDrawPile(){

        return tiles;
    }

    /******************************************************************
     * This method sets the next starting player.
     ******************************************************************/
    public void setNextStartingPlayer() {

        startingPlayer = (startingPlayer + 1) % TOTALPLAYER;
    }

    /*******************************************************************
     * This method sets the next current player.
     ******************************************************************/
    public void setNextCurrentPlayer() {

        currentPlayer = (currentPlayer + 1) % TOTALPLAYER;
        turnCount++;
    }

    /*******************************************************************
     * This method sets the next player based on index.
     *
     * @param player Index of the next player in the sequence
     ******************************************************************/
    public void setNextCurrentPlayer(int player){

        if (player >= TOTALPLAYER || player < 0){

            throw new IllegalArgumentException("Invalid Player Index");
        }
        currentPlayer = player;
    }

    /*******************************************************************
     * This method gets what game mode the game is currently in.
     * @return The game mode
     ******************************************************************/
    public boolean getGameOptionSimple() {
        return gameOptionSimple;
    }

    /*******************************************************************
     * This method sets what type of style the game is played in.
     * @param mode True for simple game mode, false for complicated or
     *             more traditional format.
     ******************************************************************/
    public void setGameOptionSimple(boolean mode){

        gameOptionSimple = mode;
    }

    /*******************************************************************
     * This method sets the difficulty of the AI.
     * @param difficulty Difficult of the AI.
     * @param playerIndex Which AI at the player index.
     ******************************************************************/
    public void setAIDiff(int difficulty, int playerIndex) {

        if (difficulty < Game.DUMB || difficulty > Game.ADVANCE) {

            throw new IllegalArgumentException("Difficulty " +
                    "setting not Excepted.");
        }

        if (playerIndex <= 0 || playerIndex > AIDiff.length) {

            throw new IllegalArgumentException("Index of Player is" +
                    "not an AI");
        }

        AIDiff[playerIndex - 1] = difficulty;
    }
}
