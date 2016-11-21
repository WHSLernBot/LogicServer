package Entitys;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Betül
 */
public class XGAufgabePK implements Serializable{
    
    private Long aufgabenID; //nicht long??
    
    private LernStatus lernStatus;

    public XGAufgabePK() {
    }

    public XGAufgabePK(Long aufgabe, LernStatus lernStatus) {
        this.aufgabenID = aufgabe;
        this.lernStatus = lernStatus;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
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
        final XGAufgabePK other = (XGAufgabePK) obj;
        if (!Objects.equals(this.aufgabenID, other.aufgabenID)) {
            return false;
        }
        if (!Objects.equals(this.lernStatus, other.lernStatus)) {
            return false;
        }
        return true;
    }
    
}
