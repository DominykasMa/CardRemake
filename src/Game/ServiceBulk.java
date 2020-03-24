package Game;/*
Class that contains a Game.MessageBroadcast object to call remote methods and the node id.
*/

import Interfaces.RemoteBroadcast;

public class ServiceBulk {

    public RemoteBroadcast messageBroadcast;
    public int id;


    public ServiceBulk(RemoteBroadcast messageBroadcast, int id) {
        this.messageBroadcast = messageBroadcast;
        this.id = id;
    }
}