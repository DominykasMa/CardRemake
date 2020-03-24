package View;

import Interfaces.GameConstants;

import javax.swing.*;
import java.awt.*;

public class tablePanel extends JPanel implements GameConstants {

    private JPanel table;
    private PlayingCard topCard;

    public tablePanel(PlayingCard firstCard){
        setOpaque(false);
        setLayout(new GridBagLayout());

        topCard = firstCard;
        table = new JPanel();
        table.setBackground(new Color(64,64,64));

        setTable();
        setComponents();
    }

    private void setTable(){

        table.setPreferredSize(new Dimension(500,200));
        table.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        table.add(topCard, c);
        System.out.println("Table set");
    }

    private void setComponents() {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 130, 0, 45);
        add(table,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_END;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(0, 1, 0, 1);
        add(infoPanel, c);
    }

    public void setPlayedCard(PlayingCard playedCard){
        table.removeAll();
        topCard = playedCard;
        setTable();

        setBackgroundColor(playedCard);
    }

    public void setBackgroundColor(PlayingCard playedCard){
        Color background;
        background = playedCard.getColor();
        table.setBackground(background);
    }

}
