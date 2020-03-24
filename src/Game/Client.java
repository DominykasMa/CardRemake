package Game;//import java.rmi.RMISecurityManager;
//import java.rmi.registry.Registry;

import Interfaces.SubscribeInterface;
import View.Board;
import View.PlayingCard;
import View.WindowRegistration;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.Arrays;
import java.util.Stack;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.Random;
import java.util.List;

/*Classe Game.Client*/

public class Client  {

    public final int PORT = 1099;
    private Player[] players;
    private Node me;
    private int nodeId;
    private Link link;
    private int playersNo;
    public static MessageBroadcast messageBroadcast;
    private MessageFactory mmaker;
    private RouterFactory rmaker;
    private BlockingQueue<GameMessage> buffer;
    public int[] processedMsg;
    private String playerName;
    private List<Integer> cardVals;
    private Stack<PlayingCard> cardStack;
    private PlayingCard topCard;
    public final Board board;
    private OnesMove move;
    private final WindowRegistration initialWindow;
    private int rightId;

    public String serverAddr;
    public int currentPlayer;
    public boolean isGameEnded;

    public Client (String username, final Board board, final WindowRegistration initialWindow,String serverAddr){

        this.board = board;
        this.playerName = username;
        this.initialWindow = initialWindow;
        this.serverAddr = serverAddr;
        inizializeGame();
    }

