package Message;

import java.util.TimerTask;
import main.ChatBotManager;


/**
 * Diese Klasse wird vom Timer des MessageHandlers aufgerufen und startet den
 * Sendevorgang der Nachrichten.
 * 
 * @author Seve
 */
public class MessageHandler extends TimerTask {

    /**
     * Initiert das Senden.
     */
    @Override
    public void run() {
        ChatBotManager.getInstance().sendeNachrichten();
    }
 
}
