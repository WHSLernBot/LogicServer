package DAO;

import Entitys.Aufgabe;
import Entitys.Benutzer;
import Entitys.Plattform;
import Entitys.Uni;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Seve
 */
public class DAO {
    
    
    private static final String ALLE_UNIS = "select object(u) from Uni u";
    
    private static final String GIB_AUFGABE = "select object(a) from Aufgabe a where a.aufgabenID := ID";
    

    public static void gibAufgabe(long id, short plattform, String modul, String thema) {
        
        
        
    }

    public static void setzeName(long id, short plattform, String name) {
        
    }

    public static void speichereAntwort(long id, short plattform, String antwort) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static void speichereNote(long id, short plattform, short asShort) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static void gibInfos(long id, short plattform, String asString, String asString0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static void setzeUni(long id, short plattform, short asShort) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static Collection<Uni> gibUnis() {
        
        ArrayList<Uni> unis = new ArrayList<>();
        
        Query q = EntityManagerHelper.getEntityManager().createQuery(ALLE_UNIS);
        
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

    public static void neuerBenutzer(long id, short plattform) {
        
        try {
            EntityManagerHelper.beginTransaction();
            
            Benutzer be = new Benutzer();
            
            EntityManagerHelper.getEntityManager().persist(be); 
            
            Plattform pt = new Plattform(id,plattform,be);
                 
            EntityManagerHelper.getEntityManager().persist(pt);
            
            EntityManagerHelper.commit();
            
        } catch (Exception e) {
            EntityManagerHelper.rollback();
        }
    }

    public static void gibKlausurInfos(long id, short plattform, String asString, String asString0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static void bewerteAufgabe(long id, boolean like) {
         
        Query q = EntityManagerHelper.getEntityManager().createQuery(GIB_AUFGABE);
        
        q.setParameter("ID", id);
        
        Aufgabe aufgabe = (Aufgabe) q.getResultList().get(0);
        
        try {
            EntityManagerHelper.beginTransaction();
            
            aufgabe.setLike(like);
            EntityManagerHelper.getEntityManager().persist(aufgabe); 
            
            
            EntityManagerHelper.commit();
            
        } catch (Exception e) {
            EntityManagerHelper.rollback();
        }
        
    }
    
}
