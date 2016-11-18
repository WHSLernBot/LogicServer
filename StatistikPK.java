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
public class StatistikPK {
    
    private Modul modul;
    private int woche;
    
    public StatistikPK(){
        
    }
    
    public StatistikPK(Modul modul, int woche){
        this.modul = modul;
        this.woche = woche;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.modul);
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
        final StatistikPK other = (StatistikPK) obj;
        if (!Objects.equals(this.modul, other.modul)) {
            return false;
        }
        if (!Objects.equals(this.woche, other.woche)) {
            return false;
        }
        return true;
    }
    
}
