package DBBot;

import DAO.EMH;
import Entitys.Modul;
import Entitys.Statistik;
import java.util.HashMap;

/**
 * Diese Klasse Wrappt die Beziehungen zwischen Statistik und Woche und kann neue
 * Statistiken erzeugen.
 * @author Seve
 */
public class StatMap {
    
    private final HashMap<Short,Statistik> stats;
    
    private final Modul modul;
    
    public StatMap(Modul modul) {
        stats = new HashMap<>();
        
        for (Statistik s : modul.getStatistiken()) {
            stats.put(s.getWoche(), s);
        }
        
        this.modul = modul;
    }
            
    
    /**
     * Gibt die Statistik aus der hashMap zurueck. Exestiert keine, so wird eine
     * neue erzeugt.
     * 
     * @param woche
     * @return 
     */
    public Statistik gibStat(short woche) {
        
        Statistik s = stats.get(woche);
        
        if(s == null) {
            s = new Statistik(modul,woche,berechneAnteil(woche));
            
            
            
            try {
                EMH.beginTransaction();

                for(Statistik sk : stats.values()) {
                    EMH.merge(sk);
                }
                EMH.persist(s);
                
                EMH.commit();
                
                stats.put(woche, s);
            } catch (Exception e) {
                EMH.rollback();
            }
        }
        
        return s;
        
    }
    
    /**
     * Berechnet den Anteil einer Statistik fuer eine neue Woche und veraendert
     * die anderen Statistiken so, dass man am ende wieder auf 100% kommt.
     * 
     * @param woche
     * @return 
     */
    private int berechneAnteil(short woche) {
        
        int summe = 0;
        
        for(Statistik sk : stats.values()) {
            summe += sk.getAnteil();
        }
        
        if(summe == 0) {
            return 100;
        } else {
            
            int neu = 100 /(woche + 1);
            summe += neu;
            int gesamt = 0;
            
            for(Statistik sk : stats.values()) {
                int ant = (sk.getAnteil() * 100) / summe;
                
                gesamt += ant;
                
                sk.setAnteil(ant);
            }
            
            return (neu * 100) / summe + (100 - gesamt);
            
        }
    }
    
    /**
     * Merged die veraenderten Anteile in die Datenbank.
     */
    public void merge() {
        try {
            EMH.beginTransaction();

            for(Statistik sk : stats.values()) {
                EMH.merge(sk);
            }

            EMH.commit();
        } catch (Exception e) {
            EMH.rollback();
        }
    }
    
}
