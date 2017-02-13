package DBBot;

import Entitys.Klausur;
import Entitys.LernStatus;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
     * 1. Klausur morgen ok
     * 2. Anmeldebeginn fuer Klausur ok
     * 3. Aktivitaet ok
     * 4. Schlechte Prognose
     * 5. Mittlere Prognose
     * 6. Gute Prognose
     * 7. Teilnahme eintragen
     * 8. Note eintragen
     * 
     * @param pn
     * @param heute 
     */
    public void erstelleNachricht(PersonaleNachricht pn, Timestamp heute) {
         
        if(!stadi.get(0).isBeendet() && stadi.get(0).istAktiv()) {
           
            String krz = klausur.getModul().getKuerzel();
            
            Date he = new Date(heute.getTime());

            he = normiere(he,0);

            if(klausur != null) {
                
                Date kl = new Date(klausur.getDatum().getTime());
                kl = normiere(kl,1);
                Date an = new Date(klausur.getPruefungsperiode().getAnfang().getTime());
                an = normiere(an,0);
                
                if(he.getTime() == kl.getTime()) {
                    //Klausur heute
                    pn.addNachricht(krz, PersonaleNachricht.MORGEN_KLAUSUR);
                }
                
                if(an.getTime() == he.getTime()) {
                    pn.addNachricht(krz, PersonaleNachricht.ANMELDEBEGINN);
                }
            }


            boolean inaktiv = true;
            for(LernStatus ls : stadi) {

                Date ak = new Date(ls.getLetztesDatum().getTime());
                ak = normiere(ak,-7);
                
                if(ak.before(he)) {
                    inaktiv = false;
                }
            }
            
            if(inaktiv) {
                pn.addNachricht(krz, PersonaleNachricht.AKTIVITAET);
            }
        }
        
    }
    
    private Date normiere(Date d, int day) {
        Calendar c = Calendar.getInstance(); 
        c.setTime(d); 
        c.add(Calendar.DATE, day);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        
        return c.getTime();
    }
}
