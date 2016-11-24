package Entitys;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * Diese Klasse stellt ein Modul eines Unistudiengangs dar.
 * @author Seve
 */
@Entity
@IdClass(ModulPK.class)
public class Modul implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * Die Uni die dieses Modul anbietet.
     */
    @Id
    @ManyToOne
    private Uni uni;
    
    /**
     * Das Kürzel des Moduls.
     */
    @Id
    @Column(length = 10)
    private String kuerzel;
    
    /**
     * Der vollständige Name des Moduls.
     */
    @Column(length = 50)
    private String name;
    
    /**
     * Die einzelnen Themen des Moduls.
     */
    @OneToMany(mappedBy="modul", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<Thema> themen;
    
    /**
     * Die Klausuren die zu diesem Modul geschrieben werden.
     */
    @OneToMany(mappedBy="modul", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<Klausur> klausuren;
    
    /**
     * Die statistiken über dieses Modul, die zeigen wie sehr sich die Zeit
     * auf die einzelnen Punktzahlen einer Aufgabe auswirkt.
     */
    @OneToMany(mappedBy="modul", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<Statistik> statistiken;

    public Modul(){}
    
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

    public void addThema(String name) {
        this.themen.add(new Thema(this,name,20)); //anteil dynamisch bestimmen
    }

    public Collection<Klausur> getKlausuren() {
        return klausuren;
    }

    public void addKlausur(Pruefungsperiode pruefungsperiode, Date datum, Time uhrzeit, short dauer, String ort, String hilfsmittel) {
        this.klausuren.add(new Klausur(this,pruefungsperiode,datum,uhrzeit,dauer,ort,hilfsmittel));
    }

    public Collection<Statistik> getStatistiken() {
        return statistiken;
    }

    public void addStatistik(short woche,int anteil) {
        this.statistiken.add(new Statistik(this,woche,anteil));
    }

    public Uni getUni() {
        return uni;
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
