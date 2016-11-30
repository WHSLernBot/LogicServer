package Entitys;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

/**
 * Diese Klasse stellt eine statisik dar, die berechnet in wie weit der Zeitfaktor
 * eine Rolle auf die Note einer Klausur hat.
 * @author Betuel
 */
@Entity
@IdClass(StatistikPK.class)
public class Statistik implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Das zugehoeroge Modul.
     */
    @Id
    @ManyToOne
    private Modul modul;
    
    /**
     * Die woche der Statistik. Gerechnet in bezug auf Abstand der Klausur.
     * => 2 = 2 wochen vor der Klausur
     */
    @Id
    private short woche;
    
    /**
     * Der Anteil den diese Statistik ausmacht.
     */
    private int anteil;
    
    public Statistik(){
        
    }
    
    public Statistik(Modul modul, short woche, int anteil) {
        this.modul = modul;
        this.woche = woche;
        this.anteil = anteil;
    }
    
    public void setWoche(short woche){
        this.woche = woche;
    }
    
    public short getWoche(){
        return woche;
    }
    
    public void setAnteil(int anteil){
        this.anteil = anteil;
    }
    
    public int getAnteil(){
        return anteil;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.modul);
        hash = 79 * hash + this.woche;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Statistik other = (Statistik) obj;
        if (!Objects.equals(this.modul, other.modul)) {
            return false;
        }
        if (this.woche != other.woche) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Statistik{" + "modul=" + modul + ", woche=" + woche + ", anteil=" + anteil + '}';
    }


   
    
}
