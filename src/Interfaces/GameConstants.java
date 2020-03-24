package Interfaces;
import java.awt.*;

import Game.InfoPanel;
import Game.MyCardListener;

public interface GameConstants extends DurakConstants {
	
	int TOTAL_CARDS = 52;
	int FIRSTHAND = 6;
	
	Color[] CARD_COLORS = {RED, BLACK};
	String [] SUITS = {"♣","♦","♥","♠"};
	
	int[] CARD_NUMBERS =  {2,3,4,5,6,7,8,9,10};
	String [] CARD_FACES = {"J","Q","K","A"};

	InfoPanel infoPanel = new InfoPanel();
	MyCardListener CARDLISTENER = new MyCardListener();

}
