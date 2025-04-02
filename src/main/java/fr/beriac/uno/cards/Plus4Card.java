package fr.beriac.uno.cards;
import fr.beriac.uno.Game;
import java.awt.*;
import java.util.Scanner;

public class Plus4Card extends Card implements PoweredCard {
    private java.awt.Color chosenColor;

    public java.awt.Color getChosenColor() {
        return chosenColor;
    }

    public void setChosenColor(java.awt.Color color) {
        this.chosenColor = color;
    }

    @Override
    public void performAction(Game game) {
        // Dans l'interface graphique, la couleur est déjà définie via setChosenColor
        // Ne pas appeler showColorChooser() ici pour éviter le blocage
        // Faire piocher 4 cartes au joueur suivant
        game.nextPlayer();
        game.makeNextPlayerDraw(4);
        game.nextPlayer();
    }

    private String colorToString(Color color) {
        if (color.equals(Color.RED)) return "ROUGE";
        if (color.equals(Color.GREEN)) return "VERT";
        if (color.equals(Color.BLUE)) return "BLEU";
        if (color.equals(Color.YELLOW)) return "JAUNE";
        return "INCONNUE";
    }

    @Override
    public boolean canBePlayedOn(Card card) {
        return true;
    }

    @Override
    public String toString() {
        return "WILD +4";
    }
}

