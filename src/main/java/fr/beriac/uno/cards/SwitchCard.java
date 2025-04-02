package fr.beriac.uno.cards;
import fr.beriac.uno.Game;
import java.awt.*;

public class SwitchCard extends ColoredCard implements PoweredCard {
    public SwitchCard(Color color) {
        super(color);
    }

    @Override
    public void performAction(Game game){
        game.reverseDirection();
    }

    @Override
    public boolean canBePlayedOn(Card card) {
        if (card instanceof SwitchCard) {
            return true;
        }
        return super.canBePlayedOn(card);
    }

    @Override
    public String toString() {
        return getColorName() + " SWITCH";
    }
}