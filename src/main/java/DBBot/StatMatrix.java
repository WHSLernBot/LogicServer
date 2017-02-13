package DBBot;

import DAO.EMH;
import Entitys.BeAufgabe;
import Entitys.Statistik;
import Entitys.Teilnahme;
import java.util.Date;
import java.util.Calendar;

/**
 *
 * @author Seve
 */
public class StatMatrix implements Comparable<StatMatrix> {
    
    private static final int ANZAHL_WOCHEN = 10;
    
    private final short note;
    
    private final long[] themen;
    
    private final AntwortItem[][] antworten;
    
    private final double[] verhaeltniss;
    private final double[] unterschied;
    private final double[] unterschiedWoche;
    private final int[] prozentWoche;
    
    private final Date[] wochen;
    
    private int highT;
    
    private int lowT;
    
    private short highW;
    private short lowW;
    
    private StatMatrix next;
    
    private final Teilnahme teilnahme;

    public StatMatrix(Teilnahme t, long[] themen) {
        this.note = t.getNote();
        this.themen = themen;
        this.teilnahme = t;
        
        this.antworten = new AntwortItem[ANZAHL_WOCHEN][themen.length];
        this.wochen = new Date[ANZAHL_WOCHEN + 1];
        this.prozentWoche = new int[ANZAHL_WOCHEN];
        this.unterschiedWoche = new double[ANZAHL_WOCHEN];
        this.highW = -1;
        this.lowW = -1;
        
        this.verhaeltniss = new double[themen.length];
        this.highT = -1;
        this.lowT = -1;
        this.unterschied = new double[themen.length];
        
        this.verhaeltniss[0] = -1;
        
        next = null;
        
        Calendar c = Calendar.getInstance();
        
        for(int i = 0; i < wochen.length; i++) {
            c.setTime(t.getKlausur().getDatum());
            c.add(Calendar.DATE, i * (-7));
            
            wochen[wochen.length - i - 1] = c.getTime();
        }
    }

    public short getNote() {
        return note;
    }
    
    public void setNext(StatMatrix sm) {
        this.next = sm;
    }
    
    public void addAntwort(BeAufgabe be) {
        
        java.sql.Date datum = be.getDatum();
        
        //Datum vor der zu betrachtenden Periode
        if(!datum.before(wochen[0])) {
            
            for(int i = 1; i < wochen.length; i++) {
                if(datum.before(wochen[i])) {
                    
                    long tID = be.getLernStatus().getThema().getId();
                    
                    int j = 0;
                    while(j < themen.length && tID != themen[j]) {
                        j++;
                    }
                    
                    if(j < themen.length) {
                        antworten[i - 1][j].addAntwort(be);
                    }
                    
                }
            }
        }          
        
    }
    
    public boolean berechneThemen(int[] prozent) {
        
        //Wenn wir eine hoehere Prozentzahl haben, obwohl wir die schlechtere Note haben
        if(this.gibProzentletzteWoche(prozent) > next.gibProzentletzteWoche(prozent) && this.note > next.note) {
            
            prozent[highT]++;
            prozent[lowT]--;
            
            
            //Die schleife soll von vorne starten.
            return true;
        }
        
        return (next == null) ? false : next.berechneThemen(prozent);
    }
    
    private int gibProzentletzteWoche(int[] prozent) {
        
        int proz = 0;
        
        for(int i = 0; i < verhaeltniss.length; i++) {
            proz += verhaeltniss[i] * prozent[i];
        }
        
        return proz;
        
    }
    
    public void berechneVerhaeltnisse() {
        
        for(int i = 0; i < this.verhaeltniss.length; i++) {
            this.verhaeltniss[i] = antworten[0][i].gibProzent() / note;
        }
        
        if(next != null) {
            this.next.berechneVerhaeltnisse();
            
            for(int i = 0; i < unterschied.length; i++) {
                unterschied[i] = next.verhaeltniss[i] - this.verhaeltniss[i];
            }
            
            //Herausfinden, was mein hoechster und was mein niedrigster Wert ist.
            if(unterschied[0] > unterschied[1]) {
                highT = 0;
                lowT = 1;
            } else {
                highT = 1;
                lowT = 0;
            }
            
            for(int i = 2; i < unterschied.length; i++) {
                if(unterschied[i] > unterschied[highT]) {
                    highT = i;
                } else if(unterschied[i] < unterschied[lowT]) {
                    lowT = i;
                }
            }
        }
        
    }
    
    public boolean berechneWochen(StatMap stats) {
        
        if(this.gibProzentGesamt(stats) > next.gibProzentGesamt(stats) && this.note > next.note) {
            
            stats.gibStat(highW).setPlusAnteil(1);
            stats.gibStat(lowW).setPlusAnteil(-1);
            
            
            //Die schleife soll von vorne starten.
            return true;
        }
        
        return (next == null) ? false : next.berechneWochen(stats);
        
    }
    
    public void setzeAnteilThemen(int[] prozent) {
        
        for(int i = 0; i < ANZAHL_WOCHEN; i++) {
            
            int ant = 0;
            
            for(int j = 0; i < themen.length; j++) {
                
                ant += antworten[i][j].gibProzent() * prozent[i];
            
            }
            
            this.prozentWoche[i] = ant;
            
        }
        
        if(next != null) {
            next.setzeAnteilThemen(prozent);
            
            for(int i = 0; i < ANZAHL_WOCHEN; i++) {
                unterschiedWoche[i] = (prozentWoche[i] / note) - (next.unterschiedWoche[i] / next.note);
            }
            
            if(unterschiedWoche[0] > unterschiedWoche[1]) {
                highW = 0;
                lowW = 1;
            } else {
                highW = 1;
                lowW = 0;
            }
            
            for(short i = 2; i < unterschiedWoche.length; i++) {
                if(unterschiedWoche[i] > unterschiedWoche[highW]) {
                    highW = i;
                } else if(unterschiedWoche[i] < unterschiedWoche[lowW]) {
                    lowW = i;
                }
            }
        }
        
    }
     
    public int gibProzentGesamt(StatMap stats) {
        
        int res = 0;
        
        for(short i = 0; i < ANZAHL_WOCHEN; i++) {
            
            res += stats.gibStat(i).getAnteil() * this.prozentWoche[i];
            
        }
        
        teilnahme.setProzent(res);
        
        return res;
    }

    /**
     * Sortiert die Matrix absteigend nach Note. Also 5,0 zuerst und dann runter
     * bis 1,0.
     * @param s
     * @return 
     */
    @Override
    public int compareTo(StatMatrix s) {

        return s.note - note;
        
    }
    
    /**
     * Persistiert die neuen Prozentdaten in der Datenbank.
     */
    public void speichereProzent() {
        
        try {
            EMH.beginTransaction();
            
            EMH.getEntityManager().merge(teilnahme);

            EMH.commit();
        } catch (Exception e) {
            EMH.rollback();
        }
        
        if(next != null) {
            next.speichereProzent();
        }
        
    }
    
}
