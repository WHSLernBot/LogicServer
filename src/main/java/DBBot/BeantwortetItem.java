package DBBot;

import java.sql.Date;

/**
 * Diese Klasse stellt eine Antwort auf eine Aufgabe dar.
 * 
 * @author Seve
 */
public class BeantwortetItem {
    
    private final Date datum;
    
    private final boolean richtig;
    
    private final boolean hinweis;

    public BeantwortetItem(Date datum, boolean richtig, boolean hinweis) {
        this.datum = datum;
        this.richtig = richtig;
        this.hinweis = hinweis;
    }

    public Date getDatum() {
        return datum;
    }

    public boolean isRichtig() {
        return richtig;
    }

    public boolean isHinweis() {
        return hinweis;
    } 
    
}
