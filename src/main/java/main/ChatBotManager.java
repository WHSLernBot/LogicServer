package main;

import Controller.ControllerPool;
import DBBot.BotPool;

/**
 *
 * @author Seve
 */
public class ChatBotManager {
    
    private ChatBotManager manager;
    
    private final BotPool botPool;
    
    private final ControllerPool contPool;

    private ChatBotManager() {
        
        this.botPool = new BotPool();
        this.contPool = new ControllerPool();
        
    }
    
    /**
     * Gibt das Singelton ChatBotManager zurück.
     * @return 
     */
    public ChatBotManager getInstance() {
        if(manager == null) {
            manager = new ChatBotManager();
        }
        
        return manager;
    }

    
    /**
     * Gibt den BotPool zurück.
     * @return 
     */
    public BotPool getBotPool() {
        return botPool;
    }

    /**
     * Gibt den ControllerPool zurück.
     * @return 
     */
    public ControllerPool getContPool() {
        return contPool;
    }
     
}
