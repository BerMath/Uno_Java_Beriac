package fr.beriac.uno.gui;

import fr.beriac.uno.Player;
import fr.beriac.uno.cards.Card;
import fr.beriac.uno.cards.ChangeColorCard;
import fr.beriac.uno.cards.Plus4Card;
import fr.beriac.uno.main.GameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GameFrame extends JFrame {
    private GameController controller;
    private JPanel playerPanel;
    private JPanel currentCardPanel;
    private JPanel controlPanel;
    private JPanel infoPanel;

    public GameFrame(GameController controller) {
        this.controller = controller;
        controller.setFrame(this);

        setTitle("Jeu de UNO");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Créer le panneau principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        // Panneau pour les informations du jeu
        infoPanel = new JPanel();
        mainPanel.add(infoPanel, BorderLayout.EAST);
        infoPanel.setPreferredSize(new Dimension(200, 600));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Informations"));

        // Panneau pour afficher les cartes du joueur
        playerPanel = new JPanel();
        playerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        playerPanel.setBorder(BorderFactory.createTitledBorder("Vos cartes"));
        mainPanel.add(playerPanel, BorderLayout.SOUTH);

        // Panneau pour afficher la carte actuelle
        currentCardPanel = new JPanel();
        currentCardPanel.setBorder(BorderFactory.createTitledBorder("Carte actuelle"));
        mainPanel.add(currentCardPanel, BorderLayout.CENTER);

        // Panneau pour les boutons de contrôle
        controlPanel = new JPanel();
        mainPanel.add(controlPanel, BorderLayout.NORTH);

        // Bouton pour démarrer le jeu
        JButton startButton = new JButton("Démarrer le jeu");
        controlPanel.add(startButton);

        // Bouton pour piocher une carte
        JButton drawButton = new JButton("Piocher une carte");
        controlPanel.add(drawButton);
        drawButton.setEnabled(false);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.initializeGame();
                startButton.setEnabled(false);
                drawButton.setEnabled(true);
            }
        });

        drawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.drawCard();
            }
        });
    }

    public void updatePlayerPanel() {
        playerPanel.removeAll();
        if (controller.isGameStarted() && controller.getCurrentPlayer() != null) {
            List<Card> hand = controller.getPlayerHand();

            // N'afficher les cartes que si c'est le joueur humain
            if (!controller.getCurrentPlayer().isAI()) {
                for (Card card : hand) {
                    JButton cardButton = new JButton(card.toString());
                    cardButton.setPreferredSize(new Dimension(100, 140));
                    cardButton.addActionListener(e -> {
                        if (card.canBePlayedOn(controller.getCurrentCard())) {
                            controller.playCard(card);
                        } else {
                            JOptionPane.showMessageDialog(this, "Cette carte ne peut pas être jouée !");
                        }
                    });
                    playerPanel.add(cardButton);
                }
            } else {
                // Si c'est le tour d'une IA, afficher un message
                JLabel waitLabel = new JLabel("C'est au tour de " + controller.getCurrentPlayer().getName());
                playerPanel.add(waitLabel);
            }
        }
        playerPanel.revalidate();
        playerPanel.repaint();
    }

    public void updateCurrentCardPanel() {
        currentCardPanel.removeAll();

        if (controller.isGameStarted()) {
            Card currentCard = controller.getCurrentCard();
            if (currentCard != null) {
                // Création d'un panneau pour afficher la carte et sa couleur
                JPanel cardInfoPanel = new JPanel();
                cardInfoPanel.setLayout(new BoxLayout(cardInfoPanel, BoxLayout.Y_AXIS));

                JLabel currentCardLabel = new JLabel(currentCard.toString());
                currentCardLabel.setFont(new Font("Arial", Font.BOLD, 24));
                currentCardLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                cardInfoPanel.add(currentCardLabel);

                // Si c'est une carte Wild ou Wild+4, afficher la couleur choisie
                if (currentCard instanceof ChangeColorCard) {
                    Color chosenColor = ((ChangeColorCard) currentCard).getChosenColor();
                    if (chosenColor != null) {
                        String colorName = colorToString(chosenColor);
                        JLabel colorLabel = new JLabel("Couleur choisie: " + colorName);
                        colorLabel.setFont(new Font("Arial", Font.BOLD, 16));
                        colorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                        // Définir la couleur du texte pour correspondre à la couleur choisie
                        colorLabel.setForeground(chosenColor);
                        cardInfoPanel.add(colorLabel);
                    }
                } else if (currentCard instanceof Plus4Card) {
                    Color chosenColor = ((Plus4Card) currentCard).getChosenColor();
                    if (chosenColor != null) {
                        String colorName = colorToString(chosenColor);
                        JLabel colorLabel = new JLabel("Couleur choisie: " + colorName);
                        colorLabel.setFont(new Font("Arial", Font.BOLD, 16));
                        colorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                        // Définir la couleur du texte pour correspondre à la couleur choisie
                        colorLabel.setForeground(chosenColor);
                        cardInfoPanel.add(colorLabel);
                    }
                }

                currentCardPanel.add(cardInfoPanel);
            } else {
                JLabel currentCardLabel = new JLabel("Aucune carte en jeu");
                currentCardPanel.add(currentCardLabel);
            }
        }
        currentCardPanel.revalidate();
        currentCardPanel.repaint();
    }

    private String colorToString(Color color) {
        if (color.equals(Color.RED)) return "ROUGE";
        if (color.equals(Color.GREEN)) return "VERT";
        if (color.equals(Color.BLUE)) return "BLEU";
        if (color.equals(Color.YELLOW)) return "JAUNE";
        return "INCONNUE";
    }

    public void updateGameInfo() {
        infoPanel.removeAll();

        if (controller.isGameStarted()) {
            // Informations sur le joueur actuel
            JLabel currentPlayerLabel = new JLabel("Tour de : " + controller.getCurrentPlayer().getName());
            currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 14));
            infoPanel.add(currentPlayerLabel);

            // Ajouter des informations sur les joueurs et leurs cartes
            infoPanel.add(Box.createVerticalStrut(20));

            for (Player player : controller.getGame().getPlayers()) {
                JPanel playerInfoPanel = new JPanel();
                playerInfoPanel.setLayout(new BoxLayout(playerInfoPanel, BoxLayout.Y_AXIS));

                JLabel playerNameLabel = new JLabel(player.getName());
                playerNameLabel.setFont(new Font("Arial", Font.BOLD, 12));

                JLabel cardCountLabel = new JLabel("Cartes : " + player.getHand().size());

                playerInfoPanel.add(playerNameLabel);
                playerInfoPanel.add(cardCountLabel);

                if (player == controller.getCurrentPlayer()) {
                    playerInfoPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                }

                infoPanel.add(playerInfoPanel);
                infoPanel.add(Box.createVerticalStrut(10));
            }
        }

        infoPanel.revalidate();
        infoPanel.repaint();
    }
}