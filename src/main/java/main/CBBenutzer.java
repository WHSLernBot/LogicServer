package main;

import Entitys.Benutzer;

/**
 *
 * @author Seve
 */
public class CBBenutzer {

    private final Benutzer benutzer;
    
    private int lock;
    
    
    public CBBenutzer(Benutzer benutzer) {
        this.benutzer = benutzer;
        
        lock = 0;
    }
    
    public void release() {
        lock--;
    }
    
    public void gain() {
        lock++;
    }
    
    public boolean wirdBenutzt() {
        
        return lock != 0;
    }

    public Benutzer getBenutzer() {
        return benutzer;
    }
    
    
    
}
