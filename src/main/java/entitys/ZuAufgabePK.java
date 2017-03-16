package entitys;

import java.io.Serializable;
import java.util.Objects;

/**
 * Primary Key Klasse von ZuAufgabe.
 * @author Betuel
 */
public class ZuAufgabePK implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private LernStatusPK lernStatus;
    
    private int kennung;

    public ZuAufgabePK() {}

    public ZuAufgabePK(LernStatusPK lernStatus, int kennung) {
        this.lernStatus = lernStatus;
        this.kennung = kennung;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.lernStatus);
        hash = 61 * hash + this.kennung;
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
        final ZuAufgabePK other = (ZuAufgabePK) obj;
        if (this.kennung != other.kennung) {
            return false;
        }
        if (!Objects.equals(this.lernStatus, other.lernStatus)) {
            return false;
        }
        return true;
    }

    
}
