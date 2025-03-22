package fr.beriac.uno.cards;

public abstract class Card {
    @Override
    public abstract String toString();


    public abstract boolean canBePlayedOn(Card card);

}