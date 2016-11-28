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
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 * Diese Klasse stellt einen LernStatus dar, der den Fortschritt eines Benutzers
 * bei einem bestimmten Thema eines Moduls persistiert..
 * 
 * @author Seve
 */
@Entity
@IdClass(LernStatusPK.class)
public class LernStatus implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * Der Benutzer dem der LernStatus gehöhrt.
     */
    @Id
    @ManyToOne
    private Benutzer benutzer;
    
    /**
     * Das Thema das der LernStatus persistiert.
     */
    @Id
    @ManyToOne
    private Thema thema;
    
    /**
     * Falls true, so hat der Benutzer dieses Thema noch nicht abgeschlossen.
     */
    private Boolean aktiv;
    
    /**
     * Summe der richtig beantowrteten Aufgaben.
     */
    private int richtige;
    
    /**
     * Anzahl der geloesten Aufgaben.
     */
    private int geloest;
    
    /**
     * Summe der Punkte die alle Aufgaben abzüglich der Häufigkeit und 
     * Richtigkeit eingebracht haben.
     */
    private int sumPunkte;
    
    /**
     * Das Datum an dem der Benutzer zuletzt Aufgaben zu diesem Thema gelöst hat.
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date letztesDatum;
    
    /**
     * Die Aufgaben die der Benutzer bearbeitet hat.
     */
    @OneToMany(mappedBy="lernStatus", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<BeAufgabe> beAufgaben;

    /**
     * Die Aufgabe, die der Benutzer als nächstes zu bearbeitet hat.
     */
    @OneToOne(mappedBy="lernStatus", cascade=CascadeType.ALL,orphanRemoval = true) //drï¿½ber sprechen
    private ZuAufgabe zuAufgaben;
    
    /**
     * Die Aufgaben die der Benutzer zusätzlich gelöst hat, die aber nicht
     * in der Reihe der zu lösenden Aufgaben war. z.b. durch spezielle suche
     * gelöste Aufgaben. XG = eXtra Gelöst
     */
    @OneToMany(mappedBy="lernStatus", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<XGAufgabe> xgAufgaben;
    
    /**
     * Zeigt an, ob sich der LernStatus seit dem letzten berechnen veraendert hat.
     */
    private boolean veraendert;
    
    public LernStatus(){}
    
    public LernStatus(Benutzer benutzer, Thema thema, Date letztesDatum){
        
        this.benutzer = benutzer;
        this.thema = thema;
        this.aktiv = true;
        this.richtige = 0;
        this.sumPunkte = 0;
        this.geloest = 0;
        this.letztesDatum = letztesDatum;
        veraendert = false;
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

    public int getGeloest() {
        return geloest;
    }

    public boolean isVeraendert() {
        return veraendert;
    }

    public void setVeraendert(boolean veraendert) {
        this.veraendert = veraendert;
    }
    
    /**
     * Zu verwenden falls eine Aufgabe zu diesem LernStatus gelöst wurde.
     */
    public void neueGeloest() {
        geloest++;
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
