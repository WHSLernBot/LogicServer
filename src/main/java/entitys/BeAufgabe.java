package entitys;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Diese Klasse stellt einer Aufgabe dar, die von einem Benutzer bearbeitet wurde.
 * 
 * @author Seve
 */
@Entity
@IdClass(BeAufgabePK.class)
@Table(name="BeAufgabe")
public class BeAufgabe implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Die bearbeitete Aufgabe.
     */
    @ManyToOne
    private Aufgabe aufgabe;
    
    /**
     * Der Zugehoerige Lernstatus des Benutzers.
     */
    @Id
    @ManyToOne
    private LernStatus lernStatus;
    
    @Id
    private int kennung;
    
    /**
     * Ist true, falls die Aufgabe richtig beantwortet wurde.
     */
    private boolean richtig;
    
    /**
     * Das Datum, an dem die Aufgabe bearbeitet wurde.
     */
    private Date datum;
    
    /**
     * Ist true, falls ein Hinweis benutzt wurde.
     */
    private boolean hinweis;
    
    /**
     * Ist true, falls die Aufgabe beantwortet wurde,
     * oder von Benutzer noch aussteht (vielleicht nichtmer sinnvoll)
     */
    private Boolean beantwortet;
    
    public BeAufgabe(){
        
    }
    
    public BeAufgabe(Aufgabe aufgabe, LernStatus lernStatus,int kennung, Boolean richtig, 
            Date datum, Boolean hinweis, Boolean beantwortet) {
        
        this.aufgabe = aufgabe;
        this.lernStatus = lernStatus;
        this.richtig = richtig;
        this.datum = datum;
        this.hinweis = hinweis;
        this.beantwortet = beantwortet;
        this.kennung = kennung;
    }
    
    public boolean istRichtig(){
        return richtig;
    }
    
    public Date getDatum(){
        return datum;
    }
    
    public boolean getHinweis(){
        return hinweis;
    }
    
    public boolean istBeantwortet(){
        return beantwortet;
    }

    public Aufgabe getAufgabe() {
        return aufgabe;
    }

    public LernStatus getLernStatus() {
        return lernStatus;
    }

    public int getKennung() {
        return kennung;
    }
    
    public void setzeAntwort(boolean richtig, boolean hinweis, Date datum) {
        this.datum = datum;
        this.beantwortet = true;
        this.richtig = richtig;
        this.hinweis = hinweis;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.lernStatus);
        hash = 83 * hash + this.kennung;
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
        final BeAufgabe other = (BeAufgabe) obj;
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
        return "Bearbeitete Aufgabe " + aufgabe + " " +  lernStatus;
    }
    
}
