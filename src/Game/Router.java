package Game;

import java.rmi.RemoteException;

/*
class that extends abstractrouter, in charge of sending game messages.
*/

public class Router extends AbstractRouter {

    private GameMessage gameMsg;


    public Router(Link link, GameMessage gameMsg, RouterFactory rmaker) {

        super(link,gameMsg);
        this.gameMsg = gameMsg;

    }



    @Override
    public void run() {
        super.run();
    }

    /*Method that uses an rmi call, as an input parameter
    there is a reference to the Game.ServiceBulk type right neighbor
    call from messageBroadcast to send a message*/
    @Override
    protected synchronized void performCallHook(ServiceBulk to) {

        GameMessage cloneMsg = (GameMessage)gameMsg.clone();
        cloneMsg.setFrom(link.getNodeId());

        try {
            to.messageBroadcast.forward(cloneMsg); //chiamata rmi
        } catch (RemoteException rE) {
            rE.printStackTrace();
            System.out.println("RemoteException");
        }
    }

}