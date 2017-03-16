package entitys;

import dao.EMH;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.Table;

/**
 * Diese Klasse stellt ein Modul eines Unistudiengangs dar.
 * @author Seve
 */
@Entity
@IdClass(ModulPK.class)
@Table(name="Modul")
public class Modul implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * Die Uni die dieses Modul anbietet.
     */
    @Id
    @ManyToOne
    private Uni uni;
    
    /**
     * Das Kuerzel des Moduls.
     */
    @Id
    @Column(length = 10)
    private String kuerzel;
    
    /**
     * Der vollstaendige Name des Moduls.
     */
    @Column(length = 50)
    private String name;
    
    /**
     * Die einzelnen Themen des Moduls.
     */
    @OneToMany(mappedBy="modul", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<Thema> themen;
    
    /**
     * Die Klausuren die zu diesem Modul geschrieben werden.
     */
    @OneToMany(mappedBy="modul", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<Klausur> klausuren;
    
    /**
     * Die statistiken ueber dieses Modul, die zeigen wie sehr sich die Zeit
     * auf die einzelnen Punktzahlen einer Aufgabe auswirkt.
     */
    @OneToMany(mappedBy="modul", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<Statistik> statistiken;

    public Modul(){}
    
    public Modul(Uni uni, String kuerzel, String name) {
        this.uni = uni;
        this.kuerzel = kuerzel;
        this.name = name;
    }
   
    public String getKuerzel(){
        return kuerzel;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }

    public Collection<Thema> getThemen() {
        Collection<Thema> result;
        
        String sql = "select object(t) from Thema t "
                + "where MODUL_KUERZEL = :KRZ and MODUL_UNI_ID = :UID";
        
        Query q = EMH.getEntityManager().createQuery(sql);
        
        q.setParameter("KRZ", this.kuerzel);
        q.setParameter("UID", this.uni.getId());
        
        result = q.getResultList();
        
        return result;
    }

//    public Collection<Klausur> getKlausuren() {
//        return klausuren;
//    }

    public Collection<Statistik> getStatistiken() {
        Collection<Statistik> result;
        
        String sql = "select object(s) from Statistik s "
                + "where MODUL_KUERZEL = :KRZ and MODUL_UNI_ID = :UID";
        
        Query q = EMH.getEntityManager().createQuery(sql);
        
        q.setParameter("KRZ", this.kuerzel);
        q.setParameter("UID", this.uni.getId());
        
        result = q.getResultList();
        
        return result;
    }

    public Uni getUni() {
        return uni;
    } 
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.uni);
        hash = 67 * hash + Objects.hashCode(this.kuerzel);
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
        final Modul other = (Modul) obj;
        if (!Objects.equals(this.kuerzel, other.kuerzel)) {
            return false;
        }
        if (!Objects.equals(this.uni, other.uni)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Fach: " + kuerzel;
    }
    
}
