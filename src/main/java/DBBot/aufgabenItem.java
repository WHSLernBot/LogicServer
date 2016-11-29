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
    
    private int prio;
    
    private final Aufgabe aufgabe;

    public aufgabenItem(Aufgabe aufgabe) {

        beantwortet = new ArrayList<>();
        prio = 0;
        this.aufgabe = aufgabe;
    }
    
    public void addAntwort(Date datum,boolean richtig, boolean hinweis) {
        beantwortet.add(new beantwortetItem(datum,richtig,hinweis));
    }

    public Aufgabe getAufgabe() {
        return aufgabe;
    }

    public int getPrio() {
        return prio;
    }

    public void setPrio(int prio) {
        this.prio = prio;
    }

    @Override
    public int compareTo(aufgabenItem o) {
        return (prio > o.prio) ? 1 : -1;
    }
    
}