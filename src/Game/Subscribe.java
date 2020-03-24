package Game;

import Interfaces.IPartecipant;
import Interfaces.SubscribeInterface;
import View.PlayingCard;

import java.rmi.RemoteException;
import java.net.InetAddress;
import java.rmi.server.UnicastRemoteObject;
import java.util.Stack;

/*
Game.Subscribe class, created and instantiated by the server
It is used to collect the clients and pass them all the shuffled cards.
*/

public class Subscribe extends UnicastRemoteObject implements SubscribeInterface {

    private Player[] players;
    private IPartecipant[] partecipants;
    private int playersMaxNo;
    private int playersNo = 0;
    private boolean openSubscribe = true;
    private Dealer dealer;
    private Stack<PlayingCard> cardStack;
    private PlayingCard topCard;

    public Subscribe(int playersMaxNo) throws RemoteException {
        this.playersMaxNo = playersMaxNo;
        players = new Player[playersMaxNo];
        partecipants = new IPartecipant[playersMaxNo];
    }

    public synchronized boolean subscribeAccepted(IPartecipant partecipant, String username, InetAddress inetAddr, int port) throws RemoteException {
        // method called by the client
        if (playersNo < playersMaxNo &&  openSubscribe) {
            System.out.println("New player --> " + username);
            partecipants[playersNo] = partecipant;

            // I create the new player
            players[playersNo] = new Player(username, inetAddr, port);
            playersNo++;

            if (playersNo==playersMaxNo) {
                // reached the maximum number of participants
                openSubscribe=false;
                replyClients();
                notify();
            }
            return true;
        }
        return false;
    }

    public synchronized void endSubscribe() {
        if (openSubscribe) {
            openSubscribe = false;
            replyClients();
            notify();
        }
    }

    public synchronized Player[] getPlayers() {
        if (openSubscribe)
            try {
                wait();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        return players;
    }

    public synchronized int getPlayersNo() {
        if (openSubscribe)
            try {
                wait();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        return playersNo;
    }

    private void replyClients() {
        final Player[] realPlayers = new Player[playersNo];
        System.arraycopy(players, 0, realPlayers, 0, playersNo);
        players = realPlayers;

        dealer = new Dealer();
        cardStack = dealer.shuffle();
        //dealer.spreadOut(players);
        topCard = dealer.getCard();

        // configure participants
        for (int i=0 ;i < playersNo;i++) {
            final IPartecipant p = partecipants[i];
            final int j=i;

            Thread t = new Thread() {
                public void run() {
                    try {
                        System.out.println("Configuring participant " + realPlayers[j].getUsername());
                        p.configure(players, cardStack, topCard);
                        System.out.println("Configuring done.");
                    } catch (RemoteException re) {
                        re.printStackTrace();
                    }
                }
            };
            t.start();
        }
    }
}
