package Entitys;

import java.util.Objects;

/**
 *
 * @author Betül
 */
public class ThemaPK {
    
    private Long themenID;
    private Modul modul;
    
    public ThemaPK(){
        
    }
    
    public ThemaPK(Long themenID, Modul modul){
        this.themenID = themenID;
        this.modul = modul;
        
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.themenID);
        hash = 41 * hash + Objects.hashCode(this.modul);
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
        final ThemaPK other = (ThemaPK) obj;
        if (!Objects.equals(this.themenID, other.themenID)) {
            return false;
        }
        if (!Objects.equals(this.modul, other.modul)) {
            return false;
        }
        return true;
    }
    
}
