package mahjongCIS350;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class TestingGameClassMahjong {

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

    @Test
    //Chi + pair
    public void isMahjongTest1(){

        ArrayList<Tile> hand = new ArrayList<>();

        hand.add(chTile[1]);
        hand.add(chTile[1]);
        hand.add(chTile[1]);
        hand.add(chTile[2]);
        hand.add(chTile[3]);

        Assert.assertTrue("Should have Mahjong",
                game.isMahjong(hand, null));
    }

    @Test
    //chi + pair
    public void isMahjongTest2(){

        ArrayList<Tile> hand = new ArrayList<>();

        hand.add(chTile[2]);
        hand.add(chTile[3]);
        hand.add(chTile[4]);
        hand.add(chTile[4]);
        hand.add(chTile[4]);

        Assert.assertTrue("Should have Mahjong",
                game.isMahjong(hand, null));
    }

    @Test
    //pong + chi + pair
    public void isMahjongTest3(){

        ArrayList<Tile> hand = new ArrayList<>();

        hand.add(chTile[1]);
        hand.add(chTile[1]);
        hand.add(chTile[1]);
        hand.add(chTile[2]);
        hand.add(chTile[3]);
        hand.add(chTile[4]);
        hand.add(chTile[5]);
        hand.add(chTile[5]);

        Assert.assertTrue("Should have Mahjong",
                game.isMahjong(hand, null));
    }

    @Test
    //2 chis + pair
    public void isMahjongTest4(){

        ArrayList<Tile> hand = new ArrayList<>();

        hand.add(chTile[1]);
        hand.add(chTile[1]);
        hand.add(chTile[2]);
        hand.add(chTile[2]);
        hand.add(chTile[3]);
        hand.add(chTile[3]);
        hand.add(chTile[4]);
        hand.add(chTile[4]);

        Assert.assertTrue("Should have Mahjong",
                game.isMahjong(hand, null));
    }

    @Test
    //2 chis + 1 pong
    public void isMahjongTest5(){

        ArrayList<Tile> hand = new ArrayList<>();

        hand.add(chTile[1]);
        hand.add(chTile[1]);
        hand.add(chTile[2]);
        hand.add(chTile[2]);
        hand.add(chTile[3]);
        hand.add(chTile[3]);
        hand.add(bTile[5]);
        hand.add(bTile[5]);

        Assert.assertFalse("Should have Mahjong",
                game.isMahjong(hand, bTile[5]));
    }

    @Test
    //2 chis + pair and 1 tile
    public void isMahjongTest6(){

        ArrayList<Tile> hand = new ArrayList<>();

        hand.add(chTile[1]);
        hand.add(chTile[1]);
        hand.add(chTile[2]);
        hand.add(chTile[2]);
        hand.add(chTile[3]);
        hand.add(chTile[3]);
        hand.add(bTile[5]);
        hand.add(bTile[5]);

        Assert.assertFalse("Should not have Mahjong",
                game.isMahjong(hand, chTile[3]));
    }

    @Test
    //1 chi, 1 pair, 2 odd tiles out
    public void isMahjongTest7(){

        ArrayList<Tile> hand = new ArrayList<>();

        hand.add(chTile[1]);
        hand.add(chTile[1]);
        hand.add(chTile[7]);
        hand.add(chTile[2]);
        hand.add(chTile[3]);
        hand.add(chTile[3]);
        hand.add(bTile[5]);
        hand.add(bTile[5]);

        Assert.assertFalse("Should not have Mahjong",
                game.isMahjong(hand, null));
    }
    
    @Test
    //lone tile
    public void isMahjongTest8(){

        ArrayList<Tile> hand = new ArrayList<>();

        
        hand.add(bTile[5]);

        Assert.assertFalse("Should not have Mahjong",
                game.isMahjong(hand, null));
    }
    
    @Test
    //complete a pair
    public void isMahjongTest9(){

        ArrayList<Tile> hand = new ArrayList<>();

        
        hand.add(bTile[5]);

        Assert.assertTrue("Should have Mahjong",
                game.isMahjong(hand, bTile[5]));
    }
    
    
    @Test
    //complete a pong
    public void isMahjongTest10(){

        ArrayList<Tile> hand = new ArrayList<>();

        
        hand.add(bTile[5]);
        hand.add(bTile[5]);

        Assert.assertFalse("Should not have Mahjong",
                game.isMahjong(hand, bTile[5]));
    }
    
    @Test
    //complete a chi
    public void isMahjongTest11(){

        ArrayList<Tile> hand = new ArrayList<>();

        
        hand.add(bTile[4]);
        hand.add(bTile[5]);

        Assert.assertFalse("Should not have Mahjong",
                game.isMahjong(hand, bTile[6]));
    }
    
    @Test
    //3 chis + 1 pair
    public void isMahjongTest12(){

        ArrayList<Tile> hand = new ArrayList<>();

        hand.add(chTile[1]);
        hand.add(bTile[4]);
        hand.add(chTile[1]);
        hand.add(bTile[5]);
        hand.add(chTile[1]);
        hand.add(chTile[2]);
        hand.add(chTile[3]);
        hand.add(chTile[7]);
        hand.add(chTile[8]);
        hand.add(chTile[9]);

        Assert.assertTrue("Should have Mahjong",
                game.isMahjong(hand, bTile[6]));
    }
    
    @Test
    //3 pongs + 1 pair
    public void isMahjongTest13(){

        ArrayList<Tile> hand = new ArrayList<>();

        hand.add(chTile[1]);
        hand.add(chTile[7]);
        hand.add(chTile[1]);
        hand.add(chTile[6]);
        hand.add(bTile[5]);
        hand.add(chTile[6]);
        hand.add(chTile[6]);
        hand.add(chTile[7]);
        hand.add(chTile[1]);
        hand.add(chTile[7]);
        

        Assert.assertTrue("Should have Mahjong",
        		 game.isMahjong(hand, bTile[5]));
    }
    @Test
    //4 pongs + 1 pair
    public void isMahjongTest14(){

        ArrayList<Tile> hand = new ArrayList<>();

        
        hand.add(chTile[1]);
        hand.add(chTile[1]);
        hand.add(chTile[1]);
        hand.add(bTile[5]);
        hand.add(chTile[7]);
        hand.add(chTile[7]);
        hand.add(chTile[7]);
        hand.add(chTile[6]);
        hand.add(chTile[6]);
        hand.add(chTile[6]);
        hand.add(chTile[8]);
        hand.add(chTile[8]);
        hand.add(chTile[8]);
        

        Assert.assertTrue("Should have Mahjong",
                game.isMahjong(hand, bTile[5]));
    }
    
    @Test
    //4 chis + 1 pair
    public void isMahjongTest15(){

        ArrayList<Tile> hand = new ArrayList<>();

        hand.add(chTile[1]);
        hand.add(bTile[4]);
        hand.add(chTile[1]);
        hand.add(bTile[5]);
        hand.add(chTile[1]);
        hand.add(chTile[2]);
        hand.add(chTile[3]);
        hand.add(chTile[7]);
        hand.add(chTile[8]);
        hand.add(chTile[9]);
        hand.add(chTile[7]);
        hand.add(chTile[8]);
        hand.add(chTile[9]);

        Assert.assertTrue("Should have Mahjong",
                game.isMahjong(hand, bTile[6]));
    }

    //4 pongs + 1 Pair
    @Test
    public void isMahjongTest16(){

        ArrayList<Tile> hand = new ArrayList<>();


        hand.add(wTile[1]);
        hand.add(wTile[1]);
        hand.add(wTile[1]);
        hand.add(bTile[5]);
        hand.add(dTile[2]);
        hand.add(dTile[2]);
        hand.add(dTile[2]);
        hand.add(chTile[6]);
        hand.add(chTile[6]);
        hand.add(chTile[6]);
        hand.add(chTile[8]);
        hand.add(chTile[8]);
        hand.add(chTile[8]);


        Assert.assertTrue("Should have Mahjong",
                game.isMahjong(hand, bTile[5]));
    }
}

