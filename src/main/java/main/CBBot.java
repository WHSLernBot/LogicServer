package main;

import java.util.TimerTask;

/**
 *
 * @author Seve
 */
public class CBBot extends TimerTask {
    
    @Override
    public void run() {
        
        ChatBotManager.getInstance().loesche();
        
    }
    
}
