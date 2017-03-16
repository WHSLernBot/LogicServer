package dbbot;

import java.sql.Date;

/**
 * Diese Klasse stellt eine Antwort auf eine Aufgabe dar. Und wird zur 
 * Berechnung der einzelnen Prioritaeten verwendet.
 * 
 * @author Seve
 */
public class BeantwortetItem {
    
    private final Date datum;
    
    private final boolean richtig;
    
    private final boolean hinweis;

    /**
     * Erstellt eine neue Antwort.
     * 
     * @param datum Das Datum der Antwort.
     * @param richtig Falls true, so war die Antwort richtig.
     * @param hinweis Falls true, so wurde bei der Antwort der Hinweis verwendet.
     */
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
