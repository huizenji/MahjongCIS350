package mahjongCIS350;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Random;

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

            bTile[i] = new Suit("Bamboo", i);
            chTile[i] = new Suit("Character", i);
            cTile[i] = new Suit("Circle", i);
        }

        for (int i = 1; i <= 8; i++){

            fTile[i - 1] = new Flower(i);
        }

        game = new Game();
    }

    // Testing Getters and Setters for Draw and Discard Pile,
    @Test
    public void testGetterSettersDrawDiscard() {

        Random rand = new Random();
        Assert.assertEquals("Should have no" +
                "tiles in discard pile",
                game.getDiscardPile().size(), 0);
        Assert.assertNotEquals("Should not have 144 tiles",
                game.getDrawPile().size(), 144);

        game.discard(game.getPlayerList(0),
                rand.nextInt(game.getPlayerList(0).
                        getHandTile().size()));
        Assert.assertNotNull(game.getRecentDiscard());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDiscardError(){

        Player pl = game.getPlayerList(0);
        game.discard(pl, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDiscardError2(){

        Player pl = game.getPlayerList(0);
        game.discard(pl, pl.getHandTile().size());
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

        game.setAIDiff(Game.ADVANCED + 1, 0);
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

    @Test
    public void getScoreSimple (){

        game.setGameOptionSimple(true);
        ArrayList<Tile> pile = game.getPlayerList(
                0).getSetPile();

        pile.clear();

        pile.add(wTile[0]);
        pile.add(dTile[0]);
        pile.add(fTile[1]);
        pile.add(dTile[2]);
        pile.add(dTile[1]);
        pile.add(wTile[3]);
        pile.add(bTile[3]);
        pile.add(cTile[5]);
        pile.add(chTile[1]);
        pile.add(dTile[0]);
        pile.add(fTile[1]);

        game.increaseScore(0, false);
        Assert.assertEquals("Scoring is incorrect",
                game.getPlayerList(0).getPoint(), 8);
        game.increaseScore(0, true);
        Assert.assertEquals("Scoring is incorrect",
                game.getPlayerList(0).getPoint(), 16);

        pile.add(dTile[0]);
        pile.add(fTile[1]);
        game.increaseScore(0,false);
        Assert.assertEquals("Scoring is incorrect",
                game.getPlayerList(0).getPoint(), 26);
    }

    @Test(expected = IllegalArgumentException.class)
    public void scoreErrorT1(){

        game.increaseScore(-1, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void scoreErrorT2(){

        game.increaseScore(4, false);
    }

    @Test
    public void testgetChiTileT1(){

        game.setGameOptionSimple(true);
        Player pl = new Player();
        ArrayList<Tile> hand = pl.getHandTile();

        hand.clear();

        hand.add(bTile[1]);
        hand.add(bTile[1]);
        hand.add(bTile[2]);
        hand.add(bTile[4]);
        hand.add(bTile[5]);
        hand.add(bTile[9]);
        hand.add(bTile[9]);
        hand.add(bTile[6]);
        hand.add(bTile[7]);
        hand.add(bTile[7]);
        hand.add(bTile[8]);

        hand.add(chTile[7]);
        hand.add(cTile[3]);

        Assert.assertEquals(game.getChiTile(pl, wTile[2]).size(),
                0);
        Assert.assertEquals(game.getChiTile(pl, cTile[3]).size(),
                0);
        Assert.assertEquals(game.getChiTile(pl, bTile[1]).size(),
                0);
        Assert.assertEquals(6,
                game.getChiTile(pl, bTile[3]).size());
        Assert.assertEquals(6,
                game.getChiTile(pl, bTile[6]).size());
        Assert.assertEquals(2,
                game.getChiTile(pl, bTile[9]).size());
    }

    @Test
    public void testgetChiTileT2(){

        game.setGameOptionSimple(true);
        Player pl = new Player();
        ArrayList<Tile> hand = pl.getHandTile();

        hand.clear();

        hand.add(cTile[1]);
        hand.add(cTile[2]);
        hand.add(cTile[1]);
        hand.add(cTile[4]);
        hand.add(cTile[5]);
        hand.add(cTile[9]);
        hand.add(cTile[9]);
        hand.add(cTile[6]);
        hand.add(cTile[7]);
        hand.add(cTile[8]);

        hand.add(chTile[7]);
        hand.add(bTile[3]);

        Assert.assertEquals(game.getChiTile(pl, fTile[5]).size(),
                0);
        Assert.assertEquals(game.getChiTile(pl, bTile[3]).size(),
                0);
        Assert.assertEquals(game.getChiTile(pl, cTile[1]).size(),
                0);
        Assert.assertEquals(6,
                game.getChiTile(pl, cTile[3]).size());
        Assert.assertEquals(6,
                game.getChiTile(pl, cTile[6]).size());
        Assert.assertEquals(game.getChiTile(pl, cTile[9]).size(),
                2);
    }
    @Test
    public void testgetChiTileT3(){

        game.setGameOptionSimple(true);
        Player pl = new Player();
        ArrayList<Tile> hand = pl.getHandTile();

        hand.clear();

        hand.add(chTile[1]);
        hand.add(chTile[2]);
        hand.add(chTile[1]);
        hand.add(chTile[4]);
        hand.add(chTile[5]);
        hand.add(chTile[9]);
        hand.add(chTile[9]);
        hand.add(chTile[6]);
        hand.add(chTile[7]);
        hand.add(chTile[8]);

        hand.add(cTile[7]);
        hand.add(bTile[3]);

        Assert.assertEquals(game.getChiTile(pl, dTile[1]).size(),
                0);
        Assert.assertEquals(game.getChiTile(pl, chTile[2]).size(),
                0);
        Assert.assertEquals(game.getChiTile(pl, chTile[1]).size(),
                0);
        Assert.assertEquals(game.getChiTile(pl, chTile[3]).size(),
                6);
        Assert.assertEquals(game.getChiTile(pl, chTile[6]).size(),
                6);
        Assert.assertEquals(game.getChiTile(pl, chTile[9]).size(),
                2);
    }

    @Test
    public void testgetChiTileT4(){

        game.setGameOptionSimple(true);
        Player pl = new Player();
        ArrayList<Tile> hand = pl.getHandTile();

        hand.clear();

        hand.add(chTile[1]);
        hand.add(chTile[2]);
        hand.add(chTile[9]);
        hand.add(chTile[9]);

        hand.add(cTile[7]);
        hand.add(bTile[3]);

        Assert.assertEquals(game.getChiTile(pl, dTile[1]).size(),
                0);
        Assert.assertEquals(game.getChiTile(pl, chTile[2]).size(),
                0);
        Assert.assertEquals(game.getChiTile(pl, chTile[1]).size(),
                0);
        Assert.assertEquals(game.getChiTile(pl, chTile[3]).size(),
                2);
    }

    @Test
    public void testTakeChi(){

        Player pl = new Player();

        ArrayList<Tile> hand = new ArrayList<>();
        hand.add(bTile[1]);
        hand.add(bTile[2]);
        hand.add(cTile[3]);
        pl.setHandTile(hand);

        game.getDiscardPile().clear();
        game.getDiscardPile().add(bTile[3]);
        game.takeChi(pl, 0, 1);

        Assert.assertEquals("Incorrect Size for Player Hand",
                1, pl.getHandTile().size());
        Assert.assertEquals("Incorrect size for Player set",
                3, pl.getSetPile().size());
        Assert.assertEquals("Wrong Tile 1", bTile[2],
                pl.getSetPile().get(0));
        Assert.assertEquals("Wrong Tile 2", bTile[1],
                pl.getSetPile().get(1));
        Assert.assertEquals("Wrong Tile 3", bTile[3],
                pl.getSetPile().get(2));
    }

    @Test
    public void testTakeChi2(){

        Player pl = new Player();

        ArrayList<Tile> hand = new ArrayList<>();
        hand.add(bTile[1]);
        hand.add(cTile[2]);
        hand.add(cTile[3]);
        pl.setHandTile(hand);

        game.getDiscardPile().clear();
        game.getDiscardPile().add(cTile[4]);
        game.takeChi(pl, 1, 2);

        Assert.assertEquals("Incorrect Size for Player Hand",
                1, pl.getHandTile().size());
        Assert.assertEquals("Incorrect size for Player set",
                3, pl.getSetPile().size());
        Assert.assertEquals("Wrong Tile 1", cTile[3],
                pl.getSetPile().get(0));
        Assert.assertEquals("Wrong Tile 2", cTile[2],
                pl.getSetPile().get(1));
        Assert.assertEquals("Wrong Tile 3", cTile[4],
                pl.getSetPile().get(2));
    }

    @Test
    public void testTakeChi3(){

        Player pl = new Player();

        ArrayList<Tile> hand = new ArrayList<>();
        hand.add(bTile[1]);
        hand.add(chTile[2]);
        hand.add(chTile[3]);
        hand.add(cTile[4]);
        pl.setHandTile(hand);

        game.getDiscardPile().clear();
        game.getDiscardPile().add(chTile[4]);
        game.takeChi(pl, 1, 2);

        Assert.assertEquals("Incorrect Size for Player Hand",
                2, pl.getHandTile().size());
        Assert.assertEquals("Incorrect size for Player set",
                3, pl.getSetPile().size());
        Assert.assertEquals("Wrong Tile 1", chTile[3],
                pl.getSetPile().get(0));
        Assert.assertEquals("Wrong Tile 2", chTile[2],
                pl.getSetPile().get(1));
        Assert.assertEquals("Wrong Tile 3", chTile[4],
                pl.getSetPile().get(2));
    }

    @Test
    public void testNoDiscardIsMethods(){

        String msg = "no tiles in discard pile so there should be " +
                "no tile that can be used for chi, pong, or kong";

        Player pl = game.getPlayerList(0);
        game.getDiscardPile().clear();
        Tile tile = game.getRecentDiscard();

        Assert.assertFalse(game.isChi(pl, tile));
        Assert.assertFalse(game.isPong(pl.getHandTile(), tile));
        Assert.assertFalse(game.isKong(pl.getHandTile(), tile));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTakeChiError() {

        game.takeChi(game.getPlayerList(0), 1 ,1);
    }

    @Test
    public void testTakePong(){

        Player pl = new Player();
        Tile[] tile = new Tile[5];

        Random rand = new Random();
        int r1 = rand.nextInt(9) + 1;
        int r2 = rand.nextInt(9) + 1;
        int r3 = rand.nextInt(9) + 1;
        int r4 = rand.nextInt(4);
        int r5 = rand.nextInt(3);

        for (int i = 0; i < 2; i++){

            pl.getHandTile().add(bTile[r1]);
            pl.getHandTile().add(cTile[r2]);
            pl.getHandTile().add(wTile[r4]);
            pl.getHandTile().add(dTile[r5]);
            pl.getHandTile().add(chTile[r3]);
        }

        tile[4] = bTile[r1];
        tile[3] = cTile[r2];
        tile[2] = wTile[r4];
        tile[1] = dTile[r5];
        tile[0] = chTile[r3];

        game.getDiscardPile().add(bTile[r1]);
        game.getDiscardPile().add(cTile[r2]);
        game.getDiscardPile().add(wTile[r4]);
        game.getDiscardPile().add(dTile[r5]);
        game.getDiscardPile().add(chTile[r3]);

        for (int i = 0; i < tile.length; i++) {
            // Error when claiming Pong Test Start
            game.takePong(pl, tile[i]);
            Assert.assertEquals("Incorrect Hand size",
                    (((tile.length * 2) - (i * 2)) - 2),
                    pl.getHandTile().size());
            for (int k = 0; k < pl.getHandTile().size(); k++) {

                Assert.assertNotEquals("Should not have " +
                                "the tile that was used to take a Pong",
                        tile[i], pl.getHandTile().get(k));
            }
            Assert.assertEquals("Incorrect Set Size",
                    i * 3 + 3, pl.getSetPile().size());
            Assert.assertEquals("Incorrect Tile removed 1",
                    tile[i], pl.getSetPile().get(i * 3));
            Assert.assertEquals("Incorrect Tile removed 2",
                    tile[i], pl.getSetPile().get(i * 3 + 1));
            Assert.assertEquals("Incorrect Tile removed 3",
                    tile[i], pl.getSetPile().get(i * 3 + 2));
        }
    }

    @Test
    public void testTakeKong(){

        game.setGameOptionSimple(false);
        Player pl = game.getPlayerList(0);
        Tile[] tile = new Tile[5];

        int r1 = 1;
        int r2 = 2;
        int r3 = 3;
        int r4 = 2;
        int r5 = 1;
        int extraTile = r1;

        pl.getHandTile().clear();
        pl.getSetPile().clear();

        for (int i = 0; i < 3; i++){

            pl.getHandTile().add(bTile[r1]);
            pl.getHandTile().add(cTile[r2]);
            pl.getHandTile().add(wTile[r3]);
            pl.getHandTile().add(dTile[r4]);
            pl.getHandTile().add(chTile[r5]);
        }


        tile[4] = bTile[r1];
        tile[3] = cTile[r2];
        tile[2] = wTile[r3];
        tile[1] = dTile[r4];
        tile[0] = chTile[r5];

        game.getDiscardPile().clear();
        game.getDiscardPile().add(tile[4]);
        game.getDiscardPile().add(tile[3]);
        game.getDiscardPile().add(tile[2]);
        game.getDiscardPile().add(tile[1]);
        game.getDiscardPile().add(tile[0]);

        game.getDrawPile().clear();
        game.getDrawPile().add(chTile[4]);
        game.getDrawPile().add(bTile[r1]);
        game.getDrawPile().add(fTile[2]);
        game.getDrawPile().add(dTile[0]);
        game.getDrawPile().add(bTile[9]);
        game.getDrawPile().add(bTile[9]);

        // Error when claiming Kong Test Start
        game.takeKong(pl, tile[0]);
        Assert.assertEquals("Incorrect Hand size", (13),
                pl.getHandTile().size());

        for (int k = 0; k < pl.getHandTile().size(); k++) {

            Assert.assertNotEquals("Should not have " +
                            "the tile that was used to take a Pong",
                    tile[0], pl.getHandTile().get(k));
        }

        Assert.assertEquals("Incorrect Set Size",
                4, pl.getSetPile().size());
        Assert.assertEquals("Incorrect Tile removed 1",
                tile[0], pl.getSetPile().get(0));
        Assert.assertEquals("Incorrect Tile removed 2",
                tile[0], pl.getSetPile().get(1));
        Assert.assertEquals("Incorrect Tile removed 3",
                tile[0], pl.getSetPile().get(2));
        Assert.assertEquals("Incorrect Tile removed 4",
                tile[0], pl.getSetPile().get(3));

        // Drawing into a Kong after claiming a Kong
        game.takeKong(pl, tile[1]);
        Assert.assertEquals("Incorrect Hand size", (8),
                pl.getHandTile().size());

        for (int k = 0; k < pl.getHandTile().size(); k++) {

            Assert.assertNotEquals("Should not have " +
                            "the tile that was used to take a Pong",
                    tile[1], pl.getHandTile().get(k));
            Assert.assertNotEquals("Should not have " +
                            "the tile that was used to take a Pong",
                    chTile[extraTile], pl.getHandTile().get(k));
        }

        Assert.assertEquals("Incorrect Set Size",
                13, pl.getSetPile().size());

        int tile1Count = 0; // Number of Tile 1
        int tileExtraCount = 0; // Number of Tile Extra that makes extra
                                // kong
        for (int i = 0; i < pl.getSetPile().size();i++){

            if (tile[1].equals(pl.getSetPile().get(i))) {
                tile1Count++;
            }

            if (tile[extraTile].equals(pl.getSetPile().get(i))) {
                tileExtraCount++;
            }
        }

        Assert.assertEquals("Should have 4 tiles from " +
                "the kong", 4, tile1Count);
        Assert.assertEquals("Should have 4 tiles from " +
                "kong that was formed a draw", 4,
                tileExtraCount);
    }



    @Test
    public void testTakeKongDraw(){

        Player pl = new Player();

        pl.getHandTile().add(bTile[1]);
        pl.getHandTile().add(bTile[1]);
        pl.getHandTile().add(bTile[1]);
        pl.getHandTile().add(bTile[2]);

        game.getDrawPile().clear();
        game.getDrawPile().add(cTile[2]);

        game.takeKongDraw(pl);
        Assert.assertEquals("Hand size should not have" +
                " changed", 4, pl.getHandTile().size());
        Assert.assertEquals("Set pile size should increase" +
                " in size", 1, pl.getSetPile().size());
    }

    @Test
    public void testTakeKongDraw2(){

        Player pl = game.getPlayerList(0);

        pl.getHandTile().clear();
        pl.getSetPile().clear();
        pl.getHandTile().add(chTile[1]);
        pl.getHandTile().add(chTile[1]);
        pl.getHandTile().add(chTile[1]);
        pl.getHandTile().add(chTile[2]);

        game.getDrawPile().clear();
        game.getDrawPile().add(chTile[1]);
        game.getDrawPile().add(cTile[2]);

        game.takeKongDraw(pl);
        Assert.assertEquals("Hand size should be 1 due to " +
                        "kong removal" , 1,
                pl.getHandTile().size());
        Assert.assertEquals("Set pile size should increase" +
                " in size", 5, pl.getSetPile().size());
    }

    @Test
    public void testDraw() {

        Player pl = new Player();
        int drawSize = game.getDrawPile().size();
        int handSize = pl.getHandTile().size();
        int setSize = pl.getSetPile().size();

        for (int i = 0; i < drawSize && !game.isStalemate(); i++){

            game.draw(pl);
            if (pl.getHandTile().size() >= handSize + 2){

                Assert.fail("Drew Too many Tiles");
            }


            if (pl.getSetPile().size() < setSize){

                Assert.fail("Set Size should not decrease");
            }
            handSize = pl.getHandTile().size();
            setSize = pl.getSetPile().size();
        }
    }

    @Test
    public void testStalemateTest1() {

        game.setGameOptionSimple(true);
        ArrayList<Tile> deck = game.getDrawPile();
        deck.clear();
        Player pl = new Player();

        Assert.assertTrue("Stalemate",
                game.isStalemate());
        deck.add(wTile[2]);
        Assert.assertTrue("Stalemate",
                game.isStalemate());
        deck.add(wTile[1]);
        Assert.assertTrue("Stalemate",
                game.isStalemate());
        deck.add(dTile[2]);
        Assert.assertTrue("Stalemate",
                game.isStalemate());
        deck.add(wTile[2]);
        Assert.assertTrue("Stalemate",
                game.isStalemate());
        deck.add(bTile[2]);
        Assert.assertFalse("No Stalemate",
                game.isStalemate());
        game.draw(pl);
        deck.add(cTile[2]);
        Assert.assertFalse("No Stalemate",
                game.isStalemate());
        deck.add(wTile[2]);
        Assert.assertFalse("No Stalemate",
                game.isStalemate());
    }

    @Test
    public void testStalemateTest2() {

        game.setGameOptionSimple(false);
        ArrayList<Tile> deck = game.getDrawPile();
        deck.clear();
        Player pl = new Player();

        Assert.assertTrue("Stalemate",
                game.isStalemate());
        deck.add(fTile[2]);
        Assert.assertTrue("Stalemate",
                game.isStalemate());
        deck.add(fTile[1]);
        Assert.assertTrue("Stalemate",
                game.isStalemate());
        deck.add(dTile[2]);
        Assert.assertFalse("Stalemate",
                game.isStalemate());
        game.draw(pl);
        deck.add(wTile[2]);
        Assert.assertFalse("Stalemate",
                game.isStalemate());
        game.draw(pl);
        deck.add(bTile[2]);
        Assert.assertFalse("No Stalemate",
                game.isStalemate());
        game.draw(pl);
        deck.add(cTile[2]);
        Assert.assertFalse("No Stalemate",
                game.isStalemate());
        game.draw(pl);
        deck.add(wTile[2]);
        Assert.assertFalse("No Stalemate",
                game.isStalemate());
        deck.add(fTile[2]);
        Assert.assertFalse("No Stalemate",
                game.isStalemate());
    }
}


