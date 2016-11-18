package Entitys;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Betül
 */
public class PruefungsperiodePK implements Serializable{
    
    
    private int jahr;
    
    private Uni uni;
    
   public PruefungsperiodePK(){
       
   } 
   
   public PruefungsperiodePK(int jahr, Uni uni){
       this.jahr = jahr;
       this.uni = uni;
   }
   
   @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.uni);
        hash = 59 * hash + Objects.hashCode(this.jahr);
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
        if (!Objects.equals(this.jahr, other.jahr)) {
            return false;
        }
        if (!Objects.equals(this.uni, other.uni)) {
            return false;
        }
        return true;
    }
    
}
