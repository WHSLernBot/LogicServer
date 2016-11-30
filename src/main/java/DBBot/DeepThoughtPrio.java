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
    
    private static final int ANTEIL = 10;
    
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
    
    public void berechnePrioritaet(List<aufgabenItem> aufgaben) {
        
        for(aufgabenItem item : aufgaben) {
            item.setPrio(100);
        }
        
    }
    
    private Statistik gibStat(short woche) {
        
        Statistik s = stats.get(woche);
        
        if(s == null) {
            try {
                EMH.beginTransaction();

                modul.addStatistik(woche, ANTEIL);

                EMH.getEntityManager().merge(modul);

                EMH.commit();
            } catch (Exception e) {
                EMH.rollback();
            }
        }
        
        return s;
        
    }
    
}
