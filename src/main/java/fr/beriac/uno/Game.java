package fr.beriac.uno;

import fr.beriac.uno.cards.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Game {
    private List<Card> cards;
    private List<Card> deck;
    private List<Card> discardPile;
    private List<Player> players = new ArrayList<>();

    private Card currentCard;
    private int currentPlayerIndex;
    private boolean clockWise;

    static final Color[] COLORS = new Color[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};

    public Game() {
        this.deck = new ArrayList<>();
        this.discardPile = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.clockWise = true;
    }


    public void initializeGame() {
        System.out.println("🎮 Initialisation de la partie !");

        players.add(new Player("Joueur 1", false));
        players.add(new Player("AI 1", true));
        players.add(new Player("AI 2", true));
        players.add(new Player("AI 3", true));

        List<Card> initialCards = createInitialCards();
        deck.addAll(initialCards);
        System.out.println("Deck initialisé avec " + deck.size() + " cartes.");
        Collections.shuffle(deck);
        dealCards();
    }

    public void startGame() {
        initializeGame();

        while (!isGameOver()) {
            Player currentPlayer = getCurrentPlayer();

            if (currentPlayer.isAI()) {
                aiPlayTurn(currentPlayer);
            } else {
                humanPlayTurn(currentPlayer);
            }
        }
    }

    public List<Card> createInitialCards() {
        List<Card> cards = new ArrayList<>();
        for (Color color : COLORS) {
            for (int i = 0; i <= 9; i++) {
                cards.add(new NormalCard(color, i));
            }
            for (int i = 0; i < 6; i++) {
                cards.add(new BlockCard(color));
                cards.add(new SwitchCard(color));
                cards.add(new PlusCard(color));
            }
        }
        for (int i = 0; i < 6; i++) {
            cards.add(new Plus4Card());
            cards.add(new ChangeColorCard());
        }
        System.out.println("Cartes créées : " + cards.size());
        return cards;
    }

    public void dealCards() {
        if (deck.isEmpty()) {
            throw new IllegalStateException("Impossible de distribuer des cartes, le deck est vide !");
        }
        for (int i = 0; i < 7; i++) { // Distribuer 7 cartes à chaque joueur
            for (Player player : players) {
                Card drawnCard = drawFromDeck();
                player.addCard(drawnCard);
                System.out.println(player.getName() + " reçoit : " + drawnCard);
            }
        }

        for (Player player : players) {
            System.out.println(player.getName() + " a en main : " + player.getHand().size() + " cartes.");
        }

        Card firstCard;
        do {
            if (deck.isEmpty()) {
                throw new IllegalStateException("Impossible de tirer une carte, le deck est vide !");
            }
            firstCard = drawFromDeck();
        } while (firstCard instanceof PoweredCard);

        discardPile.add(firstCard);
        currentCard = firstCard;
        System.out.println("🃏 Première carte sur la pile de défausse : " + currentCard);
    }

    public Card drawFromDeck() {
        System.out.println("Cartes restantes dans le deck : " + deck.size());

        if (deck.isEmpty()) {
            if (discardPile.isEmpty()) {
                throw new IllegalStateException("Le deck et la pile de défausse sont vides !");
            }
            Card topCard = discardPile.remove(discardPile.size() - 1);
            deck.addAll(discardPile);
            discardPile.clear();
            discardPile.add(topCard);
            Collections.shuffle(deck);
            System.out.println("♻️ Le deck a été re-mélangé !");
        }

        if (deck.isEmpty()) {
            throw new IllegalStateException("Le deck est vide après re-mélange !");
        }

        return deck.remove(0);
    }

    public void playCard(Card card) {
        Player currentPlayer = players.get(currentPlayerIndex);

        if (card.canBePlayedOn(currentCard)) {
            currentPlayer.removeCard(card);
            discardPile.add(card);
            currentCard = card;

            System.out.println("✅ " + currentPlayer.getName() + " a joué : " + card);
            System.out.println("Nouvelle carte en jeu : " + currentCard);

            if (currentPlayer.hasWon()) {
                System.out.println("🏆 " + currentPlayer.getName() + " a gagné la partie !");
                return;
            }

            if (card instanceof PoweredCard) {
                ((PoweredCard) card).performAction(this);
            } else {
                nextPlayer();
            }

            if (card instanceof BlockCard) {
                System.out.println("🚫 " + currentPlayerIndex + 1 + " est bloqué et passe son tour !");
                skipNextPlayer();
            }
        } else {
            System.out.println("❌ Coup invalide ! " + card + " ne peut pas être jouée sur " + currentCard);
        }
    }

    public void skipNextPlayer() {
    }

    public void nextPlayer() {
        String previousPlayer = players.get(currentPlayerIndex).getName();
        if (clockWise) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } else {
            currentPlayerIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
        }
        System.out.println("🔄 Passage au joueur suivant : " + players.get(currentPlayerIndex).getName() + " (Après " + previousPlayer + ")");
    }

    public void reverseDirection() {
        clockWise = !clockWise;
        System.out.println("🔁 Le sens du jeu a changé ! Sens actuel : " + (clockWise ? "horaire" : "anti-horaire"));
        nextPlayer();
    }

    public void makeNextPlayerDraw(int count) {
        int nextPlayerIndex = clockWise
                ? (currentPlayerIndex + 1) % players.size()
                : (currentPlayerIndex - 1 + players.size()) % players.size();

        Player nextPlayer = players.get(nextPlayerIndex);
        System.out.println(nextPlayer.getName() + " doit piocher " + count + " cartes !");

        for (int i = 0; i < count; i++) {
            Card drawnCard = drawFromDeck();
            nextPlayer.addCard(drawnCard);
            System.out.println(nextPlayer.getName() + " pioche : " + drawnCard);
        }
        nextPlayer();
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public List<Player> getPlayers() {
        return players;
    }

    Color getChoosenColor(int numColor) {
        return switch (numColor) {
            case 0 -> Color.RED;
            case 1 -> Color.GREEN;
            case 2 -> Color.BLUE;
            case 3 -> Color.YELLOW;
            default -> null;
        };
    }

    public void showColorChooser() {
        Player currentPlayer = getCurrentPlayer();
        Color chosenColor = null;

        if (!currentPlayer.isAI()) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("🎨 Choisissez une couleur : ");
            System.out.println("0 : ROUGE");
            System.out.println("1 : VERT");
            System.out.println("2 : BLEU");
            System.out.println("3 : JAUNE");
            String color = scanner.nextLine();

            try {
                chosenColor = getChoosenColor(Integer.parseInt(color));
            } catch (NumberFormatException ignored) {
                // ignored
            } finally {
                if (chosenColor == null) {
                    showColorChooser();
                }
            }
        } else {
            int randomColor = (int) (Math.random() * 4);
            chosenColor = getChoosenColor(randomColor);
        }

        if (chosenColor != null) {
            String colorName = colorToString(chosenColor);
            System.out.println("🎨 " + currentPlayer.getName() + " a choisi la couleur : " + colorName);

            if (currentCard instanceof ChangeColorCard) {
                ((ChangeColorCard) currentCard).setChosenColor(chosenColor);
            } else if (currentCard instanceof Plus4Card) {
                ((Plus4Card) currentCard).setChosenColor(chosenColor);
            } else {
                System.out.println("❌ La carte actuelle n'est pas une carte de changement de couleur ou +4 !");
            }
        }
    }

    // Méthode pour convertir Color en nom lisible
    private String colorToString(Color color) {
        if (color.equals(Color.RED)) return "ROUGE";
        if (color.equals(Color.GREEN)) return "VERT";
        if (color.equals(Color.BLUE)) return "BLEU";
        if (color.equals(Color.YELLOW)) return "JAUNE";
        return "INCONNUE";
    }


    private void aiPlayTurn(Player aiPlayer) {
        Card playableCard = aiPlayer.findPlayableCard(currentCard);

        if (playableCard != null) {
            System.out.println(aiPlayer.getName() + " a joué : " + playableCard);
            playCard(playableCard);
        } else {
            Card drawnCard = drawFromDeck();
            aiPlayer.addCard(drawnCard);
            System.out.println(aiPlayer.getName() + " a pioché : " + drawnCard);

            if (drawnCard.canBePlayedOn(currentCard)) {
                System.out.println(aiPlayer.getName() + " joue la carte piochée : " + drawnCard);
                playCard(drawnCard);
            } else {
                nextPlayer();
            }
        }
    }

    private void humanPlayTurn(Player humanPlayer) {
        System.out.println("C'est votre tour de jouer !");

        System.out.println(("Vos cartes : " + humanPlayer.getHand()));

        Card chosenCard = getUserSelectedCard(humanPlayer);

        if (chosenCard != null) {
            playCard(chosenCard);
        } else {
            Card drawnCard = drawFromDeck();
            humanPlayer.addCard(drawnCard);
            System.out.println("Vous avez pioché : " + drawnCard);

            if (drawnCard.canBePlayedOn(currentCard)) {
                playCard(drawnCard);
            } else {
                nextPlayer();
            }
        }
    }

    private Card getUserSelectedCard(Player humanPlayer) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Sélectionnez une carte à jouer ou tapez 'pioche' pour piocher une carte :");

        List<Card> hand = humanPlayer.getHand();
        for (int i = 0; i < hand.size(); i++) {
            System.out.println(i + " : " + hand.get(i));
        }
        System.out.print("Votre choix : ");
        String input = scanner.nextLine();

        if (input.trim().equalsIgnoreCase("pioche")) {
            return null;
        }

        try {
            int cardIndex = Integer.parseInt(input);
            if (cardIndex >= 0 && cardIndex < hand.size()) {
                return hand.get(cardIndex);
            } else {
                System.out.println("❌ Indice de carte invalide !");
                return getUserSelectedCard(humanPlayer);
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Veuillez entrer un nombre valide !");
            return getUserSelectedCard(humanPlayer);
        }
    }


    public boolean isGameOver() {
        for (Player player : players) {
            if(player.hasWon()) {
                return true;
            }
        }
        return false;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
}