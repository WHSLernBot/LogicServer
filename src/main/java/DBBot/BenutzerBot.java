package DBBot;

import DAO.DAO;
import Entitys.Benutzer;
import Entitys.Klausur;
import Entitys.LernStatus;
import Entitys.Modul;
import java.sql.Timestamp;
import java.util.HashMap;
import main.CBBenutzer;
import main.ChatBotManager;

/**
 * Dieser Bot geht alle Benutzerinformationen durch und schriebt ihn dann 
 * gegebenfalls an.
 * 
 * @author Seve
 */
public class BenutzerBot implements Runnable {
    
    private final CBBenutzer benutzer;
    
    private final Timestamp heute;
    
    private final HashMap<Modul,lsItem> module;
    
    public BenutzerBot(Benutzer benutzer, Timestamp heute) {
        
        this.benutzer = ChatBotManager.getInstance().gibBenutzer(benutzer);
        this.heute = heute;
        
        module = new HashMap<>();
    }
    
    
    @Override
    public void run() {
        
        for(LernStatus ls : benutzer.getBenutzer().getLernStadi()) {
            
            fuegeLSEin(ls);
        }
        PersonaleNachricht pn = new PersonaleNachricht();
        
        for(lsItem item : module.values()) {
            item.erstelleNachricht(pn, heute);
        }
        
        pn.erstelleNachrichten(benutzer);
        
        benutzer.release();
    }
    
    /**
     * Diese Methode fuegt der HashMap einem Modul den passenden LernStatus 
     * im LsItem ein. 
     * 
     * @param ls 
     */
    private void fuegeLSEin(LernStatus ls) {
        
        Modul m = ls.getThema().getModul();

        lsItem item = module.get(m);
        
        if(item == null) {
            Klausur k = DAO.gibKlausur(benutzer,m.getKuerzel());
            
            item = new lsItem(k);
            
            module.put(m, item);
        }
        
        item.addLs(ls);
            
    }
    
}
