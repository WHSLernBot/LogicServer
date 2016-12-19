package Entitys;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;

/**
 * Die Primary Key Klasse von BeAufgabe. Fuer mehr Infos siehe BeAufgabe.
 * 
 * @author Seve
 */
@Embeddable
public class BeAufgabePK implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long aufgabe;
    
    private LernStatusPK lernStatus;
    
    private int kennung;

    public BeAufgabePK() {
    }

    public BeAufgabePK(Long aufgabe, LernStatusPK lernStatus, int kennung) {
        this.aufgabe = aufgabe;
        this.lernStatus = lernStatus;
        this.kennung = kennung;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.aufgabe);
        hash = 89 * hash + Objects.hashCode(this.lernStatus);
        hash = 89 * hash + this.kennung;
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
        if (this.kennung != other.kennung) {
            return false;
        }
        if (!Objects.equals(this.aufgabe, other.aufgabe)) {
            return false;
        }
        if (!Objects.equals(this.lernStatus, other.lernStatus)) {
            return false;
        }
        return true;
    }



   
}
