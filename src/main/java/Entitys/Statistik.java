package Entitys;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

/**
 *
 * @author Bet�l
 */
@Entity
@IdClass(StatistikPK.class)
public class Statistik implements Serializable {
    
    @Id
    @ManyToOne
    private Modul modul;
    
    @Id
    private int woche;
    
    private int anteil;
    
    public Statistik(){
        
    }
    
    public Statistik(Modul modul, int woche, int anteil) {
        this.modul = modul;
        this.woche = woche;
        this.anteil = anteil;
    }
    
    public void setWoche(int woche){
        this.woche = woche;
    }
    
    public int getWoche(){
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
