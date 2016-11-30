package DBBot;

import Entitys.Aufgabe;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Seve
 */
public class aufgabenItem implements Comparable<aufgabenItem> {
        
    private final List<beantwortetItem> beantwortet;
    
    private int punkte;
    
    private final Aufgabe aufgabe;

    public aufgabenItem(Aufgabe aufgabe) {

        beantwortet = new ArrayList<>();
        punkte = 0;
        this.aufgabe = aufgabe;
    }
    
    public void addAntwort(Date datum,boolean richtig, boolean hinweis) {
        beantwortet.add(new beantwortetItem(datum,richtig,hinweis));
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
    
    public List<beantwortetItem> gibBeantwortet() {
        return this.beantwortet;
    }

    @Override
    public int compareTo(aufgabenItem o) {
        return (punkte < o.punkte) ? 1 : -1;
    }
    
}