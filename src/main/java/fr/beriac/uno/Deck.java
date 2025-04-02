package fr.beriac.uno;

import fr.beriac.uno.cards.Card;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Deck {
    private Stack<Card> cards;

    public Deck(List<Card> initialCards) {
        this.cards = new Stack<>();
        Collections.shuffle(initialCards);
        this.cards.addAll(initialCards);
    }

    public Card drawCard() {
        if (!cards.isEmpty()) {
            return cards.pop();
        }
        return null;
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public void addCardToBottom(Card card) {
        cards.insertElementAt(card, 0);
    }
}