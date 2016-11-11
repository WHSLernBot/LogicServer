package main;

import Entitys.Benutzer;

/**
 *
 * @author Seve
 */
public class CBBenutzer {

    private Benutzer benutzer;
    
    private Boolean lock;
    
    
    public CBBenutzer(Benutzer benutzer) {
        this.benutzer = benutzer;
        
        lock = false;
    }
    
    public void release() {
        lock = false;
    }
    
    public void gain() {
        lock = true;
    }
    
}
