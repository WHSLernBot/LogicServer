package entitys;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Ein Token, der ein bestimmtes Stichwort zu einer Aufgabe ist.
 * @author Seve
 */
@Entity
@IdClass(TokenPK.class)
@Table(name="Token")
public class Token implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Die Aufgabe auf die sich das Token bezieht.
     */
    @Id
    @ManyToOne
    private Aufgabe aufgabe;
    
    /**
     * Das Stichwort.
     */
    @Id
    @Column(length=20)
    private String token;
    
    public Token(){}
    
    /**
     * Erzeugt ein neues Objekt dieser Klasse mit den angegebenen Daten.
     * 
     * @param aufgabe Aufgabe, auf die sich das Token bezieht
     * @param token das Stichwort
     */
    public Token(Aufgabe aufgabe, String token){
        this.aufgabe = aufgabe;
        this.token = token;
    }
    
    public String getToken(){
        return token;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.aufgabe);
        hash = 97 * hash + Objects.hashCode(this.token);
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
        final Token other = (Token) obj;
        if (!Objects.equals(this.token, other.token)) {
            return false;
        }
        if (!Objects.equals(this.aufgabe, other.aufgabe)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return token + " Token von " + aufgabe;
    }
    
}
