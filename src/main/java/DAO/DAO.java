package DAO;

import Entitys.Aufgabe;
import Entitys.Benutzer;
import Entitys.Klausur;
import Entitys.LernStatus;
import Entitys.Modul;
import Entitys.Uni;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Query;
import main.CBBenutzer;
import main.CBPlattform;

/**
 *
 * @author Seve
 */
public class DAO {
    
    
    private static final String ALLE_UNIS = "select object(u) from Uni u";
    
    private static final String GIB_AUFGABE = "select object(a) "
            + "from Aufgabe a where a.aufgabenID := ID";
    
    private static final String GIB_LERNSTATUS = "select object(l) from "
            + "Benutzer b, LernStatus l, Thema t, Modul m"
            + "where b.id := ID and l.benutzer = b and b.uni = m.uni and Thema";
   
    public static Date gibDatum() {
        return new Date(new java.util.Date().getTime());
    }
    
    public static Aufgabe gibAufgabe(CBBenutzer benutzer, String modul, String thema) {
       
        return null;
        
    }

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

    public static void speichereAntwort(long id, short plattform, String antwort) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static void speichereNote(long id, short plattform, short asShort) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static LernStatus gibLernstatus(CBBenutzer benutzer, Long themenId) {
        return benutzer.getBenutzer().getStatus(themenId);
    }

    public static void setzeUni(CBBenutzer benutzer, short uni) {
        
        try {
            
            EMH.beginTransaction();
    
            synchronized(benutzer) {
                Benutzer b = benutzer.getBenutzer();
                
                
                
                benutzer.getBenutzer().setUni(u);
            
                EMH.getEntityManager().merge(benutzer.getBenutzer());
            }
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
        }
    
    }

    public static Collection<Uni> gibUnis() {
        
        ArrayList<Uni> unis = new ArrayList<>();
        
        Query q = EMH.getEntityManager().createQuery(ALLE_UNIS);
        
        List rs = q.getResultList();
        
        for(Object o : rs) {
            unis.add((Uni) o);
        }
        
        return unis;
    }
    
    public static Collection<Uni> gibUni(short id) {
        
        ArrayList<Uni> unis = new ArrayList<>();
        
        Query q = EMH.getEntityManager().createQuery(ALLE_UNIS);
        
        List rs = q.getResultList();
        
        for(Object o : rs) {
            unis.add((Uni) o);
        }
        
        return unis;
    }

    public static void setzePruefung(long id, short plattform, String asString, String asString0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static void neueAufgabe(long id, short plattform, String asString, String asString0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

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

    public static Klausur gibKlausur(long id, short plattform, String asString, String asString0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static void bewerteAufgabe(long id, int like) {
         
        Query q = EMH.getEntityManager().createQuery(GIB_AUFGABE);
        
        q.setParameter("ID", id);
        
        Aufgabe aufgabe = (Aufgabe) q.getResultList().get(0);
        
        try {
            EMH.beginTransaction();
            
            aufgabe.setLike(like);
            EMH.getEntityManager().persist(aufgabe); 
            
            
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
        }
        
    }

    public static Benutzer sucheBenutzer(CBPlattform pt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
