package entitys;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Diese Klasse beschreibt die Plattform mit der ein Benutzer den ChatBot aufruft.
 * @author Seve
 */
@Entity
@IdClass(PlattformPK.class)
@Table(name="Plattform")
public class Plattform implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Die id die von der Plattform mitgegeben wird
     */
    @Id
    @Column(length = 200)
    private String pfID;

    /**
     * Die Plattformnummer.
     * 0 = Facebook
     * Rest nicht verwendet
     */
    @Id
    @ManyToOne
    private Adresse pfNr;
    
    /**
     * Der zugehoerige Benutzer dieser Plattform.
     */
    @OneToOne
    private Benutzer benutzer;
    
    /**
     * Die witSession (Falls noetig)
     */
    @Column(length = 100)
    private String witSession;

    public Plattform() {}
  
    /**
     * Erzeugt ein neues Objekt dieser Klasse mit den angegebenen Daten.
     * 
     * @param pfID Plattform-ID
     * @param pfNr Plattform-Nummer.
     * @param benutzer Benutzer, der zu der Plattform gehört
     * @param witSession Die Wit.ai session 
     */
    public Plattform(String pfID, Adresse pfNr,Benutzer benutzer, String witSession){
        this.pfID = pfID;
        this.pfNr = pfNr;
        this.benutzer = benutzer;
        this.witSession = witSession;
    }
    
    public String getPfID(){
        return pfID;
    }
    
    public Adresse getAdresse(){
        return pfNr;
    }
    
    public void setWitSession(String witSession){
        this.witSession = witSession;
    }
    
    public String getWitSession(){
        return witSession;
    }

    public Benutzer getBenutzer() {
        return benutzer;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.pfID);
        hash = 17 * hash + Objects.hashCode(this.pfNr);
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
        final Plattform other = (Plattform) obj;
        if (this.pfNr != other.pfNr) {
            return false;
        }
        if (!Objects.equals(this.pfID, other.pfID)) {
            return false;
        }
        return true;
    } 

    @Override
    public String toString() {
        return "Plattform von: " + benutzer;
    }
    
}
