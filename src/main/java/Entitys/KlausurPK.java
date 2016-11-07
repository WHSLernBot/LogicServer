package Entitys;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;
import javax.persistence.Embeddable;

/**
 *
 * @author Seve
 */
@Embeddable
public class KlausurPK implements Serializable {
    
    private ModulPK modul;
    
    private Date datum;

    public KlausurPK() {
    }

    public KlausurPK(ModulPK modul, Date datum) {
        this.modul = modul;
        this.datum = datum;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.modul);
        hash = 31 * hash + Objects.hashCode(this.datum);
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
        final KlausurPK other = (KlausurPK) obj;
        if (!Objects.equals(this.modul, other.modul)) {
            return false;
        }
        if (!Objects.equals(this.datum, other.datum)) {
            return false;
        }
        return true;
    }

  
    
}
