package Entitys;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
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
    
    private boolean zuletzt;
    
    @Id
    @ManyToOne
    private Aufgabe aufgabe;
    
    @Id
    @OneToOne
    private LernStatus status;
    
    @OneToOne(mappedBy="zuAufgabe")
    private ZuAufgabe naechsteAufgabe;
    
    public ZuAufgabe(){
        
    }
    
    public ZuAufgabe(LernStatus status, Aufgabe aufgabe, boolean zuletzt){
        this.status = status;
        this.aufgabe = aufgabe;
        this.zuletzt = zuletzt;
    }
    
    public LernStatus getStatus(){
        return status;
    }
    
    public boolean istZuletzt(){
        return zuletzt;
    }

    public Aufgabe getAufgabe() {
        return aufgabe;
    }

    public ZuAufgabe getNaechsteAufgabe() {
        return naechsteAufgabe;
    }

    public ZuAufgabe setNaechsteAufgabe(Aufgabe aufgabe) {
        this.naechsteAufgabe = new ZuAufgabe(status,aufgabe,true);
        this.zuletzt = false;
        
        return naechsteAufgabe;
    } 
    

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.aufgabe);
        hash = 67 * hash + Objects.hashCode(this.status);
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
        if (!Objects.equals(this.status, other.status)) {
            return false;
        }
        return true;
    }


    

    @Override
    public String toString() {
        return "Zuletzt bearbeitete Aufgabe: " + status;
    }
    
}
