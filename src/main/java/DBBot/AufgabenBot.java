package DBBot;

import Entitys.Modul;
import main.CBBenutzer;

/**
 *
 * @author Seve
 */
public class AufgabenBot implements Runnable {

    private final CBBenutzer benutzer;
    
    private final Modul modul;
    
    public AufgabenBot(CBBenutzer benutzer, Modul modul) {
        this.benutzer = benutzer;
        this.modul = modul;
    }
    
    
    @Override
    public void run() {
        
    }
    
}
