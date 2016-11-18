 package Entitys;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Seve
 */
public class AntwortPK implements Serializable {
    
    private Long aufgabe;
    
    private int nummer;

    public AntwortPK() {
    
    }
    
    public AntwortPK(Long aufgabe, int nummer){
        
        this.aufgabe = aufgabe;
        this.nummer = nummer;
        
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.aufgabe);
        hash = 41 * hash + Objects.hashCode(this.nummer);
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
        final AntwortPK other = (AntwortPK) obj;
        if (!Objects.equals(this.aufgabe, other.aufgabe)) {
            return false;
        }
        if (!Objects.equals(this.nummer, other.nummer)) {
            return false;
        }
        return true;
    }

   
}
