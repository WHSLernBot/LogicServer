package dbbot;

import entitys.BeAufgabe;

/**
 * Diese Klasse speichert wie viele Punkte bei der Beantwortung einer Frage
 * moeglich waren und wie viele davon tatsaechlich erreicht wurden.
 * @author Seve
 */
public class CBAntwortItem {
    
    private int punkteErreicht;
    
    private int punkteMoeglich;
    
    private int prozent;
    
    public CBAntwortItem() {
        this.prozent = -1;
        this.punkteErreicht = 0;
        this.punkteMoeglich = 0;
        
    }
    
    /**
     * Fuegt dem Item eine neue Antwort hinzu.
     * @param be 
     */
    public void addAntwort(BeAufgabe be) {
        
        if(be.istBeantwortet()) {
            int punkte = be.getAufgabe().getPunkte();
            
            punkteMoeglich += punkte;
            
            if(be.istRichtig()) {
                punkteErreicht += (be.getHinweis()) ? punkte / 2 : punkte;
            }
            
        }
        
    }
    
    public int getPunkteErreicht() {
        return punkteErreicht;
    }

    public int getPunkteMoeglich() {
        return punkteMoeglich;
    }
    
    /**
     * Rechnet den Anteil aus, zu wie viel Prozent die Aufgabe richtig 
     * bearbeitet wurde.
     * 
     * Achtung! Es wird nur beim ersten Aufruf ausgerechnet und dann das Ergebniss
     * aus effizienzgruenden gespeichert.
     * 
     * @return Die errechnete Prozentzahl.
     */
    public int gibProzent() {
        
        if(prozent == -1) {
            if(punkteMoeglich == 0) {
                prozent = 0;    
            } else {
                prozent = (100 * punkteErreicht) / punkteMoeglich;      
            }
            
        }
        return prozent;
    }
}
