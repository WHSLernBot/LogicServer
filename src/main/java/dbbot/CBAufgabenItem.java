package dbbot;

import entitys.Aufgabe;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse stellt eine abzufragende Aufgabe dar und wird nach seiner
 * gegebenden Punktzahl sortiert.
 * 
 * @author Seve
 */
public class CBAufgabenItem implements Comparable<CBAufgabenItem> {
        
    /**
     * Alle Beantworteten Aufgaben.
     */
    private final List<CBBeantwortetItem> beantwortet;
    
    /**
     * Die fuer diese Aufgabe gegebende Punktzahl.
     */
    private int punkte;
    
    /**
     * Die entsprechende Aufgabe in der Datenbank.
     */
    private final Aufgabe aufgabe;

    /**
     * Erstellt ein neues Item fuer die angebende Aufgabe.
     * 
     * @param aufgabe 
     */
    public CBAufgabenItem(Aufgabe aufgabe) {

        beantwortet = new ArrayList<>();
        punkte = 0;
        this.aufgabe = aufgabe;
    }
    
    /**
     * Fuegt eine neue Antwort (im sinne von beantwortet) der Aufgabe hinzu.
     * 
     * @param datum
     * @param richtig
     * @param hinweis 
     */
    public void addAntwort(Date datum,boolean richtig, boolean hinweis) {
        beantwortet.add(new CBBeantwortetItem(datum,richtig,hinweis));
    }

    public Aufgabe getAufgabe() {
        return aufgabe;
    }

    public int getPunkte() {
        return punkte;
    }

    public void setPunkte(int prio) {
        this.punkte = prio;
    }
    
    public List<CBBeantwortetItem> gibBeantwortet() {
        return this.beantwortet;
    }

    @Override
    public int compareTo(CBAufgabenItem o) {
        return (punkte < o.punkte) ? 1 : -1;
    }
    
}