package Entitys;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Seve
 */
@Entity
@IdClass(ModulPK.class)
public class Modul implements Serializable {

    @Id
    @ManyToOne
    private Uni uni;
    
    @Id
    private String kuerzel;
    
    private String name;
    
    @OneToMany(mappedBy="modul", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<Thema> themen;
    
    @OneToMany(mappedBy="modul", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<Klausur> klausuren;
    
    @OneToMany(mappedBy="modul", cascade=CascadeType.ALL,orphanRemoval = true)
    private Statistik statistik;

    
    public Modul(){
        
    }
    
    public Modul(String kuerzel, String name){
        this.kuerzel = kuerzel;
        this.name = name;
    }
    
    public void setKuerzel(String kuerzel){
        this.kuerzel = kuerzel;
    }
    
    public String getKuerzel(){
        return kuerzel;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }

    public Collection<Thema> getThemen() {
        return themen;
    }

    public void setThemen(Collection<Thema> themen) {
        this.themen = themen;
    }

    public Collection<Klausur> getKlausuren() {
        return klausuren;
    }

    public void setKlausuren(Collection<Klausur> klausuren) {
        this.klausuren = klausuren;
    }
    
    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.uni);
        hash = 67 * hash + Objects.hashCode(this.kuerzel);
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
        final Modul other = (Modul) obj;
        if (!Objects.equals(this.kuerzel, other.kuerzel)) {
            return false;
        }
        if (!Objects.equals(this.uni, other.uni)) {
            return false;
        }
        return true;
    }

    


    @Override
    public String toString() {
        return "Fach: " + kuerzel;
    }
    
}
