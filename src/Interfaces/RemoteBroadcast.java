package Interfaces;

import Game.GameMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

/*
Class that defines the methods that can be called through RMI calls.
*/

public interface RemoteBroadcast extends Remote {

    public void forward(GameMessage msg) throws RemoteException;

    public void checkNode() throws RemoteException;

}
