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

    // Testing Getters and Setters
    @Test
    public void testGetterSetters() {

        Assert.assertEquals("Should have no" +
                "tiles in discard pile",
                game.getDiscardPile().size(), 0);

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

        game.getPlayerHand(5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetterSetterErrorPlayerHand3(){

        game.getPlayerHand(-20);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetterSetterErrorEmptyDiscard(){

        game.getRecentDiscard();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetterSetterErrorAIDiff(){

        game.setAIDiff(Game.DUMB - 1, 1);
    }
}
