package Entitys;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;

/**
 *
 * @author Seve
 */
@Embeddable
public class TeilnahmePK implements Serializable {
    
    private Long benutzer;
    
    private KlausurPK klausur;

    public TeilnahmePK() {
    }

    public TeilnahmePK(Long benutzer, KlausurPK klausur) {
        this.benutzer = benutzer;
        this.klausur = klausur;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.benutzer);
        hash = 79 * hash + Objects.hashCode(this.klausur);
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
        final TeilnahmePK other = (TeilnahmePK) obj;
        if (!Objects.equals(this.benutzer, other.benutzer)) {
            return false;
        }
        if (!Objects.equals(this.klausur, other.klausur)) {
            return false;
        }
        return true;
    }

    
}
