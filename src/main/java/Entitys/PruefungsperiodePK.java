package Entitys;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Betül
 */
public class PruefungsperiodePK implements Serializable{
    
    private Long uni;
    private Klausur klausur;
    
    public PruefungsperiodePK(Long uni, Klausur klausur){
        this.uni = uni;
        this.klausur = klausur;
    }
    
    public PruefungsperiodePK(){
        
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.uni);
        hash = 59 * hash + Objects.hashCode(this.klausur);
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
        if (!Objects.equals(this.klausur, other.klausur)) {
            return false;
        }
        if (!Objects.equals(this.uni, other.uni)) {
            return false;
        }
        return true;
    }
    
}
