package main;

import DAO.EMH;
import Entitys.Benutzer;
import java.sql.Timestamp;

/**
 *
 * @author Seve
 */
public class CBBenutzer {

    private static final int RESTSPEICHERZEIT_MINUTEN = 30;
    
//    private final Benutzer benutzer;
    private final Long benutzerID;
    
    private Timestamp zuletzt;
    
    private int lock;
    
    public CBBenutzer(Benutzer benutzer) {
//        this.benutzer = benutzer;
        this.benutzerID = benutzer.getId();
        zuletzt = ChatBotManager.getInstance().jetzt();
        
        lock = 0;
    }
    
    public synchronized void release() {
        zuletzt = ChatBotManager.getInstance().jetzt();
        lock--;
    }
    
    public synchronized void gain() {
        lock++;
    }
    
    /**
     * Gibt aus ob ein CBBenutzter aus dem Arbeitsspeicher geloescht werden darf.
     * 
     * @param jetzt Die aktuelle Zeit.
     * @return Falls true, darf der Benutzer geloescht werden.
     */
    public boolean darfLoeschen(Timestamp jetzt) {
        
        long speicherzeit = RESTSPEICHERZEIT_MINUTEN * 60 * 1000;
        
        Timestamp loeschpunkt = new Timestamp(0);
        loeschpunkt.setTime(zuletzt.getTime() + speicherzeit);
        
        return(lock == 0 && loeschpunkt.before(jetzt));
        
    }
    
    public boolean wirdBenutzt() {
        
        return lock != 0;
    }

    public Benutzer getBenutzer() {
        return EMH.getEntityManager().find(Benutzer.class, this.benutzerID);
//        return benutzer;
    }
    
}