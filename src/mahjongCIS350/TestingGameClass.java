package mahjongCIS350;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

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

    @Test
    public void testisChi() {

        Player test = new Player();
        ArrayList<Tile> hand = new ArrayList<>();

        String noChi = "This should not be a chi";
        String chi = "This should be a chi";

        hand.add(wTile[0]);
        hand.add(chTile[1]);
        hand.add(chTile[2]);
        hand.add(bTile[7]);
        hand.add(bTile[8]);
        hand.add(bTile[9]);
        hand.add(cTile[3]);
        hand.add(cTile[5]);
        hand.add(chTile[3]);

        // Make sure error does not occur if hand is empty
        Assert.assertFalse("Error when player had is " +
                "empty", game.isChi(test, wTile[2]));

        test.setHandTile(hand);

        Assert.assertFalse(noChi, game.isChi(test,
                chTile[9]));
        Assert.assertTrue(chi, game.isChi(test,
                chTile[1]));
        Assert.assertTrue(chi, game.isChi(test,
                chTile[2]));
        Assert.assertTrue(chi, game.isChi(test,
                chTile[3]));
        Assert.assertTrue(chi, game.isChi(test,
                chTile[4]));
        Assert.assertFalse(noChi, game.isChi(test,
                chTile[7]));
        Assert.assertFalse(noChi, game.isChi(test,
                bTile[2]));
        Assert.assertFalse(noChi, game.isChi(test,
                cTile[2]));

        Assert.assertFalse(noChi, game.isChi(test,
                bTile[1]));
        Assert.assertTrue(chi, game.isChi(test,
                bTile[6]));
        Assert.assertTrue(chi, game.isChi(test,
                bTile[7]));
        Assert.assertTrue(chi, game.isChi(test,
                bTile[8]));
        Assert.assertTrue(chi, game.isChi(test,
                bTile[9]));
        Assert.assertFalse(noChi, game.isChi(test,
                bTile[4]));
        Assert.assertFalse(noChi, game.isChi(test,
                cTile[8]));

        Assert.assertTrue(chi, game.isChi(test,
                cTile[4]));
        Assert.assertFalse(noChi, game.isChi(test,
                cTile[2]));
        Assert.assertFalse(noChi, game.isChi(test,
                cTile[5]));
        Assert.assertFalse(noChi, game.isChi(test,
                cTile[9]));
        Assert.assertFalse(noChi, game.isChi(test,
                dTile[2]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsChiError(){

        Player test = new Player();
        game.isChi(test, null);
    }

    @Test
    public void testIsPongIsKong() {

        ArrayList<Tile> hand = new ArrayList<>();

        Assert.assertFalse("Should be false with" +
                "empty hand", game.isPong(hand,dTile[1]));
        Assert.assertFalse("Should be false with" +
                "empty hand", game.isKong(hand,dTile[1]));

        for (int i = 0; i < 3; i++){

            hand.add(wTile[1]);
            hand.add(dTile[2]);
            hand.add(cTile[3]);
            hand.add(chTile[3]);
            hand.add(bTile[1]);
        }

        for (int i = 0; i < 2; i++){

            hand.add(cTile[2]);
            hand.add(bTile[5]);
            hand.add(wTile[2]);
            hand.add(dTile[1]);
        }

        hand.add(dTile[0]);
        hand.add(wTile[0]);

        // Testing for Pongs
        Assert.assertTrue("Should have Pong",
                game.isPong(hand, cTile[2]));
        Assert.assertTrue("Should have Pong",
                game.isPong(hand, bTile[5]));
        Assert.assertTrue("Should have Pong",
                game.isPong(hand, wTile[2]));
        Assert.assertTrue("Should have Pong",
                game.isPong(hand, dTile[1]));

        // If there is a Kong, then there should be a Pong
        Assert.assertTrue("Should have Pong",
                game.isPong(hand, wTile[1]));
        Assert.assertTrue("Should have Pong",
                game.isPong(hand, dTile[2]));
        Assert.assertTrue("Should have Pong",
                game.isPong(hand, cTile[3]));
        Assert.assertTrue("Should have Pong",
                game.isPong(hand, chTile[3]));
        Assert.assertTrue("Should have Pong",
                game.isPong(hand, bTile[1]));

        // No Pongs
        Assert.assertFalse("No Pong",
                game.isPong(hand, wTile[0]));
        Assert.assertFalse("No Pong",
                game.isPong(hand, dTile[0]));
        Assert.assertFalse("No Pong",
                game.isPong(hand,bTile[9]));
        Assert.assertFalse("No Pong",
                game.isPong(hand,cTile[7]));
        Assert.assertFalse("No Pong",
                game.isPong(hand,chTile[1]));

        // Test for Kongs
        Assert.assertTrue("Should have Kong",
                game.isKong(hand, wTile[1]));
        Assert.assertTrue("Should have Kong",
                game.isKong(hand, dTile[2]));
        Assert.assertTrue("Should have Kong",
                game.isKong(hand, cTile[3]));
        Assert.assertTrue("Should have Kong",
                game.isKong(hand, chTile[3]));
        Assert.assertTrue("Should have Kong",
                game.isKong(hand, bTile[1]));

        // Test for no Kongs
        Assert.assertFalse("No Kong",
                game.isKong(hand, cTile[2]));
        Assert.assertFalse("No Kong",
                game.isKong(hand, bTile[5]));
        Assert.assertFalse("No Kong",
                game.isKong(hand, wTile[2]));
        Assert.assertFalse("No Kong",
                game.isKong(hand, dTile[1]));
        Assert.assertFalse("No Kong",
                game.isKong(hand, wTile[2]));
        Assert.assertFalse("No Kong",
                game.isKong(hand, dTile[0]));
        Assert.assertFalse("No Kong",
                game.isKong(hand, wTile[2]));
        Assert.assertFalse("No Kong",
                game.isKong(hand, chTile[1]));
        Assert.assertFalse("No Kong",
                game.isKong(hand, cTile[8]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testisPongError(){

        ArrayList<Tile> hand = new ArrayList<>();

        game.isPong(hand, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testisKongError(){

        ArrayList<Tile> hand = new ArrayList<>();

        game.isKong(hand, null);
    }

    @Test
    public void testIsKongDraw() {

        String noKong = "There should not be a Kong that" +
                "can be formed with the tiles in the " +
                "set pile";
        String kong = "There should be a Kong";

        Player pl = new Player();
        ArrayList<Tile> hand = new ArrayList<>();

        Assert.assertFalse("Should be false with" +
                "empty hand", game.isPong(hand,dTile[1]));
        Assert.assertFalse("Should be false with" +
                "empty hand", game.isKong(hand,dTile[1]));

        for (int i = 0; i < 3; i++){

            pl.getSetPile().add(wTile[1]);
            pl.getSetPile().add(dTile[2]);
            pl.getSetPile().add(cTile[3]);
            pl.getSetPile().add(chTile[3]);
            pl.getSetPile().add(bTile[1]);
        }

        for (int i = 0; i < 2; i++){

            pl.getSetPile().add(cTile[2]);
        }

        Assert.assertFalse(noKong, game.isKongDraw(pl));

        pl.getHandTile().add(wTile[1]);
        Assert.assertTrue(kong, game.isKongDraw(pl));
        pl.getHandTile().add(wTile[2]);
        Assert.assertFalse(noKong, game.isKongDraw(pl));

        pl.getHandTile().add(dTile[2]);
        Assert.assertTrue(kong, game.isKongDraw(pl));
        pl.getHandTile().add(wTile[2]);
        Assert.assertFalse(noKong, game.isKongDraw(pl));

        pl.getHandTile().add(cTile[3]);
        Assert.assertTrue(kong, game.isKongDraw(pl));
        pl.getHandTile().add(cTile[5]);
        Assert.assertFalse(noKong, game.isKongDraw(pl));

        pl.getHandTile().add(chTile[3]);
        Assert.assertTrue(kong, game.isKongDraw(pl));
        pl.getHandTile().add(cTile[5]);
        Assert.assertFalse(noKong, game.isKongDraw(pl));

        pl.getHandTile().add(bTile[1]);
        Assert.assertTrue(kong, game.isKongDraw(pl));
        pl.getHandTile().add(cTile[2]);
        Assert.assertFalse(noKong, game.isKongDraw(pl));

        // Draw into the Kong of a different Tile
        pl.getHandTile().add(cTile[3]);
        Assert.assertTrue(noKong, game.isKongDraw(pl));
    }
}

