package Entitys;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * Diese Klasse stellt eine Klausur dar, an dem sich ein Benutzer anmelden kann. 
 * @author Seve
 */
@Entity
@IdClass(KlausurPK.class)
public class Klausur implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Das Modul ueber das die Klausur geht.
     */
    @Id
    @ManyToOne
    private Modul modul;
    
    /**
     * Das Datum der Klausur.
     */
    private Date datum;
    
    /**
     * Die Pruefungsperiode in der die Klausur liegt.
     */
    @Id
    @ManyToOne
    private Pruefungsperiode periode;
    
    /**
     * Die Benutzer die an der Klausur teilnehmen.
     */
    @OneToMany(mappedBy="klausur", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<Teilnahme> teilnahmen;
    
    /**
     * Der Ort der Klausur, also Raum, Gebaeude etc.
     */
    @Column(length = 50)
    private String ort;
    
    /**
     * Die Uhrzeit an dem die Klausur geschrieben wird.
     */
    private Time uhrzeit;
    
    /**
     * Die Dauer der Klausur in Minuten.
     */
    private short dauer;
    
    /**
     * Die Durchschnittsnote der Klausur * 10.
     * => 2,5 = 25
     */
    private short durchschnitt;
    
    /**
     * Die Hilfsmittel die in der Klausur benutzer werden koennen,
     * wie Taschenrechner, Buecher etc.
     */
    @Column(length = 200)
    private String hilfsmittel;
    
    /**
     * Der quoteint durch den die Punktzahl eines Benutzers geteil werden muss um
     * ungefaehr die Klausurnote zu bestimmen.
     */
    private int quotient;
    
    /**
     * Die art der klausur(muendlich,schriftlich, .. ).
     */
    private String typ;
    
    /**
     * Ist true, falls eine note hinzugefuegt wurde und somit das ergebnis veraendert wurde.
     */
    private boolean veraendert;
    
    public Klausur(){}
    
    public Klausur(Modul modul,Pruefungsperiode pruefungsperiode, Date datum, Time uhrzeit, short dauer, String ort, String hilfsmittel, String typ){
        
        this.modul = modul;
        this.periode = pruefungsperiode;
        this.datum = datum;
        this.uhrzeit = uhrzeit;
        this.dauer = dauer;
        this.ort = ort;
        this.durchschnitt = 0;
        this.hilfsmittel = hilfsmittel;
        this.quotient = 0;
        this.typ = typ;
        this.veraendert = false;
    }

    public boolean isVeraendert() {
        return veraendert;
    }

    public void setVeraendert(boolean veraendert) {
        this.veraendert = veraendert;
    }
    public String getTyp() {
        return typ;
    }
    
    public void setDatum(Date datum){
        this.datum = datum;
    }
    
    public Date getDatum(){
        return datum;
    }
    
    public void setUhrzeit(Time uhrzeit){
        this.uhrzeit = uhrzeit;
    }
    
    public Time getUhrzeit(){
        return uhrzeit;
    }
    
    public void setDauer(short dauer){
        this.dauer = dauer;
    }
    
    public short getDauer(){
        return dauer;
    }
    
    public void setOrt(String ort){
        this.ort = ort;
    }
    
    public String getOrt(){
        return ort;
    }
    
    public void setDurchschnitt(short durchschnitt){
        this.durchschnitt = durchschnitt;
    }
    
    public short getDurchschnitt(){
        return durchschnitt;
    }

    public void setHilfsmittel(String hilfsmittel){
        this.hilfsmittel = hilfsmittel;
    }
    
    public String getHilfsmittel(){
        return hilfsmittel;
    }
    
    public void setQuotient(int quotient){
        this.quotient = quotient;
    }
    
    public int getQuotient(){
        return quotient;
    }

    public Collection<Teilnahme> getTeilnahmen() {
        return teilnahmen;
    }

    public void addTeilnahmen(Teilnahme teilnahme) {
        this.teilnahmen.add(teilnahme);
    }

    public Pruefungsperiode getPruefungsperiode() {
        return periode;
    }

    public Modul getModul() {
        return modul;
    }
      
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.datum);
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
        final Klausur other = (Klausur) obj;
        if (!Objects.equals(this.modul, other.modul)) {
            return false;
        }
        if (!Objects.equals(this.datum, other.datum)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return modul + " Klausur am " + datum.toString();
    }
    
}
