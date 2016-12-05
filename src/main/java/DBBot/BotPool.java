package DBBot;

import DAO.DAO;
import Entitys.Modul;
import Entitys.Uni;
import Entitys.Benutzer;
import Entitys.Uni;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import main.CBBenutzer;
import main.ChatBotManager;

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
        aufgabenBots.submit(new AufgabenBot(benutzer));
        
    }
    
    private void berechneAlles() {
        
        Timestamp heute = ChatBotManager.getInstance().jetzt();
        
        Collection<Uni> unis = DAO.gibUnis();
        
        for(Uni u : unis) {
            
            
            
            for(Benutzer b : u.getBenutzer()) {
                benutzerBots.submit(new BenutzerBot(b,heute));
            }
            
            for(Modul m : u.getModul()) {
                aufgabenBots.submit(new AufgabenBot(m));
            }
            
//            u.getPruefungsperiode()
            
        }
        
    }
}
