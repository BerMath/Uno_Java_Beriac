package fr.beriac.uno.cards;

import java.awt.*;

public class Plus4Card extends Card implements PoweredCard {
    private java.awt.Color chosenColor;

    public java.awt.Color getChosenColor() {
        return chosenColor;
    }

    public void setChosenColor(java.awt.Color color) {
        this.chosenColor = color;
    }

    @Override
    public void performAction(fr.ynov.java.uno.Game game) {
        // Ask current player to choose a color
        this.chosenColor = game.getCurrentPlayer().chooseColor();

        // Next player draws 4 cards
        game.makeNextPlayerDraw(4);
    }

    @Override
    public boolean canBePlayedOn(Card card) {
        // Can be played on any card
        return true;
    }

    @Override
    public String toString() {
        return "WILD +4";
    }
}