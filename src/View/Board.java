package View;

import Game.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import java.util.*;
import java.lang.Thread;
import java.util.List;

import static Interfaces.GameConstants.FIRSTHAND;

/**
 * The View.Board class allows you to instantiate the board with cards to run the memory game
 */

public class Board extends JFrame {// the extension to JFrame allows me to directly create a graphic window
//    private List<CardGraphic> cards; // is the list of cards that will be represented on the board
//    private List<Integer> cardVals;
//    private CardGraphic selectedCard; //it is a tmp object that keeps the first card in my memory when I have to search for the second
//    private CardGraphic c1; // first card object that I need comparison
//    private CardGraphic c2; // second paper object that I need comparison
    private int remainedCards;
    private javax.swing.Timer t; // is a timer that makes the matched pair of cards visible to me (applies in both cases where the match has a positive or negative outcome
    private boolean pair = false;
    public static Client cl;
    private Player[] players;
    private Player currentPlayer;
    private OnesMove move;
    private final WindowRegistration initialWindow;
    private static ScoringBoard scoring;
    private java.util.Timer timerMove;    // move timer
    public static String serverAddr;
    private tablePanel table;
    private PlayerPanel playerPanel;
    private Stack<PlayingCard> cardStack;
    private PlayingCard selectedCard;
    private PlayingCard topCard;
    private Stack<PlayingCard> playedCards;
    private Dealer dealer;

    public Board(final WindowRegistration initialWindow) {
        this.initialWindow = initialWindow;
    }

    public void init(String userName,String serverAddr) {

        this.serverAddr = serverAddr;

        //I manage the event when the window board closes (symbol at the top left)
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setExitControl(); //function that manages the closing of the window (explained below)
            }
        });

        //creo la menubar
        JMenuBar menuBar = new JMenuBar();

        JMenu menuImpostazioni = new JMenu("Settings"); // I create the first settings menu
        JMenuItem esci = new JMenuItem("Exit", KeyEvent.VK_Q); // I create the closing item (VK_Q is the symbol that allows me to see the closing shortcut)
        KeyStroke crtlQKeyStroke = KeyStroke.getKeyStroke("control Q"); // I create the CRTL-Q shortcut for closing the window
        esci.setAccelerator(crtlQKeyStroke); // attach the event to the item item in the settings menu
        menuBar.add(menuImpostazioni); // I add the settings menu inside the menubar

        esci.setToolTipText("Exit Application");
        esci.addActionListener(new ActionListener() { // attack the event management to the item exit Click
            @Override
            public void actionPerformed(ActionEvent e) {
                setExitControl();
            }
        });

        /*
       I manage the closing event through the shortcut
         In Java, when handling the keyPressed event you always have three methods to set
         one concerning the type of key pressed, one that manages the event of pressing the key,
         one that manages the release of the key. To avoid problems, I set them all the same
         way, so that they perform the same method.
        */
        esci.addMenuKeyListener(new MenuKeyListener() {
            @Override
            public void menuKeyTyped(MenuKeyEvent menuKeyEvent) {
                if(menuKeyEvent.getKeyCode() == KeyEvent.VK_Q)
                    setExitControl();

            }

            @Override
            public void menuKeyPressed(MenuKeyEvent menuKeyEvent) {
                if(menuKeyEvent.getKeyCode() == KeyEvent.VK_Q)
                    setExitControl();
            }

            @Override
            public void menuKeyReleased(MenuKeyEvent menuKeyEvent) {
                if(menuKeyEvent.getKeyCode() == KeyEvent.VK_Q)
                    setExitControl();

            }

        });


        menuImpostazioni.add(esci); // finally I add the item exit in the settings menu
        setJMenuBar(menuBar); // finally attack the menubar inside the window
        //I create the second menu of rules
        JMenu menuRegole = new JMenu("?"); // I add the menu regarding a minimum of documentation
        JMenuItem regole = new JMenuItem("Rules"); // I create the rules item
        JMenuItem about = new JMenuItem("About"); // I create the item regarding our group
        // I manage the event at the click on the about
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setAboutControl(); // this method will be explained in detail below
            }
        });
        //I manage the event on click concerning the rules
        regole.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setRegoleControl(); // this method will be explained in detail below
            }
        });
        menuRegole.add(regole); //I add the rules item in the documentation menu
        menuRegole.add(about); // I add the about item to the documentation menu
        menuBar.add(menuRegole); // I add the rules menu to the menubar


        /*------popolo la board-------*/
        initialWindow.notifySubscribe();
        cl = new Client(userName, this, initialWindow, serverAddr);
        cardStack = cl.getCardStack();
        players = cl.getPlayers();
        currentPlayer = cl.getCurrentPlayer();