    private void inizializeGame() {

        InetAddress localHost = null;

        try{
            localHost = InetAddress.getLocalHost();
            System.out.println("Local host is " + localHost);
        } catch (UnknownHostException uh){
            uh.printStackTrace();
            System.exit(1);
        }


        String server = serverAddr;

        // A port is set to connect to
        Random random = new Random();
        int port = random.nextInt(100)+2001;

        /*if (System.getSecurityManager() == null)
            System.getSecurityManager(new RMISecurityManager());
        else
            System.out.println("Security Manager not starts.");*/

        messageBroadcast = null;
        buffer = new LinkedBlockingQueue<GameMessage>();


        try {
            // Each client creates its own RMI registry
            LocateRegistry.createRegistry(port);
        /*
        	try{
            	LocateRegistry.createRegistry(port);
            } catch (RemoteException ee) {
            	LocateRegistry.getRegistry(port);
            }
        */
            // Registering the Remote Game.MessageBroadcast class in the RMI registry
            messageBroadcast = new MessageBroadcast (buffer,this);
            String serviceURL = "rmi://" + localHost.getCanonicalHostName() + ":" + port + "/Broadcast";
            System.out.println("Registering message broadcast service at " + serviceURL);
            Naming.rebind(serviceURL,messageBroadcast);
        } catch (RemoteException rE) {
            rE.printStackTrace();
        } catch (MalformedURLException murlE) {
            murlE.printStackTrace();
        }


        Partecipant partecipant = null;
        boolean result = false;

        /* establish connection with server */
        try{
            partecipant = new Partecipant();
            System.out.println("Looking up subscribe service...");
            String url = "rmi://" + server +":" + PORT + "/Game.Subscribe";
            SubscribeInterface subscribe = (SubscribeInterface) Naming.lookup(url);
            System.out.println("Game.Subscribe service found at address " + url);

            result = subscribe.subscribeAccepted(partecipant, playerName, localHost, port);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (RemoteException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        if (result) {

            // subscribe accepted
            initialWindow.notifySubscribe();
            System.out.println("You have been added to player list.");
            players = partecipant.getPlayers();
            playersNo = players.length;

            if( playersNo > 1 ){

                cardStack = partecipant.getCardStack();
                topCard = partecipant.getTopCard();
                System.out.println("Card list acquired");
                initialWindow.notifyGameStart();

                System.out.println("Players subscribed:");
                for (int i=0; i < playersNo;i++){
                    System.out.println(players[i].getUsername());
                }

                me = new Node(localHost, port);
                link = new Link(me, players);
                nodeId = link.getNodeId();
                rmaker = new RouterFactory(link);
                mmaker = new MessageFactory(nodeId);
                messageBroadcast.configure(link,rmaker,mmaker);

                System.out.println("My id is " + nodeId + " and my name is " + players[nodeId].getUsername());
                System.out.println("My left neighbour is " + players[link.getLeftId()].getUsername());
                System.out.println("My right neighbour is " + players[link.getRightId()].getUsername());

                currentPlayer = 0;
                isGameEnded = false;

            }else{
                initialWindow.notifyErrorGameStart();
                System.out.println("Not enough players to start the game. :(");
                System.exit(0);
            }
        } else {
            initialWindow.notifyErrorSubscribe();
            System.out.println("Game subscribe unsuccessful. Exit the game.");
            System.exit(0);
        }
    }

    public synchronized void gameStart() {

        // Beginning of the game
        // The thread will looperate inside gameStart until the end of the game.
        tryToMyturn();

        while(board.getRemainedCards() > 0) {
            try {

                // I run when it's not my turn, I'm listening for messages on the buffer.
//                board.setCurrentPlayer(currentPlayer);
                int nextPlayer = 0;
                System.out.println("Waiting up to " + getWaitSeconds() + " seconds for a message..");
                GameMessage m = buffer.poll(getWaitSeconds(), TimeUnit.SECONDS);

                if(m != null) {

                    System.out.println("Processing message " + m);
                    // I get my move back from the message I got
                    move = m.getMove();

                    System.out.println("Message from Node " + m.getFrom());

                    // Check if it's a crash or game message
                    if(m.getNodeCrashed() != -1) {

                        System.out.println("Crash Message");
                        link.nodes[m.getNodeCrashed()].setNodeCrashed();
                        //board.updateCrash(m.getNodeCrashed());
                        retrieveNextPlayerCrash();

                    } else {

                        System.out.println("Game Game.Message");
                        if(m.getPair() == false) {
                            // if the player made the move
                            if(move.getCard1Index()>0 && move.getCard2Index()>0)
                                //board.updateInterface(move);
                            retrieveNextPlayer();
                        } else {

                            //board.updateInterface(move);
                            players[m.getOrig()].incPoints();
                            board.incPointPlayer(m.getOrig(),players[m.getOrig()].getPoints());
                        }
                    }
                    //board.checkRemainCards();
                    System.out.println("The next player is " + currentPlayer);
                    tryToMyturn();
                } else {

                    // Timeout -> Starting AYA check on nearby nodes
                    System.out.println("Timeout");
                    int playeId = currentPlayer;
                    rightId = link.getRightId();
                    boolean currentPlayerFirst = true;
                    int rightIdSave = rightId;

                    while(!link.checkAYANode(rightId)) {
                        if (rightId == playeId) {

                            System.out.println("Current Game.Player has crashed.Sending crash Msg");
                            link.nodes[rightId].setNodeCrashed();
                            link.setRightId((rightId + 1) % players.length);
                            boolean[] nodesCrashed = new boolean[players.length];
                            Arrays.fill(nodesCrashed, false);
                            boolean anyCrash = false;
                            //messageBroadcast.incMessageCounter();
                            int messageCounter=messageBroadcast.retrieveMsgCounter();
                            boolean sendOk = false;
                            int howManyCrash = 0;

                            checkLastNode();

                            while(link.checkAliveNode() == false) {

                                anyCrash = true;
                                howManyCrash = howManyCrash + 1;
                                nodesCrashed[link.getRightId()] = true;
                                System.out.println("Finding a new neighbour");
                                link.incRightId();
                                checkLastNode();

                            }

                            // send crashes of previous nodes to the current player
                            if (currentPlayerFirst == false) {

                                System.out.println("Sono crashati nodi precedenti al current player");
                                System.out.println("RightidSave ->" + rightIdSave);
                                System.out.println("Playeid ->" + playeId);
                                while(((rightIdSave+1) % players.length) <= playeId) {

                                    link.nodes[rightIdSave].setNodeCrashed();
                                    howManyCrash = howManyCrash + 1;
                                    //nodesCrashed[i] = true;
                                    System.out.println("Game.Node before current player");

                                    messageBroadcast.incMessageCounter();
                                    messageCounter = messageBroadcast.retrieveMsgCounter();

                                    //board.updateCrash(rightIdSave);
                                    messageBroadcast.send(mmaker.newCrashMessage(rightIdSave,messageCounter,howManyCrash));

                                    rightIdSave = rightIdSave + 1;
                                }
                            }


                            // Send the current player crash
                            while (sendOk == false) {

                                // do not check the send but first
                                messageBroadcast.incMessageCounter();
                                messageCounter = messageBroadcast.retrieveMsgCounter();
                                System.out.println("Im sending a crash message with id " + messageCounter );
                               // board.updateCrash(rightId);
                                board.clearOldPlayer(currentPlayer);
                                setCurrentPlayer(link.getRightId());
//                                board.setCurrentPlayer(currentPlayer);
                                messageBroadcast.send(mmaker.newCrashMessage(rightId,messageCounter,howManyCrash));
                                sendOk = true;
                            }

                            System.out.println("Next Game.Player is " + players[currentPlayer].getUsername() + " id " + currentPlayer);

                            // Send CrashMessage if crashes were found after the current player

                            if (anyCrash) {
                                // maybe to fix
                                // try to crash 2 players after the current one
                                howManyCrash = howManyCrash + 1;
                                for(int i=0;i<nodesCrashed.length;i++) {
                                    if (nodesCrashed[i] == true) {

                                        messageBroadcast.incMessageCounter();
                                        int messageCounterCrash = messageBroadcast.retrieveMsgCounter();
                                        System.out.println("Sending a CrashMessage id " + messageCounterCrash);
                                        // Send crash msg without error handling
                                        //board.updateCrash(i);
                                        messageBroadcast.send(mmaker.newCrashMessage(i,messageCounterCrash,howManyCrash));
                                    }
                                }
                            }
                            break;
                        } else {
                            currentPlayerFirst = false;
                        }
                        rightId = (rightId + 1) % players.length;
                    }

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void tryToMyturn() {

        while (currentPlayer == nodeId) {

            // When it's my turn, I unlock the board and wait for the move
            // The Game.Client object hangs for a moment but the remote RMI Game.MessageBroadcast class can still
            // receive messages, as soon as the client wakes up it can go back to listening on the buffer to see
            // if there are messages. If there are any, it updates the local interface.

            //board.setCurrentPlayer(currentPlayer);
            System.out.println("Unlock board.");
            board.unlockBoard();
            System.out.println("I'm trying to do a move");
            int nextPlayer = 0;

            try{
                System.out.println("Wait move");
                wait();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }

            // I send the message to my right neighbor's remote class via RMI.
            boolean[] nodesCrashed = new boolean[players.length];
            Arrays.fill(nodesCrashed, false);
            boolean anyCrash = false;
            messageBroadcast.incMessageCounter();
            int messageCounter = messageBroadcast.retrieveMsgCounter();
            boolean sendOk = false;
            int howManyCrash = 0;

            // Retrieve the next active node
            while(link.checkAliveNode() == false) {

                anyCrash = true;
                howManyCrash = howManyCrash + 1;
                nodesCrashed[link.getRightId()] = true;
                System.out.println("Finding a new neighbour");
                link.incRightId();
                if (link.getRightId() == link.getNodeId()) {
                    System.out.println("Unico giocatore, partita conclusa");
                    nextPlayer = board.updateAnyCrash(link.getNodes(),link.getNodeId());
                    board.alertLastPlayer();
                }
            }

            while (sendOk == false) {

                // do not check the send but first
                System.out.println("Im sending a message with id " + messageCounter );
                messageBroadcast.send(mmaker.newGameMessage(move,messageCounter,howManyCrash));
                sendOk = true;
            }


            // I calculate the prox player even without crash
            nextPlayer = board.updateAnyCrash(link.getNodes(),link.getNodeId());


            if (move.getPair() == false) {

                // Increase the next player to play.
                board.clearOldPlayer(currentPlayer);
                setCurrentPlayer(nextPlayer);
//                board.setCurrentPlayer(currentPlayer);

            } else {
                players[nodeId].incPoints();
                board.incPointPlayer(nodeId, players[nodeId].getPoints());
            }
            //board.checkRemainCards();
            board.lockBoard();


            System.out.println("Next Game.Player is " + players[currentPlayer].getUsername() + " id " + currentPlayer);

            // Send CrashMessage if crashes have been detected

            if (anyCrash) {

                howManyCrash = howManyCrash + 1;
                for(int i=0;i<nodesCrashed.length;i++) {
                    if (nodesCrashed[i] == true) {

                        messageBroadcast.incMessageCounter();
                        int messageCounterCrash = messageBroadcast.retrieveMsgCounter();
                        System.out.println("Sending a CrashMessage id " + messageCounterCrash);
                        // Send crash msg without error handling
                        messageBroadcast.send(mmaker.newCrashMessage(i,messageCounterCrash,howManyCrash));
                    }
                }
            }

        }

    }

    // Method that returns a tot of seconds based on the node id
    private long getWaitSeconds() {
        return 10L + nodeId * 2;
    }

    // Method that returns the list of players as soon as the server times out
    public synchronized Player[] getPlayers() {

        if (players == null) {
            try{
                System.out.println("Waiting for other players...");
                wait();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
        return players;
    }

    // When the player has made his move, the board notifies the client
    // that must package it in a message to be sent.
    public synchronized void notifyMove(OnesMove move) {

        this.move = move;
        System.out.println("Notify move");
        notifyAll();
    }

    // Method that retrieves the values ​​of the cards
    public List<Integer> getCardVals(){ return cardVals; }

    // Method that retrieves the values ​​of the cards
    public Stack<PlayingCard> getCardStack() {return cardStack;}

    public PlayingCard getTopCard() {return topCard;}

    // Method that recovers its score
    public int getOwnScore() {

        if(players!=null){
            return players[nodeId].getPoints();
        }else{
            return 0;
        }
    }

    public synchronized Player getCurrentPlayer() {
        if (players == null) {
            try{
                System.out.println("Waiting for other players...");
                wait();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }

        return players[nodeId];
    }

    // Method that retrieves the next player
    public void retrieveNextPlayer() {

        // continues until it finds the first active node

        while(!link.nodes[((currentPlayer + 1) % players.length)].getActive()) {
            board.clearOldPlayer(currentPlayer);
            setCurrentPlayer((currentPlayer + 1) % players.length);
        }
        board.clearOldPlayer(currentPlayer);
        setCurrentPlayer((currentPlayer + 1) % players.length);
        //board.setCurrentPlayer(currentPlayer);

    }

    // Method that recovers the next crashed player
    public void retrieveNextPlayerCrash() {

        if(link.nodes[currentPlayer].getActive()) {
            System.out.println("Game.Player active");
        } else {
            retrieveNextPlayer();
        }
    }

    // Return the node id
    public int getNodeId() {

        return nodeId;
    }

    // Method that checks nearby nodes
    private void checkLastNode() {

        if (link.getRightId() == link.getNodeId()) {

            //board.updateCrash(rightId);
            board.clearOldPlayer(currentPlayer);
//            board.setCurrentPlayer(nodeId);
            System.out.println("Unico giocatore, partita conclusa");
            board.alertLastPlayer();
        }
    }

    private void setCurrentPlayer(int playerId) {
        currentPlayer = playerId;
    }
}
