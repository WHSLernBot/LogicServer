package entitys;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;

/**
 * Primary Key zu Statistik.
 * @author Betuel
 */
@Embeddable
public class StatistikPK implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private ModulPK modul; // oder nur Modul ??
    
    private short woche;

    public StatistikPK() {}
    
    public StatistikPK(ModulPK modul, short woche) {
        this.modul = modul;
        this.woche = woche;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.modul);
        hash = 97 * hash + this.woche;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StatistikPK other = (StatistikPK) obj;
        if (!Objects.equals(this.modul, other.modul)) {
            return false;
        }
        if (this.woche != other.woche) {
            return false;
        }
        return true;
    }
    
    
    
}
