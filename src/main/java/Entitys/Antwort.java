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
@IdClass(AntwortPK.class)
public class Antwort implements Serializable {

    @Id
    @ManyToOne
    private Aufgabe aufgabe;

    @Id
    private int nummer;

    private String antwort;

    private Boolean richtig;

    private long haeufigkeit;

    public Antwort() {

    }

    public Antwort(int nummer, String antwort, Boolean richtig) {
        this.nummer = nummer;
        this.antwort = antwort;
        this.richtig = richtig;
    }

    public void setNummer(int nummer) {
        this.nummer = 0;
    }

    public int getNummer() {
        return nummer;
    }

    public void setAntwort(String antwort) {
        this.antwort = antwort;
    }

    public String getAntwort() {
        return antwort;
    }

    public void setRichtig(boolean richtig) {
        this.richtig = richtig;
    }

    public Boolean istRichtig() {
        return richtig;
    }

    public void setHaeufigkeit(long haeufigkeit) {
        this.haeufigkeit = haeufigkeit++;
    }

    public long getHaeufigkeit() {
        return haeufigkeit;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.antwort);
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
        final Antwort other = (Antwort) obj;
        if (this.nummer != other.nummer) {
            return false;
        }
        if (!Objects.equals(this.aufgabe, other.aufgabe)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return antwort + " von " + aufgabe;
    }

}
