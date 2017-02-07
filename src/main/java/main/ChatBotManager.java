package main;

import DAO.DAO;
import DAO.EMH;
import DBBot.BotPool;
import DBBot.BotTimer;
import Entitys.Benutzer;
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
 * Diese Klasse Managt alle komponenten des LogikSerers, die zeitweise gespeichert 
 * werden, sowie alle Bots.
 * 
 * @author Seve
 */
public class ChatBotManager {
    
    private static final int LOESCHPHASE_MINTUEN = 15;
    
    private static ChatBotManager manager;
    
    private final BotPool botPool;
        
    private final HashMap<CBPlattform,CBBenutzer> benutzer;
    
    private LinkedList<Nachricht> nachrichten;
    
    private final Calendar calendar;
    
    private final Timer nachrichtenTimer;
    
    private final Timer loeschTimer;
    
    private final Timer berechnenTimer;
    
    private final Lock nachrichtenLock;
    
    private final Lock benutzerLock;

    /**
     * Erstellt einen neunen ChatBotManager.
     */
    private ChatBotManager() {
        
        this.botPool = new BotPool();
        benutzer = new HashMap<>();
        this.calendar = Calendar.getInstance();
        
        nachrichtenLock = new ReentrantLock();
        benutzerLock = new ReentrantLock();
        
        loeschTimer =  new Timer();
        nachrichtenTimer = new Timer();
        berechnenTimer = new Timer();
        
        long dauer = LOESCHPHASE_MINTUEN * 60 * 1000;
        
        loeschTimer.schedule(new CBBot(), dauer,dauer);
        
        long day = 24 * 60 * 60 * 1000;
        
        Calendar start = Calendar.getInstance();
        start.set(Calendar.HOUR, 3);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.DAY_OF_YEAR, start.get(Calendar.DAY_OF_YEAR + 1));
                
        berechnenTimer.schedule(new BotTimer(), start.getTime(), day);
    }
    
    /**
     * Gibt das Singelton ChatBotManager zurueck.
     * @return 
     */
    public static ChatBotManager getInstance() {
        if(manager == null) {
            manager = new ChatBotManager();
        }
        
        return manager;
    }
    
    public Timestamp jetzt() {
        return new Timestamp(calendar.getTime().getTime());
    }
    
    /**
     * Gibt den BotPool zurueck.
     * @return 
     */
    public BotPool gibBotPool() {
        return botPool;
    }
    
    /**
     * Gibt den Benutzer zur zugehoerigen Plattform zurueck.
     * 
     * @param pt
     * @param name
     * @param session
     * @return 
     */
    public CBBenutzer gibBenutzer(CBPlattform pt, String name, String session) {
        
        CBBenutzer be = null;
        benutzerLock.lock();
        try {
           be = benutzer.get(pt); 
           if(be == null) {
               be = DAO.sucheBenutzer(pt);
               
               if(be == null) {
                   Benutzer b = DAO.neuerBenutzer(pt, name, session);
                   
                   be = new CBBenutzer(b);
               }
               
               benutzer.put(pt, be);
           }
           
           be.gain();
        } catch (Exception e) {
            //Error 
        } finally {
            benutzerLock.unlock();
        }
        
        
        return be;
        
    }
    
    /**
     * Diese Methode loescht alle CBBenutzer aus der Liste, die lange nicht mehr
     * aktiv waren. Die Zeit wird in CBBenutzer bestimmt.
     */
    public void loesche() {
        
        benutzerLock.lock();
        
        try {
            Timestamp jetzt = jetzt();

            for(CBPlattform p : benutzer.keySet()) {

                CBBenutzer b = benutzer.get(p);

                boolean remove = false;

                synchronized(b) {
                    if(b.darfLoeschen(jetzt)) {
                        remove = true;
                        try {
                            EMH.beginTransaction();
                            
                            b.getBenutzer().setLetzteAntwort(DAO.gibDatum());
                            
                            EMH.persist(b.getBenutzer());
                            
                            EMH.commit();
                            
                        } catch (Exception e) {
                            remove = false;
                        }
                    }
                }

                if(remove) {
                    benutzer.remove(p);
                }

            }
        } catch (Exception e) {
            
        } finally {
            benutzerLock.unlock();
        }

    }
    
    /**
     * Fuegt eine Nachricht in die zu sendende Nachrichten hinzu. Falls der 
     * Timestamp null ist wird die Nachricht sofort verschickt.
     * 
     * @param n Die zu verschieckende Nachricht.
     */
    public void addNachricht(Nachricht n) {
        
        Timestamp now = jetzt();
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
            
        } catch (Exception e) {
            //Error 
        } finally {                
            nachrichtenLock.unlock();
        }
    }
    
    /**
     * Startet den Prozess des Nachrichten sendens.
     */
    public void sendeNachrichten() {
        
        Timestamp now = jetzt();
        
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
        } catch (Exception e) {
        //Error 
        } finally {                
            nachrichtenLock.unlock();
        }      
        setzeTimer();
    }
    
    private void setzeTimer() {
        
        Timestamp now = jetzt();
        
        nachrichtenTimer.cancel();
        long dauer = 0;
        
        nachrichtenLock.lock();
        try {
            dauer = nachrichten.getFirst().getZeit().getTime() - now.getTime();

            //Server nach 28 min wecken (falls noetig)

            if(dauer > 1680000) {
                dauer = 1680000;

                Nachricht nachricht = new Nachricht(null);
                
                nachricht.setZeit(now);// Da muss was
                nachrichten.addFirst(new Nachricht(null));  
            }

        } catch (Exception e) {
        //Error 
        } finally {                
            nachrichtenLock.unlock();
        } 
        
        nachrichtenTimer.schedule(new MessageHandler(), dauer);
    }
     
}
