package Entitys;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;

/**
 * Die Primary Key Klasse von BeAufgabe. Für mehr Infos siehe BeAufgabe.
 * 
 * @author Seve
 */
@Embeddable
public class BeAufgabePK implements Serializable {
    
    private Long aufgabe;
    
    private LernStatusPK lernStatus; //prï¿½fen

    public BeAufgabePK() {
    }

    public BeAufgabePK(Long aufgabenID, LernStatusPK lernStatus) {
        this.aufgabe = aufgabenID;
        this.lernStatus = lernStatus;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.aufgabe);
        hash = 97 * hash + Objects.hashCode(this.lernStatus);
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
