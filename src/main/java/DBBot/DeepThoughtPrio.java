package DBBot;

import DAO.EMH;
import Entitys.Modul;
import Entitys.Statistik;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Seve
 */
public class DeepThoughtPrio {
    
    private HashMap<Short,Statistik> stats;
    
    private final Timestamp heute;
    
    private final Modul modul;
    
    public DeepThoughtPrio(Modul m, Timestamp heute) {
        
        this.heute = heute;
        modul = m;
        
        m.getStatistiken().stream().forEach((s) -> {
            stats.put(s.getWoche(), s);
        });
    }
    
    /**
     * Berechnet die einzelnen Prioritaeten der Aufgaben und gib den gesamten
     * erfolgswert fuer den LernStatus zuruek
     * 
     * @param aufgaben alle Aufgaben und wie sie beantwortet wurden
     * @return Der Erfolgswert des Lernstatus. Wert zwischen 0 und 1000.
     */
    public int berechnePrioritaet(List<AufgabenItem> aufgaben) {
        
        long eineWoche = (1000 * 60 * 60 * 24 * 7);
        
        long heuteMs = heute.getTime();
        
        long lsPunkte = 0;
        
        for(AufgabenItem item : aufgaben) {
            
            int punkte = item.getAufgabe().getPunkte();
            
            long aufgabenPunkte = 0;
            
            long beantwortetPunkte = 0;
            
            for(BeantwortetItem beItem : item.gibBeantwortet()) {
                
                long woche = (heuteMs - beItem.getDatum().getTime()) / eineWoche;
                
                int anteilPunkte = gibStat((short) woche).getAnteil() * punkte;
                
                aufgabenPunkte += anteilPunkte;
                
                if(beItem.isRichtig()) {
                    beantwortetPunkte += (beItem.isHinweis()) ? anteilPunkte / 2 : anteilPunkte;
                }
                
            }
            
            int prioPunkte = 0;
            
            if(aufgabenPunkte != 0) {
                prioPunkte = (int)((beantwortetPunkte * 1000) / aufgabenPunkte);
            }
            item.setPunkte(prioPunkte);
            
            lsPunkte += prioPunkte;
        }
        
        return (int) lsPunkte / aufgaben.size();
        
    }
    
    private Statistik gibStat(short woche) {
        
        Statistik s = stats.get(woche);
        
        if(s == null) {
            try {
                EMH.beginTransaction();

                modul.addStatistik(woche,berechneAnteil(woche));

                EMH.getEntityManager().merge(modul);

                EMH.commit();
            } catch (Exception e) {
                EMH.rollback();
            }
        }
        
        return s;
        
    }
    
    private int berechneAnteil(short woche) {
        return 100 / (woche + 1);
    }
    
}
