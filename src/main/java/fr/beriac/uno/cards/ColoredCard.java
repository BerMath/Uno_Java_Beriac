package fr.beriac.uno.cards;

import java.awt.*;

public abstract class ColoredCard extends Card {
    private Color color;

    public ColoredCard(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public boolean  canBePlacedOn(Card card) {
        if (card instanceof ColoredCard) {
            return ((ColoredCard) card).getColor().equals(this.color);
        }
        return false;

    }

    protected String getColorName() {
        if (color.equals(Color.RED)) return "RED";
        if (color.equals(Color.GREEN)) return "GREEN";
        if (color.equals(Color.BLUE)) return "BLUE";
        if (color.equals(Color.YELLOW)) return "YELLOW";
        return "UNKNOWN";
    }
}