package main;

import java.util.Objects;

/**
 * Diese Klasse stellt eine Plattform dar, die die Plattformspezifischen 
 * Informationen speichert, um diese auf einen CBBenutzer zu mappen.
 * @author Seve
 */
public class CBPlattform {
    
    private final String id;
    
    private final short plattform;

    /**
     * Erstellt eine neue CBPlattoform.
     * 
     * @param id Die id des Benutzers auf der entsprechenden Plattform.
     * @param plattform Die id der Plattfom in der Datenbank.
     */
    public CBPlattform(String id, short plattform) {
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

    public String getId() {
        return id;
    }

    public short getPlattform() {
        return plattform;
    }
    
    
    
}
