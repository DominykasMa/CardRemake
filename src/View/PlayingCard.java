package View;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import Interfaces.CardInterface;
import Interfaces.DurakConstants;

public abstract class PlayingCard extends JPanel implements CardInterface, DurakConstants {

    private Color cardColor = null;
    private String value = null;
    private String suit = null;
    private int type = 0;

    private Border defaultBorder = BorderFactory.createEtchedBorder(WHEN_FOCUSED, Color.white, Color.gray);
    private Border focusedBorder = BorderFactory.createEtchedBorder(WHEN_FOCUSED, Color.red, Color.gray);

    public PlayingCard(Color cardColor, int cardType, String cardValue, String cardSuit ){
        this.cardColor = cardColor;
        this.type = cardType;
        this.value = cardValue;
        this.suit = cardSuit;

        this.setPreferredSize(CARDSIZE);
        this.setBorder(defaultBorder);

        this.addMouseListener(new MouseHandler());
    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int cardWidth = CARDSIZE.width;
        int cardHeight = CARDSIZE.height;
        String cardText = value + suit;

        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, cardWidth, cardHeight);

        int margin = 5;

        g2.setColor(Color.white);
        AffineTransform org = g2.getTransform();
        g2.rotate(45,cardWidth*3/4,cardHeight*3/4);

        g2.fillOval(0,cardHeight*1/4,cardWidth*3/5, cardHeight);
        g2.setTransform(org);

        //Value in the center
        Font defaultFont = new Font("Helvetica", Font.BOLD, cardWidth/2+5);
        FontMetrics fm = this.getFontMetrics(defaultFont);
        int StringWidth = fm.stringWidth(value)/2;
        int FontHeight = defaultFont.getSize()*1/3;

        g2.setColor(cardColor);
        g2.setFont(defaultFont);
        g2.drawString(suit, cardWidth/2-StringWidth, cardHeight/2+FontHeight);

        //Value in the corner
        defaultFont = new Font("Helvetica", Font.ITALIC, cardWidth/5);
        fm = this.getFontMetrics(defaultFont);
        StringWidth = fm.stringWidth(value)/2;
        FontHeight = defaultFont.getSize()*1/3;


        g2.setColor(cardColor);
        g2.setFont(defaultFont);
        g2.drawString(cardText, 2*margin,5*margin);

    }

    /**
     * My Mouse Listener
     */
    class MouseHandler extends MouseAdapter {

        public void mouseEntered(MouseEvent e){
            setBorder(focusedBorder);
        }

        public void mouseExited(MouseEvent e){
            setBorder(defaultBorder);
        }
    }

    public void setCardSize(Dimension newSize){
        this.setPreferredSize(newSize);
    }

    public void setColor(Color newColor) {
        this.cardColor = newColor;
    }

    public Color getColor() {
        return cardColor;
    }

    public String getSuit() {
        return suit;
    }

    @Override
    public void setValue(String newValue) {
        this.value = newValue;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setType(int newType) {
        this.type = newType;
    }

    @Override
    public int getType() {
        return type;
    }
}
