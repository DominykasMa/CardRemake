package Game;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

/*
Game.Player's parent class, each player creates an instance of this with its own network address and port.
When an object of the link class is created, a node object is passed as a parameter instead of a player.
It is done because at the network level only the node info is needed and not all those contained in the player.

*/

public class Node implements Serializable, Comparable<Node> {

    private InetAddress inetAddr;
    private int port;
    private int id;
    private boolean active = true;	// indicates whether the node is active or not



    public Node(String host, int port) throws UnknownHostException {
        this(InetAddress.getByName(host), port);
    }

    public Node(InetAddress inetAddr, int port) {

        this.inetAddr = inetAddr;
        this.port = port;
    }

    public InetAddress getInetAddress() { return inetAddr; }
    public int getPort() { return port; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public boolean isActive(){ return active; }
    public void setActive( boolean active ) { this.active = active; }

    public int compareTo(Node player) {
        if (port < player.port)
            return -1;
        if (port > player.port)
            return 1;
        if (inetAddr.equals(player.inetAddr))
            return 0;
        return -1;
    }

    public String toString() {
        return inetAddr.getHostAddress() + ":" + port;
    }

    public void setNodeCrashed() {
        active = false;
    }

    public boolean getActive() {
        return active;
    }

}