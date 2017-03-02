package Entitys;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Diese Klasse stellt ein Thema eines Moduls dar.
 * @author Seve
 */
@Entity
@Table(name="Thema")
public class Thema implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Eindeutige id des Themas.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long thmenID;
    
    /**
     * Vollstaendiger Name des Themas
     */
    @Column(length = 200)
    private String name;
    
    /**
     * Anteil den das Thema an der gesamten Klausur ausmacht.
     */
    private int anteil;
    
    /**
     * Anzahl der Aufgaben, die das Thema besitzt.
     */
    private int aufgabenZahl;
    
    /**
     * Das Modul zu dem das Thema gehoehrt.
     */
    @ManyToOne
    private Modul modul;
    
    /**
     * Die Lernstadi, die das Thema bearbeiten.
     */
    @OneToMany(mappedBy="thema", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<LernStatus> lernStadi;
    
    /**
     * Die Aufgaben die auf das Thema bezogen sind.
     */
    @OneToMany(mappedBy="thema", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<Aufgabe> aufgaben;

    public Thema(){}
    
    public Thema(Modul modul, String name, int anteil) {
        this.modul = modul;
        this.name = name;
        this.anteil = anteil;
        this.aufgabenZahl = 0;
    }
    
    
    public Long getId() {
        return thmenID;
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

    public int getAufgabenZahl() {
        return aufgabenZahl;
    }

    public Collection<LernStatus> getLernStadi() {
        return lernStadi;
    }
    
    /**
     * Wenn ueberhaupt sinnvoll
     * @param lernStatus 
     */
    public void addLernStatus(LernStatus lernStatus) {
        this.lernStadi.add(lernStatus);
    }

    public Collection<Aufgabe> getAufgaben() {
        return aufgaben;
    }

    public void addAufgabe() {
        this.aufgabenZahl++;
    }

    public Modul getModul() {
        return modul;
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
