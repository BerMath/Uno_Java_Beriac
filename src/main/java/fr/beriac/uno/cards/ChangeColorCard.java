package fr.beriac.uno.cards;

import java.awt.*;

public class ChangeColorCard extends Card implements PoweredCard {
    private Color chosenColor;

    public ChangeColorCard() {
        this.chosenColor = null;
    }

    public void SetChosenColor(Color chosenColor) {
        this.chosenColor = chosenColor;
    }

    public Color getChosenColor() {
        return this.chosenColor;
    }

    @Override
    public void performAction(Game game) {
        game.showColorChooser(this);
    }

    @Override
    public boolean canBePlacedOn(Card card) {
        return true; // Can be placed on any card
    }

    @Override
    public void render(Graphics g, int x, int y, int width, int height) {
        if (chosenColor != null) {
            g.setColor(chosenColor);
            g.fillRoundRect(x, y, width, height, 10, 10);
        } else {
            // Draw four-colored card
            g.setColor(Color.RED);
            g.fillRoundRect(x, y, width / 2, height / 2, 10, 10);
            g.setColor(Color.BLUE);
            g.fillRoundRect(x + width / 2, y, width / 2, height / 2, 10, 10);
            g.setColor(Color.GREEN);
            g.fillRoundRect(x, y + height / 2, width / 2, height / 2, 10, 10);
            g.setColor(Color.YELLOW);
            g.fillRoundRect(x + width / 2, y + height / 2, width / 2, height / 2, 10, 10);
        }
        g.setColor(Color.WHITE);
        g.drawRoundRect(x, y, width, height, 10, 10);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString("WILD", x + width / 2 - 20, y + height / 2 + 5);
    }

    @Override
    public String toString() {
        return "Wild";
    }
}