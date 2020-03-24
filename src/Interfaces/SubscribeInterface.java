package Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.net.InetAddress;

/*
Remote interface of the Game.Subscribe class
*/

public interface SubscribeInterface extends Remote {

    public boolean subscribeAccepted(IPartecipant partecipant, String username, InetAddress inetAddr, int portt) throws RemoteException;
}
