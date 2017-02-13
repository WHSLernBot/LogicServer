package DBBot;

import Entitys.BeAufgabe;

/**
 *
 * @author Seve
 */
public class AntwortItem {
    
    private int punkteErreicht;
    
    private int punkteMoeglich;
    
    private int prozent;
    
    public AntwortItem() {
        this.prozent = -1;
        this.punkteErreicht = 0;
        this.punkteMoeglich = 0;
        
    }
    
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
