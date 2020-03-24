package CardModel;

import View.PlayingCard;

import java.awt.*;

public class FaceCard extends PlayingCard {

//    public FaceCard() {
//    }

    public FaceCard(Color cardColor, String cardValue, String cardSuit){
        super(cardColor,FACES, cardValue, cardSuit);
    }
}
