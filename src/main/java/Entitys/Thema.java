package Entitys;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Seve
 */
@Entity
public class Thema implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long thmenID;
    
    private String name;
    
    private int anteil;
    
    private int aufgabenZahl;
    
    @ManyToOne
    private Modul modul;
    
    @OneToMany(mappedBy="thema", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<LernStatus> lernStadi;
    
    @OneToMany(mappedBy="thema", cascade=CascadeType.ALL,orphanRemoval = true)
    private Aufgabe aufgabe;

    

    public Thema(){
        
    }
    
    public Thema(Long themenID, String name, int anteil, int aufgabenZahl){
        
        this.thmenID = themenID;
        this.name = name;
        this.anteil = anteil;
        this.aufgabenZahl = aufgabenZahl;
    }
    
    
    public Long getId() {
        return thmenID;
    }

    public void setId(Long id) {
        this.thmenID = id;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
    
    public void setAnteil(int anteil){
        this.anteil = anteil;
    }
    
    public int getAnteil(){
        return anteil;
    }
    
    public void setAufgabenZahl(int aufgabenZahl){
        this.aufgabenZahl = aufgabenZahl;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (thmenID != null ? thmenID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Thema)) {
            return false;
        }
        Thema other = (Thema) object;
        if ((this.thmenID == null && other.thmenID != null) || (this.thmenID != null && !this.thmenID.equals(other.thmenID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entitys.Thema[ id=" + thmenID + " ]";
    }
    
}
