package Game;

import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

import Interfaces.GameConstants;
import View.PlayingCard;

public class Dealer implements GameConstants {

    private CardDeck cardDeck;
    private Stack<PlayingCard> CardStack;

    public Dealer(){
        this.cardDeck = new CardDeck();
    }

    //Shuffle cards
    public Stack<PlayingCard> shuffle(){

        LinkedList<PlayingCard> DeckOfCards = cardDeck.getCards();
        LinkedList<PlayingCard> shuffledCards = new LinkedList<PlayingCard>();

        while(!DeckOfCards.isEmpty()){
            int totalCards = DeckOfCards.size();

            Random random = new Random();
            int pos = (Math.abs(random.nextInt()))% totalCards;

            PlayingCard randomCard = DeckOfCards.get(pos);
            DeckOfCards.remove(pos);
            shuffledCards.add(randomCard);
        }

        CardStack = new Stack<PlayingCard>();
        for(PlayingCard card : shuffledCards){
            CardStack.add(card);
        }

        return CardStack;
    }

    //Spread cards to players - 6 each
    public void spreadOut(Player[] players){

        for(int i=1;i<=FIRSTHAND;i++){
            for(Player p : players){
                p.obtainCard(CardStack.pop());
            }
        }
    }
    public PlayingCard getCard(){
        return CardStack.pop();
    }
}
