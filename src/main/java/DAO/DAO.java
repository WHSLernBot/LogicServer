package DAO;

import DBBot.aufgabenItem;
import Entitys.Antwort;
import Entitys.Aufgabe;
import Entitys.Benutzer;
import Entitys.Klausur;
import Entitys.LernStatus;
import Entitys.Teilnahme;
import Entitys.Thema;
import Entitys.Uni;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import main.CBBenutzer;
import main.CBPlattform;

/**
 * Diese Klasse stellt Methoden zur verfügung, die zum Zugriff auf einzelne persistente Entitäten
 * erlauben. 
 */
public class DAO {
    
    
    private static final String ALLE_UNIS = "select object(u) from Uni u";
    
    private static final String GIB_AUFGABE = "select object(a) "
            + "from Aufgabe a where a.aufgabenID := ID";
    
    private static final String GIB_LERNSTATUS = "select object(l) from "
            + "Benutzer b, LernStatus l, Thema t, Modul m"
            + "where b.id := ID and l.benutzer = b and b.uni = m.uni and Thema";
    
   private static final String GIB_ANTWORT = "select object(aw)"
            + "from Antwort aw, Aufgabe a where aw.antwort = a";
    
    private static final String GIB_NOTE = "select object(t)"
            + "from Teilnahme t, Klausur k, Benutzer b where t.benutzer = b"
            + "and l.klausur = k and b.id := ID";
    
    private static final String GIB_KLAUSUR = "select object(k)"
            + "from Klausur k, Modul m where k.kuerzel = m";
    
    private static final String GIB_MODUL = "select object(m)"
            + "from Modul m where m.kuerzel";
    
    /**
     * Gibt das Datum zurück.
     */
    public static Date gibDatum() {
        return new Date(new java.util.Date().getTime());
    }
    
    /**
     * Gibt dem Benutzer nach Angabe des Moduls und des Themas eine Aufgabe.
     * 
     * @param benutzer Der ChatBot-Benutzer.
     * @param modul Das angegebene Modul.
     * @param thema Das angegebene Thema.
     * @return 
     */
    public static Aufgabe gibAufgabe(CBBenutzer benutzer, String modul, String thema) {
        
        Aufgabe a = EMH.getEntityManager().find(Aufgabe.class, thema);
        Thema t = EMH.getEntityManager().find(Thema.class, modul);
        
        synchronized(benutzer) {
                Benutzer b = benutzer.getBenutzer();
               
                b.getLernStadi();
            
                EMH.getEntityManager().merge(benutzer.getBenutzer());
                
                a.getThema();
            }
        
        
           return null;
        
    }

    /**
     * Setzt dem Benutzer einen Namen, wie er angesprochen werden möchte.
     * 
     * @param benutzer Der ChatBot-Benutzer.
     * @param name Name, die gesetzt werden soll.
     */
    public static void setzeName(CBBenutzer benutzer, String name) {
        try {
            
            EMH.beginTransaction();
    
            synchronized(benutzer) {
                Benutzer b = benutzer.getBenutzer();
                
                b.setName(name);
                
                EMH.getEntityManager().persist(b);
            }
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
        }
    }

    /**
     * Diese Methode speichert die Antwort, die der Benutzer gegeben hat. 
     * 
     * @param id Id der Plattform.
     * @param plattform Plattform, die der Benutzer nutzt.
     * @param antwort Antwort, die der Benutzer gegeben hat.
     */
    public static void speichereAntwort(long id, short plattform, String antwort) {
        
        Query q = EMH.getEntityManager().createQuery(GIB_ANTWORT);
        
        q.setParameter("ID", id);
        q.setParameter("Plattform", plattform);
        
        Aufgabe a = (Aufgabe) q.getResultList().get(0);
        try{
            EMH.beginTransaction();
            a.addAntwort(antwort, Boolean.TRUE);
            EMH.getEntityManager().persist(a);
            EMH.commit();  
        } catch (Exception e){
            EMH.rollback();
        }
        
    }

    /**
     * Hier wird die Note des Benutzer im System gespeichert.
     * 
     * @param id Id der Plattform.
     * @param plattform Plattform, die der Benutzer nutzt.
     * @param note Die Note, die der Benutzer geschrieben hat.
     */
    public static void speichereNote(long id, short plattform, short note) {
        
        Query q = EMH.getEntityManager().createQuery(GIB_NOTE);
        
        q.setParameter("ID", id);
        q.setParameter("Plattform", plattform);
        
        Teilnahme t = (Teilnahme) q.getResultList().get(0);
        
        try{
            EMH.beginTransaction();
            t.setNote(note);
            EMH.getEntityManager().persist(t);
            EMH.commit();  
        } catch (Exception e){
            EMH.rollback();
        }
    }

    /**
     * Gibt den lernstatus des Benutzers zurück.
     * 
     * @param benutzer Der ChatBot-Benutzer.
     * @param themenId ID des Themas
     * @return 
     */
    public static LernStatus gibLernstatus(CBBenutzer benutzer, Long themenId) {
        return benutzer.getBenutzer().getStatus(themenId);
    }

