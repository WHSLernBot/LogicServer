package Entitys;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Seve
 */
public class PlattformPK implements Serializable {
    
    private String pfID;
    
    private int pfNr;

    public PlattformPK() {
    }

    public PlattformPK(String pfID, int pfNr) {
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
