package DAO;

import DBBot.aufgabenItem;
import Entitys.*;
import com.google.gson.JsonObject;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
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
            + "from Aufgabe a where a.AUFGABENID := ID";
    
   private static final String GIB_ANTWORT = "select object(aw) "
            + "from Antwort aw, Aufgabe a where aw.antwort = a and aw.NUMMER := NR";
   
   private static final String GIB_BEAUFGABE = "select object(b) "
           + "from Beaufgabe b, Aufgabe a"
           + "where a.AufgabenID := AID and b.AUFGABE_AUFGABENID = a.AUFGABENID "
           + "and b.LERNSTATUS_THEMA_THEMENID = a.THEMA_THEMENID "
           + "and a.LERNSTATUS_BENUTZER_ID := BID";
    
    private static final String GIB_NOTE = "select object(t) "
            + "from Teilnahme t, Klausur k, Benutzer b where t.benutzer = b "
            + "and l.klausur = k and b.id := ID";
    
    private static final String GIB_KLAUSUR = "select object(k) "
            + "from Klausur k, Modul m where k.kuerzel = m";
    
    private static final String GIB_MODUL = "select object(m) "
            + "from Modul m where m.kuerzel";
    
    private static final String GIB_ZUAUFGABEN = "select object(z) "
            + "from ZuAufgabe z where z.lernStatus := LS";
    
    private static final String GIB_SELEKTOREN = "select object(t) "
            + "from Benutzer b, LernStatus l, Thema t "
            + "where b.ID := BID and  b.ID = l.BENUTZER_ID "
            + "and l.THEMA_ThemenID = t.THEMENID order by t.MODUL_KUERZEL";
    
    /**
     * Gibt das Datum zurück.
     * @return 
     */
    public static Date gibDatum() {
        return new Date(new java.util.Date().getTime());
    }
    
    /**
     * Gibt die naechste zu loesende Aufgabe eines Lernstatus aus und traegt diese
     * in die bearebeitenden Aufgaben ein.
     * 
     * @param ls 
     * @return 
     * @throws java.lang.Exception 
     */
    public static Aufgabe gibAufgabe(LernStatus ls) throws Exception {
        
        Aufgabe a = null;
        
        try {
            
            EMH.beginTransaction();
    
            ZuAufgabe za = ls.getZuAufgaben();
            
            a = za.getAufgabe();
            
            ls.setZuAufgaben(za.getNaechsteAufgabe());
            
            ls.addBeAufgaben(a, false, gibDatum(), false, false);
            
            EMH.getEntityManager().remove(za);
            
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
            throw new Exception("Aufgabe konnte nicht gefunden werden.");
        }
        
        return a;
        
    }
    
    public static Aufgabe gibAufgabe(CBBenutzer be, String token) {
        
        Aufgabe a = null;
        
        return a;
    }

    /**
     * Setzt dem Benutzer einen Namen, wie er angesprochen werden möchte.
     * 
     * @param benutzer Der ChatBot-Benutzer.
     * @param name Name, die gesetzt werden soll.
     * @throws java.lang.Exception
     */
    public static void setzeName(CBBenutzer benutzer, String name) throws Exception {
        try {
            
            EMH.beginTransaction();
    
            synchronized(benutzer) {
                Benutzer b = benutzer.getBenutzer();
                
                b.setName(name);
                
                EMH.getEntityManager().merge(b);
            }
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
            throw new Exception("Der Name konnte nicht gaendert werden.");
        }
    }

