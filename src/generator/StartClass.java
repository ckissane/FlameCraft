package generator;

import java.awt.*;
import java.net.*;
import javax.swing.*;

import java.awt.Toolkit;
public class StartClass {
    public static void main(String [] args) {
        // create an object of type CaesarCode which is the main applet class
        FlameGenerator theApplet = new FlameGenerator();
        theApplet.start();  // starts the applet

        // Create a window (JFrame) and make applet the content pane.
         JFrame window = new JFrame("FlameGenerator");
         window.setJMenuBar(new JMenuBar());
         window.setUndecorated(true);
         window.setContentPane(theApplet);
         window.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
         window.pack();              // Arrange the components.
         window.setVisible(true);    // Make the window visible.
       }
    
}