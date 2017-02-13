package Entitys;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    private Klausur klausur;
    
    /**
     * Die Note die geschrieben wurde * 10.
     * => 3,2 = 32
     */
    private short note;
    
    private int prozent;
    
    public Teilnahme(){}

    public Teilnahme(Benutzer benutzer, Klausur klausur) {
        this.benutzer = benutzer;
        this.klausur = klausur;
        this.note = 0;
        this.prozent = 0;
    }
    
    public Teilnahme(short note){
        this.note = note;
    }
    
    public void setNote(short note){
        this.note = note;
    }
    
    public short getNote(){
        return note;
    }

    public Klausur getKlausur() {
        return klausur;
    }

    public int getProzent() {
        return prozent;
    }

    public void setProzent(int prozent) {
        this.prozent = prozent;
    }
    
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.benutzer);
        hash = 67 * hash + Objects.hashCode(this.id);
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
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return id + " Klausur von " + benutzer;
    }
    
}
