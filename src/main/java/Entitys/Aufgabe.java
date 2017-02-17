package Entitys;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * Diese Klasse stellt eine Aufgabe zu einem bestimmten Thema dar.
 * 
 * @author Seve
 */
@Entity
public class Aufgabe implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * Eindeutige Nummer der Antwort.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //schlau machen
    private Long aufgabenID;
    
    /**
     * Das Thema, zu dem die Antwort gehoehrt.
     */
    @ManyToOne
    private Thema thema;
    
    /**
     * Die Fragestellung.
     */
    @Column(length = 2000)
    private String frage;
    
    /**
     * Der Schwiereigkeitsgrad der Aufgabe.
     */
    private int schwierigkeit;
    
    /**
     * Ein Hinweistext zur Loesung der Aufgabe.
     */
    @Column(length = 2000)
    private String hinweis;
    
    /**
     * Die Bewertung einer Aufgabe. Falls dieser Wert bei 0 liegt,
     * muss die Aufgabe geloescht oder geprueft werden.
     */
    private int bewertung;
    
    /**
     * Ein Verweis, wo man etwas nachlesen kann, falls man die Aufgabe nicht
     * beantworten kann.
     */
    @Column(length = 1000)
    private String verweis;
    
    /**
     * Die Punktzahl die die Aufgabe gibt. So wichtiger die Aufgabe fuer eine
     * Klausur, desto hoeher auch dieser Wert.
     */
    private int punkte;
    
    /**
     * Die Anzahl der bisherigen Antworten.
     */
    private short anzAntworten;
    
    /**
     * Die Antworten zu dieser Frage.
     */
    @OneToMany(mappedBy="aufgabe", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<Antwort> antworten;
    
    /**
     * Die Token, die Begriffe zu dieser Frage enthalten.
     */
    @OneToMany(mappedBy="aufgabe", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<Token> token;
    
    /**
     * Alle Bewertungen der Aufgabe.
     */
    @OneToMany(mappedBy="aufgabe", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<Bewertung> bewertungen;
    
    @OneToMany(mappedBy="aufgabe", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<BeAufgabe> bearbeitet;

    public Aufgabe(){    
    }
    
    /**
     * 
     * @param thema Das zugehoerige Thema der Frage.
     * @param frage Der Fragetext.
     * @param schwierigkeit Der schwierigkeitsgrad.
     * @param hinweis Ein Hinweistext.
     * @param verweis Ein Verweistext zum nachlesen.
     */
    public Aufgabe(Thema thema, String frage, int schwierigkeit, String hinweis, String verweis){
        
        this.thema = thema;
        this.frage = frage;
        this.schwierigkeit = schwierigkeit; //??
        this.hinweis = hinweis;
        this.bewertung = 10;
        this.verweis = verweis;
        this.punkte = 100;
        this.anzAntworten = 0;
    }
    
    /**
     * 
     * @return Alle Antwortmoeglichkeiten dieser Frage.
     */
    public Collection<Antwort> getAntworten(){
        return this.antworten;
    }
    
    /**
     * 
     * @return Alle Token der Aufgabe.
     */
    public Collection<Token> getToken(){
        return this.token;
    }
    
    public long getAufgabenID(){
        return aufgabenID;
    }
    
    public void setFrage(String frage){
        this.frage = frage;
    }
    
    public String getFrage(){
        return frage;
    }
    
    public void setSchwierigkeit(int schwierigkeit){
        this.schwierigkeit = schwierigkeit;
    }
    
    public int getSchwierigkeit(){
        return schwierigkeit;
    }
    
    public void setHinweis(String hinweis){
        this.hinweis = hinweis;
    }
    
    public String getHinweis(){
        return hinweis;
    }
    
    public void positivBewertet(){
        this.bewertung++;
    } 
    
    public void negativBewertet() {
        this.bewertung--;
    }
    
    public int getBewertung(){
        return bewertung;
    } 
    
    public void setVerweis(String verweis){
        this.verweis = verweis;
    }
    
    public String getVerweis(){
        return verweis;
    }

    public void setPunkte(int punkte){
        this.punkte = punkte;
    }
    
    public int getPunkte(){
        return punkte;
    }

    public Thema getThema() {
        return thema;
    }

    public short getAnzAntworten() {
        return anzAntworten;
    }
    
    public void addAntwort() {
        anzAntworten++;
    }

    public Collection<Bewertung> getBewertungen() {
        return bewertungen;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.aufgabenID);
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
        final Aufgabe other = (Aufgabe) obj;
        if (!Objects.equals(this.aufgabenID, other.aufgabenID)) {
            return false;
        }
        return true;
    }

    
    @Override
    public String toString() {
        return frage + " " + aufgabenID;
    }
    
}
