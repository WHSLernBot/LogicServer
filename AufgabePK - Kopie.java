package Entitys;

import java.util.Objects;

/**
 *
 * @author Betül
 */
public class AufgabePK {
    
    private Long aufgabenID;
    
    public AufgabePK(){
        
    }
    
    public AufgabePK(Long aufgabenID){
        this.aufgabenID = aufgabenID;
        
    }
     @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.aufgabenID);
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
        final AufgabePK other = (AufgabePK) obj;
        if (!Objects.equals(this.aufgabenID, other.aufgabenID)) {
            return false;
        }
        return true;
    }
    
}