//        remainedCards = cardStack.size();
        topCard = cl.getTopCard();


        setTitle("Card  - "+ userName);
        Container boardLayout = this.getRootPane(); // I take the portion of the window area I need
        boardLayout.setBackground(new Color(4,58,21));
        boardLayout.setLayout(new BorderLayout()); // I set the layout as BorderLayout

//        scoring = new ScoringBoard(players); // I create the scoring board (it's a JPanel extend)
//        scoring.buildGridForScore();


        //boardLayout.add(scoring, BorderLayout.LINE_START);
        for(int i=1;i<=FIRSTHAND;i++)
        currentPlayer.obtainCard(cardStack.pop());

        playerPanel = new PlayerPanel(currentPlayer);
        playerPanel.setBackground(new Color(56,27,0));
        playerPanel.setBorder(BorderFactory.createMatteBorder(
                1, 1, 1, 1, Color.red));
        boardLayout.add(playerPanel, BorderLayout.SOUTH);
        System.out.println("player panel");
        table = new tablePanel(topCard);
        boardLayout.add(table, BorderLayout.CENTER);


//        playedCards = new Stack<PlayingCard>();
//        playedCards.add(topCard);

        //request to play a card
//
//        if (isValidMove(selectedCard)) {
//
//            selectedCard.removeMouseListener(CARDLISTENER);
//            playedCards.add(selectedCard);
//            cardStack.removePlayedCard(clickedCard);
//
////            // function cards ??
////            switch (clickedCard.getType()) {
////                case ACTION:
////                    performAction(clickedCard);
////                    break;
////                case WILD:
////                    performWild((WildCard) clickedCard);
////                    break;
////                default:
////                    break;
////            }
//
//            updatePanel(selectedCard);
//        }





//        cards = new ArrayList<CardGraphic>();  // I use a list of cards to add the cards that will be marked
//
//        int i = 0;
//        for (int val : cardVals) {
//            final CardGraphic c = new CardGraphic();
//            c.setValue(val); // add the value of the card
//            c.setId(i); 	// add the position of the card
//
//            // I'm handling events for each card
//            c.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    selectedCard = c; // this implies that with each click there will be the selectedCard that will carry out the matching
//                    doTurn(); // explained later
//                }
//            });
//
//            cards.add(c);
//            i++;
//        }

        /*
         * The timer allows me to have a margin of seconds to see the cards, by default it is set to 750 but it can vary
         */
//        t = new javax.swing.Timer(750, new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                checkCards(true); // this is the function that controls card matching
//            }
//        });
//
//        t.setRepeats(false);

        /*--- I place the cards on the board----*/
        //pane.setLayout(new GridLayout(4, 5)); // I create a grid layout
//        for (CardGraphic c : cards) { // I place the cards (by increasing IDs) within the grid
//            c.setImageLogo(); // in the initialisation phase of the board we want all the cards to be face down
//            pane.add(c); // I insert the cards inside the gridlayout
//        }


//
//        /*
//         * I visualize the graphic window inserting all the parameters that I need
//         */
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // this method sets the default setting when the window is closed.
        setSize(new Dimension(1024,900)); // set the window size (we can also change it)
        //setExtendedState(JFrame.MAXIMIZED_BOTH);
       // Dimension dim = Toolkit.getDefaultToolkit().getScreenSize(); // these two lines allow me to center the window with respect to the screen absolutely
        //setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
        setVisible(true); // obviously I make the window visible

        lockBoard();
        doClientThread();
    } //----End of the manufacturer


    // I create Thread Game.Client, it will last until the end of the game.
    public void doClientThread() {
        Thread t2 = new Thread() {
            public synchronized void run() {
                cl.gameStart();
            }
        };
        t2.start();
    }

    // Method used to update the ui. The method is called
    // from the client when it receives new messages.
