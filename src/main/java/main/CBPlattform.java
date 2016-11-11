package main;

import java.util.Objects;

/**
 *
 * @author Seve
 */
public class CBPlattform {
    
    private final Long id;
    
    private final int plattform;

    public CBPlattform(Long id, int plattform) {
        this.id = id;
        this.plattform = plattform;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
        hash = 67 * hash + this.plattform;
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
        final CBPlattform other = (CBPlattform) obj;
        if (this.plattform != other.plattform) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    public Long getId() {
        return id;
    }

    public int getPlattform() {
        return plattform;
    }
    
    
    
}
