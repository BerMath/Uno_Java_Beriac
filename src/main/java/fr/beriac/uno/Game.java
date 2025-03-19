package fr.beriac.uno;

import fr.beriac.uno.cards.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Game {

    List<Card> cards = new ArrayList<>();
    List<Card> pioche = new ArrayList<>();

    static final Color[] COLORS = new Color[]{Color.RED, Color.GREEN, Color.BLUE, Color.yellow};

    public void instanciateCards() {

        for (Color color : COLORS) {
            for (int i = 1; i <= 10; i++) {
                cards.add(new NormalCard(color, i));
            }

            for (int i = 0; i < 4; i++) {
                cards.add(new BlockCard(color));
                cards.add(new SwitchCard(color));
                cards.add((new PlusCard(color)));
            }
        }

        for (int i = 0; i <= 6; i++) {
            cards.add(new Plus4Card());
            cards.add(new ChangeColorCard());
        }

        cards.forEach(System.out::println);
    }
}
