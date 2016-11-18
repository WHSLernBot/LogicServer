package Entitys;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Seve
 */
@Entity
public class Uni implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String name;
    
    @OneToMany(mappedBy="uni", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<Benutzer> benutzer;
    
    @OneToMany(mappedBy="uni", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<Modul> modul;
    
    @OneToMany(mappedBy="uni", cascade=CascadeType.ALL,orphanRemoval = true)
    private Pruefungsperiode pruefungsperiode;
    
    
    public Uni(){
        
    }
    
    public Uni(Long id, String name){
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
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
        if (!(object instanceof Uni)) {
            return false;
        }
        Uni other = (Uni) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entitys.Uni[ id=" + id + " ]";
    }
    
}
