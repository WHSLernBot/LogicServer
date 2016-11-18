package Entitys;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author Betül
 */
@Entity
@IdClass(ZuAufgabePK.class)
public class ZuAufgabe implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    @Id
    @ManyToOne
    private Aufgabe aufgabe;
    
    @Id
    @OneToOne(mappedBy="zuAufgabe")
    private LernStatus lernStatus;
    
    @OneToOne(mappedBy="zuAufgabe")
    private ZuAufgabe zuAufgabe;
    
    private LernStatus status;
    
    private boolean zuletzt;
    
    public ZuAufgabe(){
        
    }
    
    public ZuAufgabe(LernStatus status, boolean zuletzt, Aufgabe aufgabe, LernStatus lernStatus){
        this.status = status;
        this.zuletzt = zuletzt;
        this.aufgabe = aufgabe;
        this.lernStatus = lernStatus;
    }

    public void setStatus(LernStatus status){
        this.status = status;
    }
    
    public LernStatus getStatus(){
        return status;
    }
    
    public void setZuletzt(boolean zuletzt){
        this.zuletzt = zuletzt;
    }
    
    public boolean getZuletzt(){
        return zuletzt;
    }
    
    public void setAufgabe(Aufgabe aufgabe){
        this.aufgabe = (Aufgabe) aufgabe;
    }
    
    public Aufgabe getAufgabe(){
        return aufgabe;
    }
    
    
    public void setLernStatus(LernStatus lernStatus){
        this.lernStatus = (LernStatus) lernStatus;
    }
    
    public LernStatus getLernStatus(){
        return lernStatus;
    }

   @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.aufgabe);
        hash = 79 * hash + Objects.hashCode(this.lernStatus);
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
        final ZuAufgabe other = (ZuAufgabe) obj;
        if (!Objects.equals(this.aufgabe, other.aufgabe)) {
            return false;
        }
        if (!Objects.equals(this.lernStatus, other.lernStatus)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Zuletzt bearbeitete Aufgabe: " + status;
    }
    
}
