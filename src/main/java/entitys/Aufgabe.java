package entitys;

import dao.EMH;
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
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Diese Klasse stellt eine Aufgabe zu einem bestimmten Thema dar.
 * 
 * @author Seve
 */
@Entity
@Table(name="Aufgabe")
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
    
    /**
     * Alle bearbeiteten Aufgaben.
     */
    @OneToMany(mappedBy="aufgabe", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<BeAufgabe> bearbeitet;

    @Transient
    private int bekennung;
    
    public Aufgabe(){    
    }
    
    /**
     * Erzeugt ein neues Objekt dieser Klasse mit den angegebenen Daten.
     * 
     * @param thema Das zugehoerige Thema der Aufgabe.
     * @param frage Der Fragetext.
     * @param schwierigkeit Der schwierigkeitsgrad.
     * @param hinweis Der Hinweistext.
     * @param verweis Der Verweistext zum nachlesen.
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
        this.bekennung = -1;
    }
    
    /**
     * Liefert alle Antwortmoeglichkeiten zu der Aufgabe.
     * 
     * @return Alle Antwortmoeglichkeiten dieser Frage.
     */
    public Collection<Antwort> getAntworten(){
        Collection<Antwort> result;
        
        String sql = "select object(a) from Antwort a "
                + "where AUFGABE_AUFGABENID = :AID";
        
        Query q = EMH.getEntityManager().createQuery(sql);
        
        q.setParameter("AID", this.aufgabenID);
        
        result = q.getResultList();
        
        return result;
    }

    public int getBekennung() {
        return bekennung;
    }

    public void setBekennung(int bekennung) {
        this.bekennung = bekennung;
    }
    
    /**
     * 
     * @return Alle Token der Aufgabe.
     */
//    public Collection<Token> getToken(){
//        return this.token;
//    }
    
    
    /**
     * Liefert die ID der Aufgabe.
     * 
     * @return ID der Aufgabe
     */
    public long getAufgabenID(){
        return aufgabenID;
    }
    
    /**
     * Setzt die Frage zu der Aufgabe.
     * 
     * @param frage, die gesetzt werden soll
     */
    public void setFrage(String frage){
        this.frage = frage;
    }
    
    /**
     * Liefert die Frage.
     * 
     * @return Frage, die gestellt wird. 
     */
    public String getFrage(){
        return frage;
    }
    
    /**
     * Setzt einen Wert, wie schwer eine Aufgabe ist.
     * 
     * @param schwierigkeit 
     */
    public void setSchwierigkeit(int schwierigkeit){
        this.schwierigkeit = schwierigkeit;
    }
    
    /**
     * Liefert den Schwierigkeitsgrad der Aufgabe.
     * 
     * @return schwierigkeit
     */
    public int getSchwierigkeit(){
        return schwierigkeit;
    }
    
    /**
     * Setzt einen Hinweistext zu der Aufgabe.
     * 
     * @param hinweis Der Hinweistext
     */
    public void setHinweis(String hinweis){
        this.hinweis = hinweis;
    }
    
    /**
     * Gibt einen Hinweistext zurueck.
     * 
     * @return Hinweistext
     */
    public String getHinweis(){
        return hinweis;
    }
    
    /**
     *  Erhoeht den Wert, wenn die Aufgabe positiv bewertet wird.
     * 
     */
    public void positivBewertet(){
        this.bewertung++;
    } 
    
    /**
     * Erniedrigt den Wert, wenn die Aufgabe negativ bewertet wird.
     */
    public void negativBewertet() {
        this.bewertung--;
    }
    
    /**
     * Gibt eine Bewertung fuer die Augabe zurueck.
     * 
     * @return Die Bewertung
     */
    public int getBewertung(){
        return bewertung;
    } 
    
    /**
     * Setzt Verweise zu der Aufgabe.
     * 
     * @param verweis Der Verweistext
     */
    public void setVerweis(String verweis){
        this.verweis = verweis;
    }
    
    /**
     * Gibt einen Verweis zu der Aufgabe zurueck.
     * 
     * @return der Verweistext
     */
    public String getVerweis(){
        return verweis;
    }

    /**
     * Setzt Punkte zu der Aufgabe. 
     * 
     * @param punkte 
     */
    public void setPunkte(int punkte){
        this.punkte = punkte;
    }
    
    /**
     * Gibt die Punktzahl der Aufgabe zuruek.
     * 
     * @return Die Punkte
     */
    public int getPunkte(){
        return punkte;
    }

    /**
     * Liefert das Thema zu dem der Aufgabe gestellt werden soll.
     * 
     * @return Thema der Aufgabe
     */
    public Thema getThema() {
        return thema;
    }

    /**
     * Gibt die Anzhalt der beantworteten Aufgaben zurueck.
     * 
     * @return Anzahl der Antworten
     */
    public short getAnzAntworten() {
        return anzAntworten;
    }
    
    /**
     * Erhoeht die Anzahl der Antworten, wenn eine Antwort hinzugeguegt wird.
     */
    public void addAntwort() {
        anzAntworten++;
    }

//    public Collection<Bewertung> getBewertungen() {
//        return bewertungen;
//    }
    
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
