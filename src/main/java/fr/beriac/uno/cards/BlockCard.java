package fr.beriac.uno.cards;


import fr.ynov.java.uno.Game;

import java.awt.*;

public class BlockCard extends ColoredCard implements PoweredCard {
    public BlockCard(Color color) {
        super(color);
    }

    @Override
    public void perforAction(Game game) {
        game.skipNextPlayer();
    }

    @Override
    public boolean canBePlayedOn(Card card) {
        if (card instanceof BlockCard) {
            return true;
        }
        return super.canBePlacedOn(card);
    }

    @Override
    public String toString() {
        return getColorName() + " BLOCK";
    }
}