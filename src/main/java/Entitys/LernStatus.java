package Entitys;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Seve
 */
@Entity
@IdClass(LernStatusPK.class)
public class LernStatus implements Serializable {

    @Id
    @ManyToOne
    private Benutzer benutzer;
    
    @Id
    @ManyToOne
    private Thema thema;
    
    private Boolean aktiv;
    
    private int richtige;
    
    private int sumPunkte;
    
    private Date letztesDatum;
    
    private Timestamp letzteAktualisierung;
    
    @OneToMany(mappedBy="lernStatus", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<BeAufgabe> beAufgaben;

    @OneToMany(mappedBy="lernStatus", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<ZuAufgabe> zuAufgaben;
    
    @OneToMany(mappedBy="lernStatus", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<XGAufgabe> xgAufgaben;
    
    
    public LernStatus(){
        
    }
    
    public LernStatus(Boolean aktiv, int richtige, int sumPunkte, Date letztesDatum,
            Timestamp letzteAktualisierung){
        this.aktiv = aktiv;
        this.richtige = richtige;
        this.sumPunkte = sumPunkte;
        this.letztesDatum = letztesDatum;
        this.letzteAktualisierung = letzteAktualisierung;
    }
    
    public void setAktiv(Boolean aktiv){
        this.aktiv = aktiv;
    }
    
    public boolean getAktiv(){
        return aktiv;
    }
    
    public void setRichtige(int richtige){
        this.richtige = richtige;
    }
    
    public int getRichtige(){
        return richtige;
    }
    
    public void setSumPunkte(int sumPunkte){
        this.sumPunkte = sumPunkte;
    }
    
    public int getSumPunkte(){
        return sumPunkte;
    }
    
    public void setLetztesDatum(Date letztesDatum){
        this.letztesDatum = letztesDatum;
    }
    
    public Date getLetztesDatum(){
        return letztesDatum;
    }
    
    public void setLetzteAktualisierung(Timestamp letzteAktualisierung){
        this.letzteAktualisierung = letzteAktualisierung;
    }
    
    public Timestamp getLetzteAktualisierung(){
        return letzteAktualisierung;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.benutzer);
        hash = 59 * hash + Objects.hashCode(this.thema);
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
        final LernStatus other = (LernStatus) obj;
        if (!Objects.equals(this.benutzer, other.benutzer)) {
            return false;
        }
        if (!Objects.equals(this.thema, other.thema)) {
            return false;
        }
        return true;
    }

    
    

    @Override
    public String toString() {
        return thema + " Lernstatus von " + benutzer;
    }
    
}
