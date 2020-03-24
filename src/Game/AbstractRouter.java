package Game;/*
Abstract class that retrieves the right neighbor reference and defines the performCallHook method
which is used within the Game.Router class.
*/

public abstract class AbstractRouter implements Runnable {

    protected Link link;
    protected GameMessage gameMsg;
    protected Message crashMsg;


    // Method used to create an Game.AbstractRouter that manages a Game.GameMessage
    public AbstractRouter(Link link, GameMessage gameMsg) {

        this.link = link;
        this.gameMsg = gameMsg;
        this.crashMsg = null;

    }

    // Method used to create an Game.AbstractRouter that manages a CrashMessage
    public AbstractRouter(Link link, Message crashMsg) {

        this.link = link;
        this.crashMsg = crashMsg;
        this.gameMsg = null;
    }


    //Method used to create an Abstract Game.Router that manages an AYA request
    public AbstractRouter(Link link) {

        this.link = link;
    }

    //Metodo Runnable
    public void run() {


        try{
            ServiceBulk right = null;
            // If the reference is not found, set active = false in the node
            right = link.getRight(); //the reference of the right neighbor is retrieved
            performCallHook(right);	// router function
            System.out.println("I got right reference");

        }catch (NullPointerException np) {

            //recipient unreachable
            System.out.println("Can't forward the message to neighbour.");

        }
    }

    protected abstract void performCallHook(ServiceBulk to);

}