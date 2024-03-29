package mahjongCIS350;

import java.util.*;

/***********************************************************************
 * This class contains all pieces and rule functionality for the game
 * Mahjong. It is displayed by the Board and GUI classes.
 *
 * @Authors: Wayne Chen, Jillian Huizenga, Chris Paul, Xianghe Zhao
 * @Version: 4/08/2020
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
    public static final int ADVANCED = 3;

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
    public void reset() {

        for(int i = 0; i < 4; i++) {

            playerList[i].clearHandPile();
            playerList[i].clearSetPile();
        }

        tiles.clear();
        discardPile.clear();
        createTile();
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

            autoSort(playerList[i]);
            kongTile = isKongHand(playerList[i]);
            while (kongTile != null) {

                // Remove the Entire Kong in Hand
                for (int k = playerList[i].getHandTile().size() - 1;
                     k >= 0 ; k--) {

                    if (compareTile(playerList[i].getHandTile().get(k),
                            kongTile)){

                        playerList[i].removeTileSet(k);
                    }
                }

                // reset till there is no more kongs
                // Draw 1 after Removal and autosort
                draw(playerList[i]);
                autoSort(playerList[i]);

                // Check to see if there is another Kong
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
    private Tile isKongHand(final Player player) {

        ArrayList<Tile> hand = player.getHandTile();
        Tile kong = null;

        for (int i = 0; i < hand.size(); i++) {

            for (int j = i + 1; j < hand.size(); j++) {

                for (int k = j + 1; k < hand.size(); k++) {

                    for (int l = k + 1; l < hand.size(); l++) {

                        if (compareTile(hand.get(i),
                                hand.get(j)) && compareTile(
                                hand.get(i), hand.get(k))
                                && compareTile(hand.get(i),
                                hand.get(l))) {

                           kong = hand.get(i);
                        }
                    }
                }
            }
        }

        return kong;
    }

    /*******************************************************************
     * This method sets the AI to the basic level (beginner)
     ******************************************************************/
    private void setupAIDiff() {

        AIDiff = new int[TOTALPLAYER - 1];

        for (int i = 0; i < AIDiff.length; i++) {

            AIDiff[i] = BEGINNER;
        }
    }

    /*******************************************************************
     * This method increases the score of a player based on the mode
     * the game is set in.
     * @param plIndex The player index who score is increased.
     * @param discard True if the player has won off a discard.
     ******************************************************************/
     public void increaseScore(final int plIndex ,
                               final boolean discard) {

        if (plIndex < 0 || plIndex >= TOTALPLAYER) {

            throw new IllegalArgumentException("Player Index does not"
                    + " exist");
        }

        if (gameOptionSimple){

            scoreSimple(playerList[plIndex]);
        }

        else {

            scoreTrad(playerList[plIndex], discard);
        }
     }

    /*******************************************************************
     * This methods increases a players score based on the number of
     * point tiles the player has.
     * @param player The player who score is increased.
     ******************************************************************/
    private void scoreSimple(final Player player) {

        int point = player.getPoint();

        for(int i = 0; i < player.getSetPile().size(); i++) {

            if (isPointTile(player.getSetTile(i))) {

                point++;
            }
        }

        player.setPoint(point);
    }

    /*******************************************************************
     * This methods increase the players score based on the set of chi
     * , pongs, and kongs in their hand and set pile.
     * @param player The player who score is increased.
     * @param discard True if the player has won off a discard.
     ******************************************************************/
    private void scoreTrad(final Player player,
                            final boolean discard) {

        int point = player.getPoint();

        ArrayList<Tile> handCopy = new ArrayList<>();
        ArrayList<Tile> setCopy = new ArrayList<>();

        // Making Copies of Hand and Set Pile
        for (Tile t: player.getHandTile()) {

            handCopy.add(t);
        }

        for (Tile t: player.getSetPile()) {

            setCopy.add(t);
        }


        int score = scoreSet(player.getSetPile())
                + scoreHand(player.getHandTile() , discard);


        // Double Score of Player if they meet Conditions
        // Did no win off of discard
        if (!discard) {

            score = score * 2;
        }

        // Remove all Flower tiles from the set pile
        for (int i = setCopy.size() - 1; i >= 0; i--) {

            if (setCopy.get(i) instanceof Flower) {

                setCopy.remove(i);
            }
        }

        if (allSame(handCopy ,setCopy, discard)) {

            score = score * 2;
        }

        player.setPoint(point + score);
    }

    /*******************************************************************
     * The methods scores the set pile.
     * @param setPile The score of the set pile
     * @return The score of the set pile
     ******************************************************************/
    private int scoreSet(final ArrayList<Tile> setPile) {

        int point = 0;

        // Scan Through Set Pile First
        for (int i = setPile.size() - 1; i >= 0; i--) {

            // Remove Flower Tile and add 1 Point
            if (setPile.get(i) instanceof Flower) {

                setPile.remove(i);
                point++;
            }
        }

        // Check for Any Kongs and Removes them and adds Score
        for (int i = 0; i < setPile.size() - 3; i++) {

            if (compareTile(setPile.get(i), setPile.get(i + 1))
                && compareTile(setPile.get(i), setPile.get(i + 2))
                && compareTile(setPile.get(i), setPile.get(i + 3))) {

                if (setPile.get(i) instanceof Suit) {

                    point = point + 2;

                } else {

                    point = point + 4;
                }

                // Remove Tiles
                setPile.remove(i + 3);
                setPile.remove(i + 2);
                setPile.remove(i + 1);
                setPile.remove(i);
                i--;
            }
        }

        return point + scoreChiPong(setPile);
    }

    /*******************************************************************
     * This method finds the score of the player.
     * @param hand The hand of the player.
     * @param discard True if the player has won off a discard.
     * @return The score of the player hand.
     ******************************************************************/
    private int scoreHand(final ArrayList<Tile> hand,
                          final boolean discard) {

        ArrayList<Tile> temp = new ArrayList<>();

        // The combination that got the player a Mahjong
        for (int index = 0; index < hand.size(); index++) {

            temp.add(hand.get(index));
        }

        // Add the most recent Discard if won off of discard
        if (discard) {

            temp.add(getRecentDiscard());
        }

        return scoreChiPong(findHandComb(temp));
    }

    /*******************************************************************
     * This methods finds the hand combination that gets the player the
     * mahjong.
     * @param hand Hand of the Player
     * @return The Tile in order of the Combination
     ******************************************************************/
    private ArrayList<Tile> findHandComb(final ArrayList<Tile> hand) {

        ArrayList<Tile> temp = new ArrayList<>();
        ArrayList<Tile> handComb = new ArrayList<>();

        Boolean validComb = false;

        // Copy of Hand
        for (int index = 0; index < hand.size(); index++) {

            temp.add(hand.get(index));
        }

        if (hand.size() == 2) {

            return temp;
        }

        // Go until there is only 1 pair
        for (int i = 0; i < temp.size(); i++) {

            for (int j = i + 1; j < temp.size(); j++) {

                for (int k = j + 1; k < temp.size(); k++) {

                    // Remove Tiles and see if it still is a Mahjong
                    Tile tile1 = temp.remove(k);
                    Tile tile2 = temp.remove(j);
                    Tile tile3 = temp.remove(i);

                    handComb.add(tile1);
                    handComb.add(tile2);
                    handComb.add(tile3);

                    // The combination picked should be chi or pong
                    if ((compareTile(tile1, tile2) &&
                            compareTile(tile1, tile3))) {

                        validComb = true;
                    }

                    if (tile1 instanceof Suit && tile2 instanceof Suit
                        && tile3 instanceof Suit) {

                        if (compareConsecutiveSuits((Suit) tile1,
                                (Suit) tile2, (Suit) tile3)) {

                            validComb = true;
                        }
                    }

                    // Make sure it is still a Mahjong
                    if (isMahjong(temp, null) && validComb) {

                        // reset Counters
                        i = 0;
                        j = i + 1;
                        k = j; // No Plus 1 to negate extra k
                    }

                    // Wrong Combination
                    else {

                        // Return the most recently added
                        temp.add(handComb.remove(
                                handComb.size() - 1));
                        temp.add(handComb.remove(
                                handComb.size() - 1));
                        temp.add(handComb.remove(
                                handComb.size() - 1));

                        temp = autoSort(temp);
                    }

                    validComb = false;
                }
            }
        }

        return handComb;
    }

    /*******************************************************************
     * This method finds the score of all chi and pongs.
     * @param tile The hand or set pile that is scored.
     * @return The score that consist of only chi and pongs.
     ******************************************************************/
    private int scoreChiPong (final ArrayList<Tile> tile) {

        int point = 0;

        if (tile.size() <= 2) {

            return 0;
        }

        // Scan Through and Total for Each chi and Pong
        for (int i = 0; i < tile.size(); i += 3) {

            Tile tile1 = tile.get(i);
            Tile tile2 = tile.get(i + 1);
            Tile tile3 = tile.get(i + 2);

            // Check for Pong and add points
            if (compareTile(tile1, tile2)
                    && compareTile(tile1, tile3)) {

                if (tile1 instanceof Suit) {

                    point++;

                } else {

                    point = point + 2;
                }
            }

            // Add points if it is a Chi
            else if (tile1 instanceof Suit
                    && tile2 instanceof Suit && tile3 instanceof Suit) {

                if (compareConsecutiveSuits((Suit) (tile1),
                        (Suit) (tile2), (Suit) (tile3))) {

                    if (((Suit) tile1).getValue() == 1
                            || ((Suit) tile2).getValue() == 1
                            || ((Suit) tile3).getValue() == 1) {

                        point = point + 2;

                    } else if (((Suit) tile1).getValue() == 9
                            || ((Suit) tile2).getValue() == 9
                            || ((Suit) tile3).getValue() == 9) {

                        point = point + 2;

                    } else {

                        point++;
                    }
                }
            }
        }

        return point;
    }

    /*******************************************************************
     * This method determines if the user won with all pongs, chi, and
     * kongs.
     * @param hand The hand of the player.
     * @param set The set pile of the player
     * @param discard True if the player has won off a discard.
     * @return True if won with one of these conditions.
     ******************************************************************/
    private boolean allSame(final ArrayList<Tile> hand,
                            final ArrayList<Tile> set,
                            final boolean discard) {

        // All Kongs and Flowers are removed due to previous call other
        // other methods

        ArrayList<Tile> copy = new ArrayList<>();
        ArrayList<Tile> temp = new ArrayList<>();

        if (discard) {

            copy.add(getRecentDiscard());
            temp.add(getRecentDiscard());
        }

        for (Tile tile : hand) {

            copy.add(tile);
            temp.add(tile);
        }

        for (Tile tile : set) {

            copy.add(tile);
            temp.add(tile);
        }

        // Check if All Pongs
        for (int i = 0; i < copy.size(); i++) {

            for (int j = i + 1; j < copy.size(); j++) {

                for (int k = j + 1; k < copy.size(); k++) {

                    // Remove Pong
                    if (compareTile(copy.get(i),
                            copy.get(j)) && compareTile(
                            copy.get(i), copy.get(k))) {

                        // If so, remove the pong from temp
                        if (compareTile(copy.get(i), copy.get(k))) {

                            copy.remove(k);
                            copy.remove(j);
                            copy.remove(i);

                            // Reset check Tiles
                            i = 0;
                            j = i + 1;
                            k = j + 1;
                        }
                    }
                }
            }
        }

        // If 2 Tiles left, then all pongs
        if (copy.size() == 2) {

            return true;
        }

        // If not redo and check for all Kongs
        copy.clear();
        for (Tile tile : temp) {

            copy.add(tile);
        }

        // Check if All Kongs
        for (int i = 0; i < copy.size(); i++) {

            for (int j = i + 1; j < copy.size(); j++) {

                for (int k = j + 1; k < copy.size(); k++) {

                    for (int l = k + 1; l < copy.size(); l++) {

                        // Remove Kong
                        if (compareTile(copy.get(i),
                                copy.get(j)) && compareTile(
                                copy.get(i), copy.get(k))
                                && compareTile(copy.get(i),
                                copy.get(l))) {

                            // If so, remove the Kong from temp
                            if (compareTile(copy.get(i),
                                    copy.get(k))) {

                                copy.remove(l);
                                copy.remove(k);
                                copy.remove(j);
                                copy.remove(i);

                                // Reset check Tiles
                                i = 0;
                                j = i + 1;
                                k = j + 1;
                                l = k + 1;
                            }
                        }
                    }
                }
            }
        }

        // If 2 Tiles left, then all Kongs
        if (copy.size() == 2) {

            return true;
        }

        // If not redo and check for all chi
        copy.clear();
        for (Tile tile : temp) {

            copy.add(tile);
        }

        for (int i = 0; i < copy.size(); i++) {

            for (int j = i + 1; j < copy.size(); j++) {

                for (int k = j + 1; k < copy.size(); k++) {

                    // Remove Chi
                    if (copy.get(i) instanceof Suit
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

                            // Reset check Tiles
                            i = 0;
                            j = i + 1;
                            k = j + 1;
                        }
                    }
                }
            }
        }

        // If 2 Tiles left, then all Chi
        if (copy.size() == 2) {

            return true;
        }

        return false;
    }

    /*******************************************************************
     * To check if there is chi for a specific Player.
     *
     * @param pl The player that is being checked.
     * @param check the checked_Suite should belongs to the last
     *              player of the current player
     * @return True if the player can claim a chi.
     ******************************************************************/
    public boolean isChi(final Player pl, final Tile check) {

        ArrayList<Tile> plHand = pl.getHandTile();

        if (check == null) {

            return false;
        }

        if (!(check instanceof Suit)) {

            return false;
        }

        for (int i = 0; i < plHand.size(); i++) {

            for (int j = i; j < plHand.size(); j++) {

                if (i != j) {

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
    public boolean isPong(final ArrayList<Tile> handTile,
                          final Tile check) {

        // Number of Tiles in Player hand that can used for a pong
        int matchTile = 0;

        if (check == null) {

            return false;
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
    private boolean compareTile(final Tile tile1, final Tile tile2) {

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
    public boolean isKong(final ArrayList<Tile> search,
                          final Tile check) {

        int numOfMatch = 0;

        if (check == null) {

            return false;
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
    public boolean isKongDraw(final Player player) {

        boolean kong = false;
        // Note when a tile is draw, the tile is in the last index in
        // the array.

        // Counter to keep track of the number of tiles that are the
        // same
        int totalTile = 0;

        if (player.getHandTile().size() == 0) {

            return false;
        }

        ArrayList<Tile> setPile = player.getSetPile();

        for (int i = 0; i < setPile.size(); i++) {

            if (compareTile(setPile.get(i), player.getHandTile().get(
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
    public boolean isMahjong(final ArrayList<Tile> handTile,
                             final Tile check) {

        // Make a copy of what is in the Player's hand
        ArrayList<Tile> temp = new ArrayList<>();

        for (Tile t : handTile) {

            temp.add(t);
        }

        if (check != null) {

            temp.add(check);
            temp = autoSort(temp);
        }

        return isMahjongBacktrackAlg(temp);
    }

    /*******************************************************************
     * This is an recursive Algorithm that determines if the user
     * has a Mahjong. This uses a backtracking algorithm.
     * @param hand The hand that is checked
     * @return True if the hand has a Mahjong, false if not.
     ******************************************************************/
    private boolean isMahjongBacktrackAlg(final ArrayList<Tile> hand) {

        // Make a copy of what is in the Player's hand
        ArrayList<Tile> temp = new ArrayList<>();
        ArrayList<Tile> copy = new ArrayList<>();

        boolean win = false;

        for (Tile t : hand) {

            temp.add(t);
            copy.add(t);
        }

        if (temp.size() == 2) {

            if (compareTile(temp.get(0), temp.get(1))) {

                return true;

            } else {

                return false;
            }
        }

        // Just in Case of an event occurring
        else if (temp.size() % 3 != 2) {

            return false;

        } else {

            // This section removes the first chi or pong in the players
            // hand, then applies the algorithm
            for (int i = 0; i < temp.size(); i++) {

                for (int j = i + 1; j < temp.size(); j++) {

                    for (int k = j + 1; k < temp.size(); k++) {

                        // Remove Pong
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
                        if (win) {

                            return win;
                        }

                        // Not a Winning Combination, So retry with a
                        // different combination
                        else {

                            copy.clear();

                            for (Tile t : temp) {

                                copy.add(t);
                            }
                        }
                    }
                }
            }

            return win;
        }
    }

    /*******************************************************************
     * This method finds the tiles the player can used to claim a chi.
     * @param pl The player that claimed the chi.
     * @param discard The tile that is discarded to claim the chi.
     * @return AN array list of integers the chi consisting of index.
     ******************************************************************/
     public ArrayList<Integer> getChiTile(final Player pl,
                                          final Tile discard) {

        ArrayList<Tile> desired = new ArrayList<>();
        ArrayList<Integer> loc = new ArrayList<>();

        // Return Empty Array if discard is not a Suit Tile
        if (!(discard instanceof Suit)) {

            return loc;
        }

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

            // Trying to find if tiles are the same
            if (loc.get(i) == loc.get(i + 2)
                    && loc.get(i + 1) == loc.get(i + 3)) {

                loc.remove(i);
                loc.remove(i);
                i = i - 2;

            } else if (loc.get(i) == loc.get(i + 3)
                    && loc.get(i + 1) == loc.get(i + 2)) {

                loc.remove(i + 2);
                loc.remove(i + 2);
                i = i - 2;
            }
        }

        return loc;
    }

    /*******************************************************************
     * This method allows the player to claim a chi. Note that the index
     * of tile2 must be higher than the index at tile 1.
     * @param player The player who claims the chi.
     * @param tile1 The tile index of the first tile that is used to
     *              the chi.
     * @param tile2 The tile index of the second tile that is used to
     *              claim the chi.
     ******************************************************************/
    public void takeChi(final Player player, final int tile1,
                        final int tile2) {

        if (tile1 == tile2) {

            throw new IllegalArgumentException("Can not discard"
                    + " same tile");
        }

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
    public void takePong(final Player pl, final Tile tile) {

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
    public void takeKong(final Player pl, final Tile tile) {

        ArrayList<Tile> desired = new ArrayList<>();
        desired.add(tile);
        desired.add(tile);
        desired.add(tile);

        ArrayList<Integer> loc = findTile(pl.getHandTile(), desired);

        for (int i = loc.size() - 1; i >= 0; i--) {

            pl.removeTileSet(loc.get(i));
        }

        pl.addTileSet(discardPile.remove(discardPile.size() - 1));
        draw(pl);
    }

    /*******************************************************************
     * This method allows the player to move a tile to the set pile
     * to form a Kong
     *
     * @param player Player who drew into a kong.
     ******************************************************************/
    public void takeKongDraw(final Player player) {

        // Add the tile from hand to setpile
        int lastLoc = player.getHandTile().size() - 1;
        player.getSetPile().add(player.getHandTile().remove(lastLoc));

        draw(player);
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
    private ArrayList<Integer> findTile(final ArrayList<Tile> playerHand,
                                        final ArrayList<Tile> desired) {

        ArrayList<Integer> index_loc = new ArrayList();

        for (int i = 0; i < desired.size(); i++) {

            search:

            for (int hand_index = 0; hand_index < playerHand.size();
                 hand_index++) {

                if (compareTile(desired.get(i),
                        playerHand.get(hand_index))) {

                    if (!index_loc.contains(hand_index)) {

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
    private ArrayList<Integer> findTileVer2(final ArrayList<Tile>
                                                    playerHand,
                                       final  ArrayList<Tile> desired) {

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
    private void autoSort(final Player player) {

        ArrayList<Tile> temp = new ArrayList<>();
        String[] dragonColor = {"Red", "Green", "White"};
        String[] windDir = {"North", "East", "South", "West"};

        // Tile from player that is being checked
        Tile playerTile;

        // Tiles created for comparision
        Dragon compD = new Dragon();
        Wind compW = new Wind();
        Suit compS = new Suit();

        for (int i = 0; i < dragonColor.length; i++) {

            compD.setColor(dragonColor[i]);

            for (int k = 0; k < player.getHandTile().size(); k++) {

                playerTile = player.getHandTile().get(k);

                if (compareTile(compD, playerTile)) {

                    temp.add(playerTile);
                }
            }
        }

        for (int i = 0; i < windDir.length; i++) {

            compW.setDirection(windDir[i]);

            for (int k = 0; k < player.getHandTile().size(); k++) {

                playerTile = player.getHandTile().get(k);

                if (compareTile(compW, playerTile)) {

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
    private ArrayList<Tile> autoSort(final ArrayList<Tile> hand) {

        ArrayList<Tile> temp = new ArrayList<>();
        String[] dragonColor = {"Red", "Green", "White"};
        String[] windDir = {"North", "East", "South", "West"};

        // Tile from player that is being checked
        Tile playerTile;

        // Tiles created for comparision
        Dragon compD = new Dragon();
        Wind compW = new Wind();
        Suit compS = new Suit();

        for (int i = 0; i < dragonColor.length; i++) {

            compD.setColor(dragonColor[i]);

            for (int k = 0; k < hand.size(); k++) {

                playerTile = hand.get(k);

                if (compareTile(compD, playerTile)) {

                    temp.add(playerTile);
                }
            }
        }

        for (int i = 0; i < windDir.length; i++) {

            compW.setDirection(windDir[i]);

            for (int k = 0; k < hand.size(); k++) {

                playerTile = hand.get(k);

                if (compareTile(compD, playerTile)) {

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
    private boolean compareSuit(final Suit tile1, final Suit tile2) {

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
    private boolean compareConsecutiveSuits(final Suit tile1,
                                            final Suit tile2,
                                            final Suit tile3) {

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

            } else {

                isPoint = true;
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
    private boolean isPair(final ArrayList<Tile> plHand,
                           final Tile tile) {

        int counter = 0;

        for (int i = 0; i < plHand.size(); i++) {

            if (compareTile(plHand.get(i), tile)) {

                counter++;
            }
        }

        if (counter == 2) {

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
    private boolean isAlmostChi(final Player pl, final Tile tile) {

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
                                        suit2,
                                        (Suit) tiles.get(check))) {

                                    if (compareTile(suit1, tile) ||
                                            compareTile(suit2, tile)) {

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

        if (isKong(pl.getHandTile(), drawn)) {

            pl.getHandTile().add(drawn);
            removeKongHand();

        } else {

            pl.getHandTile().add(drawn);
        }
    }

    /*******************************************************************
     * This methods removes a tile from hand based on the player and
     * the tile index in their hand.
     *
     * @param player The player that is discarding the tile.
     * @param tileIndex The tile index in the selected players hand.
     ******************************************************************/
    public void discard(final Player player, final int tileIndex) {

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
    public boolean isStalemate() {

        for (int i = 0; i < tiles.size(); i++) {

            if (!isPointTile(tiles.get(i))) {

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
    public void dumbAIDraw(final Player pl) {

        if (pl == null) {

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
    private void dumbAIDiscard(final Player player) {

        if (player == null) {

            throw new IllegalArgumentException("Player can not be "
                    + "Null");
        }

        Random rand = new Random();
        discard(player, rand.nextInt(player.getHandTile().size()));
    }

    /*******************************************************************
     * AI design to get rid of a tiles and draw tiles. This
     * method tells the AI to discard a random tile.
     *
     * @param pl The player whose action will be determined by
     *               an AI.
     ******************************************************************/
    private void beginnerAIDiscard(final Player pl) {

        if (pl == null) {

            throw new IllegalArgumentException("Player can not be "
                    + "Null");
        }

        Random rand = new Random();
        discard(pl, rand.nextInt(pl.getHandTile().size()));
    }

    /*******************************************************************
     * The discard sequence for the advanced AI option. This takes
     * into account advanced strategy, discarding Tiles that have the
     * least amount of use to the AI player, and takes the discard
     * pile into account.
     * @param pl the AI player that needs to discard.
     ******************************************************************/
    private void advancedAIDiscard(Player pl) {

        if (pl == null) {

            throw new IllegalArgumentException("Player can not be "
                    + "Null");
        }

        // check each Tile in hand one by one
        for (int tileIndex = 0; tileIndex < pl
                .getHandTile().size(); tileIndex++) {

            // Tile can no longer be used due to previous discards
            if (numInDiscard(pl.getHandTile()
                    .get(tileIndex)) == 3) {
                discard(pl, tileIndex);
                return;
            }
        }

        for (int tileIndex = 0; tileIndex < pl
                .getHandTile().size(); tileIndex++) {

            // Tile isn't part of an existing set, pair, or
            // almost-Chi
            if (!isPong(pl.getHandTile(),
                    pl.getTileFromHand(tileIndex))) {

                if (!isChi(pl, pl.getTileFromHand(tileIndex))) {

                    if (!isPair(pl.getHandTile(),
                            pl.getTileFromHand(tileIndex))) {

                        if (!isAlmostChi(pl, pl.getTileFromHand(
                                tileIndex))) {

                            discard(pl, tileIndex);
                            return;
                        }
                    }
                }
            }
        }

        for (int tileIndex = 0; tileIndex < pl
                    .getHandTile().size(); tileIndex++) {

            // Tile is part of a near-Chi
            if (isAlmostChi(pl,
                    pl.getTileFromHand(tileIndex))) {

                discard(pl, tileIndex);
                return;
            }
        }

        for (int tileIndex = 0; tileIndex < pl
                .getHandTile().size(); tileIndex++) {

            // Tile is part of a pair that can no longer pong
            if (isPair(pl.getHandTile(),
                    pl.getTileFromHand(tileIndex)) && (numInDiscard
                    (pl.getTileFromHand(tileIndex)) == 2)) {

                discard(pl, tileIndex);
                return;
            }
        }

        Random rand = new Random();
        discard(pl, rand.nextInt(pl.getHandTile().size()));
    }

    /*******************************************************************
     * A general discard method that determines how an AI should
     * choose a Tile discard based on the AI difficulty.
     * @param pl player that is discarding
     ******************************************************************/
    public void generalAIDiscard(final Player pl) {

        if (pl.equals(playerList[1])) {

            if (AIDiff[0] == DUMB) {

                dumbAIDiscard(pl);

            } else if (AIDiff[0] == BEGINNER) {

                beginnerAIDiscard(pl);

            } else {

                advancedAIDiscard(pl);
            }

        } else if (pl.equals(playerList[2])) {

            if (AIDiff[1] == DUMB) {

                dumbAIDiscard(pl);

            } else if (AIDiff[1] == BEGINNER) {

                beginnerAIDiscard(pl);

            } else {

                advancedAIDiscard(pl);
            }


        } else if (pl.equals(playerList[3])) {

            if (AIDiff[2] == DUMB) {

                dumbAIDiscard(pl);

            } else if (AIDiff[2] == BEGINNER) {

                beginnerAIDiscard(pl);

            } else {

                advancedAIDiscard(pl);
            }

        } else {

            throw new IllegalArgumentException("Player 1 is not an " +
                    "AI, please choose a Tile to discard");
        }
    }

    /*******************************************************************
     * A helper method that finds how many of a certain type of Tile is
     * in the discard pile.
     * @param tile indicated Tile
     * @return the number of the indicated Tile that is in the 
     * discard pile
     ******************************************************************/
    private int numInDiscard(final Tile tile) {

        int count = 0;

        for (int i = 0; i < discardPile.size(); i++) {

            if (compareTile(discardPile.get(i), tile)) {

                count++;
            }
        }

        return count;
    }

    /*******************************************************************
     * This method copies the winning player index and moves it into the
     * discard pile. This essitenally displays the winning players hand
     * in the discard pile.
     * @param plIndex The winning player index.
     ******************************************************************/
    public void showWinningHand(final int plIndex) {

        if (plIndex < 0 || plIndex >= playerList.length) {

            throw new IllegalArgumentException("Not a valid index");
        }

        ArrayList<Tile> hand = playerList[plIndex].getHandTile();

        discardPile.clear();

        for (int i = 0; i < hand.size(); i++) {

            discardPile.add(hand.get(i));
        }

    }

    /******************************************************************
     * This method explains the rules of the game.
     *
     * @return The rules of the game.
     *****************************************************************/
    public String ruleBook() {

        return starting() + declareMahjong() + setRule() +
                claimChiRule() + claimPongRule() + claimKongRule()
                + scoring() + gameMode();
    }

    /*******************************************************************
     * This method explains the introduction of the rules of the game.
     *
     * @return The introduction of the rules of the game.
     ******************************************************************/
    private String starting() {

        return "General Rules: \n"
                + "The objective of the game is to collect tiles in "
                + "order to form a specific kind of hand, called "
                + "mahjong. Tiles resemble cards, and have "
                + "five "
                + "different suits (Circle, Character, Bamboo, Wind, "
                + "and Dragon). \nAn additional suit, called Flower, " +
                "gives a player a point when the tile is drawn."
                + "\nPlayers start with 13 tiles each. Each player "
                + "is assigned a wind direction"
                + " (East-South-West-North). "
                + "East starts the game by picking a tile from the "
                + "wall and discarding a tile.\nPlayers then take"
                + " clockwise turns picking a tile from "
                + "the wall and then discarding one tile from their " +
                "hand."
                + "\nIt is also possible to start your turn by " +
                "claiming" +
                " a tile " +
                "discarded by "
                + "another player under certain circumstances. "
                + "In such cases, the player who claimed the " +
                "discarded tile now takes their turn, \nand the " +
                "player " +
                "to the " +
                "left of the "
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
                + "A set of tiles consists of 3 tiles. These 3 tiles "
                + "may all be the same, thus forming a 3 of a kind "
                + "(pong) or they all form a 3 tile straight of the "
                + "same suit (chi). You can also form a 4 of a kind "
                + "(kong). \nAll sets that are formed in the hand "
                + "by drawing do not have to be revealed to the "
                + "opposing players. \nHowever, any set that is " +
                "claimed "
                + "from the discard pile must be revealed and " +
                "placed in your set pile, directly above your " +
                "hand.\nIt is important to know that a "
                + "kong is considered as 1 set. Once a player "
                + "declares that they can form a set, they move the "
                + "tiles that they used\nto form a set along with the "
                + "recently discarded tile to the set pile. Then, the "
                + "player who claimed the set discards a tile. \nThe"
                + " next player's turn is the player that is next in "
                + "rotation of the player that discarded the tile (to" +
                " their left)" +
                ".\n\n";
    }

    /*******************************************************************
     * This method explains the rules about claiming Chi.
     *
     * @return The rules about claiming Chi.
     ******************************************************************/
    private String claimChiRule() {

        String msg = "Claiming Chi:\n"
                + "A chi can only be claimed when the opposing "
                + "player directly to your right discards a "
                + "tile. In addition, the discarded the tile must be "
                + "able to use to form a 3 tile straight\nin the "
                + "players hand using the same suit. Also, the straight"
                + " cannot extend from 9 to 1 or vise versa and"
                + " cannot be done with Dragon or Wind tiles.\n\n";

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

        return  "Claiming Kong:\n" +
                "A kong can be claimed when any " +
                "opposing player"
                + " discards a tile and you have a 3 tiles in your hand"
                + " that can be used to form a 4 of a kind with the "
                + "discarded tile.\nThe player must draw a "
                + "new tile from the main wall before discarding a"
                + " tile. In addition, a kong can not remain in "
                + "the hand.\nInstead the kong that forms by drawing "
                + "must be moved to the"
                + " set pile or discard a tile to break up the kong. "
                + "A special rule with kong is that you can form a\n"
                + "kong with a pong already in your set pile.\n\n";
    }

    /*******************************************************************
     * This method explains the rules on how to declare Mahjong.
     *
     * @return The rules on how to declare Mahjong.
     ******************************************************************/
    private String declareMahjong() {

        return  "Declaring Mahjong: \n"
                + "A Mahjong can be declared when a player's hand " +
                "consists entirely of sets (chi, pong, kong), point " +
                "tiles, and a "
                + "single pair in their hand. "
                + "\nIn addition, any kongs must be "
                + "in the declared player's set pile. At this point, " +
                "the "
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
                + "Once a player has declared Mahjong, they win. "
                + "In Simple mode, the winning player will receive of" +
                " a score of 1 "
                + "point and 1 additional point for every Dragon, Wind"
                + " and Flower tile\nthat is in the set pile. " +
                "Traditional mode sets " +
                "scores based"
                + " on the composition of the player's hand and set " +
                "pile when they "
                + "declare Mahjong.\n\n" + scoreTraditional();
    }

    /*******************************************************************
     * This method explains the rules about the 2 different game modes.
     *
     * @return The rules about the game modes.
     ******************************************************************/
    private String gameMode() {

        return  "\n\nGame Mode: \n"
                + "The Simple game mode automatically moves all " +
                "Flower, Dragon, and Wind" +
                " tiles to the set pile and they are treated as a "
                + "point.\nTraditional game mode only moves"
                + " Flower tiles to the set pile. This will allow "
                + "for pongs to be made with Dragon and Wind tiles.\n";
    }

    /*******************************************************************
     * This method displays the scoring on the traditional mode.
     * @return The rules about traditional mode.
     ******************************************************************/
    private String scoreTraditional() {

        return "Scoring Traditional Mode: \n"
                + " The player is awarded the following points: "
                + "Chi: 1 Point for every Chi, 1 Additional Point if"
                + " the chi contains a tile with a 1 or 9\n"
                + "Pong: 1 Point for every pong and 2 points if the "
                + "Pong uses of a dragon or wind tiles\n"
                + "Kong: 2 Point for every pong and 4 points if the "
                + "Kong uses of a dragon or wind tiles\n"
                + "Flower: 1 Point for each Flower tile\n"
                + "Double points if you draw into Mahjong or your"
                + "hand and set piles consist of only chis, pongs,"
                + " or kongs. The pair and flower tiles"
                + " do not effect this";
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
    public Player getCurrentPlayer() {

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
    public Player getPlayerList(final int playerNum) {

        if (playerNum < 0 || playerNum > 4) {

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
    public ArrayList<Tile> getPlayerHand(int playerNum) {

        if (playerNum < 0 || playerNum >= 4) {

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
    public Tile getRecentDiscard() {

        if (discardPile.size() == 0) {

            return null;
        }

        return discardPile.get(discardPile.size() - 1);
    }

    /*******************************************************************
     * This method gets the draw pile/main deck.
     *
     * @return The tiles that are in the draw pile/ main deck.
     ******************************************************************/
    public ArrayList<Tile> getDrawPile() {

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
    public void setNextCurrentPlayer(final int player) {

        if (player >= TOTALPLAYER || player < 0) {

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
    public void setGameOptionSimple(final boolean mode) {

        gameOptionSimple = mode;
    }

    /*******************************************************************
     * This method sets the difficulty of the AI.
     * @param difficulty Difficult of the AI.
     * @param playerIndex Which AI at the player index.
     ******************************************************************/
    public void setAIDiff(final int difficulty, final int playerIndex) {

        if (difficulty < Game.DUMB || difficulty > Game.ADVANCED) {

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
