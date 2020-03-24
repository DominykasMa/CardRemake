package Game;

import java.rmi.RemoteException;

/*
Game.AYARouter class used to create the router that manages the AYA control
*/


public class AYARouter extends AbstractRouter {



    // Method for creating an Game.AYARouter instance
    public AYARouter(Link link,RouterFactory rmaker) {

        super(link);
    }

    @Override
    public void run() {
        super.run();
    }

    // Method that makes a remote RMI call on the nearby node
    @Override
    protected synchronized void performCallHook(ServiceBulk to) {

        try {
            to.messageBroadcast.checkNode();
        } catch (RemoteException re) {
            re.printStackTrace();
            System.out.println("Remote Exception");
        }
    }
}