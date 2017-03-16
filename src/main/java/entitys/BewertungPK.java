package entitys;

import java.io.Serializable;
import java.util.Objects;

/**
 * Primary Key Klasse von Bewertung.
 * 
 * @author Seve
 */
public class BewertungPK implements Serializable {
    
    private Long aufgabe;
    
    private LernStatusPK lernStatus;

    public BewertungPK(Long aufgabe, LernStatusPK lernStatus) {
        this.aufgabe = aufgabe;
        this.lernStatus = lernStatus;
    }

    public BewertungPK() {
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.aufgabe);
        hash = 67 * hash + Objects.hashCode(this.lernStatus);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BewertungPK other = (BewertungPK) obj;
        if (!Objects.equals(this.aufgabe, other.aufgabe)) {
            return false;
        }
        if (!Objects.equals(this.lernStatus, other.lernStatus)) {
            return false;
        }
        return true;
    }

    
    
    
    
}
