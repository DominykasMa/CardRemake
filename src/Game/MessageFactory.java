package Game;/*

Game.MessageFactory class, behaves like the Game.RouterFactory class but with messages.

*/

public class MessageFactory {

    private int myId;

    public MessageFactory(int myId) {

        this.myId = myId;
    }

    //Creation of a classic Game.GameMessage where the move made is contained
    public GameMessage newGameMessage(OnesMove move,int messageCounter,int howManyCrash) {

        return new GameMessage(myId,messageCounter,move,howManyCrash);
    }

    //Creation of a Game.GameMessage used to report node crashes
    public GameMessage newCrashMessage(int nodeCrashedId,int messageCounter,int howManyCrash) {

        return new GameMessage(myId,messageCounter,nodeCrashedId,howManyCrash);
    }
}