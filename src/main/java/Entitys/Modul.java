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
    
    public Modul(Uni uni, String kuerzel, String name) {
        this.uni = uni;
        this.kuerzel = kuerzel;
        this.name = name;
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

    public void addThema(Thema thema) {
        this.themen.add(new Thema(this));
    }

    public Collection<Klausur> getKlausuren() {
        return klausuren;
    }

    public void addKlausuren(Klausur klausur) {
        this.klausuren.add(new Klausur(this));
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
