package DBBot;

import Entitys.Klausur;
import Entitys.LernStatus;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 * @author Seve
 */
public class lsItem {
    
    private final Klausur klausur;
    
    private final ArrayList<LernStatus> stadi;

    public lsItem(Klausur klausur) {
        this.klausur = klausur;
        
        this.stadi = new ArrayList<>();
    }
    
    public void addLs(LernStatus ls) {
        stadi.add(ls);
    }
    
    /**
     * Erstellt Nachrichten die an den Benutzer geschrieben werden muessen.
     * 1. Anmeldebeginn fuer Klausur
     * 2. Klausur morgen
     * 3. 
     * 
     * @param pn
     * @param heute 
     */
    public void erstelleNachricht(PersonaleNachricht pn, Timestamp heute) {
        
        long tag = 1000 * 60 * 60 * 24;
        
        long kDate = klausur.getDatum().getTime();
        
        long heuteDate = (heute.getTime() - (heute.getTime() % tag));
        
        long tageVK = (kDate - heuteDate) / tag;
        
        if(heuteDate - klausur.getDatum().getTime() == tag) {
            //Klausur heute
            pn.addNachricht(klausur.getModul().getName(), PersonaleNachricht.MORGEN_KLAUSUR);
        } else if (true) {
            
        }
        
    }
    
}
