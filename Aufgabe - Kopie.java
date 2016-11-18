package Entitys;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Seve
 */
@Entity
@IdClass(AufgabePK.class)
public class Aufgabe implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //schlau machen
    private Long aufgabenID;
    
    @ManyToOne
    private Thema thema;
    
    private String frage;
    
    @OneToMany(mappedBy="aufgabe", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<BeAufgabe> bearbeiteteAufgaben;
    
    private int schwierigkeit;
    
    private String hinweis;
    
    private int like;
    
    private String verweis;
    
    private int punkte;
    
    @OneToMany(mappedBy="aufgabe", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<Antwort> antworten;
    
    @OneToMany(mappedBy="aufgabe", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<Token> token;
    
    
    @OneToMany(mappedBy="aufgabe", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<ZuAufgabe> zuAufgabe;
    
    @OneToMany(mappedBy="aufgabe", cascade=CascadeType.ALL,orphanRemoval = true)
    private Collection<XGAufgabe> xgAufgabe;
        
    public Aufgabe(){
        
        this.bearbeiteteAufgaben = new HashSet<>();
        this.antworten = new HashSet<>();
        this.token = new HashSet<>();
        this.zuAufgabe = new HashSet<>();
        this.xgAufgabe = new HashSet<>();
        
        
    }
    
    public Aufgabe(Long aufgabenID, String frage, int schwierigkeit, String hinweis,
            int like, String verweis, int punkte, Thema thema){
        this.aufgabenID = aufgabenID;
        this.frage = frage;
        this.schwierigkeit = schwierigkeit;
        this.hinweis = hinweis;
        this.like = like;
        this.verweis = verweis;
        this.punkte = punkte;
        this.thema = thema;
        
    }
    
    public void fuegeBearbeiteteAufgabeHinzu(BeAufgabe afg){
        this.bearbeiteteAufgaben.add(afg);
    }
    
    public void setBearbeiteteAufgaben(Collection<BeAufgabe> bearbeiteteAufgaben){
        this.bearbeiteteAufgaben = (Collection) bearbeiteteAufgaben;
    }
    
    public  Collection<BeAufgabe> getBearbeiteteAufgaben(){
        return this.bearbeiteteAufgaben;
    }
    
    public void setAntwort(Collection<Antwort> antw){
        this.antworten = (Collection) antw;
    }
    
    public  Collection<Antwort> getAntwort(){
        return this.antworten;
    }
    
    public void setToken(Collection<Token> token){
        this.token = (Collection) token;
    }
    
    public  Collection<Token> getToken(){
        return this.token;
    }
    
    public void setZuAufgabe(Collection<ZuAufgabe> zuAufgabe){
        this.zuAufgabe = (Collection) zuAufgabe;
    }
    
    public  Collection<ZuAufgabe> getZuAufgabe(){
        return this.zuAufgabe;
    }
    
    public void setXGAufgabe(Collection<XGAufgabe> xgAufgabe){
        this.xgAufgabe = (Collection) xgAufgabe;
    }
    
    public  Collection<XGAufgabe> getXGAufgabe(){
        return this.xgAufgabe;
    }
    
     public void setAufgabenID(Long aufgabenID){
        this.aufgabenID = aufgabenID;
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
    
    public void setLike(int like){
        this.like = like;
    } 
    
    public int getLike(){
        return like;
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
    
    public void setThema(Thema thema){
        this.thema = thema;
    }
    
    public Thema getThema(){
        return thema;
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
