package entitys;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Diese Klasse stellt die Bewertung einer Aufgabe durch einen Benutzer wieder,
 * da jeder Benutzer eine Aufgabe nur einmal bewerten darf.
 * 
 * @author Seve
 */
@Entity
@IdClass(BewertungPK.class)
@Table(name="Bewertung")
public class Bewertung implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * Die Bewertete Aufgabe.
     */
    @ManyToOne
    private Aufgabe aufgabe;
    
    /**
     * Der zugehoerige Lernstatus.
     */
    @Id
    @OneToOne
    private LernStatus lernStatus;

    /**
     * true, falls die Aufgabe positiv bewertet wurde, ansonsten negativ false.
     */
    private boolean positiv;

    public Bewertung() {}
    
    public Bewertung(Aufgabe aufgabe, LernStatus lernStatus, boolean positiv) {
        this.aufgabe = aufgabe;
        this.lernStatus = lernStatus;
        this.positiv = positiv;
    }

    public Aufgabe getAufgabe() {
        return aufgabe;
    }

    public LernStatus getLernStatus() {
        return lernStatus;
    }

    public boolean isPositiv() {
        return positiv;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.aufgabe);
        hash = 59 * hash + Objects.hashCode(this.lernStatus);
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
        final Bewertung other = (Bewertung) obj;
        if (this.positiv != other.positiv) {
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
    
    @Override
    public String toString() {
        return aufgabe + " bewertet von " + lernStatus;
    }
    
}
