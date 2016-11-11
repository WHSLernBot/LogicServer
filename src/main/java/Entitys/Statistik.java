/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entitys;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Betül
 */
@Entity
public class Statistik implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    private Modul modul;
    
    private int woche;
    
    private int anteil;
    
    public Statistik(){
        
    }
    
    public Statistik(int woche, int anteil){
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof Statistik)) {
            return false;
        }
        Statistik other = (Statistik) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entitys.Statistik[ id=" + id + " ]";
    }
    
}
