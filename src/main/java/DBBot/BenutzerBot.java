package DBBot;

import Entitys.Benutzer;
import Entitys.LernStatus;
import java.sql.Timestamp;

/**
 *
 * @author Seve
 */
public class BenutzerBot implements Runnable {
    
    private final Benutzer benutzer;
    
    public BenutzerBot(Benutzer benutzer, Timestamp heute) {
        this.benutzer = benutzer;
    }
    
    
    @Override
    public void run() {
        
        for(LernStatus ls : benutzer.getLernStadi()) {
            berechne(ls);
        }
    }
    
    private void berechne(LernStatus ls) {
        
        if(ls.isVeraendert()) {
            
            //ne garnicht .. nur gucken ob angeschrieben werden muss
            
        }
    }
    
}
