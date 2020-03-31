package mahjongCIS350;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestingGameClass {

    Dragon []dTile = new Dragon[3];
    Wind []wTile = new Wind[4];

    // Circle tile
    Suit []cTile = new Suit[10];

    // bamboo tile
    Suit []bTile = new Suit[10];

    // character tile
    Suit []chTile = new Suit[10];

    // Flower tile4
    Flower []fTile = new Flower[8];

    Game game;


    // Create all the tiles
    @Before
    public void createTile() {

        dTile[0] = new Dragon("Green");
        dTile[1] = new Dragon("Red");
        dTile[2] = new Dragon("White");

        wTile[0] = new Wind("North");
        wTile[1] = new Wind("East");
        wTile[2] = new Wind("South");
        wTile[3] = new Wind("West");

        for (int i = 1; i <= 9; i++){

            bTile[i] = new Suit("bamboo", i);
            chTile[i] = new Suit("character", i);
            cTile[i] = new Suit("circle", i);
        }

        for (int i = 1; i <= 8; i++){

            fTile[i - 1] = new Flower(i);
        }

        game = new Game();
    }

    // Testing Getters and Setters for Draw and Discard Pile,
    @Test
    public void testGetterSettersDrawDiscard() {

        Assert.assertEquals("Should have no" +
                "tiles in discard pile",
                game.getDiscardPile().size(), 0);
        Assert.assertNotEquals("Should not have 144 tiles",
                game.getDrawPile().size(), 144);

        game.discard(game.getPlayerList(0),0);
        Assert.assertNotNull(game.getRecentDiscard());
    }

    // Testing Getters and Setters for Current and Starting Player
    @Test
    public void testGetterSetterCurSrtPlayerTest1() {

        for (int i = 0; i < 40; i++) {

            game.setNextCurrentPlayer();
            game.setNextStartingPlayer();
            if (game.getCurrentPlayerIndex() > 4 ||
                    game.getCurrentPlayerIndex() < 0) {

                Assert.fail("Player Index reach an undesirable number");
            }

            if (game.getStartingPlayer() > 4 ||
                    game.getStartingPlayer() < 0) {

                Assert.fail("Player Index reach an undesirable number");
            }
        }
    }

    @Test
    public void testGetterSetterCurSrtPlayerTest2() {

        int srt = game.getStartingPlayer();
        int curr = game.getCurrentPlayerIndex();

        Player srtPlayer = game.getPlayerList(game.getStartingPlayer());
        Player currPlayer = game.getCurrentPlayer();

        // Rotate Player 4 times should be the same person
        for (int i = 0; i < 4; i++){

            game.setNextStartingPlayer();
            game.setNextCurrentPlayer();
        }

        Assert.assertEquals("Player should be the same",
                srt, game.getStartingPlayer());
        Assert.assertEquals("Player should be the same",
                curr, game.getCurrentPlayerIndex());
        Assert.assertEquals("Player should be the same",
                srtPlayer, game.getPlayerList(
                        game.getStartingPlayer()));
        Assert.assertEquals("Player should be the same",
                currPlayer, game.getCurrentPlayer());

        game.setNextCurrentPlayer(curr);
        Assert.assertEquals("Player should be the same",
                currPlayer, game.getCurrentPlayer());
    }

    // Test game mode swap
    @Test
    public void testGetterSetterSwapGameMode (){

        Assert.assertTrue("Should be simple Game Mode at " +
                "start", game.getGameOptionSimple());
        game.setGameOptionSimple(false);
        Assert.assertFalse("Game Mode should have been " +
                        "set to traditional game mode",
                game.getGameOptionSimple());
        game.setGameOptionSimple(true);
        Assert.assertTrue("Game Mode should have been " +
                        "set to simple game mode",
                game.getGameOptionSimple());
    }

    @Test
    public void testGetterSettersPlayerHand() {

        boolean validHand = true;
        boolean oddHand = false;
        int handsize;
        for (int i = 0; i < Game.TOTALPLAYER; i++){

            handsize = game.getPlayerHand(i).size();

            if ((handsize % 3 == 2)){
                if (!oddHand){

                    oddHand = true;
                }

                else{

                    Assert.fail("Only 1 player should have 1 extra " +
                            "tile at the start");
                }
            }

            else if ((handsize % 3 == 0)){

                validHand = false;
            }
        }

        Assert.assertTrue("1 Player should have 1 extra tile" +
                "at the start", oddHand);
        Assert.assertTrue("1 of the Player's hand is not " +
                "valid", validHand);
    }

    // Error Test for get player list
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetterSetterErrorPlayerList(){

        game.getPlayerList(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetterSetterErrorPlayerList2(){

        game.getPlayerList(5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetterSetterErrorPlayerList3(){

        game.getPlayerList(-20);
    }

    // Error Test for get player list
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetterSetterErrorPlayerHand(){

        game.getPlayerHand(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetterSetterErrorPlayerHand2(){

        game.getPlayerHand(4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetterSetterErrorPlayerHand3(){

        game.getPlayerHand(-20);
    }

    @Test(expected = NullPointerException.class)
    public void testGetterSetterErrorEmptyDiscard(){

        game.getRecentDiscard();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetterSetterErrorAIDiff(){

        game.setAIDiff(Game.DUMB - 1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetterSetterErrorAIDiff2(){

        game.setAIDiff(Game.DUMB, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetterSetterErrorAIDiff3(){

        game.setAIDiff(Game.DUMB - 1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetterSetterErrorAIDiff4(){

        game.setAIDiff(Game.ADVANCE + 1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetterSetterErrorAIDiff5(){

        game.setAIDiff(Game.DUMB, 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetterSetterErrorNextPlayerT1(){

        game.setNextCurrentPlayer(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetterSetterErrorNextPlayerT2(){

        game.setNextCurrentPlayer(4);
    }
}

