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
    
    
    public AntwortPK(Long aufgabe, int nummer) {
        this.aufgabe = aufgabe;
        this.nummer = nummer;
    }
    

    public AntwortPK() {
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.aufgabe);
        hash = 89 * hash + this.nummer;
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
        final AntwortPK other = (AntwortPK) obj;
        if (!Objects.equals(this.aufgabe, other.aufgabe)) {
            return false;
        }
        if (this.nummer != other.nummer) {
            return false;
        }
        return true;
    }

    

   
}
