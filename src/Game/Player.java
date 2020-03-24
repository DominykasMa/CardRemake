package Game;

import View.PlayingCard;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;

/*
The player class extends the node class to add functionality
player specifications (username, points)
*/

public class Player extends Node {
    private String username;
    private int points;
    private LinkedList<PlayingCard> myCards;
    private int playedCards = 0;

    public Player(String user, String host, int port) throws UnknownHostException {
        this(user,InetAddress.getByName(host),port);
    }

    public Player(String user,InetAddress inetAddr, int port){
        super(inetAddr, port);
        this.username = user;
        this.points = 0;
        myCards = new LinkedList<PlayingCard>();
    }

    public String getUsername(){ return username; }
    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }
    public void incPoints(){ points++; }

    public void obtainCard(PlayingCard card){
        myCards.add(card);
    }

    public LinkedList<PlayingCard> getAllCards(){
        return myCards;
    }

    public int getTotalCards(){
        return myCards.size();
    }

    public boolean hasCard(PlayingCard thisCard){
        return myCards.contains(thisCard);
    }

    public void removeCard(PlayingCard thisCard){
        myCards.remove(thisCard);
        playedCards++;
    }

    public int totalPlayedCards(){
        return playedCards;
    }


    public void setCards(){
        myCards = new LinkedList<PlayingCard>();
    }

}
