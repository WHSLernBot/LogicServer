package DBBot;

import Entitys.Benutzer;
import Entitys.LernStatus;
import Entitys.Modul;
import java.sql.Timestamp;
import java.util.HashMap;

/**
 *
 * @author Seve
 */
public class BenutzerBot implements Runnable {
    
    private final Benutzer benutzer;
    
    private final Timestamp heute;
    
    private final HashMap<Modul,lsItem> module;
    
    public BenutzerBot(Benutzer benutzer, Timestamp heute) {
        this.benutzer = benutzer;
        this.heute = heute;
        
        module = new HashMap<>();
    }
    
    
    @Override
    public void run() {
        
        for(LernStatus ls : benutzer.getLernStadi()) {
            
            fuegeLSEin(ls);
        }
        PersonaleNachricht pn = new PersonaleNachricht();
        
        for(lsItem item : module.values()) {
            item.erstelleNachricht(pn, heute);
        }
    }
    
    private void fuegeLSEin(LernStatus ls) {
        
        Modul m = ls.getThema().getModul();

        lsItem item = module.get(m);
        
        if(item == null) {
//            Klausur k = DAO.gibKlausur(benutzer.getPlattform().getPfID(),benutzer.getPlattform().getPfNr() , ""); // Mal drueber gehen
            
//            item = new lsItem(k);
//            
//            module.put(m, item);
        }
        
//        item.addLs(ls);
            
    }
    
}
