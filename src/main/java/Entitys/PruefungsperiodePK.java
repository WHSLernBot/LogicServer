package Entitys;

import java.io.Serializable;
import java.util.Objects;

/**
 * Primary Key Klasse zu Pruefungsperiode.
 * @author Betuel
 */
public class PruefungsperiodePK implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private short jahr;
   
    private short uni;
    
    private short phase;

    public PruefungsperiodePK() {}
    
    public PruefungsperiodePK(short jahr, short uni, short phase) {
        this.jahr = jahr;
        this.uni = uni;
        this.phase = phase;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.jahr;
        hash = 71 * hash + Objects.hashCode(this.uni);
        hash = 71 * hash + this.phase;
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
        final PruefungsperiodePK other = (PruefungsperiodePK) obj;
        if (this.jahr != other.jahr) {
            return false;
        }
        if (this.phase != other.phase) {
            return false;
        }
        if (!Objects.equals(this.uni, other.uni)) {
            return false;
        }
        return true;
    }

    
    
}
