package dbbot;

import dao.EMH;
import entitys.BeAufgabe;
import entitys.Teilnahme;
import java.util.Date;
import java.util.Calendar;

/**
 * Diese Klasse stellt den Vergeleich zwischen Klausurnoten und Aufgaben
 * ergebnissen auf und berechnet durch den Vergeleich mit anderen Statistiken
 * in wie weit diese Ergebnisse veraendert werden muessen, sodass am Ende die
 * Statistiken und Themenanteile so stimmen, dass dem Benutzer ein moeglichst
 * genaues Klausurergebniss praesentiert werden kann.
 * 
 * Das hier implementierte System ist nur grundlegend und auf jeden Fall noch
 * verbesserungswuerdig!
 * @author Seve
 */
public class StatMatrix implements Comparable<StatMatrix> {
    
    /**
     * Anzahl der Wochen die fuer die BErechnung nur betrachtet werden soll.
     */
    private static final int ANZAHL_WOCHEN = 10;
    
    private final short note;
    
    private final long[] themen;
    
    /**
     * Mapped auf jedes Thema wie viele Punkte in der jeweiligen Woche erreicht 
     * werden konnten und erreicht wurden.
     */
    private final AntwortItem[][] antworten;
    
    /**
     * Das Prozentuarisches Verhaeltniss der Einzelnen Themenergebnisse zur Note.
     */
    private final double[] verhaeltniss;
    
    /**
     * Unteschied der Verhaeltnisse des Naechsten zu dieser Matrix im Bezug auf
     * die Themen.
     */
    private final double[] unterschied;
    
    /**
     * Unteschied der Verhaeltnisse des Naechsten zu dieser Matrix im Bezug auf
     * die Wochen.
     */
    private final double[] unterschiedWoche;
    
    /**
     * Prozent der Thmen des Moduls mal den einzelen Themenanteile fuer die
     * betrachteten Wochen.
     */
    private final int[] prozentWoche;
    
    /**
     * Daten wann die Einzelnen Wochen beginnen.
     */
    private final Date[] wochen;
    
    /**
     * Thema mit dem hoechsten Unterschied zum Naechsten.
     */
    private int highT;
    
    /**
     * Thema mit dem niedrigsten Unterschied zum Naechsten.
     */
    private int lowT;
    
    /**
     * Woche mit dem hoeschten Unterschied zum Naechsten.
     */
    private short highW;
    
    /**
     * Woche mit dem niedrigesten Unterschied zum Naechsten.
     */
    private short lowW;
    
    /**
     * Die auf diese Matrix folgenden Matrix.
     */
    private StatMatrix next;
    
    /**
     * Die zugehoerige Klausurteilnahme.
     */
    private final Teilnahme teilnahme;

    /**
     * Erzeugt eine neue Matrix fuer eine Klausurteilnahme.
     * 
     * @param t Die Klausurteilnahme
     * @param themen IDs der einzelnen Themen des Moduls der Klausur.
     */
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
    
    /**
     * Setzt die Statistik, also die naechst bessere Note die auf diese Matrix folgt.
     * Somit wird zum vergeleich nur diese Matrix verwendet.
     * @param sm Die naechste Matrix.
     */
    public void setNext(StatMatrix sm) {
        this.next = sm;
    }
    
    /**
     * Fuegt der Matrix eine Antwort des Benutzers hinzu um sie damit zu fuellen.
     * @param be 
     */
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
    
    /**
     * Berechnet mit den angegebenen Prozenten der Themen die aus den Ergebnissen
     * resultierenden neuen Prozentzahlen. So vergeleicht diese Funktion die
     * Prozentzhal der eigenen Matix mit der naechsten. Falls also die eigene
     * Prozentzahl hoeher ist als die der nachsten (die naechste Matrix ist ja
     * immer eine bessere Note), so wird der Prozentsatz des Themas, dass den 
     * hoehsten unterschied hat um eins erhoeht und die des niedigends um eins
     * verringert und false ausgegeben. 
     * Ist die prozentzahl jedoch richtig, so wird diese Funktion in der 
     * naechsten Matrix aufgerufen.
     * 
     * @param prozent Prozente der Themen, bezogen auf die IDs die im
     * Konstruktor hinterlegt wurden.
     * @return Gibt true aus, falls eine veraenderung getaetigt wurde, somit die
     * Berechnung von vorne beginnt.
     */
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
    
    /**
     * Gibt die Prozentzahl, resultierend aus den Einzelnen Prozentzahlen der
     * Themen nur fuer die letzt woche aus um eine bessere Vergeleichbarkeit
     * der Themen zu erziehlen.
     * 
     * @param prozent
     * @return Wert zwischen 0 und 1000
     */
    private int gibProzentletzteWoche(int[] prozent) {
        
        int proz = 0;
        
        for(int i = 0; i < verhaeltniss.length; i++) {
            proz += verhaeltniss[i] * prozent[i];
        }
        
        return proz;
        
    }
    
    /**
     * Berechnet das Verhaeltniss dieser Matrix im vergleich zur naechsten neu
     * aus.
     */
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
    
    /**
     * Funktionsweise aehnlich wie bei berechneThemen nur im bezug auf die
     * Wochen.
     * 
     * @param stats
     * @return 
     */
    public boolean berechneWochen(StatMap stats) {
        
        if(this.gibProzentGesamt(stats) > next.gibProzentGesamt(stats) && this.note > next.note) {
            
            stats.gibStat(highW).setPlusAnteil(1);
            stats.gibStat(lowW).setPlusAnteil(-1);
            
            
            //Die schleife soll von vorne starten.
            return true;
        }
        
        return (next == null) ? false : next.berechneWochen(stats);
        
    }
    
    /**
     * Diese Methode setzt die prozente der Themen fuer die Berechnung der 
     * Wochen ein. Und sollte aufgerufen werden, nachdem die Themenberechnung
     * beendet wurde.
     * 
     * @param prozent 
     */
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
     
    /**
     * Gibt die Prozentzhals aus, die 
     * @param stats
     * @return 
     */
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
