package Entitys;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

/**
 *
 * @author Seve
 */
@Entity
@IdClass(TeilnahmePK.class)
public class Teilnahme implements Serializable {

    @Id
    @ManyToOne
    private Benutzer benutzer;
    
    @Id
    @ManyToOne
    private Klausur klausur;
    
    private int note;
    
    public Teilnahme(){
        
    }

    public Teilnahme(Benutzer benutzer, Klausur klausur) {
        this.benutzer = benutzer;
        this.klausur = klausur;
        this.note = 0;
    }
    
    public Teilnahme(int note){
        this.note = note;
    }
    
    public void setNote(int note){
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
