package mahjongCIS350;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class TestingBaseClasses {

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

    Player player;

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

    }

    @Before
    public void newPlayer() {

        player = new Player("North");
    }

    // Test Wind Tiles
    @Test
    public void testWind() {

        Assert.assertEquals(wTile[0].getDirection(), "North");
        Assert.assertEquals(wTile[1].getDirection(), "East");
        Assert.assertEquals(wTile[2].getDirection(), "South");
        Assert.assertEquals(wTile[3].getDirection(), "West");

        // Test Set direction
        wTile[0].setDirection("A");
        Assert.assertEquals(wTile[0].getDirection(), "A");
        wTile[0].setDirection("North");
        Assert.assertEquals(wTile[0].getDirection(), "North");
    }

    // Test Dragon Tiles
    @Test
    public void testDragon() {

        Assert.assertEquals(dTile[0].getColor(), "Green");
        Assert.assertEquals(dTile[1].getColor(), "Red");
        Assert.assertEquals(dTile[2].getColor(), "White");


        // Test Set direction
        dTile[0].setColor("A");
        Assert.assertEquals(dTile[0].getColor(), "A");
        dTile[0].setColor("Green");
        Assert.assertEquals(dTile[0].getColor(), "Green");
    }

    // Test
    @Test
    public void testSuit(){

        int min = 1;
        int max = 9;
        int num;

        // Run 10000 amount of times
        for (int run = 0; run < 10000; run++){
            num = (int)(Math.random() * ((max - min) + 1)) + min;

            Assert.assertEquals(bTile[num].getValue(), num);
            Assert.assertEquals(chTile[num].getValue(), num);
            Assert.assertEquals(cTile[num].getValue(), num);

            Assert.assertEquals(bTile[num].getDesign(), "bamboo");
            Assert.assertEquals(chTile[num].getDesign(),
                    "character");
            Assert.assertEquals(cTile[num].getDesign(),
                    "circle");
        }

        num = (int)(Math.random() * ((max - min) + 1)) + min;
        // Change value and design for 1 of the Suit classes
        bTile[num].setDesign("a");
        bTile[num].setValue(5);

        Assert.assertEquals(bTile[num].getValue(), 5);
        Assert.assertEquals(bTile[num].getDesign(), "a");

        bTile[num].setDesign("bamboo");
        bTile[num].setValue(num);

        Assert.assertEquals(bTile[num].getValue(), num);
        Assert.assertEquals(bTile[num].getDesign(), "bamboo");
    }

    // Test Flower Class
    @Test
    public void testFlowerClass() {

        for (int i = 1; i <= 8; i++){

            Assert.assertEquals(fTile[i - 1].getNumber(), i);
        }
    }

    // Test Empty Constructors
    @Test
    public void testEmptyTileConstructor() {

        Suit eSuit = new Suit();
        Dragon eDragon = new Dragon();
        Wind eWind = new Wind();
        Flower eFlower = new Flower(2);

        Assert.assertNotNull("Tile Type should not be null",
                eSuit.getType());
        Assert.assertNotNull("Tile Type should not be null",
                eDragon.getType());
        Assert.assertNotNull("Tile Type should not be null",
                eWind.getType());

        Assert.assertEquals(eSuit.getType(), "Suit");
        Assert.assertEquals(eDragon.getType(), "Dragon");
        Assert.assertEquals(eWind.getType(), "Wind");
        Assert.assertEquals(eFlower.getType(), "Flower");

        Assert.assertNull("Design should be" +
                "null", eSuit.getDesign());
        Assert.assertNull("Color should be null",
                eDragon.getColor());
        Assert.assertNull("Direction should be null",
                eWind.getDirection());
    }

    @Test
    public void testPlayerSetUp() {

        String msg1 = "Set Pile should be empty at start";
        String msg2 = "Hand should be empty at start";

        Player ePlayer = new Player();

        Assert.assertEquals(msg2, ePlayer.getHandTile().size(),0);
        Assert.assertEquals(msg1, ePlayer.getSetPile().size(), 0);
        Assert.assertEquals(msg2, player.getHandTile().size(),0);
        Assert.assertEquals(msg1, player.getSetPile().size(), 0);
        Assert.assertNull("Direction should be null",
                ePlayer.getDirection());
        Assert.assertEquals("Direction should be north",
                player.getDirection(), "North");

        Assert.assertEquals("Score should be at 0 at start",
                ePlayer.getPoint(), 0);
        Assert.assertEquals("Score should be at 0 at start",
                player.getPoint(), 0);
    }

    @Test
    public void testPlayerClassMethods(){

        String msg1 = "Set Pile should be empty";
        String msg2 = "Hand should be empty";
        String msg3 = "Set Pile should not be empty";
        String msg4 = "Hand should be not empty";

        ArrayList<Tile> test = new ArrayList<>();
        test.add(bTile[1]);
        test.add(cTile[2]);
        test.add(dTile[2]);


        player.addTile(bTile[1]);
        Assert.assertEquals(msg4, player.getHandTile().size(),1);
        Assert.assertEquals(msg1, player.getSetPile().size(), 0);

        player.addTileSet(bTile[2]);
        Assert.assertEquals(msg4, player.getHandTile().size(),1);
        Assert.assertEquals(msg3, player.getSetPile().size(), 1);
        Assert.assertEquals("Tiles should be the same",
                player.getTileFromHand(0), bTile[1]);

        player.removeTileSet(0);
        Assert.assertEquals(msg2, player.getHandTile().size(),0);
        Assert.assertEquals(msg3, player.getSetPile().size(), 2);
        Assert.assertEquals("Tiles should be the same",
                player.getSetTile(1), bTile[1]);

        Assert.assertEquals("Incorrect Point value",
                player.getPoint(), 0);
        player.setPoint(5);
        Assert.assertEquals("Incorrect Point value",
                player.getPoint(), 5);

        player.clearHandPile();
        player.clearSetPile();
        Assert.assertEquals(msg1, player.getSetPile().size(), 0);
        Assert.assertEquals(msg2, player.getHandTile().size(),0);

        player.setHandTile(test);
        Assert.assertEquals("Should be the same size as " +
                "local test array", player.getHandTile().size(),
                test.size());
        Assert.assertEquals("Should be the tile",
                player.getHandTile().get(1), test.get(1));
        Assert.assertEquals("Should be the tile",
                player.getHandTile().get(2), test.get(2));


        Assert.assertEquals("Player Direction should be North",
                player.getDirection(),"North");
        player.setDirection("East");
        Assert.assertEquals("Player Direction should be East",
                player.getDirection(),"East");


    }

    // Test Player Class Errors
    @Test(expected = NullPointerException.class)
    public void testPlayerClassNullErrors1 (){

        player.addTile(null);
    }

    @Test(expected = NullPointerException.class)
    public void testPlayerClassNullErrors2 (){

        player.addTileSet(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlayerClassIllegalArgErrorsReSet(){

        // Testing for adding to set
        player.removeTileSet(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlayerClassIllegalArgErrorsReSet2() {

        // Testing for adding to set
        player.removeTileSet(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlayerClassIllegalArgErrorsReSet3(){

        // Testing for adding to set
        player.removeTileSet(-2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlayerClassIllegalArgErrorsReSet4(){

        // Testing for adding to set
        player.addTile(bTile[2]);
        player.removeTileSet(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlayerClassIllegalArgErrorsReSet5(){

        // Testing for adding to set
        player.addTile(bTile[2]);
        player.clearHandPile();
        player.removeTileSet(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlayerClassIllegalArgErrorsHand(){

        // Testing for adding to set
        player.getTileFromHand(-233);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlayerClassIllegalArgErrorsHand2() {

        // Testing for adding to set
        player.getTileFromHand(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlayerClassIllegalArgErrorsHand3() {

        // Testing for adding to set
        player.getTileFromHand(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlayerClassIllegalArgErrorsHand4() {

        // Testing for adding to set
        player.getTileFromHand(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlayerClassIllegalArgErrorsHand5() {

        // Testing for adding to set
        player.addTile(bTile[2]);
        player.getTileFromHand(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlayerClassIllegalArgErrorsGetSet(){

        // Testing for adding to set
        player.getSetTile(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlayerClassIllegalArgErrorsGetSet2() {

        // Testing for adding to set
        player.getSetTile(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlayerClassIllegalArgErrorsGetSet3() {

        // Testing for adding to set
        player.getSetTile(39);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlayerClassIllegalArgErrorsGetSet4() {

        // Testing for adding to set
        player.getSetTile(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlayerClassIllegalArgErrorsGetSet5() {

        // Testing for adding to set
        player.addTileSet(cTile[4]);
        player.getSetTile(1);
    }

    // Make sure point values can be added
    @Test(expected = IllegalArgumentException.class)
    public void testPlayerClassIllegalArgErrorsPoint(){

        // Testing for adding to set
        player.setPoint(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlayerClassIllegalArgErrorsPoint2() {

        // Testing for adding to set
        player.setPoint(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlayerClassIllegalArgErrorsPoint3() {

        // Testing for adding to set
        player.setPoint(-30);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlayerClassIllegalArgErrorsPoint4() {

        // Testing for adding to set
        player.setPoint(-203);
    }
}
