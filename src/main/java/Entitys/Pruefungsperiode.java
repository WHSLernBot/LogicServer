package Entitys;

import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Betül
 */
@Entity
@IdClass(PruefungsperiodePK.class)
public class Pruefungsperiode implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    private int jahr;
    
    private int phase;
    
    private Date anmeldebeginn;
    
    @Id
    @ManyToOne 
    private Uni uni;
    
    @OneToMany(mappedBy="pruefungsperiode", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<Klausur> klausuren;

    public Pruefungsperiode(){
        
    }
    
    public Pruefungsperiode(int jahr, int phase, Date anmeldebeginn){
        
        this.jahr = jahr;
        this.phase = phase;
        this.anmeldebeginn = anmeldebeginn;
    }
    

    public void setJahr(int jahr){
        this.jahr = jahr;
    }
    
    public int getJahr(){
        return jahr;
    }
    
    public void setPhase(int phase){
        this.phase = phase;
    }
    
    public int getPhase(){
        return phase;
    }
    
    public void setAnmeldebeginn(Date anmeldebeginn){
        this.anmeldebeginn = anmeldebeginn;
    }
    
    public Date getAnmeldebeginn(){
        return anmeldebeginn;
    }

    public Collection<Klausur> getKlausuren() {
        return klausuren;
    }

    public void setKlausuren(Collection<Klausur> klausuren) {
        this.klausuren = klausuren;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.jahr;
        hash = 97 * hash + Objects.hashCode(this.uni);
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
        final Pruefungsperiode other = (Pruefungsperiode) obj;
        if (this.jahr != other.jahr) {
            return false;
        }
        if (!Objects.equals(this.uni, other.uni)) {
            return false;
        }
        return true;
    }
    
    
  
    @Override
    public String toString() {
        return "Pruefungsperiode " + phase + ":" + jahr +
                "Anmeldebeginn: " + anmeldebeginn;
    }
    
}
