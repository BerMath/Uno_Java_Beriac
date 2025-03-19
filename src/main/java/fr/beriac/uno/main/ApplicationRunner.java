package fr.beriac.uno.main;

import fr.beriac.uno.Game;

public class ApplicationRunner {

    public static void main(String[] args) {
        final Game game = new Game();

        game.instanciateCards();
    }
}
