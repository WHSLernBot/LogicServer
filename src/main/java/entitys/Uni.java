package entitys;

import dao.EMH;
import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.Table;

/**
 * Diese Klasse stellt eine Universitaet und einen dort angesiedelten Fachbereich dar.
 * @author Seve
 */
@Entity
@Table(name="Uni")
public class Uni implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * Eideutige id der Uni.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private short id;
    
    /**
     * Name der Universitaet - Name des Fachbereichs.
     */
    @Column(length = 200)
    private String name;
    
    /**
     * Password fuer die Webseite
     */
    @Column(length = 200)
    private String password;
    
    /**
     * Benutzer die diese Uni/Fachbereich besuchen.
     */
    @OneToMany(mappedBy="uni", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<Benutzer> benutzer;
    
    /**
     * Module die dieser Fachbereich anbietet.
     */
    @OneToMany(mappedBy="uni", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<Modul> modul;
    
    /**
     * Pruefungsperioden die dieser Fachbereich hat.
     */
    @OneToMany(mappedBy="uni", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<Pruefungsperiode> pruefungsperiode;
    
    public Uni() {}
    
    public Uni(String name) {
        this.name = name;
        this.password = "projekt";
    }

    public short getId() {
        return id;
    }

    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Benutzer> getBenutzer() {
        Collection<Benutzer> result;
        
        String sql = "select object(b) from Benutzer b "
                + "where UNI_ID = :UID";
        
        Query q = EMH.getEntityManager().createQuery(sql);
        
        q.setParameter("UID", this.id);
        
        result = q.getResultList();
        
        return result;
    }

    public void addBenutzer(Benutzer benutzer) {
        this.benutzer.add(benutzer);
    }

    public Collection<Modul> getModul() {
        Collection<Modul> result;
        
        String sql = "select object(m) from Modul m "
                + "where UNI_ID = :UID";
        
        Query q = EMH.getEntityManager().createQuery(sql);
        
        q.setParameter("UID", this.id);
        
        result = q.getResultList();
        
        return result;
    }

//    public void addModul(String kuerzel, String name) {
//        this.modul.add(new Modul(this,kuerzel,name));
//    }

    public Collection<Pruefungsperiode> getPruefungsperiode() {
        Collection<Pruefungsperiode> result;
        
        String sql = "select object(p) from Pruefungsperiode p "
                + "where UNI_ID = :UID";
        
        Query q = EMH.getEntityManager().createQuery(sql);
        
        q.setParameter("UID", this.id);
        
        result = q.getResultList();
        
        return result;
    }

//    public void addPruefungsperiode(short jahr, short phase, Date anmeldebeginn, Date anfang, Date ende) {
//        this.pruefungsperiode.add(new Pruefungsperiode(this,jahr,phase,anmeldebeginn,anfang,ende));
//    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.id;
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
        final Uni other = (Uni) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entitys.Uni[ id=" + id + " ]";
    }
    
}
