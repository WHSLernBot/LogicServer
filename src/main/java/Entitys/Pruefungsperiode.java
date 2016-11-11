/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entitys;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Betül
 */
@Entity
public class Pruefungsperiode implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private int jahr;
    
    private int phase;
    
    private Date anmeldebeginn;
    
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
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pruefungsperiode)) {
            return false;
        }
        Pruefungsperiode other = (Pruefungsperiode) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
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
