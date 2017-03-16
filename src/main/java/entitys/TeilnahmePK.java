package entitys;

import java.io.Serializable;
import java.util.Objects;

/**
 * Primary Key von Teilnahme.
 * @author Seve
 */
public class TeilnahmePK implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long benutzer;
    
    private Long id;

    public TeilnahmePK() {}

    public TeilnahmePK(Long benutzer, Long id) {
        this.benutzer = benutzer;
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.benutzer);
        hash = 53 * hash + Objects.hashCode(this.id);
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
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    
}