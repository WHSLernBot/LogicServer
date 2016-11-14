package DAO;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

/**
 *
 * @author Seve
 */
public class DAO {
    
    private static DAO dao;
    
    private EntityManager em;
    
    private DAO() {

        em = Persistence.createEntityManagerFactory("LernServletPU").createEntityManager();
        
    }
    
    public static DAO getInstance() {
        
        if(dao == null) {
            dao = new DAO();
        }
        
        return dao;
    }
    
    public String gibSession(long id, short plattform) {
        return "";
    }

    public void gibAufgabe(long id, short plattform, String modul, String thema) {
        
    }

    public void setzeName(long id, short plattform, String name) {
        
    }

    public void speichereAntwort(long id, short plattform, String antwort) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void speichereNote(long id, short plattform, short asShort) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void gibInfos(long id, short plattform, String asString, String asString0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setzeUni(long id, short plattform, short asShort) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void gibUnis() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setzePruefung(long id, short plattform, String asString, String asString0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void neueAufgabe(long id, short plattform, String asString, String asString0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void neuerBenutzer(long id, short plattform) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void gibKlausurInfos(long id, short plattform, String asString, String asString0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void bewerteAufgabe(long id, short plattform, short asShort, boolean asBoolean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
