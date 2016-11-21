package Entitys;

import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
//@IdClass(BenutzerPK.class)
public class Benutzer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String name;
    
    @ManyToOne
    private Uni uni;
    
    private Date letzteAntwort;
    
    private Boolean datenschutz;
    
    @OneToMany(mappedBy="benutzer", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<LernStatus> lernStadi;
    
    @OneToMany(mappedBy="benutzer" , cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<Teilnahme> teilnahmen;
    
    @OneToOne(mappedBy="benutzer", cascade=CascadeType.ALL,orphanRemoval = true)
    private Plattform plattform;
    
    public Benutzer(){
        
    }
    
    public Benutzer(String pfID, int pfNr, String witSession,String name, Date letzteAntwort){
        this.name = name;
        this.letzteAntwort = letzteAntwort;
        plattform = new Plattform(pfID,pfNr,witSession);
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
    
    public LernStatus getStatus(Long id) {
        
        LernStatus ls = null;
        
        for(LernStatus l : lernStadi) {
            if(l.getThema().getId() == id) {
                ls = l;
                break;
            }
        }
        return ls;
    }

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
