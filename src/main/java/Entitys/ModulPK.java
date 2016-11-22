package Entitys;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;

/**
 *
 * @author Seve
 */
@Embeddable
public class ModulPK implements Serializable {
    
    private short uni;
    
    private String kuerzel;

    public ModulPK(short uni, String kuerzel) {
        this.uni = uni;
        this.kuerzel = kuerzel;
    }

    public ModulPK() {
    }

    public short getUni() {
        return uni;
    }

    public String getKuerzel() {
        return kuerzel;
    }
    
    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.uni);
        hash = 59 * hash + Objects.hashCode(this.kuerzel);
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
        final ModulPK other = (ModulPK) obj;
        if (!Objects.equals(this.kuerzel, other.kuerzel)) {
            return false;
        }
        if (!Objects.equals(this.uni, other.uni)) {
            return false;
        }
        return true;
    }
 
    
}
