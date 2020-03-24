package Game;/*
Game.MainP class used to start the client, the server address can be entered
in ipv4 command line format
*/

import View.WindowRegistration;

public class MainP {

    public static WindowRegistration windowMain;

    public static void main(String[] args) {

        String serverAddr;
        /* If the server address in ipv4 format has been entered from the command line * /
        / * The entered address is read as a string*/
        if (args.length == 1) {
            serverAddr = args[0];

        } else {

            /*If nothing is entered from the command line, localhost is used*/
            serverAddr = "localhost";
            System.out.println("Nessun indirizzo ipv4 inserito per cercare il server di gioco.");
            System.out.println("Il server per l'iscrizione al gioco verra' ricercato in locale");
            System.out.println("Per utilizzare un server remoto inserire il suo indirizzo da riga di comando");
        }
        windowMain = new WindowRegistration(serverAddr);

    }
}