package Entitys;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Diese Klasse beschreibt die Adresse einer Plattform.
 * 
 * @author Seve
 */
@Entity
@Table(name="Adresse")
public class Adresse implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private short id;
    
    @Column(length = 2000)
    private String adresse;

    public Adresse() {
    }

    public Adresse(short id, String adresse) {
        this.id = id;
        this.adresse = adresse;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    public short getId() {
        return id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this.id;
        hash = 37 * hash + Objects.hashCode(this.adresse);
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
        final Adresse other = (Adresse) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.adresse, other.adresse)) {
            return false;
        }
        return true;
    }

    
    
    @Override
    public String toString() {
        return "Entitys.Adresse[ id=" + id + " ]";
    }
    
}
