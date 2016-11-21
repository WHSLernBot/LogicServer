package Entitys;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Seve
 */
public class TokenPK implements Serializable {
    
    private Long aufgabenID;
    
    private String token;

    public TokenPK() {
    }

    public TokenPK(Long aufgabe, String token) {
        this.aufgabenID = aufgabe;
        this.token = token;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.aufgabenID);
        hash = 23 * hash + Objects.hashCode(this.token);
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
        final TokenPK other = (TokenPK) obj;
        if (!Objects.equals(this.token, other.token)) {
            return false;
        }
        if (!Objects.equals(this.aufgabenID, other.aufgabenID)) {
            return false;
        }
        return true;
    }

    
}
