package Entitys;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;

/**
 * Der Primary Key von Plattform.
 * @author Seve
 */
@Embeddable
public class PlattformPK implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String pfID;
    
    private short pfNr;

    public PlattformPK() {}

    public PlattformPK(String pfID, short pfNr) {
        this.pfID = pfID;
        this.pfNr = pfNr;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.pfID);
        hash = 79 * hash + this.pfNr;
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
        final PlattformPK other = (PlattformPK) obj;
        if (this.pfNr != other.pfNr) {
            return false;
        }
        if (!Objects.equals(this.pfID, other.pfID)) {
            return false;
        }
        return true;
    }
    
}
