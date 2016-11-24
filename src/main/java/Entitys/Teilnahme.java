package Entitys;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

/**
 * Diese Klasse beschreibt die Teilnahme eines Benutzers an einer Klausur.
 * @author Seve
 */
@Entity
@IdClass(TeilnahmePK.class)
public class Teilnahme implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * Der Benutzer der teilnimmt.
     */
    @Id
    @ManyToOne
    private Benutzer benutzer;
    
    /**
     * Die Klausur die der Benutzer schreibt.
     */
    @Id
    @ManyToOne
    private Klausur klausur;
    
    /**
     * Die Note die geschrieben wurde * 10.
     * => 3,2 = 32
     */
    private short note;
    
    public Teilnahme(){}

    public Teilnahme(Benutzer benutzer, Klausur klausur) {
        this.benutzer = benutzer;
        this.klausur = klausur;
        this.note = 0;
    }
    
    public Teilnahme(short note){
        this.note = note;
    }
    
    public void setNote(short note){
        this.note = note;
    }
    
    public int getNote(){
        return note;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.benutzer);
        hash = 37 * hash + Objects.hashCode(this.klausur);
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
        final Teilnahme other = (Teilnahme) obj;
        if (!Objects.equals(this.benutzer, other.benutzer)) {
            return false;
        }
        if (!Objects.equals(this.klausur, other.klausur)) {
            return false;
        }
        return true;
    }

    
    
    @Override
    public String toString() {
        return klausur + " Klausur von " + benutzer;
    }
    
}
