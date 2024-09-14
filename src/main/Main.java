package main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //this lets the window close properly when clicked on "x" button
        window.setResizable(false); //cannot resize this window
        window.setTitle("2D Adventure");

        window.setLocationRelativeTo(null); //does not specify the location of the window, default it will be displayed at the center
        window.setVisible(true);  //we can see this window

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack();
        gamePanel.setupGame();
        gamePanel.startGameThread();
    }
}