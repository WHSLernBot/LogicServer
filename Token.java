package Entitys;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

/**
 *
 * @author Seve
 */
@Entity
@IdClass(TokenPK.class)
public class Token implements Serializable {
    
    @Id
    @ManyToOne
    private Aufgabe aufgabe;
    
    @Id
    private String token;
    
    public Token(){
        
    }
   
    public Token(Aufgabe aufgabe, String token){
        
        this.aufgabe = aufgabe;
        this.token = token;
        
    }
    
    
    public void setToken(String token){
        this.token = token;
    }
    
    public String getToken(){
        return token;
    }
    
    public void setAufgabe(Aufgabe aufgabe){
        this.aufgabe = (Aufgabe) aufgabe;
    }
    
    public Aufgabe getAufgabe(){
        return aufgabe;
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