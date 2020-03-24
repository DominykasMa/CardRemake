package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;

import javax.swing.*;

import Game.Player;
import Interfaces.GameConstants;

public class PlayerPanel extends JPanel implements GameConstants {

    private Player player;
    private String name;

    private Box myLayout;
    private JLayeredPane cardHolder;
    private Box controlPanel;

    private JButton draw;
    private JLabel nameLbl;

    //private MyButtonHandler handler;

    // Constructor
    public PlayerPanel(Player newPlayer) {
        setPlayer(newPlayer);

        myLayout = Box.createHorizontalBox();
        cardHolder = new JLayeredPane();
        cardHolder.setPreferredSize(new Dimension(600, 175));

        // Set
        setCards();
        setControlPanel();

        myLayout.add(cardHolder);
        myLayout.add(Box.createHorizontalStrut(40));
        myLayout.add(controlPanel);
        add(myLayout);

        // Register Listener
    }

    public void setCards() {
        cardHolder.removeAll();
        // Origin point at the center
        Point origin = getPoint(cardHolder.getWidth(), player.getTotalCards());
        int offset = calculateOffset(cardHolder.getWidth(),
                player.getTotalCards());

        int i = 0;
        for (PlayingCard card : player.getAllCards()) {
            card.setBounds(origin.x, origin.y, card.CARDSIZE.width,
                    card.CARDSIZE.height);
            cardHolder.add(card, i++);
            cardHolder.moveToFront(card);
            origin.x += offset;
        }
        repaint();
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
        setPlayerName(player.getUsername());
    }

    public void setPlayerName(String playername) {
        this.name = playername;
    }

    private void setControlPanel() {
        draw = new JButton("Draw");
        nameLbl = new JLabel(name);

        // style
        draw.setBackground(new Color(255, 255, 255));
        draw.setFont(new Font("Arial", Font.BOLD, 20));
        draw.setFocusable(false);

        nameLbl.setForeground(Color.WHITE);
        nameLbl.setFont(new Font("Arial", Font.BOLD, 15));

        controlPanel = Box.createVerticalBox();
        controlPanel.add(nameLbl);
        controlPanel.add(draw);
        controlPanel.add(Box.createVerticalStrut(15));
    }

    private int calculateOffset(int width, int totalCards) {
        int offset = 71;
        if (totalCards <= 8) {
            return offset;
        } else {
            double o = (width - 100) / (totalCards - 1);
            return (int) o;
        }
    }

    private Point getPoint(int width, int totalCards) {
        Point p = new Point(0, 20);
        if (totalCards >= FIRSTHAND) {
            return p;
        } else {
            p.x = (width - calculateOffset(width, totalCards) * totalCards) / 2;
            return p;
        }
    }

//    class MyButtonHandler implements ActionListener{
//
//        public void actionPerformed(ActionEvent e) {
//
//            if(player.isMyTurn()){
//                if(e.getSource()==draw)
//                    System.out.println("aaaaa");
//
//            }
//        }
//    }
}
