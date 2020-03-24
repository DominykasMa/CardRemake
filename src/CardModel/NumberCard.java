package CardModel;

import View.PlayingCard;

import java.awt.*;

public class NumberCard extends PlayingCard {

//    public NumberCard(){
//    }

    public NumberCard(Color cardColor, String cardValue, String cardSuit){
        super(cardColor, NUMBERS, cardValue, cardSuit);
    }

}
