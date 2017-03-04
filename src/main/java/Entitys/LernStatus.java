package Entitys;

import DAO.EMH;
import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.Table;

/**
 * Diese Klasse stellt einen LernStatus dar, der den Fortschritt eines Benutzers
 * bei einem bestimmten Thema eines Moduls persistiert..
 * 
 * @author Seve
 */
@Entity
@IdClass(LernStatusPK.class)
@Table(name="LernStatus")
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
    private boolean aktiv;
    
    /**
     * Falls true, wurde dieses Modul ervolgreich bearbeitet.
     */
    private boolean beendet;
    
    
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
        this.beendet = false;
        this.richtige = 0;
        this.sumPunkte = 0;
        this.geloest = 0;
        this.letztesDatum = letztesDatum;
        veraendert = true;
        kennungBe = 0;
        kennungXG = 0;
        kennungZu = 0;
    }
    
    public void setAktiv(boolean aktiv){
        this.aktiv = aktiv;
    }
    
    public boolean istAktiv(){
        return aktiv;
    }

    public boolean isBeendet() {
        return beendet;
    }

    public void setAsBeendet() {
        this.beendet = true;
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
        Collection<BeAufgabe> result;
        
        String sql = "select object(a) from BeAufgabe a "
                + "where LERNSTATUS_BENUTZER_ID = :BID and LERNSTATUS_THEMA_THEMENID = :TID";
        
        Query q = EMH.getEntityManager().createQuery(sql);
        
        q.setParameter("TID", this.thema.getId());
        q.setParameter("BID", this.benutzer.getId());
        
        result = q.getResultList();
        
        return result;
    }

//    public void addBeAufgaben(Aufgabe aufgabe, boolean richtig, 
//            Date datum, boolean hinweis, boolean beantwortet) {
//        
//        
//        this.beAufgaben.add(new BeAufgabe(aufgabe,this,kennungBe,richtig,datum,
//                hinweis,beantwortet));    
//        this.kennungBe++;
//    }

    public Collection<XGAufgabe> getXgAufgaben() {
        
        Collection<XGAufgabe> result;
        
        String sql = "select object(x) from XGAufgabe x "
                + "where LERNSTATUS_BENUTZER_ID = :BID and LERNSTATUS_THEMA_THEMENID = :TID";
        
        Query q = EMH.getEntityManager().createQuery(sql);
        
        q.setParameter("BID", this.benutzer.getId());
        q.setParameter("TID", this.thema.getId());
        
        result = q.getResultList();
        
        return result;
    }

//    public void addXgAufgaben(Aufgabe aufgabe) {
//        this.xgAufgaben.add(new XGAufgabe(aufgabe,this,kennungXG));
//        
//        kennungXG++;
//    }

//    public Collection<ZuAufgabe> getZuAufgaben() {
//        return zuAufgaben;
//    }

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

    public void addedBeAufgabe() {
        this.kennungBe++;
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

    public void addedXGAufgabe() {
        this.kennungXG++;
    }
    
    public void resetXGAufgabe() {
        this.kennungXG = 0;
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
