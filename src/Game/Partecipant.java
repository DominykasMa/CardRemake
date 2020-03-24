package Game;

import Interfaces.IPartecipant;
import View.PlayingCard;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Stack;

/*Participant class used to notify game info to all players*/
public class Partecipant extends UnicastRemoteObject implements IPartecipant {

    private Player[] players;
    private boolean gotPartecipants = false;
    private Stack<PlayingCard> cardStack;
    private  PlayingCard topcard;


    public Partecipant() throws RemoteException {}

    public synchronized void configure(Player[] players, Stack<PlayingCard> cardStack,PlayingCard topcard ) throws RemoteException {

        // method called by subscribe to configure participant variables
        this.players = players;
        this.cardStack = cardStack;
        this.topcard = topcard;
        gotPartecipants = true;
        notifyAll();
        System.out.println("Participants and card list has been received.");
    }

    public synchronized Player[] getPlayers() {
        if (!gotPartecipants)
            try {
                System.out.println("Participants list unavailable: waiting...");
                wait();
                System.out.println("Timeout end or object notified.");
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        return players;
    }

    public Stack<PlayingCard> getCardStack() {
        return cardStack;
    }
    public PlayingCard getTopCard() {
        return topcard;
    }
}
