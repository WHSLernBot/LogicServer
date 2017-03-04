package Entitys;

import DAO.EMH;
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
import javax.persistence.Query;
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
    private Long themenID;
    
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
    
    
    public void addAufgabe() {
        this.aufgabenZahl++;
    }
    
    public Long getId() {
        return themenID;
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
        Collection<LernStatus> result;
        
        String sql = "select object(ls) from LernStatus ls "
                + "where THEMA_THEMENID = :TID";
        
        Query q = EMH.getEntityManager().createQuery(sql);
        
        q.setParameter("TID", this.themenID);
        
        result = q.getResultList();
        
        return result;
    }
    
//    /**
//     * Wenn ueberhaupt sinnvoll
//     * @param lernStatus 
//     */
//    public void addLernStatus(LernStatus lernStatus) {
//        this.lernStadi.add(lernStatus);
//    }

    public Collection<Aufgabe> getAufgaben() {
        Collection<Aufgabe> result;
        
        String sql = "select object(a) from Aufgabe a "
                + "where THEMA_THEMENID = :TID";
        
        Query q = EMH.getEntityManager().createQuery(sql);
        
        q.setParameter("TID", this.themenID);
        
        result = q.getResultList();
        
        return result;
    }

    public Modul getModul() {
        return modul;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (themenID != null ? themenID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Thema)) {
            return false;
        }
        Thema other = (Thema) object;
        if ((this.themenID == null && other.themenID != null) || (this.themenID != null && !this.themenID.equals(other.themenID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entitys.Thema[ id=" + themenID + " ]";
    }
    
}
