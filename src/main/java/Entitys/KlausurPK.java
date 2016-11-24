package Entitys;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;

/**
 * Die Primary Key Klasse zu Klausur.
 * 
 * @author Seve
 */
@Embeddable
public class KlausurPK implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private ModulPK modul;
    
    private PruefungsperiodePK periode;

    public KlausurPK() {
    }

    public KlausurPK(ModulPK modul, PruefungsperiodePK periode) {
        this.modul = modul;
        this.periode = periode;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.modul);
        hash = 31 * hash + Objects.hashCode(this.periode);
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
        if (!Objects.equals(this.periode, other.periode)) {
            return false;
        }
        return true;
    }

  
    
}
