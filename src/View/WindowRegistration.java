package View;

import View.Board;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
/**
 * The Window Registration class allows you to initialize a graphical registration interface to the game.
  * Launch the client on registration click.
 */

public class WindowRegistration {
    private static boolean RIGHT_TO_LEFT = false; // variable that sets the orientation of the position of the elements in the window
    private static final int SIZE_OF_TEXTFIELD = 10; // variable that handles the textfield length
    public static String IMG_PATH = "C:\\Users\\Domas\\Desktop\\DistributedProject\\RemakeMemory\\src\\img\\durak.png"; //string for logo path
    public static Board board;
    public static boolean awaitTurn;
    public static JLabel waiting;
    public static JLabel feedback;
    public static JFrame frame;
    public static JButton btnRegistration;
    public static String serverAddr;


    /*
     * setCloseWindow is a method which manages the closing of the window
     */
    public WindowRegistration(String serverAddr) {

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        UIManager.put("swing.boldMetal", Boolean.FALSE);
        this.serverAddr = serverAddr;

        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });

    }

    private void setCloseWindow(final JFrame frame) {
        int input = JOptionPane.showOptionDialog(frame, //the root is the frame
                "Are you sure you want to exit the game?",
                "Exit",
                JOptionPane.YES_NO_OPTION, //type of alert button
                JOptionPane.INFORMATION_MESSAGE, // type of alert
                null,null,null);
        if( input == JOptionPane.YES_OPTION)
            System.exit(0);// if I click Yes I will exit the game
    }

    /*
     * settingEventRegistration is a method that handles the name entry event in the textfied
     */
    private void settingEventRegistration(final JFrame fr, JTextField tf){
        if(tf.getText() == null || tf.getText().isEmpty()){
            // I handle the case where I don't add any name
            JOptionPane.showOptionDialog(null,
                    "You need to add a username!",
                    "Exit",
                    JOptionPane.CLOSED_OPTION,
                    JOptionPane.ERROR_MESSAGE, // type of message
                    null,null,null);
        }


        // I manage the case in which I insert an empty space in the textfield, I don't want them
        else if(tf.getText().contains(" ") ){
            JOptionPane.showOptionDialog(null,
                    "Not insert \"Blank Spaces\" in the Username",
                    "Exit",
                    JOptionPane.CLOSED_OPTION,
                    JOptionPane.ERROR_MESSAGE, // type of error message
                    null,null,null);

        }else{
            // in case it's all ok, then I run the client and pass it the string
    // as soon as the username is confirmed the windowregistration window disappears with fr.setVisible ()
    // Lastly create a board and start the doClientThread thread which will last until the end.
            String userName = tf.getText();
            board = new Board(this);
            board.init(userName,serverAddr);
        }
    }

    /*
     * addComponentsToPane is a borad layout method for inserting all objects
     */

    public void addComponentsToPane(final JFrame pane) {


        if (!(pane.getLayout() instanceof BorderLayout)) { // check if the frame is in bordarLayout mode
            pane.add(new JLabel("Container doesn't use BorderLayout!"));
            return;
        }

        if (RIGHT_TO_LEFT) { // check if the position is must be eastern or western
            pane.setComponentOrientation(
                    java.awt.ComponentOrientation.RIGHT_TO_LEFT);
        }

        pane.getContentPane().setForeground(Color.WHITE); // I set the background color

        JPanel panelImg = new JPanel(); // I create the panel to insert the logo

        // I try to open the image
        try{
            BufferedImage imgLogo;
            imgLogo = ImageIO.read(new File(IMG_PATH));
            ImageIcon icon = new ImageIcon(imgLogo);
            JLabel labelLogo = new JLabel(icon);
            panelImg.add(labelLogo,BorderLayout.CENTER); // and place the label in the center

        }catch (IOException e){
            e.printStackTrace();
        }

        panelImg.setSize(new Dimension(400,375)); // I set the size of the panel
        panelImg.setBorder(new EmptyBorder(10,0,0,0)); // set the top edge so that it is not attached to the edge of the window
        pane.add(panelImg, BorderLayout.PAGE_START); // I add the panel in the window (PAGE_START is equivalent to the TOP part of the window)


        JPanel panelRegistration = new JPanel(); // I create the registration panel
        JLabel userLabel = new JLabel("User"); // I create the label with User written


        final JTextField userEntry = new JTextField(); //I create the textfield for entering the name of the person who registers
        userEntry.setColumns(SIZE_OF_TEXTFIELD); // I set the size of the textfield

        btnRegistration = new JButton("Registration"); // I create the button to start recording

        feedback = new JLabel();
        waiting = new JLabel();

        // now I manage the event related to the button to register on click
        btnRegistration.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        settingEventRegistration(pane,userEntry);
                        btnRegistration.setEnabled(false);
                    }
                });
                t.start();
            }
        });

        // I manage the event linked to the button but pressing enter
        btnRegistration.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    btnRegistration.setEnabled(false);
                    userEntry.setEditable(false);
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    btnRegistration.setEnabled(false);
                    userEntry.setEditable(false);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    settingEventRegistration(pane,userEntry);
                    btnRegistration.setEnabled(false);
                    userEntry.setEditable(false);
                }
            }
        });

        // I handle the registration event by sending from textfield
        userEntry.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    btnRegistration.setEnabled(false);
                    userEntry.setEditable(false);
                }
            }
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    btnRegistration.setEnabled(false);
                    userEntry.setEditable(false);
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            settingEventRegistration(pane,userEntry);
                            btnRegistration.setEnabled(false);
                            userEntry.setEditable(false);
                        }
                    });
                    t.start();
                }

            }
        });

        // I now create the grouping of the userlabel, textfield objects and the register button
        GroupLayout groupRegistration = new GroupLayout(panelRegistration); // I'm going to pass him the root where to create the GroupLayout
        groupRegistration.setAutoCreateGaps(true); // septum the space between objects


        //setto gli oggetti con  l'orientamento orizontale
        groupRegistration.setHorizontalGroup(groupRegistration.createParallelGroup()
                        .addComponent(userLabel) // add the label
                        .addComponent(userEntry) // add the textfield
                        .addComponent(btnRegistration) // I add the button
                        .addComponent(feedback)
                .addComponent(waiting)
        );
        //setto gli oggetti con l'orientamento verticaole
        groupRegistration.setVerticalGroup(groupRegistration.createSequentialGroup()
                .addComponent(userLabel) // add the label
                .addComponent(userEntry) // add the textfield
                .addComponent(btnRegistration) // add the button
                .addComponent(feedback)
                .addComponent(waiting)
        );

        // I manage the window closing event
        pane.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setCloseWindow(pane);

            }
        });

        panelRegistration.setBorder(new EmptyBorder(50,50,0,50)); // set the edges of the recording panel
        pane.add(panelRegistration, BorderLayout.CENTER); // position the panel in the center
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private void createAndShowGUI() {

        //Create and set up the window.
        frame = new JFrame("Registration - Memory");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //Set up the content pane.
        addComponentsToPane(frame);

        //Use the content pane's default BorderLayout.
        //Display the window.
        frame.setSize(new Dimension(275,275));
        frame.setResizable(false);
        frame.setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
    }

    public void notifySubscribe() {
        feedback.setText("Registration Successful");
        waiting.setText("I'm waiting for other players to start the game.");
    }

    public void notifyErrorSubscribe() {
        int input = JOptionPane.showOptionDialog(null, // the root is the frame
                "Registration not occured",
                "Sorry",
                JOptionPane.YES_OPTION, // type of alert button
                JOptionPane.INFORMATION_MESSAGE, // type of alert
                null,null,null);
        if(input == JOptionPane.YES_OPTION)
            System.exit(0);
    }

    public void notifyGameStart() {
        feedback.setText("");
        waiting.setText("Starting game...");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        frame.setVisible(false);
    }

    public void notifyErrorGameStart() {
        int exit = JOptionPane.showConfirmDialog(null,
                "Can't find other players. Do you want to exit?" ,
                "Sorry",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE);
        if (exit == JOptionPane.YES_OPTION)
            System.exit(0);
        else{
            feedback.setText("Can't find other players.");
            waiting.setText("Exit game.");
        }
    }
}