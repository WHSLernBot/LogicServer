package Entitys;

import java.util.Objects;

/**
 *
 * @author Betül
 */
public class BenutzerPK {
    
    private Long id;
    private Uni uni;
    private Plattform plattform;
    
    public BenutzerPK(){
        
    }
    
    public BenutzerPK(Long id, Uni uni, Plattform plattform){
        
        this.id = id;
        this.uni = uni;
        this.plattform = plattform;   
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.id);
        hash = 41 * hash + Objects.hashCode(this.uni);
        hash = 41 * hash + Objects.hashCode(this.plattform);
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
        final BenutzerPK other = (BenutzerPK) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.uni, other.uni)) {
            return false;
        }
        if (!Objects.equals(this.plattform, other.plattform)) {
            return false;
        }
        return true;
    }
}
