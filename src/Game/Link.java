package Game;

import Interfaces.RemoteBroadcast;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/*
This is the class that manages connections with nearby nodes and retrieves the info
on your node and neighbors.
*/

public class Link {

    public Node[] nodes;
    private int myId = 0;
    private int rightId = 0;
    private int leftId = 0;
    private Node me;
    private RemoteBroadcast rightNode = null;



    public Link(Node me, Node[] nodes) {
        this.me = me;
        this.nodes = nodes;
        configure();
    }

    // searches for your id and neighbors within the node array returned by the server.
    private void configure() {
        int j;

        for (int i = 0; i < nodes.length; i++) {
            if (me.compareTo(nodes[i]) == 0 ) {
                myId = i;
                j=1;

                // takes the next active node
                do{
                    rightId = (i + j) % nodes.length;

                    j=j+1;
                }while ( nodes[rightId].isActive()==false );

            }
        }
    }

    public int getNodeId() {
        return myId;
    }

    public int getLeftId() {
        return leftId;
    }
    public int getRightId() {
        return rightId;
    }

    public void setRightId(int id) {
        rightId = id;
    }

    public Node[] getNodes() {
        return nodes;
    }

    public void incRightId() {
        rightId = (rightId +1) % nodes.length;
    }

	/* Method that retrieves the reference to the Interfaces.RemoteBroadcast object of the near right node
through the lookupnode method to then be able to send him messages during the game, subsequently
creates an object of type Game.ServiceBulk.*/

    public ServiceBulk getRight() {

        rightNode = lookupNode(rightId);
        return new ServiceBulk(rightNode,rightId);

    }

    //Method that uses RMI, returns a reference of type Interfaces.RemoteBroadcast

    private RemoteBroadcast lookupNode(int id)  {
        RemoteBroadcast broadcast = null;
        String url = "rmi://" + nodes[id].getInetAddress().getCanonicalHostName() + ":"
                + nodes[id].getPort() + "/Broadcast";
        try {
            System.out.println("looking up " + url);
            broadcast = (RemoteBroadcast)Naming.lookup(url);
        } catch (MalformedURLException e) {
            System.out.println("Malformed");
            nodes[id].setNodeCrashed();
        } catch (NotBoundException e) {
            System.out.println("Notbound");
            nodes[id].setNodeCrashed();
        } catch (RemoteException e) {
            System.out.println("Remote");
            nodes[id].setNodeCrashed();
        }
        return broadcast;
    }

    // Method that controls active nodes, other than lookupnode
    public boolean checkAliveNode() {

        int id = getRightId();
        boolean success = true;
        RemoteBroadcast broadcast = null;
        String url = "rmi://" + nodes[id].getInetAddress().getCanonicalHostName() + ":"
                + nodes[id].getPort() + "/Broadcast";
        try {
            System.out.println("looking up " + url);
            broadcast = (RemoteBroadcast)Naming.lookup(url);
        } catch (MalformedURLException e) {
            System.out.println("Malformed");
            nodes[id].setNodeCrashed();
            success = false;
        } catch (NotBoundException e) {
            System.out.println("Notbound");
            nodes[id].setNodeCrashed();
            success = false;
        } catch (RemoteException e) {
            System.out.println("Remote");
            nodes[id].setNodeCrashed();
            success = false;
        }
        return success;
    }

    // Method that checks nodes during an AYA check
    public boolean checkAYANode(int rightId) {


        boolean success = true;
        RemoteBroadcast broadcast = null;
        String url = "rmi://" + nodes[rightId].getInetAddress().getCanonicalHostName() + ":"
                + nodes[rightId].getPort() + "/Broadcast";
        try {
            System.out.println("looking up " + url);
            broadcast = (RemoteBroadcast)Naming.lookup(url);
        } catch (MalformedURLException e) {
            System.out.println("Malformed");
            success = false;
        } catch (NotBoundException e) {
            System.out.println("Notbound");
            success = false;
        } catch (RemoteException e) {
            System.out.println("Remote");
            success = false;
        }
        return success;
    }

}