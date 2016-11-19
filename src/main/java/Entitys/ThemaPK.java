/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entitys;

import java.util.Objects;

/**
 *
 * @author Betül
 */
public class ThemaPK {
    
    private Long themenID;
    
    private Modul modul;

    public ThemaPK(Long themenID, Modul modul) {
        this.themenID = themenID;
        this.modul = modul;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.themenID);
        hash = 17 * hash + Objects.hashCode(this.modul);
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
