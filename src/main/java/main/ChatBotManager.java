package main;

import Controller.ControllerPool;
import DBBot.BotPool;
import Message.MessageHandler;
import Message.Nachricht;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Timer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
    
    private final Lock nachrichtenLock;
    
    private final Lock benutzerLock;

    private ChatBotManager() {
        
        this.botPool = new BotPool();
        this.contPool = new ControllerPool();
        benutzer = new HashMap<>();
        this.calendar = Calendar.getInstance();
        
        nachrichtenLock = new ReentrantLock();
        benutzerLock = new ReentrantLock();
        
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
        
        CBBenutzer be = null;
        benutzerLock.lock();
        try {
           be = benutzer.get(pt); 
           be.gain();
        } finally {
            benutzerLock.unlock();
        }
        
        
        return be;
        
    }
    
    public void addNachricht(Nachricht n) {
        
        Timestamp now = new Timestamp(calendar.getTime().getTime());
        nachrichtenLock.lock();
        try {
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
        } finally {                
            nachrichtenLock.unlock();
        }
    }
    
    public void sendeNachrichten() {
        
        Timestamp now = new Timestamp(calendar.getTime().getTime());
        
        nachrichtenLock.lock();
        try {
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
        } finally {                
            nachrichtenLock.unlock();
        }      
        setzeTimer();
    }
    
    private void setzeTimer() {
        
        Timestamp now = new Timestamp(calendar.getTime().getTime());
        
        timer.cancel();
        long dauer = 0;
        
        nachrichtenLock.lock();
        try {
            dauer = nachrichten.getFirst().getZeit().getTime() - now.getTime();

            //Server nach 28 min wecken (falls nötig)

            if(dauer > 1680000) {
                dauer = 1680000;

                    nachrichten.addFirst(new Nachricht(null,now));  
            }

        } finally {                
            nachrichtenLock.unlock();
        } 
        
        timer.schedule(new MessageHandler(), dauer);
    }
     
}
