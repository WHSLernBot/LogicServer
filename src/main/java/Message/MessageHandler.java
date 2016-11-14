package Message;

import java.util.TimerTask;
import main.ChatBotManager;


/**
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
