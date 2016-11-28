package DBBot;

import Entitys.Benutzer;
import Entitys.LernStatus;
import java.sql.Timestamp;
import main.CBBenutzer;

/**
 *
 * @author Seve
 */
public class BenutzerBot implements Runnable {

    private final CBBenutzer cbbenutzer;
    
    private final Benutzer benutzer;
    
    public BenutzerBot(CBBenutzer benutzer, Timestamp heute) {
        this.cbbenutzer = benutzer;
        this.cbbenutzer.gain();
        this.benutzer = cbbenutzer.getBenutzer();
    }
    
    public BenutzerBot(Benutzer benutzer, Timestamp heute) {
        this.benutzer = benutzer;
        this.cbbenutzer = null;
    }
    
    
    @Override
    public void run() {
        
        for(LernStatus ls : benutzer.getLernStadi()) {
            berechne(ls);
        }
        
        if(cbbenutzer != null) {
            cbbenutzer.release();
        }
    }
    
    private void berechne(LernStatus ls) {
        
        if(ls.isVeraendert()) {
            
        }
        
        
        
    }
    
}
