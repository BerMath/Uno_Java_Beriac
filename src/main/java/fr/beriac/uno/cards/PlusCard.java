package fr.beriac.uno.cards;

import fr.ynov.java.uno.Game;

import java.awt.*;

public class PlusCard extends ColoredCard implements PoweredCard {
    private int power = 2;

    public PlusCard(Color color) {
        super(color);
    }

    public int getPower() {
        return power;
    }

    @Override
    public void performAction(Game game) {
        game.makeNextPlayerDraw(power);
    }

    @Override
    public boolean canBePlayedOn(Card card) {
        if (card instanceof PlusCard) {
            return true;
        }
        return super.canBePlacedOn(card);
    }

    @Override
    public String toString() {
        return getColorName() + " +2";
    }
}