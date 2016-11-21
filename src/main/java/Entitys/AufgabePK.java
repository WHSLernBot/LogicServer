package Entitys;

import java.util.Objects;

/**
 *
 * @author Betül
 */
public class AufgabePK {
    private Long aufgabenID;
    
    private Long themenID;

    public AufgabePK(Long aufgabenID, Long themenID) {
        this.aufgabenID = aufgabenID;
        this.themenID = themenID;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.aufgabenID);
        hash = 37 * hash + Objects.hashCode(this.themenID);
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
        final AufgabePK other = (AufgabePK) obj;
        if (!Objects.equals(this.aufgabenID, other.aufgabenID)) {
            return false;
        }
        if (!Objects.equals(this.themenID, other.themenID)) {
            return false;
        }
        return true;
    }
    
    
}
