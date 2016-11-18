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
@IdClass(BenutzerPK.class)
public class Benutzer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String name;
    
    @Id
    @ManyToOne
    private Uni uni;
    
    private Date letzteAntwort;
    
    private Boolean datenschutz;
    
    @OneToMany(mappedBy="benutzer", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<LernStatus> lernStadi;
    
    @OneToMany(mappedBy="benutzer" , cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<Teilnahme> teilnahmen;
    
    @Id
    @OneToOne(mappedBy="benutzer")
    private Plattform plattform;

    
    public Benutzer(){
        
    }
    
    public Benutzer(Long id, String name, Date letzteAntwort, Boolean datenschutz){
        this.id = id;
        this.name = name;
        this.letzteAntwort = letzteAntwort;
        this.datenschutz = datenschutz;
    }
    
   

    public void setId(Long id){
        this.id = id;
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