//    /**
//     * Diese Methode speichert die Antwort, die der Benutzer gegeben hat. 
//     * -------------------------------------------Was soll das?
//     * @param id Id der Plattform.
//     * @param plattform Plattform, die der Benutzer nutzt.
//     * @param antwort Antwort, die der Benutzer gegeben hat.
//     */
//    public static void speichereAntwort(long id, short plattform, String antwort) {
//        
//        Query q = EMH.getEntityManager().createQuery(GIB_ANTWORT);
//        
//        q.setParameter("ID", id);
//        q.setParameter("Plattform", plattform);
//        
//        Aufgabe a = (Aufgabe) q.getResultList().get(0);
//        try{
//            EMH.beginTransaction();
//            a.addAntwort(antwort, Boolean.TRUE);
//            EMH.getEntityManager().persist(a);
//            EMH.commit();  
//        } catch (Exception e){
//            EMH.rollback();
//        }
//        
//    }
    
    public static void speichereAntwort(long id, long aufgabe,
            short antwort, boolean hinweis) throws Exception {
        
        try {
            
            EMH.beginTransaction();
    
            Query q = EMH.getEntityManager().createQuery(GIB_ANTWORT);
            q.setParameter("NR", antwort);
            
            Antwort aw = (Antwort) q.getResultList().get(0);
            
            aw.wurdeGewaehlt();
            
            boolean richtig = aw.getRichtig();
            
            EMH.getEntityManager().merge(aw);
            
            q = EMH.getEntityManager().createQuery(GIB_BEAUFGABE);
            
            BeAufgabe be = (BeAufgabe) q.getResultList();
            
            LernStatus ls = be.getLernStatus();
            
            ls.neueGeloest(gibDatum());
            
            be.setzeAntwort(richtig, hinweis, gibDatum());
            
            EMH.getEntityManager().merge(be);
            EMH.getEntityManager().merge(ls);
            
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
            throw new Exception("Die Antwort konnte nicht gespeichert werden.");
        }
         
    }

    /**
     * Hier wird die Note des Benutzer im System gespeichert.
     * ------------------------------------------DAFUQ?
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
            EMH.getEntityManager().merge(t);
            EMH.commit();  
        } catch (Exception e){
            EMH.rollback();
        }
    }

    /**
     * Gibt den entsprechenden Lernstatus des Benutzers zurück.
     * 
     * @param benutzer Der ChatBot-Benutzer.
     * @param themenId ID des Themas
     * @return 
     */
    public static LernStatus gibLernstatus(CBBenutzer benutzer, Long themenId) {
        
        LernStatus ls;
        
        synchronized(benutzer) {
            LernStatusPK pk = new LernStatusPK(benutzer.getBenutzer().getId(), themenId);
            
            ls = EMH.getEntityManager().find(LernStatus.class, pk);
        }
        return ls;
    }

    /**
     * Diese Methode weist dem CBBenutzer eine entsprechende Uni zu.
     * 
     * @param benutzer Der ChatBot-Benutzer.
     * @param uni Uni, die gesetzt werden soll.
     * @throws java.lang.Exception
     */
    public static void setzeUni(CBBenutzer benutzer, short uni) throws Exception {
        
        try {
            
            EMH.beginTransaction();
            Uni u = EMH.getEntityManager().find(Uni.class, uni);
    
            synchronized(benutzer) {
                Benutzer b = benutzer.getBenutzer();
               
                b.setUni(u);
            
                EMH.getEntityManager().merge(benutzer.getBenutzer());
            }
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
            throw new Exception("Die Uni konnte nicht zugewiesen werden");
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
    
    public static JsonObject gibSelektoren(CBBenutzer b) {
        
        JsonObject ob = new JsonObject();
        
        Query q = EMH.getEntityManager().createQuery(GIB_SELEKTOREN);
        
        q.setParameter("BID", b.getBenutzer().getId());
        
        List ls = q.getResultList();
        
        String modul = "";
        
        JsonObject mod = new JsonObject();
        
        int i = 0;
        int j = 0;
        
        for(Object o: ls) {
            
            Thema t = (Thema) o;
            
            if(t.getModul().getKuerzel().equals(modul)) {
                
                JsonObject thema = new JsonObject();
                
                thema.addProperty("name", t.getName());
                thema.addProperty("id", t.getId());
                
                mod.add("thema" + i, thema);
                
                i++;
                
            } else {
                if(!modul.equals("")) {
                    ob.add("modul" + j, mod);
                    j++;
                    mod = new JsonObject();
                    i = 0;
                }
                
                modul = t.getModul().getKuerzel();
                mod.addProperty("name", modul);     
                
            }
             
        }
        
        return ob;
    }

    /**
     * Diese Methode legt das Datum der entsprechende Modulprüfung fest, um dem
     * Benutzer für die Prüfung zu erinnern.
     * ----------------------------------
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

//    /**
//     * Diese Methode gibt zu einem bestimmten Modul und zu einem bestimmten Thema 
//     * eine neue Aufgabe.
//     * ------------------------------------------- Dafuer gibt es doch schon gibAufgabe
//     * @param id id der Plattform.
//     * @param plattform Plattform, die der Benutzer nutzt.
//     * @param modul Das angegebene Modul.
//     * @param thema Das angegebene Thema.
//     */
//    public static void neueAufgabe(long id, short plattform, String modul, String thema) {
// 
//        Aufgabe a = EMH.getEntityManager().find(Aufgabe.class, thema);
//        Thema t = EMH.getEntityManager().find(Thema.class, modul);
//        
//        Query q = EMH.getEntityManager().createQuery(GIB_AUFGABE);
//        q.setParameter("ID", id);
//        q.setParameter("Plattform", plattform);
//        
//        try {
//            EMH.beginTransaction();
//            Aufgabe aufgabe = new Aufgabe(a.getThema(), a.getFrage(), a.getSchwierigkeit(), a.getHinweis(), a.getVerweis());
//            EMH.getEntityManager().persist(aufgabe); 
//                 
//            
//            EMH.commit();
//            
//        } catch (Exception e) {
//            EMH.rollback();
//        }
//        
//       
//    }

    /**
     * Diese Methode nimmt einen neuen Benutzer auf.
     * 
     * @param pt die Plattform mit der der Benutzer den ChatBot aufruft.
     * @param name Name des Benutzers.
     * @param witSession 
     * @throws java.lang.Exception 
     */
    public static void neuerBenutzer(CBPlattform pt, String name, String witSession) throws Exception {
        
        try {
            EMH.beginTransaction();
           
            Adresse ad = EMH.getEntityManager().find(Adresse.class, pt.getPlattform());
            
            Benutzer be = new Benutzer(pt.getId(),ad,witSession,name,gibDatum());
            
            EMH.getEntityManager().persist(be); 
            
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
            throw new Exception("Der Benutzer konnte nicht angelegt werden.");
        }
    }
    
  
    /**
     * 
     * ----------------------------
     * 
     * @param id
     * @param plattform
     * @param modul
     * @return 
     */
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
            EMH.getEntityManager().merge(aufgabe); 
            
            
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
    public static CBBenutzer sucheBenutzer(CBPlattform pt) {
        
        Benutzer be = EMH.getEntityManager().find(Benutzer.class, pt.getId());
        
        return new CBBenutzer(be);
    }
    
    /**
     * 
     * @param ls
     * @param aufgaben 
     */
    public static void setztZuAufgaben(LernStatus ls,Collection<aufgabenItem> aufgaben) {

        try{
            EMH.beginTransaction();
            
            Iterator it = aufgaben.iterator();
            Aufgabe a = ((aufgabenItem) it.next()).getAufgabe();
            
            ZuAufgabe zu = new ZuAufgabe(ls,a,true);
            
            ZuAufgabe zua = zu;
        
            ls.setZuAufgaben(zu);
            
            while(it.hasNext()) {
            
                a = ((aufgabenItem) it.next()).getAufgabe();
                
                zua = zua.setNaechsteAufgabe(a);
            
            }
            EMH.getEntityManager().persist(zu);
            
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
        }
        
    }
    
    public static void loescheZuAufgaben(LernStatus ls) {
        
        Query q = EMH.getEntityManager().createQuery(GIB_ZUAUFGABEN);
        
//        q.setParameter("ZU", ls.get);
        
        try {
            EMH.beginTransaction();
            
            for(Object zu : q.getResultList()) {
                
                EMH.getEntityManager().remove((ZuAufgabe) zu); 
                
            } 
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
        }
        
        
    }
    
    public static void  setzeLSPunkte(LernStatus ls,int punkte) {
        
        try {
            EMH.beginTransaction();
            
            ls.setSumPunkte(punkte);
            
            EMH.getEntityManager().persist(ls);
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
        }
        
    }
}
