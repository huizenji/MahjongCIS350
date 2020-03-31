package mahjongCIS350;

import org.junit.Before;

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
}