//    public void updateInterface(OnesMove move) {
//
//        this.move = move;
//
//        c1 = cards.get(move.getCard1Index());
//        c2 = cards.get(move.getCard2Index());
//        c1.removeImage();
//        c1.setImage();
//        c2.removeImage();
//        c2.setImage();
//
//        // Used to slow down the animation
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException ie) {
//            ie.printStackTrace();
//        }
//
//        checkCards(false);
//    }

    /*
     * checkCard () is the method that controls the card match
     */
//    private void checkCards(boolean send){
//        if(c1.getValue() == c2.getValue()){ // if the values ​​are equal
//            c1.setEnabled(false);
//            c2.setEnabled(false);
//            c1.setMatched(true);
//            c2.setMatched(true);
//
//            remainedCards = remainedCards - 2;
//
//            if(send){
//                pair = true;
//                timerMove.cancel();
//                timerMove.purge();
//                sendMove();
//            }
//
//        }else{ // in case the matching is not successful
//            c1.setText(""); // I don't show the first card (method inherited from JButton)
//            c2.setText(""); // I don't show the first card (method inherited from JButton)
//            c1.setImageLogo();// reset the logo image
//            c2.setImageLogo(); // reset the logo image
//
//            if(send){
//                pair = false;
//                timerMove.cancel();
//                timerMove.purge();
//                sendMove();
//            }
//        }
//
//        c1 = null;
//        c2 = null;
//
//    } //--- fine checkCards()


    /*
     * sendMove()notifies the client of the move
     */
//    public void sendMove(){
//        move = new OnesMove(c1.getId(),c2.getId(),pair);
//        cl.notifyMove(move);
//        lockBoard();
//    }
//
//
//    /*
//    * doTurn () is the method that allows me to discover the cards in fact it has two conditions: one to discover the first card, one to discover
//    * the second card
//    */
//    public void doTurn(){
//
//        // if none of the cards are face up
//        if(c1 == null  && c2 == null){
//            c1 = selectedCard;
//            c1.removeImage(); // remove the logo image
//            c1.setImage(); 	// set the image referring to the card
//
//        }
//
//        // if the second card is selected
//        if(c1 != null && c1 != selectedCard && c2 == null){
//            c2 = selectedCard;
//            c2.removeImage (); // I remove the logo
//            c2.setImage (); // set the image referring to the card
//            t.start (); // I start the timer for the card display
//        }
//    } //---- fine doTurn()



    /* ----methods for managing the interface-------*/
    private void setExitControl(){ // set the exit from the viewer window an alert
        int input = JOptionPane.showOptionDialog(null, //root that opens the alert (in this case there is no need to specify one)
                "Are you sure you want to exit the game?", // the message
                "Exit", // title of the alert window
                JOptionPane.YES_NO_OPTION, // type of alert buttons
                JOptionPane.INFORMATION_MESSAGE, //type of message
                null,null,null); // other parameters that are useless
        if(input == JOptionPane.YES_OPTION) { // if the YES button is clicked I make a brutal exit (0)!
            System.exit(0);
        }

    }

    private void setAboutControl(){ // displays the alert for info on the about
        JOptionPane.showOptionDialog(null,
                "Memery Game was realized by: Salvatore Alescio,\n" +
                        "Alice Valentini, Andrea Zuccarini. For \n" +
                        "Distributed Systems' project.",
                "Exit",
                JOptionPane.CLOSED_OPTION, // the button is only for the close
                JOptionPane.INFORMATION_MESSAGE, // type of message
                null,null,null);
    }

    private void setRegoleControl(){ // show me the alert for the info on the rules
        JOptionPane.showOptionDialog(null,
                "Memory is a card game in which all of the card are laid face down on a surface and two cards\n"+
                        "are flipped face up over each turn. The object of the game is to turn over pairs of matching \n"+
                        "cards. Memory, can be played with any number of players or as solataire.\n"+
                        "It is a particularly good game for young children, though adults may find it challenging and \n"+
                        "stimulating as well. The scheme is often used in quiz shows and can be employed as an educatio-\n" +
                        "nal game.",
                "Exit",
                JOptionPane.CLOSED_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,null,null);

    }

    /*----card blocking method----*/
    public void lockBoard(){
        playerPanel.setEnabled(false);
        playerPanel.setVisible(false);
    }

    /*----method that unlocks cards----*/
    public void unlockBoard(){
//        for (CardGraphic c : cards) {
//            if (c.isMatched()==false) c.setEnabled(true); // enable all card buttons
//        }

        playerPanel.setEnabled(true);
        playerPanel.setVisible(true);
        timerMove = new java.util.Timer();
        pair = false;

        // "saw" the player after 30 seconds by sending a card with negative ids
        timerMove.schedule( new TimerTask() {
            @Override
            public void run() {
                System.out.println("Timer expired. Move forwarded.");
//                c1 = new CardGraphic();
//                c2 = new CardGraphic();
//                c1.setId(-1);
//                c2.setId(-1);
//                sendMove();
//                c1 = null;
//                c2 = null;
            }
        } , 30000);
    }

    public void refreshPanel(){
        currentPlayer.setCards();
        table.revalidate();
        revalidate();
    }

    public void updatePanel(PlayingCard playedCard){
        table.setPlayedCard(playedCard);
        refreshPanel();
    }

    public boolean isValidMove(PlayingCard playedCard) {
        PlayingCard topCard = peekTopCard();

        if (playedCard.getColor().equals(topCard.getColor())
                || playedCard.getValue().equals(topCard.getValue())) {
            return true;
        }
        return false;
    }

    public PlayingCard peekTopCard() {
        return playedCards.peek();
    }


    public void incPointPlayer(int nodeId, int score) {
        scoring.setPlayerScore(nodeId, score);
    }

//    public void setCurrentPlayer(int id){
//        scoring.setCurrentPlayer(id);
//    }

    public void clearOldPlayer(int id) {
        scoring.clearOldPlayer(id);
    }

    public int getRemainedCards(){ return remainedCards; }

    //method that allows me to know who won
//    public List<String> getPlayerWins(Player[] players){
//
//        List<Integer> scoreList = new ArrayList<Integer>(); // list of scores
//        List<String>  returnPlayer = new ArrayList<String>(); //list of winners' names
//
//        //populate the scoreList
//        for(int i = 0; i<players.length; i++)
//            scoreList.add(players[i].getPoints());
//        System.out.println(scoreList);
//
//        int max = Collections.max(scoreList); // I'm looking for the best
//
//        //returnPlayer people looking for players who have the same highest score (set for draw)
//        for(int i = 0; i < players.length; i++){
//            if(players[i].getPoints() == max)
//                returnPlayer.add(players[i].getUsername());
//        }
//
//        return returnPlayer;
//    }

    //Method that graphically sets the players found inactive
    public int updateAnyCrash(Node[] nodes,int myId) {


        boolean crash = true;
        int i = (myId + 1) % nodes.length;
        while(crash) {
            if (!nodes[i].getActive()) {
                scoring.setPlayerCrashed(i);
                i = (i + 1) % nodes.length;
            } else {
                crash = false;
            }
        }
        // The prox active player returns
        return  i;
    }

    // Method that updates the graphical interface of a node that has crashed
//    public void updateCrash(int id) {
//        scoring.setPlayerCrashed(id);
//    }

    // Graphic alert if there is only one player left
    public void alertLastPlayer(){

        int input = JOptionPane.showOptionDialog(null,
                "Unico giocatore rimasto.Vittoria",
                "Game.Player Win",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,null,null);
        if(input == JOptionPane.OK_OPTION) {
            System.exit(0);
        }

    }

//    public void checkRemainCards() {
//
//        if(remainedCards==0){
//            List<String> playerWin = this.getPlayerWins(players); // winning players
//            String playerWinText = "";
//            for(String text : playerWin){
//                playerWinText += text+" "; // to be honest stylistically it sucks a little
//            }
//            int input = JOptionPane.showOptionDialog(null,
//                    "Game Ended -> Your Score is " + String.valueOf(cl.getOwnScore())+". "+playerWinText+ "wins!",
//                    "Game Ended",
//                    JOptionPane.DEFAULT_OPTION,
//                    JOptionPane.INFORMATION_MESSAGE,
//                    null,null,null);
//            if(input == JOptionPane.OK_OPTION) { // if the YES button is clicked I make a brutal exit (0)!
//                System.exit(0);
//            }
//
//        }
//    }

}


