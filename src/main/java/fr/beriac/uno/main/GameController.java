package fr.beriac.uno.main;

import fr.beriac.uno.Game;
import fr.beriac.uno.Player;
import fr.beriac.uno.cards.Card;
import fr.beriac.uno.cards.ChangeColorCard;
import fr.beriac.uno.cards.Plus4Card;
import fr.beriac.uno.gui.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameController {
    private Game game;
    private GameFrame frame;
    private boolean gameStarted = false;
    private Timer aiTimer;

    public GameController() {
        this.game = new Game();
        // Timer pour les tours des IAs (joue toutes les 1.5 secondes)
        this.aiTimer = new Timer(1500, e -> playAITurn());
    }

    public void setFrame(GameFrame frame) {
        this.frame = frame;
    }

    public void initializeGame() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                game.initializeGame();
                return null;
            }

            @Override
            protected void done() {
                gameStarted = true;
                updateUI();

                // Démarrer les tours si c'est à une IA de jouer
                checkAndStartAITurn();
            }
        };
        worker.execute();
    }

    public Game getGame() {
        return game;
    }

    public Card getCurrentCard() {
        return game.getCurrentCard();
    }

    public Player getCurrentPlayer() {
        return game.getCurrentPlayer();
    }

    public List<Card> getPlayerHand() {
        return getCurrentPlayer().getHand();
    }

    public void playCard(Card card) {
        System.out.println("Tentative de jouer une carte: " + card);

        if (card instanceof ChangeColorCard || card instanceof Plus4Card) {
            // Afficher un sélecteur de couleur pour les cartes spéciales
            Color chosenColor = showColorChooserDialog();

            if (chosenColor != null) {
                if (card instanceof ChangeColorCard) {
                    ((ChangeColorCard) card).setChosenColor(chosenColor);
                    System.out.println("Couleur définie pour ChangeColorCard: " + colorToString(chosenColor));
                } else if (card instanceof Plus4Card) {
                    ((Plus4Card) card).setChosenColor(chosenColor);
                    System.out.println("Couleur définie pour Plus4Card: " + colorToString(chosenColor));
                }

                // Maintenant jouer la carte
                System.out.println("Jouer la carte après avoir défini la couleur");
                game.playCard(card);

                System.out.println("Carte jouée avec succès");
                updateUI();

                // Vérifier si c'est maintenant au tour d'une IA
                checkAndStartAITurn();
            } else {
                System.out.println("Aucune couleur choisie, la carte n'a pas été jouée");
            }
        } else {
            // Pour les cartes normales
            System.out.println("Jouer une carte normale");
            game.playCard(card);
            updateUI();

            // Vérifier si c'est maintenant au tour d'une IA
            checkAndStartAITurn();
        }

        System.out.println("Fin de playCard, joueur actuel: " + getCurrentPlayer().getName());
    }


    private Color showColorChooserDialog() {
        String[] options = {"ROUGE", "VERT", "BLEU", "JAUNE"};
        int choice = JOptionPane.showOptionDialog(frame,
                "Choisissez une couleur:",
                "Sélection de couleur",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        switch (choice) {
            case 0: return Color.RED;
            case 1: return Color.GREEN;
            case 2: return Color.BLUE;
            case 3: return Color.YELLOW;
            default: return Color.RED; // Par défaut si l'utilisateur ferme la boîte de dialogue
        }
    }

    private String colorToString(Color color) {
        if (color.equals(Color.RED)) return "ROUGE";
        if (color.equals(Color.GREEN)) return "VERT";
        if (color.equals(Color.BLUE)) return "BLEU";
        if (color.equals(Color.YELLOW)) return "JAUNE";
        return "INCONNUE";
    }

    public void drawCard() {
        Card card = game.drawFromDeck();
        getCurrentPlayer().addCard(card);

        // Si la carte piochée peut être jouée, demander au joueur
        if (card.canBePlayedOn(game.getCurrentCard())) {
            int response = JOptionPane.showConfirmDialog(frame,
                    "Vous avez pioché " + card + ". Voulez-vous la jouer ?",
                    "Jouer la carte", JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                game.playCard(card);
            } else {
                game.nextPlayer();
            }
        } else {
            // Passer au joueur suivant
            game.nextPlayer();
        }

        updateUI();

        // Vérifier si c'est maintenant au tour d'une IA
        checkAndStartAITurn();
    }

    private void playAITurn() {
        if (!gameStarted || game.isGameOver()) {
            aiTimer.stop();
            return;
        }

        Player currentPlayer = game.getCurrentPlayer();
        System.out.println("Tour de l'IA: " + currentPlayer.getName());

        if (currentPlayer.isAI()) {
            // Trouve une carte jouable
            Card playableCard = currentPlayer.findPlayableCard(game.getCurrentCard());

            if (playableCard != null) {
                System.out.println("L'IA joue: " + playableCard);

                // Si c'est une carte Wild ou +4, choisir une couleur aléatoire
                if (playableCard instanceof ChangeColorCard || playableCard instanceof Plus4Card) {
                    Color randomColor = getRandomColor();
                    String colorName = colorToString(randomColor);
                    System.out.println("L'IA choisit la couleur: " + colorName);

                    if (playableCard instanceof ChangeColorCard) {
                        ((ChangeColorCard) playableCard).setChosenColor(randomColor);
                    } else if (playableCard instanceof Plus4Card) {
                        ((Plus4Card) playableCard).setChosenColor(randomColor);
                    }

                    // L'IA joue une carte
                    game.playCard(playableCard);

                    // Afficher un message pour la couleur choisie
                    JOptionPane.showMessageDialog(frame,
                            currentPlayer.getName() + " a choisi la couleur: " + colorName,
                            "Couleur choisie", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // L'IA joue une carte normale
                    game.playCard(playableCard);
                }
            } else {
                System.out.println("L'IA pioche une carte");
                // L'IA pioche
                Card drawnCard = game.drawFromDeck();
                currentPlayer.addCard(drawnCard);

                // Si la carte piochée peut être jouée
                if (drawnCard.canBePlayedOn(game.getCurrentCard())) {
                    System.out.println("L'IA joue la carte piochée: " + drawnCard);

                    if (drawnCard instanceof ChangeColorCard || drawnCard instanceof Plus4Card) {
                        Color randomColor = getRandomColor();
                        String colorName = colorToString(randomColor);
                        System.out.println("L'IA choisit la couleur: " + colorName);

                        if (drawnCard instanceof ChangeColorCard) {
                            ((ChangeColorCard) drawnCard).setChosenColor(randomColor);
                        } else if (drawnCard instanceof Plus4Card) {
                            ((Plus4Card) drawnCard).setChosenColor(randomColor);
                        }

                        game.playCard(drawnCard);

                        // Afficher un message pour la couleur choisie
                        JOptionPane.showMessageDialog(frame,
                                currentPlayer.getName() + " a choisi la couleur: " + colorName,
                                "Couleur choisie", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        game.playCard(drawnCard);
                    }
                } else {
                    System.out.println("L'IA ne peut pas jouer la carte piochée, passe au joueur suivant");
                    game.nextPlayer();
                }
            }

            updateUI();
            System.out.println("Après le tour de l'IA, joueur actuel: " + game.getCurrentPlayer().getName());

            // Vérifier si le jeu est terminé
            if (game.isGameOver()) {
                aiTimer.stop();
                JOptionPane.showMessageDialog(frame, currentPlayer.getName() + " a gagné la partie !");
                return;
            }

            // Vérifier si c'est toujours au tour d'une IA
            if (game.getCurrentPlayer().isAI()) {
                System.out.println("C'est encore le tour d'une IA");
                // Ne rien faire, le timer déclenchera le prochain tour d'IA
            } else {
                System.out.println("C'est maintenant au tour du joueur humain");
                // C'est au tour du joueur humain, arrêter le timer
                aiTimer.stop();
            }
        } else {
            // Si c'est le tour du joueur humain, arrêter le timer
            aiTimer.stop();
        }
    }

    private Color getRandomColor() {
        int randomColor = (int) (Math.random() * 4);
        switch (randomColor) {
            case 0: return Color.RED;
            case 1: return Color.GREEN;
            case 2: return Color.BLUE;
            case 3: return Color.YELLOW;
            default: return Color.RED;
        }
    }

    private void checkAndStartAITurn() {
        Player currentPlayer = game.getCurrentPlayer();

        if (currentPlayer.isAI() && !aiTimer.isRunning()) {
            // Démarrer le timer pour les tours d'IA
            aiTimer.start();
        }
    }

    private void updateUI() {
        if (frame != null) {
            frame.updatePlayerPanel();
            frame.updateCurrentCardPanel();
            frame.updateGameInfo();
        }
    }

    public boolean isGameStarted() {
        return gameStarted;
    }
}


