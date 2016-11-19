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
    
    private LernStatus status;
    
    private boolean zuletzt;
    
    @Id
    @ManyToOne
    private Aufgabe aufgabe;
    
    @Id
    @OneToOne(mappedBy="zuAufgabe")
    private LernStatus lernStadi;
    
    @OneToOne(mappedBy="zuAufgabe")
    private ZuAufgabe zuAufgabe;
    
    public ZuAufgabe(){
        
    }
    
    public ZuAufgabe(LernStatus status, boolean zuletzt){
        this.status = status;
        this.zuletzt = zuletzt;
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
    
    public boolean istZuletzt(){
        return zuletzt;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.aufgabe);
        hash = 23 * hash + Objects.hashCode(this.lernStadi);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
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
        if (!Objects.equals(this.lernStadi, other.lernStadi)) {
            return false;
        }
        return true;
    }
    

    @Override
    public String toString() {
        return "Zuletzt bearbeitete Aufgabe: " + status;
    }
    
}