    /**
     * Diese Methode weist dem CBBenutzer eine entsprechende Uni zu.
     * 
     * @param benutzer Der ChatBot-Benutzer.
     * @param uni Uni, die gesetzt werden soll.
     */
    public static void setzeUni(CBBenutzer benutzer, short uni) {
        
        try {
            
            EMH.beginTransaction();
            Uni u = EMH.getEntityManager().find(Uni.class, uni);
    
            synchronized(benutzer) {
                Benutzer b = benutzer.getBenutzer();
               
//                benutzer.getBenutzer().setUni(u);
                b.setUni(u);
            
                EMH.getEntityManager().merge(benutzer.getBenutzer());
            }
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
        }
    
    }

    /**
     * Die methode erstellt eine Liste von Unis.
     * 
     * @return Gibt eine Liste von Unis zurück.
     */
    public static Collection<Uni> gibUnis() {
        
        ArrayList<Uni> unis = new ArrayList<>();
        
        Query q = EMH.getEntityManager().createQuery(ALLE_UNIS);
        
        List rs = q.getResultList();
        
        for(Object o : rs) {
            unis.add((Uni) o);
        }
        
        return unis;
    }
    
    /**
     * Die Methode gibt eine bestimmte Uni zurück.
     * 
     * @param id Eideutige Id der Uni.
     * @return 
     */
    public static Uni gibUni(short id){
        
        return EMH.getEntityManager().find(Uni.class, id);
        
    }

    /**
     * Diese Methode legt das Datum der entsprechende Modulprüfung fest, um dem
     * Benutzer für die Prüfung zu erinnern.
     * 
     * @param id ID der Plattform.
     * @param plattform
     * @param modul Das entsprechende Modul.
     */
    public static void setzePruefung(long id, short plattform, String modul) {
        
        Query q = EMH.getEntityManager().createQuery(GIB_MODUL);
        
        q.setParameter("ID", id);
        q.setParameter("Plattform", plattform);
        q.setParameter("Modul", modul);
        
        Klausur k = (Klausur) q.getResultList().get(0);
        
        try {
            EMH.beginTransaction();
            
            k.setDatum(gibDatum());
            
            EMH.getEntityManager().persist(k); 
            
            
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
        }
    }

    /**
     * Diese Methode gibt zu einem bestimmten Modul und zu einem bestimmten Thema 
     * eine neue Aufgabe.
     * 
     * @param id id der Plattform.
     * @param plattform Plattform, die der Benutzer nutzt.
     * @param modul Das angegebene Modul.
     * @param thema Das angegebene Thema.
     */
    public static void neueAufgabe(long id, short plattform, String modul, String thema) {
 
        Aufgabe a = EMH.getEntityManager().find(Aufgabe.class, thema);
        Thema t = EMH.getEntityManager().find(Thema.class, modul);
        
        Query q = EMH.getEntityManager().createQuery(GIB_AUFGABE);
        q.setParameter("ID", id);
        q.setParameter("Plattform", plattform);
        
        try {
            EMH.beginTransaction();
            Aufgabe aufgabe = new Aufgabe(a.getThema(), a.getFrage(), a.getSchwierigkeit(), a.getHinweis(), a.getVerweis());
            EMH.getEntityManager().persist(aufgabe); 
                 
            
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
        }
        
       
    }

    /**
     * Diese Methode nimmt neuer Benutzer auf.
     * 
     * @param pt die Plattform mit der der Benutzer den ChatBot aufruft.
     * @param name Name des Benutzers.
     * @param witSession 
     */
    public static void neuerBenutzer(CBPlattform pt, String name, String witSession) {
        
        try {
            EMH.beginTransaction();
           
            
            Benutzer be = new Benutzer(pt.getId(),pt.getPlattform(),witSession,name,gibDatum());
            
            EMH.getEntityManager().persist(be); 
                 
            EMH.getEntityManager().persist(pt);
            
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
        }
    }
    
  
    
    public static Klausur gibKlausur(long id, short plattform, String modul) {
        
        Query q = EMH.getEntityManager().createQuery(GIB_KLAUSUR);
        
        q.setParameter("ID", id);
        q.setParameter("Plattform", plattform);
        
        Klausur k = (Klausur) q.getResultList().get(0);
        
        try{
            EMH.beginTransaction();
            EMH.getEntityManager().persist(k);
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
        }
        
          return k;     
    }

    /**
     * Diese Methode bewertet die Aufgabe.
     * @param id Aufgaben-ID
     * @param like Bewertung
     */
    public static void bewerteAufgabe(long id, int like) {
         
        Query q = EMH.getEntityManager().createQuery(GIB_AUFGABE);
        
        q.setParameter("ID", id);
        
        Aufgabe aufgabe = (Aufgabe) q.getResultList().get(0);
        
        try {
            EMH.beginTransaction();
            

            if(like <= 0){
                
                aufgabe.negativBewertet();
                
            }else{
                
                aufgabe.positivBewertet();
                
            }
            EMH.getEntityManager().persist(aufgabe); 
            
            
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
        }
        
    }

    /**
     * Diese Methode sucht einen bestimmten Benutzer.
     * 
     * @param pt die Plattform mit der der Benutzer den ChatBot aufruft.
     * @return 
     */
    public static Benutzer sucheBenutzer(CBPlattform pt) {
        
        return EMH.getEntityManager().find(Benutzer.class, pt.getId());
    }
    
    /**
     * 
     * @param ls
     * @param aufgaben 
     */
    public static void setztZuAufgaben(LernStatus ls,Collection<aufgabenItem> aufgaben) {
        
    }
}
