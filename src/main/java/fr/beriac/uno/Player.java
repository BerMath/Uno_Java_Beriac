package fr.beriac.uno;


import fr.beriac.uno.cards.Card;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private ArrayList<Card> hand;
    private String name;
    private boolean isAI;
    Card[] cards = new Card[6];

    public Player(String name, boolean isAI) {
        this.name = name;
        this.isAI = isAI;
        this.hand = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public boolean isAI() {
        return isAI;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public void removeCard(Card card) {
        hand.remove(card);
    }

    public boolean hasWon() {

        return hand.isEmpty();
    }

    public Card findPlayableCard(Card topCard) {
        for (Card card : hand) {
            if (card.canBePlayedOn(topCard)) {
                return card;
            }
        }
        return null;
    }
}