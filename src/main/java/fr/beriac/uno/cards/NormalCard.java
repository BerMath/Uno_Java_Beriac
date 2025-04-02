package fr.beriac.uno.cards;

import java.awt.*;

public class NormalCard extends ColoredCard {
    private int number;

    public NormalCard(Color  color, int number) {
        super(color);
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public boolean canBePlayedOn(Card card) {
        if (card instanceof NormalCard && ((NormalCard) card).getNumber() == this.number) {
            return true;
        }
        return super.canBePlayedOn(card);
    }

    @Override
    public String toString() {
        return getColorName() + " " + getNumber();
    }
}