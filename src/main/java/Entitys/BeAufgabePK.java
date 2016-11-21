package Entitys;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Seve
 */
public class BeAufgabePK implements Serializable {
    
    private Long aufgabenID;
    
    private LernStatusPK lernStatus; //prüfen

    public BeAufgabePK() {
    }

    public BeAufgabePK(Long aufgabenID, LernStatus lernStatus) {
        this.aufgabenID = aufgabenID;
        this.lernStatus = lernStatus;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.aufgabenID);
        hash = 41 * hash + Objects.hashCode(this.lernStatus);
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
        final BeAufgabePK other = (BeAufgabePK) obj;
        if (!Objects.equals(this.aufgabenID, other.aufgabenID)) {
            return false;
        }
        if (!Objects.equals(this.lernStatus, other.lernStatus)) {
            return false;
        }
        return true;
    }

    
    
}
