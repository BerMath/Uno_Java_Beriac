package fr.beriac.uno.cards;

import java.awt.*;

public class NormalCard extends ColoredCard {
    int number;

    public NormalCard(Color color, int number) {
        super(color);
        this.number = number;
    }
}
