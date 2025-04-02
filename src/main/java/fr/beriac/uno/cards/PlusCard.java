// PlusCard.java
package fr.beriac.uno.cards;
import fr.beriac.uno.Game;
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
        // Next player draws 2 cards
        game.makeNextPlayerDraw(power);
    }

    @Override
    public boolean canBePlayedOn(Card card) {
        if (card instanceof PlusCard) {
            return true;
        }
        return super.canBePlayedOn(card);
    }

    @Override
    public String toString() {
        return getColorName() + " +2";
    }


}