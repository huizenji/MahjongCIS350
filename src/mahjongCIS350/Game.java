package MahjongCIS350;

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

    /** All Tiles that have been discard by players**/
    private ArrayList<Tile> discardPile;

    /** The max amount of Tiles in Mahjong **/
    private int maxTile;

    /** Index of current Player to indicate turn order **/
    private int currentPlayer;

    /** Index of Player who has the "East" direction **/
    private int startingPlayer;

    /** List of all Players **/
    Player[] playerList;

    /** Total number of players per game **/
    private int TOTAL_PLAYER = 4;

    /** The total amount of turns that have gone by**/
    private int turnCount = 0;

    /** A static method that has been used for testing purposes **/
    public static void main(String[] args) {

        Game test = new Game();
        ArrayList<Tile> hand = new ArrayList<>();
        ArrayList<Tile> desired = new ArrayList<>();

        Suit tile1 = new Suit();
        tile1.setValue(1);
        tile1.setDesign("1");

        Suit tile2 = new Suit();
        tile2.setValue(2);
        tile2.setDesign("2");

        Dragon tile3 = new Dragon("3");
        Wind tile4 = new Wind("4");

        hand.add(tile1);
        hand.add(tile2);
        hand.add(tile3);
        hand.add(tile3);
        hand.add(tile4);

        //desired.add(tile1);
        desired.add(tile3);
        desired.add(tile3);

        System.out.println(test.findTile(hand,desired));
    }

    /*******************************************************************
     * This is the constructor for the Game class.
     ******************************************************************/
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
     * Whenever Player draws a Tile, if it's a point Tile then score
     * increase by 1. If there's a point Tile in hand, also increase
     * the score by 1.
     * @param p The player.
     ******************************************************************/
    public void pile_score(Player p){

        int s = 0;

        for(int i = 0; i <= p.getHandTile().size(); ++i){

            if(isPointTile(p.getTileFromHand(i))){

                s++;
            }
        }

        p.setPoint(p.getPoint() + s);
    }

    /*******************************************************************
     * A getter function for individual Tiles.
     *
     * @param index Index of the tile.
     * @return Tile at indicated index
     ******************************************************************/
    public Tile getTile(int index) {

        return tiles.get(index);
    }

    /*******************************************************************
     * This method creates all the players in the game and randomly
     * assigns one of the player to be the starting player.
     ******************************************************************/
    private void setupPlayer() {

        playerList = new Player[TOTAL_PLAYER];

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

            for (int j = 0; j < TOTAL_PLAYER; j++) {

                rotatePlayerDir(playerList[j]);
            }
        }

        // Find the starting player index
        startingPlayer = randVal % TOTAL_PLAYER;
        currentPlayer = startingPlayer;
    }

    /*******************************************************************
     * This method rotates the wind direction of each Player.
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
        dealTile_13();
        removeKongHand();
    }

    /*******************************************************************
     * This method deals out 13 Suit Tiles to three Players, and 14
     * Tiles to the East Player
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

    /*******************************************************************
     * If a Player draws a point Tile, then the Tile is moved to their
     * set pile and they draw a new Tile to replace it. This is done
     * until all Tiles in a Player's hand are Suits.
     ******************************************************************/
    private void replacePointTile() {

        // Looping through each players hand
        for (int index = 0; index < 4; index++) {

            // Keep replacing tile until no point tile is in hand
            for (int i = 0; i < playerList[index].
                    getHandTile().size(); i++) {

                // Check tile if it is a point tile
                if (!(playerList[index].getHandTile().get(i)
                        instanceof Suit)) {

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

        int kongIndex;

        for (int i = 0; i < playerList.length; i++) {

            while (isKongHand(playerList[i]) != -1) {

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
     * This method determines if the Player has a kong in their hand.
     *
     * @param pl The player hands that is being checked.
     * @return It will return -1 if there is no Kong, but it will
     *         return the starting index of where the Kong starts if
     *         there is a Kong.
     ******************************************************************/
    private int isKongHand(Player pl) {

        ArrayList<Tile> hand = pl.getHandTile();
        boolean temp = true;

        for (int i = 0; i < pl.getHandTile().size() - 4; i++) {

            for (int k = 1; k < 4; k++) {

                if (!(compareSuit((Suit) (hand.get(i)),
                        (Suit) (hand.get(i + k))))) {

                    temp = false;
                }
            }

            // Checking if there is a kong
            if (temp) {

                return i;
            }

            temp = true;
        }

        return -1;
    }

    /*******************************************************************
     * To check if there is chi for a specific Player.
     *
     * @param pl The player that is being checked.
     * @param check the checked_Suite should belongs to the last
     *              player of the current player
     * @return True if the player can claim a chi.
     ******************************************************************/
    public boolean isChi(Player pl, Suit check) {

        ArrayList<Tile> plHand = pl.getHandTile();

        if (currentPlayer != 3){
            return false;
        }

        for (int i = 0; i < plHand.size(); i++){

            for (int j = i; j < plHand.size(); j++){

                if (i != j){

                    Suit suit1 = (Suit)plHand.get(i);
                    Suit suit2 = (Suit)plHand.get(j);

                    if (compareConsecutiveSuits(suit1, suit2, check)){

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
    public boolean isPong(ArrayList<Tile> handTile, Tile check) {

        // Number of Tiles in Player hand that can used for a pong
        int matchTile = 0;

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
            if (tile1.getType() == "Wind") {

                if (((Wind) tile1).getDirection().equals(((Wind) tile2).
                        getDirection())) {

                    return true;

                }
            }

            //If the tiles are Dragons, check color match
            if (tile1.getType() == "Dragon") {

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

        if (check != null){

            temp.add(check);
            temp = autoSort(temp);
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

        // This section checks for any chi in the players hand
        for (int i = 0; i < temp.size(); i++){

            outer:
            for (int j = i + 1; j < temp.size(); j++){

                for (int k = j + 1; k < temp.size(); k++){

                    Suit a = (Suit)temp.get(i);
                    Suit b = (Suit)temp.get(j);
                    Suit c = (Suit)temp.get(k);

                    if (compareConsecutiveSuits(a, b, c)){

                        temp.remove(k);
                        temp.remove(j);
                        temp.remove(i);
                        i--;
                        break outer;
                    }
                }
            }
        }

        // If a player has 2 tiles remaining and they are the same,
        // then they have mahjong.
        if (temp.size() == 2 && compareTile(temp.get(0), temp.get(1))){

            return true;
        }

        return false;
    }

    /******************************************************************
     * This method allows a player to claim a chi.
     *
     * @param pl The player that claimed the chi.
     * @param discard The tile that is discarded to claim the chi.
     *****************************************************************/
    public void takeChi(Player pl, Tile discard){

        ArrayList<Tile> desired = new ArrayList<>();

        outloop:
        for (int i = 0; i < pl.getHandTile().size(); i++){

            for (int j = i + 1; j < pl.getHandTile().size(); j++){

                if (i != j){

                    Suit suit1 = (Suit) pl.getHandTile().get(i);
                    Suit suit2 = (Suit) pl.getHandTile().get(j);

                    if (compareConsecutiveSuits(suit1,suit2,
                            (Suit)discard)){

                        desired.add(suit1);
                        desired.add(suit2);
                        break outloop;
                    }
                }
            }
        }

        ArrayList<Integer> loc = findTile(pl.getHandTile(), desired);

        for (int i = loc.size() - 1; i >= 0; i--) {

            pl.removeTileSet(loc.get(i));
        }

        pl.addTileSet(discardPile.remove(discardPile.size() - 1));
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
     * @param pl The player that is claiming the kong.
     * @param tile The tile that is being used to claim the kong.
     ******************************************************************/
    public void takeKong(Player pl, Tile tile){

        // Add Tile and remove Kong
        pl.getHandTile().add(tile);
        removeKongHand();

        // draw Tile and remove any Kong
        draw(pl);
        removeKongHand();
    }

    /*******************************************************************
     * This method finds the desired Tiles of an ArrayList and returns
     * the index from the Player's hand that they are located at.
     *
     * @param playerHand The player's hand that is being searched.
     * @param desired The tiles that are searched in the players hand
     *                as an arraylist.
     * @return The indexs where the searched tiles are found.
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
     * This method sorts the players hand with by circle, bamboo, and
     * character with the value incremented.
     *
     * @param player The player hand that is being sorted.
     ******************************************************************/
    private void autoSort(Player player) {

        ArrayList<Tile> temp = new ArrayList<>();

        // Suit that will be placed first
        Suit comp = new Suit();

        // Tile from player that is being checked
        Suit playerTile;

        // Adding Circle Tiles
        comp.setDesign("Circle");

        for (int value = 1; value <= 9; value++) {

            for (int i = 0; i < player.getHandTile().size(); i++) {

                playerTile = (Suit) player.getHandTile().get(i);
                comp.setValue(value);

                if (compareSuit(comp, playerTile)) {

                    temp.add(playerTile);
                }
            }
        }

        // Adding Bamboo Tiles
        comp.setDesign("Bamboo");

        for (int value = 1; value <= 9; value++) {

            for (int i = 0; i < player.getHandTile().size(); i++) {

                playerTile = (Suit) player.getHandTile().get(i);
                comp.setValue(value);

                if (compareSuit(comp, playerTile)) {

                    temp.add(playerTile);
                }
            }
        }

        // Adding Character Tiles
        comp.setDesign("Character");

        for (int value = 1; value <= 9; value++) {

            for (int i = 0; i < player.getHandTile().size(); i++) {

                playerTile = (Suit) player.getHandTile().get(i);
                comp.setValue(value);

                if (compareSuit(comp, playerTile)) {

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

        // Suit that will be placed first
        Suit comp = new Suit();

        // Tile from Player that is being checked
        Suit playerTile;

        // Adding Circle Tiles
        comp.setDesign("Circle");

        for (int value = 1; value <= 9; value++) {

            for (int i = 0; i < hand.size(); i++) {

                playerTile = (Suit) hand.get(i);
                comp.setValue(value);

                if (compareSuit(comp, playerTile)) {

                    temp.add(playerTile);
                }
            }
        }

        // Adding Bamboo Tiles
        comp.setDesign("Bamboo");

        for (int value = 1; value <= 9; value++) {

            for (int i = 0; i < hand.size(); i++) {

                playerTile = (Suit) hand.get(i);
                comp.setValue(value);

                if (compareSuit(comp, playerTile)) {

                    temp.add(playerTile);
                }
            }
        }

        // Adding Character Tiles
        comp.setDesign("Character");

        for (int value = 1; value <= 9; value++) {

            for (int i = 0; i < hand.size(); i++) {

                playerTile = (Suit) hand.get(i);
                comp.setValue(value);

                if (compareSuit(comp, playerTile)) {

                    temp.add(playerTile);
                }
            }
        }

        // Setting the sorted hand to Player
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
        boolean[] visited = new boolean[3];

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
    private boolean isPointTile(Tile tile) {

        if (tile instanceof Suit) {

            return false;

        } else if (tile instanceof Dragon || tile instanceof Wind ||
            tile instanceof Flower) {

            return true;

        } else {

            return false;
        }
    }

    /*******************************************************************
     * This method draws a tile from the main wall/tiles. It will then
     * draw another tile if the original tile draw is a point tile.
     *
     * @param pl The current players turn.
     ******************************************************************/
    public void draw(Player pl) {

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
     * @param pl The player that is discarding the tile.
     * @param tileIndex The tile index in the selected players hand.
     ******************************************************************/
    public void discard(Player pl, int tileIndex) {

        if (tileIndex >= pl.getHandTile().size() || tileIndex < 0) {

            throw new IndexOutOfBoundsException("tile index is out of" +
                    " bounds.");
        }

        discardPile.add(pl.getHandTile().remove(tileIndex));
        autoSort(pl);
    }

    public boolean isStalemate(){

        for (int i = 0; i < tiles.size();i++){

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

            throw new IllegalArgumentException("Player can not be " +
                    "Null");
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
     * @param pl The player whose action will be determined by an AI.
     ******************************************************************/
    public void dumbAIDiscard(Player pl) {

        if (pl == null){

            throw new IllegalArgumentException("Player can not be " +
                    "Null");
        }

        Random rand = new Random();
        discard(pl, rand.nextInt(pl.getHandTile().size()));
    }

    /******************************************************************
     * This method explains the rules of the game.
     *
     * @return The rules of the game.
     *****************************************************************/
    public String ruleBook() {

        String rules = starting() + setRule() + claimChiRule() +
                claimPongRule() + claimKongRule() + declareMahjong()
                + scoring();

        return rules;
    }

    /*******************************************************************
     * This method explains the introduction of the rules of the game.
     *
     * @return The introduction of the rules of the game.
     ******************************************************************/
    private String starting() {

        String msg = "Players start with 13 tiles each. Each player " +
                "is assigned a wind (East-South-West-North). " +
                "East starts the game by picking a tile from the " +
                "wall and discarding a tile. Players then take" +
                " clockwise turns picking a tile from " +
                "the wall and then discarding one tile from the hand." +
                " It is also possible to claim a tile discarded by " +
                "another player under certain circumstances. " +
                "In such cases, the player to the left of the " +
                "claiming player becomes next in turn. So some " +
                "players may lose their turn in a go-around.\n\n";

        return msg;
    }

    /*******************************************************************
     * This explains the rules about sets.
     *
     * @return The rules about sets.
     ******************************************************************/
    private String setRule() {

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

    /*******************************************************************
     * This method explains the rules about claiming Chi.
     *
     * @return The rules about claiming Chi.
     ******************************************************************/
    private String claimChiRule() {

        String msg = "A chi can only be claimed when the opposing " +
                "player directly to right discards a " +
                "tile. In addition, the discarded the tile must be " +
                " able to use to form a 3 tile straight in the " +
                "players hand using the same suit. Also, the straight" +
                "can not extend from 9 to 1 or vise versa.\n\n";

        return msg;
    }

    /*******************************************************************
     * This method explain the rules about claiming pongs.
     *
     * @return The rules about claiming pongs.
     ******************************************************************/
    private String claimPongRule() {

        String msg = "A pong can be claimed when any opposing player" +
                " discards a tile and you have a 2 tiles in your hand" +
                " that can be used to form a 3 of a kind with the " +
                "discarded tile.\n\n";

        return msg;
    }

    /*******************************************************************
     * This method explain the rules about claiming kongs.
     *
     * @return The rules about claming kongs.
     ******************************************************************/
    private String claimKongRule() {

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

    /*******************************************************************
     * This method explains the rules on how to declare Mahjong.
     *
     * @return The rules on how to declare Mahjong.
     ******************************************************************/
    private String declareMahjong() {

        String msg = "A mahjong can be claimed when a player has " +
                "all sets(chi,pong,kong), any point tiles and a " +
                "single pair in their hand or set pile combined." +
                "In addition, there can not be a kong that " +
                "in the declared players hand. At this point, the " +
                "player that declares Mahjong wins and their score" +
                "will be calculated based on the scoring rules.\n\n";

        return msg;
    }

    /*******************************************************************
     * This method explains the rules about scoring.
     *
     * @return The rules about scoring.
     ******************************************************************/
    private String scoring() {

        String msg = "Once a player has declared mahjong, they win. " +
                "The winning player will receive of a score of 1 " +
                "point and 1 additional point for every dragon, wind" +
                "and flower tile that is in the set pile.";

        return msg;
    }

    /*******************************************************************
     * This method gets the index of the current player.
     *
     * @return THe index of the current player
     ******************************************************************/
    public int getCurrentPlayerIndex() {

        return currentPlayer;
    }

    /******************************************************************
     * This method get the current player.
     *
     * @return The current player.
     *****************************************************************/
    public Player getCurrentPlayer(){

        return playerList[currentPlayer];
    }

    /******************************************************************
     * This method gets the starting player index.
     *
     * @return The starting player index.
     *****************************************************************/
    public int getStartingPlayer() {

        return startingPlayer;
    }

    /******************************************************************
     * This method sets the starting player based on index value.
     *
     * @param startingPlayer The index of the starting player.
     *****************************************************************/
    public void setStartingPlayer(int startingPlayer) {

        this.startingPlayer = startingPlayer;
    }

    /******************************************************************
     * This method sets the next starting player.
     *****************************************************************/
    private void setNextStartingPlayer() {

        startingPlayer = (startingPlayer + 1) % 4;
    }

    /******************************************************************
     * This method sets the next current player.
     *****************************************************************/
    public void setNextCurrentPlayer() {

        currentPlayer = (currentPlayer + 1) % 4;
        turnCount++;
    }

    /******************************************************************
     * This method sets the next player based on index.
     *
     * @param pl Index of the next player in the sequence
     *****************************************************************/
    public void setNextCurrentPlayer(int pl){

        currentPlayer = pl;
    }

    /******************************************************************
     * This method gets the player based off the player list.
     *
     * @param playerNum THe index of the player.
     * @return The player based of the index.
     *****************************************************************/
    public Player getPlayerList(int playerNum) {

        return playerList[playerNum];
    }

    /******************************************************************
     * This method gets the player hand.
     *
     * @param playerNum The index of the player.
     * @return The players hand based off the players index.
     *****************************************************************/
    public ArrayList<Tile> getPlayerHand(int playerNum){

        if (playerNum < 0 || playerNum > 4){

            throw new IndexOutOfBoundsException("Index is out of " +
                    "bounds. Must be from 0 - 3");
        }

        return playerList[playerNum].getHandTile();
    }

    /******************************************************************
     * This method gets the entire discard pile.
     *
     * @return The discard pile.
     *****************************************************************/
    public ArrayList<Tile> getDiscardPile() {

        return discardPile;
    }

    /*******************************************************************
     * This method sets the discard Pile.
     *
     * @param discardPile The tiles that are to be set ot the
     *                   discard pile.
     ******************************************************************/
    public void setDiscardPile(ArrayList<Tile> discardPile) {

        this.discardPile = discardPile;
    }

    /******************************************************************
     * This method gets the most recently discarded tile.
     *
     * @return The most recently discarded tile.
     *****************************************************************/
    public Tile getRecentDiscard(){

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
}

