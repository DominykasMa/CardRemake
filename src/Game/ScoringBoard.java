package Game;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

/*
Classe Game.ScoringBoard
 */
public class ScoringBoard extends JPanel {

    private Player[] allPlayers;
    private JLabel[] lbScore ;

    public ScoringBoard(Player[] players) {
        this.allPlayers = players;
        lbScore = new JLabel[allPlayers.length];
    }


    public void buildGridForScore() {
        setLayout(new GridLayout(allPlayers.length, 1)); // the layout of the board
        setBorder(new EmptyBorder(0, 15, 500, 15));
        createLabelScore();

    }

    // updates the current player by coloring the label
    public void setCurrentPlayer(int id){
        lbScore[id].setForeground(Color.red);
    }

    public void clearOldPlayer(int id) {
        lbScore[id].setForeground(Color.black);
    }

    public void setPlayerScore(int nodeId, int score){
        lbScore[nodeId].setText(allPlayers[nodeId].getUsername() + ": " + score + " Punti");
    }

    public void createLabelScore() {
        for (int i = 0; i < allPlayers.length; i++) {
            lbScore[i] = new JLabel(allPlayers[i].getUsername() + ": " + allPlayers[i].getPoints() + " Punti");
            add(lbScore[i]);

        }
    }

    public void setPlayerCrashed(int nodeId) {
        lbScore[nodeId].setText(allPlayers[nodeId].getUsername() + ": " + "PLAYER OUT !!!!");
    }

}
