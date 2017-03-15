package main;

import java.util.TimerTask;

/**
 * Dieser TimerTask ruft die loesche Methode des ChatBotManager auf, um alte
 * CBBenutzer aus der dortigen map zu loeschen.
 * @author Seve
 */
public class CBBot extends TimerTask {
    
    @Override
    public void run() {
        
        ChatBotManager.getInstance().loesche();
        
    }
    
}
