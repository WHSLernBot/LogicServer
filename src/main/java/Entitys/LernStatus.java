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
import javax.persistence.OneToOne;

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
    
    @OneToMany(mappedBy="lernStatus", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<BeAufgabe> beAufgaben;

    @OneToOne(mappedBy="lernStatus", cascade=CascadeType.ALL,orphanRemoval = true) //drüber sprechen
    private ZuAufgabe zuAufgaben;
    
    @OneToMany(mappedBy="lernStatus", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<XGAufgabe> xgAufgaben;
    
    
    public LernStatus(){
        
    }
    
    public LernStatus(Benutzer benutzer, Thema thema, Date letztesDatum){
        
        this.benutzer = benutzer;
        this.thema = thema;
        this.aktiv = true;
        this.richtige = 0;
        this.sumPunkte = 0;
        this.letztesDatum = letztesDatum;
    }
    
    public void setAktiv(Boolean aktiv){
        this.aktiv = aktiv;
    }
    
    public boolean istAktiv(){
        return aktiv;
    }
    
    public void richtigGeloest(){
        this.richtige++;
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

    public Collection<BeAufgabe> getBeAufgaben() {
        return beAufgaben;
    }

    public void addBeAufgaben(Aufgabe aufgabe, Boolean richtig, 
            Date datum, Boolean hinweis, Boolean beantwortet) {
        this.beAufgaben.add(new BeAufgabe(aufgabe,this,richtig,datum,hinweis,beantwortet));
    }

    public ZuAufgabe getZuAufgaben() {
        return zuAufgaben;
    }

    public void setZuAufgaben(ZuAufgabe zuAufgaben) {
        this.zuAufgaben = zuAufgaben;
    }

    public Collection<XGAufgabe> getXgAufgaben() {
        return xgAufgaben;
    }

    public void addXgAufgaben(Aufgabe aufgabe) {
        this.xgAufgaben.add(new XGAufgabe(aufgabe,this));
    }

    public Benutzer getBenutzer() {
        return benutzer;
    }

    public Thema getThema() {
        return thema;
    }

    public Boolean getAktiv() {
        return aktiv;
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
