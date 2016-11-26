package DBBot;

import DAO.DAO;
import Entitys.LernStatus;
import Entitys.Modul;
import Entitys.Uni;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import main.CBBenutzer;

/**
 *
 * @author Seve
 */
public class BotPool {
    
    private static final int AUFGABEN_POOL_SIZE = 10;
    private static final int BENUTZER_POOL_SIZE = 10;
    private static final int KLAUSUR_POOL_SIZE = 5;
    
    private final ExecutorService aufgabenBots;
    
    private final ExecutorService benutzerBots;
    
    private final ExecutorService klausurBots;
    
    public BotPool() {
        
        aufgabenBots = Executors.newFixedThreadPool(AUFGABEN_POOL_SIZE);
        benutzerBots = Executors.newFixedThreadPool(BENUTZER_POOL_SIZE);
        klausurBots = Executors.newFixedThreadPool(KLAUSUR_POOL_SIZE);

    }
    
    public void berechneNeu(CBBenutzer benutzer) {
        
        benutzerBots.submit(new BenutzerBot(benutzer));
        
    }
    
    public void neueAufgaben(CBBenutzer benutzer, Modul modul) {
        
//        LernStatus ls = DAO.gibLernstatus(benutzer, modul);
        
//        aufgabenBots.submit(new AufgabenBot(ls));
        
    }
    
    private void berechneAlles() {
        
        Collection<Uni> unis = DAO.gibUnis();
        
        for(Uni u : unis) {
            
            
            
        }
        
    }
}
