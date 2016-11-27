package DBBot;

import Entitys.Aufgabe;
import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author Seve
 */
public class lsItem implements Comparable<lsItem> {
        
    private Date zuletzt;

    private int richtig;

    private int falsch;
    
    private int hinweis;
    
    private int prio;
    
    private final Aufgabe aufgabe;

    public lsItem(Aufgabe aufgabe) {
        richtig = 0;
        falsch = 0;
        zuletzt = null;
        hinweis = 0;
        prio = 0;
        this.aufgabe = aufgabe;
    }
    
    public void preufeDatum(Date datum) {
        if(zuletzt == null || zuletzt.before(datum)) {
            zuletzt = datum;
        }
    }
    
    public void beantwortet(boolean richtig) {
        
        if(richtig){
            this.richtig++;
        } else {
            this.falsch++;
        }
        
    }
    
    public void hinweis(boolean hinweis) {
        if(hinweis) {
            this.hinweis++;
        }
    }

    public Aufgabe getAufgabe() {
        return aufgabe;
    }
    
    public int gibPrio() {
        return prio;
    }
    
    public void berechnePrio(Timestamp heute) {
        //Hier passiert die Magie
        
        prio = aufgabe.getPunkte();
        
        int insgesammt = richtig + falsch;
        int wieLange = heute.compareTo(zuletzt);
        
        if(insgesammt == 0) {
            prio += 100000; //Damit die Aufgabe auf jeden Fall zuerst gestellt wird.
        } else if(wieLange == 0) {
            prio = 0; //Damit die Aufgabe garnicht gestellt wird.
        } else {
            //Das kann man noch alles genauer machen!!!
            int wieGut = (richtig * 3) - (falsch * 3) - hinweis; 
            
            //so höher wieGut desto besser wurde beantwortet
            
            prio = (wieLange * prio) - wieGut;
            
            if(prio < 0) {
                prio = 0;
            }
            
        }
    }

    public int getHinweis() {
        return hinweis;
    }
    
    public Date getZuletzt() {
        return zuletzt;
    }

    public int getRichtig() {
        return richtig;
    }

    public int getFalsch() {
        return falsch;
    }

    @Override
    public int compareTo(lsItem o) {
        return (prio > o.prio) ? 1 : -1;
    }
    
}