package Entitys;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Seve
 */
public class BeAufgabePK implements Serializable {
    
    private Aufgabe aufgabe;
    
    private LernStatus lernStatus;

    public BeAufgabePK() {
    }

    public BeAufgabePK(Aufgabe aufgabe, LernStatus lernStatus) {
        this.aufgabe = aufgabe;
        this.lernStatus = lernStatus;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.aufgabe);
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
        if (!Objects.equals(this.aufgabe, other.aufgabe)) {
            return false;
        }
        if (!Objects.equals(this.lernStatus, other.lernStatus)) {
            return false;
        }
        return true;
    }

   
     
    
}
