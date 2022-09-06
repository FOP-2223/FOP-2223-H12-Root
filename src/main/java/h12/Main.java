package h12;

import h12.gui.components.MainFrame;

import javax.swing.*;
import java.io.IOException;

/**
 * Main entry point in executing the program.
 */
public class Main {

    /**
     * Main entry point in executing the program.
     *
     * @param args program arguments, currently ignored
     */
    public static void main(String[] args) {

        //These two lines are needed to display the GUI but can be removed if this method is used for something else.
        JFrame frame = new MainFrame();
        frame.setVisible(true);
    }
}
