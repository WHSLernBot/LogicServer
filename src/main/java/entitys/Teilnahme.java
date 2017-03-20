package entitys;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Diese Klasse beschreibt die Teilnahme eines Benutzers an einer Klausur.
 * @author Seve
 */
@Entity
@IdClass(TeilnahmePK.class)
@Table(name="Teilnahme")
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
    private Long id;
    
    /**
     * Die Klausur an der der Benutzer teilgenommen hat.
     */
    @ManyToOne
    private Klausur klausur;
    
    /**
     * Die Note die geschrieben wurde * 10.
     * => 3,2 = 32
     */
    private short note;
    
    /**
     * Der Prozentwert der Teilnahme an der Klausur.
     */
    private int prozent;
    
    public Teilnahme(){}

    /**
     * Erzeugt ein neues Objekt dieser Klasse mit den angegebenen Daten.
     * 
     * @param benutzer der Benutzer, der an der Klausur teilnimmt
     * @param klausur Klausur an der der Benutzer teilgenommen hat
     */
    public Teilnahme(Benutzer benutzer, Klausur klausur) {
        this.benutzer = benutzer;
        this.klausur = klausur;
        this.note = 0;
        this.prozent = 0;
        this.id = benutzer.gibKlausurNummer();
        benutzer.neueKlausur();
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
