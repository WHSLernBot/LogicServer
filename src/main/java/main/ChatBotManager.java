package main;

import Controller.ControllerPool;
import DBBot.BotPool;
import java.util.HashMap;

/**
 *
 * @author Seve
 */
public class ChatBotManager {
    
    private static ChatBotManager manager;
    
    private final BotPool botPool;
    
    private final ControllerPool contPool;
    
    private final HashMap<CBPlattform,CBBenutzer> benutzer;

    private ChatBotManager() {
        
        this.botPool = new BotPool();
        this.contPool = new ControllerPool();
        benutzer = new HashMap<>();
        
    }
    
    /**
     * Gibt das Singelton ChatBotManager zur�ck.
     * @return 
     */
    public static ChatBotManager getInstance() {
        if(manager == null) {
            manager = new ChatBotManager();
        }
        
        return manager;
    }
    
    /**
     * Gibt den BotPool zur�ck.
     * @return 
     */
    public BotPool gibBotPool() {
        return botPool;
    }

    /**
     * Gibt den ControllerPool zur�ck.
     * @return 
     */
    public ControllerPool gibContPool() {
        return contPool;
    }
    
    public CBBenutzer gibBenutzer(CBPlattform pt) {
        
        CBBenutzer be = benutzer.get(pt);
        
        //Zugriffe z�hlen/verwalten
        
        return be;
        
    }
     
}
