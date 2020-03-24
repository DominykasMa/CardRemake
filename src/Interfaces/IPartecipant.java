package Interfaces;

import Game.Player;
import View.PlayingCard;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Stack;


/*Remote interface of the Participant class, here they can be declared
the methods used through RMI calls*/

public interface IPartecipant extends Remote {

    public void configure(Player[] players, Stack<PlayingCard> cardStack, PlayingCard topcard) throws RemoteException;
}
