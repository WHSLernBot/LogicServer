package Entitys;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Diese Klasse stellt eine eXtra Geloeste Aufgabe dar, die auserhalb der
 * ausgedachten Reihenfolge bearbeitet wurde.
 * @author Betuel
 */
@Entity
@IdClass(XGAufgabePK.class)
@Table(name="XGAufgabe")
public class XGAufgabe implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Aufgabe die bearbeitet wurde.
     */
    @ManyToOne
    private Aufgabe aufgabe;
    
    /**
     * Zugehoeriger Lernstatus der diese Aufgabe bearbeitet hat.
     */
    @Id
    @ManyToOne
    private LernStatus lernStatus;
    
    @Id
    private int kennung;
    
    public XGAufgabe(){}

    public XGAufgabe(Aufgabe aufgabe, LernStatus lernStatus, int kennung) {
        this.aufgabe = aufgabe;
        this.lernStatus = lernStatus;
        this.kennung = kennung;
    }

    public Aufgabe getAufgabe() {
        return aufgabe;
    }

    public LernStatus getLernStatus() {
        return lernStatus;
    }

    public int getKennung() {
        return kennung;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.lernStatus);
        hash = 71 * hash + this.kennung;
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
        final XGAufgabe other = (XGAufgabe) obj;
        if (this.kennung != other.kennung) {
            return false;
        }
        if (!Objects.equals(this.lernStatus, other.lernStatus)) {
            return false;
        }
        return true;
    }



    @Override
    public String toString() {
        return "Aufgabe " + aufgabe + " " +  lernStatus;
    }
    
}
