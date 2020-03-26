package mahjongCIS350;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

/***********************************************************************
 * This class allows the user to change the settings in the game.
 * Such as AI difficulty or color.
 * @Authors: Wayne Chen, Jillian Huizenga, Chris Paul, Aron Zhao
 * @Version: 3/26/2020
 **********************************************************************/
public class Setting extends JPanel {

    /** Default Background color of Red Shade. **/
    private final int defaultR = 0;

    /** Default Background color of Green Shade. **/
    private final int defaultG = 150;

    /** Default Background color of Blue Shade. **/
    private final int defaultB = 100;

    /** The maximum color shade of each color. **/
    private final int maxShade = 255;

    /** The minimum color shade of each color. **/
    private final int minShade = 0;

    /** Represents JSlider for the red shade. **/
    private JSlider redShade;

    /** Represents JSlider for the green shade. **/
    private JSlider greenShade;

    /** Represents JSlider for the blue shade. **/
    private JSlider blueShade;

    /** JButton To Apply Changes. **/
    private JButton apply;

    /** JButton To return to default settings. **/
    private JButton defaultSetting;

    /** Background Color of GameBoard. **/
    private JButton bgColor;

    /** The label of the minimum value side. **/
    private final String minLabel = "0";

    /** The label of the maximum value side. **/
    private final String maxLabel = "255";

    /** Listener for all Sliders. **/
    private final SliderListener slider;

    /** Button Listener for all applicable buttons. **/
    private final ButtonListener button;

    /** JPanel of Board, use to connect the 2 Panels. **/
    private final Board board;

    /*******************************************************************
     * This method is the constructor for the Setting Class.
     * @param gameBoard The game board JPanel of which the main board is
     *                  on.
     ******************************************************************/
    public Setting(final Board gameBoard) {

        super();

        this.board = gameBoard;
        int numGrid = 4;

        // Set Overall Layout
        setLayout(new GridLayout(numGrid, 0));

        // Initiate Listeners
        slider = new SliderListener();
        button = new ButtonListener();

        // Setting up Panels
        setBgColorPanel();
        applySetting();
    }

    /*******************************************************************
     * This method sets up the background color settings.
     ******************************************************************/
    private void setBgColorPanel() {

        int numColor = 3;
        int colorLabel = (maxShade - minShade) / 2;

        // Create the panel
        final JPanel bgColor = new JPanel();
        final JPanel colorSlider = new JPanel();
        bgColor.setLayout(new BorderLayout());
        colorSlider.setLayout(new GridLayout(0, numColor));

        // Creating the JSliders
        redShade = new JSlider(JSlider.HORIZONTAL, minShade, maxShade,
                defaultR);
        greenShade = new JSlider(JSlider.HORIZONTAL, minShade, maxShade,
                defaultG);
        blueShade = new JSlider(JSlider.HORIZONTAL, minShade, maxShade,
                defaultB);

        // CustomLabel for each JSlider
        final Hashtable rLabel = new Hashtable();
        rLabel.put(minShade, new JLabel(minLabel));
        rLabel.put(maxShade, new JLabel(maxLabel));
        rLabel.put(colorLabel, new JLabel("Red"));

        final Hashtable gLabel = new Hashtable();
        gLabel.put(minShade, new JLabel(minLabel));
        gLabel.put(maxShade, new JLabel(maxLabel));
        gLabel.put(colorLabel, new JLabel("Green"));

        final Hashtable bLabel = new Hashtable();
        bLabel.put(minShade, new JLabel(minLabel));
        bLabel.put(maxShade, new JLabel(maxLabel));
        bLabel.put(colorLabel, new JLabel("Blue"));

        redShade.setLabelTable(rLabel);
        greenShade.setLabelTable(gLabel);
        blueShade.setLabelTable(bLabel);

        redShade.setPaintLabels(true);
        greenShade.setPaintLabels(true);
        blueShade.setPaintLabels(true);

        // Add Listener
        redShade.addChangeListener(slider);
        greenShade.addChangeListener(slider);
        blueShade.addChangeListener(slider);

        // Add to Panel
        colorSlider.add(redShade);
        colorSlider.add(greenShade);
        colorSlider.add(blueShade);
        bgColor.add(colorSlider, BorderLayout.CENTER);
        bgColor.add(new JLabel("Background Color Adjustment",
                        SwingConstants.CENTER), BorderLayout.NORTH);

        // Add to main Component
        add(bgColor);
    }

    private void applySetting() {

        JPanel applyPanel = new JPanel();
        JPanel buttomPanel = new JPanel();
        int totalButton = 2;

        apply = new JButton("Apply");
        defaultSetting = new JButton("Default");
        bgColor = new JButton("Background Color");

        apply.addActionListener(button);
        defaultSetting.addActionListener(button);

        bgColor.setBackground(new Color(redShade.getValue(),
                greenShade.getValue(), blueShade.getValue()));

        buttomPanel.setLayout(new GridLayout(0, totalButton));
        buttomPanel.add(apply);
        buttomPanel.add(defaultSetting);
        applyPanel.setLayout(new BorderLayout());
        applyPanel.add(buttomPanel, BorderLayout.SOUTH);
        applyPanel.add(bgColor, BorderLayout.CENTER);

        // Add to Main Panel
        add(applyPanel);
    }

    /*******************************************************************
     * This is a private class for any slider listener.
     ******************************************************************/
    private class ButtonListener implements ActionListener {

        /***************************************************************
         * Action Performed by any JSlider.
         * @param event Which slider was triggered
         **************************************************************/
        @Override
        public void actionPerformed(ActionEvent event) {

            if (event.getSource() == apply) {
                board.updateBgColor(redShade.getValue(),
                        greenShade.getValue(), blueShade.getValue());
            } else if(event.getSource() == defaultSetting){
                board.updateBgColor(defaultR, defaultG, defaultB);
                bgColor.setBackground(new Color(defaultR, defaultG,
                        defaultB));
            }
        }
    }

    /*******************************************************************
     * This is a private class for any slider listener.
     ******************************************************************/
    private class SliderListener implements ChangeListener {

        /***************************************************************
         * Action Performed by any JSlider.
         * @param event Which slider was triggered
         **************************************************************/
        @Override
        public void stateChanged(final ChangeEvent event) {

                bgColor.setBackground(new Color(redShade.getValue(),
                        greenShade.getValue(), blueShade.getValue()));

        }
    }
}

