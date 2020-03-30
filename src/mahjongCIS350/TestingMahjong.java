package mahjongCIS350;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestingMahjong {

    Dragon []dTile = new Dragon[3];
    Wind []wTile = new Wind[4];


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
}
