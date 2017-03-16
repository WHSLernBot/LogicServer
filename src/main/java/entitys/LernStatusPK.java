package entitys;

import java.io.Serializable;
import java.util.Objects;

/**
 * Der Primary Key eines LernStatus.
 * @author Seve
 */
public class LernStatusPK implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long benutzer;
    
    private Long thema;

    public LernStatusPK() {}

    public LernStatusPK(Long benutzer, Long thema) {
        this.benutzer = benutzer;
        this.thema = thema;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.benutzer);
        hash = 23 * hash + Objects.hashCode(this.thema);
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
        final LernStatusPK other = (LernStatusPK) obj;
        if (!Objects.equals(this.benutzer, other.benutzer)) {
            return false;
        }
        if (!Objects.equals(this.thema, other.thema)) {
            return false;
        }
        return true;
    }

}
