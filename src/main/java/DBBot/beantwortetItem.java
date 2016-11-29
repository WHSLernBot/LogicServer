package DBBot;

import java.sql.Date;

/**
 *
 * @author Seve
 */
public class beantwortetItem {
    
    private final Date datum;
    
    private final boolean richtig;
    
    private final boolean hinweis;

    public beantwortetItem(Date datum, boolean richtig, boolean hinweis) {
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
