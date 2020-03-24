package Game;

import java.awt.Color;
import java.util.LinkedList;

import CardModel.FaceCard;
import Interfaces.GameConstants;
import CardModel.NumberCard;
import View.PlayingCard;

/**
 * This Class contains standard 108-Card stack
 */
public class CardDeck implements GameConstants {

    private LinkedList<PlayingCard> playingCards;

    public CardDeck(){
        //Initialize Cards
        playingCards = new LinkedList<PlayingCard>();

        addCards();
        addCardListener(CARDLISTENER);
    }


    //Create 108 cards for this CardDeck
    private void addCards() {
        int a = 0;
        Color color = null;
        for(String suit:SUITS) {
            if(a<2)
                color = CARD_COLORS[0];
            else
                color = CARD_COLORS[1];
            //Create 40 number cards
            for(int num : CARD_NUMBERS){
                int i=0;
                do{
                    playingCards.add(new NumberCard(color, Integer.toString(num), suit));
                    i++;
                }while(i<1);
                System.out.println(num);
            }
            //Create 16 face cards
            for(String type : CARD_FACES){
                    playingCards.add(new FaceCard(color, type, suit));
            }
            a++;
        }
    }

    //Cards have MouseListener
    public void addCardListener(MyCardListener listener){
        for(PlayingCard card: playingCards)
            card.addMouseListener(listener);
    }


    public LinkedList<PlayingCard> getCards(){
        return playingCards;
    }
}
