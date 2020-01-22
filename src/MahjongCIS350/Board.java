package MahjongCIS350;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Board extends JPanel{

    private Game game;

    private ImageIcon circle1;
    private ImageIcon circle2;
    private ImageIcon circle3;
    private ImageIcon circle4;
    private ImageIcon circle5;
    private ImageIcon circle6;
    private ImageIcon circle7;
    private ImageIcon circle8;
    private ImageIcon circle9;

    private ImageIcon bamboo1;
    private ImageIcon bamboo2;
    private ImageIcon bamboo3;
    private ImageIcon bamboo4;
    private ImageIcon bamboo5;
    private ImageIcon bamboo6;
    private ImageIcon bamboo7;
    private ImageIcon bamboo8;
    private ImageIcon bamboo9;

    private ImageIcon character1;
    private ImageIcon character2;
    private ImageIcon character3;
    private ImageIcon character4;
    private ImageIcon character5;
    private ImageIcon character6;
    private ImageIcon character7;
    private ImageIcon character8;
    private ImageIcon character9;


    public Board(){
        game = new Game();

        createIcons();


    }


    private void createIcons() {
        // Sets the Image for circle pieces
        circle1 = new ImageIcon("./src/MahjongCIS350/Images/circle1.jpg");
        circle2 = new ImageIcon("./src/MahjongCIS350/Images/circle2.jpg");
        circle3 = new ImageIcon("./src/MahjongCIS350/Images/circle3.jpg");
        circle4 = new ImageIcon("./src/MahjongCIS350/Images/circle4.jpg");
        circle5 = new ImageIcon("./src/MahjongCIS350/Images/circle5.jpg");
        circle6 = new ImageIcon("./src/MahjongCIS350/Images/circle6.jpg");
        circle7 = new ImageIcon("./src/MahjongCIS350/Images/circle7.jpg");
        circle8 = new ImageIcon("./src/MahjongCIS350/Images/circle8.jpg");
        circle9 = new ImageIcon("./src/MahjongCIS350/Images/circle9.jpg");

        // Sets the Image for bamboo pieces
        bamboo1 = new ImageIcon("./src/MahjongCIS350/Images/bamboo1.jpg");
        bamboo2 = new ImageIcon("./src/MahjongCIS350/Images/bamboo2.jpg");
        bamboo3 = new ImageIcon("./src/MahjongCIS350/Images/bamboo3.jpg");
        bamboo4 = new ImageIcon("./src/MahjongCIS350/Images/bamboo4.jpg");
        bamboo5 = new ImageIcon("./src/MahjongCIS350/Images/bamboo5.jpg");
        bamboo6 = new ImageIcon("./src/MahjongCIS350/Images/bamboo6.jpg");
        bamboo7 = new ImageIcon("./src/MahjongCIS350/Images/bamboo7.jpg");
        bamboo8 = new ImageIcon("./src/MahjongCIS350/Images/bamboo8.jpg");
        bamboo9 = new ImageIcon("./src/MahjongCIS350/Images/bamboo9.jpg");

        // Sets the Image for character pieces
        character1 = new ImageIcon("./src/MahjongCIS350/Images/character1.jpg");
        character2 = new ImageIcon("./src/MahjongCIS350/Images/character2.jpg");
        character3 = new ImageIcon("./src/MahjongCIS350/Images/character3.jpg");
        character4 = new ImageIcon("./src/MahjongCIS350/Images/character4.jpg");
        character5 = new ImageIcon("./src/MahjongCIS350/Images/character5.jpg");
        character6 = new ImageIcon("./src/MahjongCIS350/Images/character6.jpg");
        character7 = new ImageIcon("./src/MahjongCIS350/Images/character7.jpg");
        character8 = new ImageIcon("./src/MahjongCIS350/Images/character8.jpg");
        character9 = new ImageIcon("./src/MahjongCIS350/Images/character9.jpg");
    }

}
