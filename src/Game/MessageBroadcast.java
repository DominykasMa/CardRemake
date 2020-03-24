package Game;

import Interfaces.RemoteBroadcast;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

/*
Class used for RMi remote calls, implements the Interfaces.RemoteBroadcast remote class,
therefore remote methods of this class can be called.
It manages the arrival of messages, reorders them, can discard them or insert them in the buffer.
*/

public class MessageBroadcast extends UnicastRemoteObject implements RemoteBroadcast {

    private Link link = null;
    private RouterFactory rmaker;
    private MessageFactory mmaker;
    private BlockingQueue<GameMessage> buffer;
    private int messageCounter;
    private TreeMap<Integer, GameMessage> pendingMessage;
    private ReentrantLock msgCounterLock;
    public Client clientBoard;

    public MessageBroadcast(BlockingQueue<GameMessage> buffer,final Client clientBoard) throws RemoteException {
        this.buffer = buffer;
        this.messageCounter = 0;
        this.clientBoard = clientBoard;
        pendingMessage = new TreeMap<Integer,GameMessage>();
        msgCounterLock = new ReentrantLock();

    }

    public void configure(Link link, RouterFactory rmaker, MessageFactory mmaker) {

        this.link = link;
        this.rmaker = rmaker;
        this.mmaker = mmaker;
    }

    //Sending a message on the network
    public synchronized void send(GameMessage msg) {

        //quando r.run termina ho il link.Game.Node[] aggiornato
        Router r = rmaker.newRouter(msg);
        new Thread(r).start();

    }
    // Start AYA check
    public synchronized void sendAYA() {

        AYARouter r = rmaker.newAYARouter();
        new Thread(r).start();
    }

    //Forwarding the message to the right neighbor if this is necessary
    public synchronized void forward(GameMessage msg) throws  RemoteException {

        if (enqueue(msg)) {

            boolean anyCrash = false;
            boolean[] nodesCrashed = new boolean[link.getNodes().length];
            Arrays.fill(nodesCrashed, false);
            int initialMsgCrash = msg.getHowManyCrash();


            while(link.checkAliveNode() == false) {

                msg.incCrash();
                anyCrash = true;
                nodesCrashed[link.getRightId()] = true;
                System.out.println("Finding a new neighbour");
                link.incRightId();
                if (link.getRightId() == link.getNodeId()) {
                    System.out.println("Unico giocatore, partita conclusa");
                    System.exit(0);

                }
            }



            // send the message arrived from the previous node
            send(msg);

            if (anyCrash) {

                // 1 for the gamemessage of the node
                int nextIdMsg = initialMsgCrash + messageCounter + 1 ;


                for(int i=0;i<nodesCrashed.length;i++) {

                    if (nodesCrashed[i] == true) {


                        System.out.println("Sending a CrashMessage id "+ nextIdMsg +" for node " + i);

                        // Sending crash messages without error handling
                        GameMessage msgProv = mmaker.newCrashMessage(i,nextIdMsg,0);

                        if (initialMsgCrash == 0) {
                            incMessageCounter();

                        } else {
                            pendingMessage.put(nextIdMsg,(GameMessage)msgProv.clone());
                        }



                        send(msgProv);
                        nextIdMsg = nextIdMsg + 1;
                        System.out.println("Update View.Board crash");
                        //clientBoard.board.updateCrash(i);
                    }
                }
            }
        } else {
            System.out.println("Game.Message discarded. " + msg.toString());
        }
    }

    // Method that puts messages in the queue if they are to be processed
    private synchronized boolean enqueue(GameMessage msg) {

        boolean doForward = false;
        System.out.println("initialMsgCrash -> " + msg.getHowManyCrash());
        System.out.println("messagecounter-> " + messageCounter);
        System.out.println("MsgId -> " + msg.getId());

        if (msg.getOrig() != link.getNodeId()) {
            if((msg.getId() > messageCounter) && (pendingMessage.containsKey(msg.getId()) == false)) {
                if(msg.getId() == messageCounter + 1) {
                    try {
                        buffer.put(msg);
                        System.out.println("msg put into the queue");
                    } catch (InterruptedException e) {
                        System.out.println("Error! Can't put message in the queue.");
                    }

                    incMessageCounter();

                    while(pendingMessage.containsKey(messageCounter + 1)) {
                        GameMessage pendMessage = pendingMessage.remove(messageCounter + 1);
                        try {
                            buffer.put(pendMessage);
                        } catch (InterruptedException e) {
                            System.out.println("error!");
                        }

                        incMessageCounter();
                    }
                } else {
                    pendingMessage.put(msg.getId(),(GameMessage)msg.clone());
                }
                doForward = true;
            }
        }
        return doForward;
    }

	/*
	Method that increments the msgcounter, a lock is used to access the variable
in mutual exclusion
	*/

    public void incMessageCounter() {
        msgCounterLock.lock();
        try {
            messageCounter++;
        } finally {
            msgCounterLock.unlock();
        }
    }


    public int retrieveMsgCounter() {
        return messageCounter;
    }

    // Method used by the AYA control on the neighbor
    public synchronized void checkNode() {

        System.out.println("My neighbor is alive");
    }
}