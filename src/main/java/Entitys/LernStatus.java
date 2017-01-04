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
     * Der Benutzer dem der LernStatus gehoehrt.
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
     * Summe der Punkte die alle Aufgaben abzueglich der Haeufigkeit und 
     * Richtigkeit eingebracht haben. Maximal 100.
     */
    private int sumPunkte;
    
    /**
     * Die naechste Kennnummer fuer eine beantwortete Aufgabe.
     */
    private int kennungBe;
    private int kennungZu;
    private int kennungXG;
    
    /**
     * Das Datum an dem der Benutzer zuletzt Aufgaben zu diesem Thema geloest hat.
     */
    private Date letztesDatum;
    
    /**
     * Die Aufgaben die der Benutzer bearbeitet hat.
     */
    @OneToMany(mappedBy="lernStatus", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<BeAufgabe> beAufgaben;

    /**
     * Die Aufgabe, die der Benutzer als naechstes zu bearbeitet hat.
     */
    @OneToMany(mappedBy="lernStatus", cascade=CascadeType.ALL,orphanRemoval = true) //drueber sprechen
    private Collection<ZuAufgabe> zuAufgaben;
    
    /**
     * Die Aufgaben die der Benutzer zusaetzlich geloest hat, die aber nicht
     * in der Reihe der zu loesenden Aufgaben war. z.b. durch spezielle suche
     * geloeste Aufgaben. XG = eXtra Geloest
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
        kennungBe = 0;
        kennungXG = 0;
        kennungZu = 0;
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
        
        
        this.beAufgaben.add(new BeAufgabe(aufgabe,this,kennungBe,richtig,datum,
                hinweis,beantwortet));    
        this.kennungBe++;
    }
    
    public void addedBeAufgaben() {
        this.kennungBe++;
    }

    public Collection<XGAufgabe> getXgAufgaben() {
        return xgAufgaben;
    }

    public void addXgAufgaben(Aufgabe aufgabe) {
        this.xgAufgaben.add(new XGAufgabe(aufgabe,this,kennungXG));
        
        kennungXG++;
    }

    public Collection<ZuAufgabe> getZuAufgaben() {
        return zuAufgaben;
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

    public int getKennungBe() {
        return kennungBe;
    }

    public void setKennungBe(int kennungBe) {
        this.kennungBe = kennungBe;
    }

    public int getKennungZu() {
        return kennungZu;
    }

    public void resetKennungZu() {
        this.kennungZu = 0;
    }
    
    public void gotZuAufgabe() {
        kennungZu++;
    }

    public int getKennungXG() {
        return kennungXG;
    }

    public void setKennungXG(int kennungXG) {
        this.kennungXG = kennungXG;
    }
    
    /**
     * Zu verwenden falls eine Aufgabe zu diesem LernStatus geloest wurde.
     * @param datum Das aktuelle Datum
     */
    public void neueGeloest(Date datum) {
        geloest++;
        this.letztesDatum = datum;
        this.veraendert = true;
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
