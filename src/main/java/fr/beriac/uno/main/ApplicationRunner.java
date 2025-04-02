package fr.beriac.uno.main;

import fr.beriac.uno.gui.GameFrame;

import javax.swing.*;

public class ApplicationRunner {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                GameController controller = new GameController();
                GameFrame frame = new GameFrame(controller);
                frame.setVisible(true);
                System.out.println("GUI lancé !");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}