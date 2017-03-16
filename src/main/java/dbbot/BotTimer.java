package dbbot;

import java.util.TimerTask;
import main.ChatBotManager;

/**
 * Dieser TimerTask initiert das neu berechner aller Daten.
 * @author Seve
 */
public class BotTimer extends TimerTask {

    /**
     * Berechnet alles neu.
     */
    @Override
    public void run() {
        ChatBotManager.getInstance().gibBotPool().berechneAlles();
    }
    
}
