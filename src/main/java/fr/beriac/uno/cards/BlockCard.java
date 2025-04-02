package fr.beriac.uno.cards;
import fr.beriac.uno.Game;

import java.awt.*;

public class BlockCard extends ColoredCard implements PoweredCard {
    public BlockCard(Color color) {
        super(color);
    }

    @Override
    public void performAction(Game game) {
        game.skipNextPlayer();
    }

    @Override
    public boolean canBePlayedOn(Card card) {
        if (card instanceof BlockCard) {
            return true;
        }
        return super.canBePlayedOn(card);
    }

    @Override
    public String toString() {
        return getColorName() + " BLOCK";
    }
}