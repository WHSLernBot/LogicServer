package Entitys;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
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
@IdClass(KlausurPK.class)
public class Klausur implements Serializable {
    
    @Id
    @ManyToOne
    private Modul modul;
    
    @Id
    private Date datum;
    
    @ManyToOne
    private Pruefungsperiode pruefungsperiode;
    
    @OneToMany(mappedBy="klausur", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<Teilnahme> teilnahmen;
    
    private String ort;
    
    private Time uhrzeit;
    
    //Time als format??
    private Time dauer;
    
    private int durchschnitt;
    
    private String hilfsmittel;
    
    private int quotient;
    
    public Klausur(){
        
    }
    
    public Klausur(Date datum, Time uhrzeit, Time dauer, String ort,
            int durchschnitt, String hilfsmittel, int quotient){
        
        this.datum = datum;
        this.uhrzeit = uhrzeit;
        this.dauer = dauer;
        this.ort = ort;
        this.durchschnitt = durchschnitt;
        this.hilfsmittel = hilfsmittel;
        this.quotient = quotient;
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
    
    public void setDauer(Time dauer){
        this.dauer = dauer;
    }
    
    public Time getDauer(){
        return dauer;
    }
    
    public void setOrt(String ort){
        this.ort = ort;
    }
    
    public String getOrt(){
        return ort;
    }
    
    public void setDurchschnitt(int durchschnitt){
        this.durchschnitt = durchschnitt;
    }
    
    public int getDurchschnitt(){
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
    
    public int getQuootient(){
        return quotient;
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
