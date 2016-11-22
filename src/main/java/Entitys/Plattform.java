package Entitys;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToOne;

/**
 *
 * @author Seve
 */
@Entity
@IdClass(PlattformPK.class)
public class Plattform implements Serializable {

    @Id
    private String pfID;

    @Id
    private int pfNr;
    
    @OneToOne
    private Benutzer benutzer;
    
    private String witSession;

    public Plattform() {
    }

    
    
    public Plattform(String pfID, int pfNr,Benutzer benutzer, String witSession){
        this.pfID = pfID;
        this.pfNr = pfNr;
        this.benutzer = benutzer;
        this.witSession = witSession;
    }
    
    public String getPfID(){
        return pfID;
    }
    
    public int getPfNr(){
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
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.pfID);
        hash = 97 * hash + this.pfNr;
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
