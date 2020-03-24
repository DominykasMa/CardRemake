package Game;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import View.PlayingCard;

public class MyCardListener extends MouseAdapter {

    PlayingCard sourceCard;

//    public void mousePressed(MouseEvent e) {
//        sourceCard = (PlayingCard) e.getSource();
//
//        try{
//            if(myServer.canPlay)
//                myServer.playThisCard(sourceCard);
//
//        }catch(NullPointerException ex){
//            ex.printStackTrace();
//        }
//    }

    @Override
    public void mouseEntered(MouseEvent e) {
        super.mouseEntered(e);

        sourceCard = (PlayingCard) e.getSource();
        Point p = sourceCard.getLocation();
        p.y -=20;
        sourceCard.setLocation(p);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        sourceCard = (PlayingCard) e.getSource();
        Point p = sourceCard.getLocation();
        p.y +=20;
        sourceCard.setLocation(p);
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

}
