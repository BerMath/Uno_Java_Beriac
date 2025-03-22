package fr.beriac.uno.cards;

import java.awt.*;

public class SwtichCard extends ColoredCard implements PoweredCard {
    public SwtichCard(Color color) {
        super(color);
    }

    @Override
    public void performAction(fr.ynov.java.uno.Game game){
        game.reverseDirection();
    }

    @Override
    public boolean canBePlayedOn(Card card) {
        if (card instanceof SwtichCard) {
            return true;
        }
        return super.canBePlacedOn(card);
    }

    @Override
    public String toString() {
        return getColorName() + " SWITCH";
    }
}