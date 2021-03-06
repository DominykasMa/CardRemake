package Game;

import java.io.Serializable;

/*
Class that creates a general messagge type object, the Game.GameMessage class inherits from this class.
*/

public class Message implements Serializable, Cloneable {

    private int origId;
    private int fromId;
    private int messageId;


    // Method used to create a Game.GameMessage

    public Message(int origId,int messageId) {
        this.origId = origId;
        this.fromId = origId;
        this.messageId = messageId;
    }

    public int getOrig() {
        return origId;
    }

    public void setFrom(int fromId) {
        this.fromId = fromId;
    }

    public int getFrom() {
        return fromId;
    }
    public Object clone() {
        Message m = new Message(origId,messageId);
        m.setFrom(fromId);
        return m;
    }
    public String toString() {
        return "received from " + fromId + ", created by " + origId;
    }

    public int getId() {
        return messageId;
    }

}