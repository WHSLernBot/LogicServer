package dbbot;

import dao.DAO;
import dao.EMH;
import entitys.Benutzer;
import entitys.Klausur;
import entitys.LernStatus;
import entitys.Modul;
import java.sql.Timestamp;
import java.util.HashMap;
import main.CBBenutzer;
import main.ChatBotManager;

/**
 * Dieser Bot geht alle Benutzerinformationen durch und schreibt ihn dann 
 * gegebenfalls an.
 * 
 * @author Seve
 */
public class BenutzerBot implements Runnable {
    
    private final CBBenutzer benutzer;
    
    private final Timestamp heute;
    
    private final HashMap<Modul,LsItem> module;
    
    /**
     * Erstellt einen neuen BenutzerBot fuer den angegebenen Benutzer.
     * 
     * @param benutzer
     * @param heute Die Zeit, fuer die die Berechnung im verhaeltniss gestzt 
     * werden soll. (Eigentlich immer der aktuelle Zeitpunkt)
     */
    public BenutzerBot(Benutzer benutzer, Timestamp heute) {
        
        this.benutzer = ChatBotManager.getInstance().gibBenutzer(benutzer);
        this.heute = heute;
        
        module = new HashMap<>();
    }
    
    /**
     * Startet den Benutzerbot.
     */
    @Override
    public void run() {
        
        for(LernStatus ls : benutzer.getBenutzer().getLernStadi()) {
            
            fuegeLSEin(ls);
        }
        PersonaleNachricht pn = new PersonaleNachricht();
        
        for(LsItem item : module.values()) {
            item.erstelleNachricht(pn, heute);
        }
        
        pn.erstelleNachrichten(benutzer);
        
        benutzer.release();
        
        EMH.closeEntityManager();
    }
    
    /**
     * Diese Methode fuegt der HashMap einem Modul den passenden LernStatus 
     * im LsItem ein. 
     * 
     * @param ls 
     */
    private void fuegeLSEin(LernStatus ls) {
        
        Modul m = ls.getThema().getModul();

        LsItem item = module.get(m);
        
        if(item == null) {
            Klausur k = DAO.gibKlausur(benutzer,m.getKuerzel());
            
            item = new LsItem(k);
            
            module.put(m, item);
        }
        
        item.addLs(ls);
            
    }
    
}
