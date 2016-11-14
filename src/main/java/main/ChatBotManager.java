package main;

import Controller.ControllerPool;
import DBBot.BotPool;
import Message.Nachricht;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Timer;

/**
 *
 * @author Seve
 */
public class ChatBotManager {
    
    private static ChatBotManager manager;
    
    private final BotPool botPool;
    
    private final ControllerPool contPool;
    
    private final HashMap<CBPlattform,CBBenutzer> benutzer;
    
    private LinkedList<Nachricht> nachrichten;
    
    private final Calendar calendar;
    
    private Timer timer;

    private ChatBotManager() {
        
        this.botPool = new BotPool();
        this.contPool = new ControllerPool();
        benutzer = new HashMap<>();
        this.calendar = Calendar.getInstance();
        
    }
    
    /**
     * Gibt das Singelton ChatBotManager zurück.
     * @return 
     */
    public static ChatBotManager getInstance() {
        if(manager == null) {
            manager = new ChatBotManager();
        }
        
        return manager;
    }
    
    /**
     * Gibt den BotPool zurück.
     * @return 
     */
    public BotPool gibBotPool() {
        return botPool;
    }

    /**
     * Gibt den ControllerPool zurück.
     * @return 
     */
    public ControllerPool gibContPool() {
        return contPool;
    }
    
    public CBBenutzer gibBenutzer(CBPlattform pt) {
        
        CBBenutzer be = benutzer.get(pt);
        
        //Zugriffe zählen/verwalten
        be.gain();
        
        return be;
        
    }
    
    public void addNachricht(Nachricht n) {
        
        Timestamp now = new Timestamp(calendar.getTime().getTime());
        
        if (n.getZeit() == null || n.getZeit().before(now)) {
            // spark bekommt Nachricht
        } else {
            
            Iterator it = nachrichten.iterator();
            
            int i = 0;
            
            Nachricht na;
            
            while(it.hasNext()) {
                
                na = (Nachricht) it.next();
                
                if(na.getZeit().before(n.getZeit())) {
                    break;
                }
                i++;
            }
            
            nachrichten.add(i, n);
            
            setzeTimer();
            
        }
    }
    
    public void sendeNachrichten() {
        
        Timestamp now = new Timestamp(calendar.getTime().getTime());
        
        Iterator it = nachrichten.iterator();
        
        Nachricht na;
            
        while(it.hasNext()) {

            na = (Nachricht) it.next();

            if(na.getZeit().after(now)) {
                break;
            }

            //spark muss nachricht kriegen

            it.remove();
        }
            
        setzeTimer();
    }
    
    private void setzeTimer() {
        
        timer.schedule(task, time);
        
    }
     
}
