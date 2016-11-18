/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Betül
 */
@Entity
public class Pruefungsperiode implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @ManyToOne 
    private Uni uni;
    
    @OneToMany(mappedBy="pruefungsperiode", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<Klausur> klausuren;
    
   @Id
    private int jahr;
    
    private int phase;
    
    private Date anmeldebeginn;
    
    
    

    public Pruefungsperiode(){
        
    }
    
    public Pruefungsperiode(int jahr, int phase, Date anmeldebeginn, Uni uni,
            Klausur klausuren){
        
        this.jahr = jahr;
        this.phase = phase;
        this.anmeldebeginn = anmeldebeginn;
        this.uni = uni;
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
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.uni);
        hash = 59 * hash + Objects.hashCode(this.jahr);
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
        final Pruefungsperiode other = (Pruefungsperiode) obj;
        if (!Objects.equals(this.jahr, other.jahr)) {
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
