package Entitys;

import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 * Diese Klasse stellt einen Benutzer des ChatBots dar.
 * 
 * @author Seve
 */
@Entity
public class Benutzer implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * Die eindeutige id des Benutzers.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    /**
     * Der Name des Benutzers, wie er angesprochen werden moechte.
     */
    @Column(length = 30)
    private String name;
    
    /**
     * Die entsprechende Universitaet auf die der Benutzer geht.
     */
    @ManyToOne
    private Uni uni;
    
    /**
     * Das Datum der letzten Antwort.
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date letzteAntwort;
    
    /**
     * Falls true moechte der Benutzer keine Daten ueber sich angeben.
     */
    private Boolean datenschutz;
    
    /**
     * Die entsprechenden Lern Stadi die der Benutzer zu jedem Thema 
     * eines Fachs besitzt.
     */
    @OneToMany(mappedBy="benutzer", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<LernStatus> lernStadi;
    
    /**
     * Die Klausuren an den der Benutzer teilnimmt.
     */
    @OneToMany(mappedBy="benutzer" , cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<Teilnahme> teilnahmen;
    
    /**
     * Die Plattforminformationen des Benutzers.
     */
    @OneToOne(mappedBy="benutzer", cascade=CascadeType.ALL,orphanRemoval = true)
    private Plattform plattform;
    
    public Benutzer(){
        
    }
    
    /**
     * Erstellt einen neuen Benutzer.
     * 
     * @param pfID Plattform id des Benutzers.
     * @param pfNr Plattform Nummer. Fuer weitere infos siehe Plattform.
     * @param witSession Die Wit.ai session (Falls immer gleich)
     * @param name Name des Benutzers.
     * @param letzteAntwort Datum der letzten Antwort (hier erstelldatum).
     */
    public Benutzer(String pfID, short pfNr, String witSession,String name, Date letzteAntwort){
        this.name = name;
        this.letzteAntwort = letzteAntwort;
        plattform = new Plattform(pfID,pfNr,this,witSession);
    }
    
    
    public Long getId(){
        return id;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
    
    public void setLetzteAntwort(Date letzteAntwort){
        this.letzteAntwort = letzteAntwort;
    }
    
    public Date getLetzteAntwort(){
        return letzteAntwort;
    }

    public Boolean istDatenschutz() {
        return datenschutz;
    }

    public void setDatenschutz(Boolean datenschutz) {
        this.datenschutz = datenschutz;
    }
    

    public Collection<LernStatus> getLernStadi() {
        return lernStadi;
    }
    
    /**
     * 
     * @param id Themen id des entsprechenden Status.
     * @return Der entsprechende LernStatus zum Thema.
     */
    public LernStatus getStatus(long id) {
        
        LernStatus ls = null;
        
        for(LernStatus l : lernStadi) {
            if(l.getThema().getId() == id) {
                ls = l;
                break;
            }
        }
        return ls;
    }

    /**
     * Fuegt einen neuen Lernstatus hinzu.
     * 
     * @param thema
     * @param datum 
     */
    public void addLernStadi(Thema thema,Date datum) {
        this.lernStadi.add(new LernStatus(this,thema,datum));
    }

    public Collection<Teilnahme> getTeilnahmen() {
        return teilnahmen;
    }

    public void addTeilnahmen(Klausur klausur) {
        this.teilnahmen.add(new Teilnahme(this,klausur));
    }

    public Plattform getPlattform() {
        return plattform;
    }

    public Uni getUni() {
        return uni;
    }

    public void setUni(Uni uni) {
        this.uni = uni;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Benutzer)) {
            return false;
        }
        Benutzer other = (Benutzer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Benutzer: " + name;
    }
    
}
