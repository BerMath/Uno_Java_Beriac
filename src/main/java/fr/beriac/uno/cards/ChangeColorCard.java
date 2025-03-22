package fr.beriac.uno.cards;

import java.awt.*;

public class ChangeColorCard extends Card implements PoweredCard {
    private java.awt.Color chosenColor;

    public java.awt.Color getChosenColor() {
        return chosenColor;
    }

    public void  setChosenColor(java.awt.Color color) {
        this.chosenColor = color;
    }

    @Override
    public void performAction(fr.ynov.java.uno.Game game) {
        this.chosenColor = game.getCurrentPlyer().chosenColor();
    }

    @Override
    public boolean canBePlayedOn(Card card) {
            return true;
    }

    @Override
    public String toString() {
        return "WILD)";
    }
}