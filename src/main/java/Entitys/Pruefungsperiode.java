package Entitys;

import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author Betül
 */
@Entity
@IdClass(PruefungsperiodePK.class)
public class Pruefungsperiode implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Das Jahr der Prüfungsperiode.
     */
    @Id
    private short jahr;
    
    /**
     * DIe entsprechende nummer der Phase.
     */
    @Id
    private short phase;
    
    /**
     * Der anmeldebeginn zu dieser Prüfunsphase.
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date anmeldebeginn;
    
    /**
     * Das Anfangsdatum dieser Periode.
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date anfang;
    
    /**
     * Das Enddatum dieser Periode.
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date ende;
    
    /**
     * Die zugehörige Uni dieser Prüfungsperiode.
     */
    @Id
    @ManyToOne 
    private Uni uni;
    
    /**
     * Die Klausuren die geschrieben werden.
     */
    @OneToMany(mappedBy="periode", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<Klausur> klausuren;

    public Pruefungsperiode(){}
    
    public Pruefungsperiode(Uni uni, short jahr, short phase, Date anmeldebeginn, Date anfang, Date ende){
        this.uni = uni;
        this.jahr = jahr;
        this.phase = phase;
        this.anmeldebeginn = anmeldebeginn;
        this.anfang = anfang;
        this.ende = ende;
    }

    public Uni getUni() {
        return uni;
    }
    
    public int getJahr(){
        return jahr;
    }
    
    public void setPhase(short phase){
        this.phase = phase;
    }
    
    public short getPhase(){
        return phase;
    }
    
    public void setAnmeldebeginn(Date anmeldebeginn){
        this.anmeldebeginn = anmeldebeginn;
    }
    
    public Date getAnmeldebeginn(){
        return anmeldebeginn;
    }

    public Collection<Klausur> getKlausuren() {
        return klausuren;
    }

    public void addKlausuren(Klausur klausuren) {
        this.klausuren.add(klausuren);
    }

    public Date getAnfang() {
        return anfang;
    }

    public void setAnfang(Date anfang) {
        this.anfang = anfang;
    }

    public Date getEnde() {
        return ende;
    }

    public void setEnde(Date ende) {
        this.ende = ende;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.jahr;
        hash = 97 * hash + Objects.hashCode(this.uni);
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
        final Pruefungsperiode other = (Pruefungsperiode) obj;
        if (this.jahr != other.jahr) {
            return false;
        }
        if (!Objects.equals(this.uni, other.uni)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "Pruefungsperiode " + phase + ":" + jahr +
                "Anmeldebeginn: " + anmeldebeginn;
    }
    
}
