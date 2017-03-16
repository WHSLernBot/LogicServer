package entitys;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Diese Klasse stellt eine Aufgabe dar, die der Benutzer als naechstes 
 * zu bearbeitet hat. Ausserdem verweist sie auf eine Aufgabe die 
 * er dannach bearbeiten soll.
 * 
 * @author Betuel
 */
@Entity
@IdClass(ZuAufgabePK.class)
@Table(name="ZuAufgabe")
public class ZuAufgabe implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Die zubearbeitende Aufgabe.
     */
    @ManyToOne
    private Aufgabe aufgabe;
    
    /**
     * Der zugehoerige Lernstatus.
     */
    @Id
    @OneToOne
    private LernStatus lernStatus;
    
    @Id
    private int kennung;
    
    public ZuAufgabe(){}
    
    public ZuAufgabe(LernStatus status, Aufgabe aufgabe,int kennung){
        this.lernStatus = status;
        this.aufgabe = aufgabe;
        this.kennung = kennung;
    }
    
    public LernStatus getStatus(){
        return lernStatus;
    }
    

    public Aufgabe getAufgabe() {
        return aufgabe;
    }

    public int getKennung() {
        return kennung;
    }   

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.lernStatus);
        hash = 67 * hash + this.kennung;
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
        if (this.kennung != other.kennung) {
            return false;
        }
        if (!Objects.equals(this.lernStatus, other.lernStatus)) {
            return false;
        }
        return true;
    }

 

    @Override
    public String toString() {
        return "Zuletzt bearbeitete Aufgabe: " + lernStatus;
    }
    
}
