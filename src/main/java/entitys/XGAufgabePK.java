package entitys;

import java.io.Serializable;
import java.util.Objects;

/**
 * Primary Key Klasse von XGAufgabe.
 * @author Betuel
 */
public class XGAufgabePK implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private LernStatusPK lernStatus;
    
    private int kennung;

    public XGAufgabePK() {}

    public XGAufgabePK(LernStatusPK lernStatus, int kennung) {
        this.lernStatus = lernStatus;
        this.kennung = kennung;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.lernStatus);
        hash = 97 * hash + this.kennung;
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
        final XGAufgabePK other = (XGAufgabePK) obj;
        if (this.kennung != other.kennung) {
            return false;
        }
        if (!Objects.equals(this.lernStatus, other.lernStatus)) {
            return false;
        }
        return true;
    }
    
  
}
