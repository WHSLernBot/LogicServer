package DAO;

import DBBot.aufgabenItem;
import Entitys.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Query;
import main.CBBenutzer;
import main.CBPlattform;

/**
 * Diese Klasse stellt Methoden zur verfuegung, die zum Zugriff auf einzelne persistente Entitaeten
 * erlauben. 
 */
public class DAO {
    
    private static final String MODULE_ARRAY = "module";
    private static final String MODULE_NAME = "name";
    private static final String MODULE_KUERZEL = "kuerzel";
    private static final String MODULE_THEMEN_ARRAY = "themen";
    private static final String MODULE_THEMEN_NAME = "name";
    private static final String MODULE_THEMEN_ID = "id";
    
    private static final String AUFGABE_OBJEKT = "aufgabe";
    private static final String AUFGABE_FRAGE = "frage";
    private static final String AUFGABE_VERWEIS = "verweis";
    private static final String AUFGABE_HINWEIS = "hinweis";
    private static final String AUFGABE_ID = "id";
    private static final String AUFGABE_TOKEN_ARRAY = "token";
    private static final String AUFGABE_ANTWORT_ARRAY = "antworten";
    
    private static final String AUFGABE_ANTWORT_ANTWORT = "antwort";
    private static final String AUFGABE_ANTWORT_RICHTIG = "richtig";
    private static final String AUFGABE_ANTWORT_NUMMER = "nummer";
    
    private static final String ALLE_UNIS = "select object(u) from Uni u";
    
    private static final String GIB_ANTWORT = "select object(aw) "
            + "from Antwort aw, Aufgabe a "
            + "where aw.AUFGABE_AUFGABENID = a.AUFGABENID and aw.NUMMER := NR "
            + "and a.AUFGABENID := ID";
   
    private static final String GIB_BEAUFGABE = "select object(b) "
            + "from Beaufgabe b, Aufgabe a "
            + "where a.AufgabenID := AID and b.AUFGABE_AUFGABENID = a.AUFGABENID "
            + "and b.LERNSTATUS_THEMA_THEMENID = a.THEMA_THEMENID "
            + "and a.LERNSTATUS_BENUTZER_ID := BID "
            + "and b.KENNUNG := K";
    
    private static final String GIB_NOTE = "select object(t) "
            + "from Teilnahme t, Klausur k, Benutzer b where t.benutzer = b "
            + "and l.klausur = k and b.id := ID";
    
    private static final String GIB_KLAUSUR = "select object(k) "
            + "from Klausur k, Modul m where k.kuerzel = m";
    
    private static final String GIB_MODUL = "select object(m) "
            + "from Modul m where m.kuerzel";
    
    private static final String GIB_ZUAUFGABEN = "select object(z) "
            + "from ZuAufgabe z where z.LERNSTATUS_BENUTZER_ID := ID and "
            + "z.LERNSTATUS_THEMA_THEMENID := THEMA and z.KENNUNG := K";
    
    private static final String LOESCHE_ZUAUFGABEN = "delete from ZuAufgabe "
            + "where LERNSTATUS_BENUTZER_ID := ID "
            + "and LERNSTATUS_THEMA_THEMENID := THEMA";
    
    private static final String GIB_SELEKTOREN = "select object(t) "
            + "from LernStatus l, Thema t "
            + "where l.BENUTZER_ID := BID and l.AKTIV "
            + "and l.THEMA_ThemenID = t.THEMENID order by t.MODUL_KUERZEL";
    
    private static final String ALLE_SELEKTOREN = "select object(t) "
            + "from Thema t order by t.MODUL_KUERZEL";
    
    //--------------------------- Allgemeine Methoden --------------------------
    
    /**
     * Gibt das Datum zurueck.
     * @return 
     */
    public static Date gibDatum() {
        return new Date(new java.util.Date().getTime());
    }
    
    //---------------------- Aufgaben bezogene Methoden ------------------------
    
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
        
        Query q = EMH.getEntityManager().createQuery(GIB_ZUAUFGABEN);
        
        q.setParameter("ID", ls.getBenutzer().getId());
        q.setParameter("THEMA", ls.getThema().getId());
        q.setParameter("K", ls.getKennungZu()); 
        
        try {
            
            EMH.beginTransaction();
    
            ZuAufgabe za = (ZuAufgabe) q.getResultList().get(0);
            
            if(za == null) {
                return null;
            }
            
            a = za.getAufgabe();
            
            ls.gotZuAufgabe();
            
            EMH.persist(new BeAufgabe(a,ls,ls.getKennungBe(),false,gibDatum(),false,false));
            ls.addedBeAufgaben();
            
            EMH.merge(ls);
            EMH.remove(za);
            
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
     * Diese Methode stellt eine uebergebene Aufgabe in die Datenbank ein.
     * 
     * @param thema
     * @param aufgabe Die Aufgabe als Json Objekt in definiertem aussehen.
     */
    public static void neueAufgabe(long thema, JsonObject aufgabe) {
 
        try {
            Thema t = EMH.getEntityManager().find(Thema.class, thema);
            
            String frage = aufgabe.get(AUFGABE_FRAGE).getAsString();
            int schwierigkeit = 1; // Das kann man mal irgenwie machen
            String hinweis = aufgabe.get(AUFGABE_HINWEIS).getAsString();
            String verweis = aufgabe.get(AUFGABE_VERWEIS).getAsString();
            
            EMH.beginTransaction();
            
            Aufgabe a = new Aufgabe(t,frage,schwierigkeit,hinweis,verweis);
            
            for(JsonElement el : aufgabe.getAsJsonArray(AUFGABE_ANTWORT_ARRAY)) {
                JsonObject obj = el.getAsJsonObject();
                
                String antwort = obj.get(AUFGABE_ANTWORT_ANTWORT).getAsString();
                boolean richtig = obj.get(AUFGABE_ANTWORT_RICHTIG).getAsBoolean();
                
                a.addAntwort(antwort, richtig); //Bei fehlern hier ein eigenes persist
            }
            
            for(JsonElement el : aufgabe.getAsJsonArray(AUFGABE_TOKEN_ARRAY)) {
                a.addToken(el.getAsString());
            }
            
            EMH.persist(a); 
                 
            
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
        }    
    }
    
    /**
     * Diese Methode bewertet eine Aufgabe.
     * @param id Aufgaben-ID
     * @param like falls true wurde die Aufgabe positiv bewertet
     * @throws java.lang.Exception
     */
    public static void bewerteAufgabe(long id, boolean like) throws Exception {
         
        Aufgabe aufgabe = EMH.getEntityManager().find(Aufgabe.class, id);
        
        try {
            EMH.beginTransaction();
            

            if(like) {
                aufgabe.positivBewertet();

            } else {
               aufgabe.negativBewertet();
            }
            
            if(aufgabe.getBewertung() <= 0) {
                EMH.remove(aufgabe);
            } else {
                EMH.merge(aufgabe);
            }      
            
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
            throw new Exception("Aufgabe konnte nicht bewertet werden.");
        }
        
    }
    
    /**
     * Diese Funktion speichert eine vom Benutzer gegebene Antwort ab.
     * 
     * @param id des Benutzers
     * @param aufgabe id der Aufgabe
     * @param kennung kennummer der beAufgabe
     * @param antwort nummer der Antwort
     * @param hinweis true falls ein hinweis benoetigt wurde
     * @throws Exception 
     */
    public static void speichereAntwort(long id, long aufgabe, int kennung,
            short antwort, boolean hinweis) throws Exception {
        
        try {
            
            EMH.beginTransaction();
    
            Query q = EMH.getEntityManager().createQuery(GIB_ANTWORT);
            q.setParameter("NR", antwort);
            q.setParameter("ID", aufgabe);
            
            Antwort aw = (Antwort) q.getResultList().get(0);
            
            aw.wurdeGewaehlt();
            
            boolean richtig = aw.getRichtig();
            
            EMH.merge(aw);
            
            q = EMH.getEntityManager().createQuery(GIB_BEAUFGABE);
            q.setParameter("AID", aufgabe);
            q.setParameter("BID", id);
            q.setParameter("K", kennung);
            
            BeAufgabe be = (BeAufgabe) q.getResultList().get(0);
            
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

    //--------------------- Benutzer bezogene Methoden -------------------------
    
    /**
     * Diese Methode nimmt einen neuen Benutzer auf.
     * 
     * @param pt die Plattform mit der der Benutzer den ChatBot aufruft.
     * @param name Name des Benutzers.
     * @param witSession 
     * @return  
     * @throws java.lang.Exception 
     */
    public static Benutzer neuerBenutzer(CBPlattform pt, String name, String witSession) throws Exception {
        
        Benutzer be;
        
        try {
            EMH.beginTransaction();
           
            Adresse ad = EMH.getEntityManager().find(Adresse.class, pt.getPlattform());
            
            be = new Benutzer(pt.getId(),ad,witSession,name,gibDatum());
            
            EMH.persist(be); 
            
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
            throw new Exception("Der Benutzer konnte nicht angelegt werden.");
        }
        
        return be;
    }
    
    /**
     * Setzt dem Benutzer einen Namen, wie er angesprochen werden moechte.
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
                
                EMH.merge(b);
            }
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
            throw new Exception("Der Name konnte nicht gaendert werden.");
        }
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
     * Diese Methode legt das Datum der entsprechende Modulpruefung fest, um dem
     * Benutzer fuer die Pruefung zu erinnern.
     * ---------------------------------- Nochmal ueberarbeiten
     * @param id ID der Plattform.
     * @param modul Das entsprechende Modul.
     */
    public static void setzePruefung(long id, String modul) {
        
        Query q = EMH.getEntityManager().createQuery(GIB_MODUL);
        
        q.setParameter("ID", id);
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
     * Gibt den entsprechenden Lernstatus des Benutzers zurueck.
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
     * Setzt die Punkte die ein Lernstatus nach neuberechnung hat.
     * 
     * @param ls
     * @param punkte 
     */
    public static void  setzeLSPunkte(LernStatus ls,int punkte) {
        
        try {
            EMH.beginTransaction();
            
            ls.setSumPunkte(punkte);
            
            EMH.merge(ls);
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
        
        if(be == null) {
            return null;
        }
        
        return new CBBenutzer(be);
    }
    
    /**
     * Diese Funktion fuegt einer Nachricht alle Module und dessen Themen hinzu,
     * die der Benutzer zurzeit bearbeitet.
     * 
     * @param nachricht Die Nachricht der die Selektoren hinzugefuegt werden
     * @param b Der Benutzer der die Nachricht erhaelt. Falls null werden alle selektoren ausgegeben.
     */
    public static void gibSelektoren(JsonObject nachricht, CBBenutzer b) {
        Query q;
        if(b != null) {
            q = EMH.getEntityManager().createQuery(GIB_SELEKTOREN);
        
            q.setParameter("BID", b.getBenutzer().getId());
        } else {
            q = EMH.getEntityManager().createQuery(ALLE_SELEKTOREN);
        }
        
        List ls = q.getResultList();
        
        String modul = "";
        
        JsonArray module = new JsonArray();
        JsonArray themen = new JsonArray();
        
        for(Object o: ls) {
            
            Thema t = (Thema) o;
            
            if(t.getModul().getKuerzel().equals(modul)) {
                
                JsonObject thema = new JsonObject();
                
                thema.addProperty(MODULE_THEMEN_NAME, t.getName());
                thema.addProperty(MODULE_THEMEN_ID, t.getId());
                
                themen.add(thema);
                
            } else {
                if(!modul.equals("")) {
                    JsonObject mod = new JsonObject();
                    mod.addProperty(MODULE_KUERZEL, modul);  
                    mod.addProperty(MODULE_NAME, t.getModul().getName());
                    mod.add(MODULE_THEMEN_ARRAY, themen);
                    
                    themen = new JsonArray();
                }
                
                modul = t.getModul().getKuerzel();
            }
             
        }
        nachricht.add(MODULE_ARRAY, module);
    }
    
    /**
     * Setzt eine Liste von aufgaben als zu bearbeitenden Aufgaben eines 
     * LernStatuses ein.
     * 
     * @param ls
     * @param aufgaben 
     */
    public static void setztZuAufgaben(LernStatus ls,Collection<aufgabenItem> aufgaben) {

        int kennung = 0;
        
        try {
            EMH.beginTransaction();
            
            loescheZuAufgaben(ls);  
            
            for(aufgabenItem ai : aufgaben) {
            
                Aufgabe a = ai.getAufgabe();

                EMH.persist(new ZuAufgabe(ls,a,kennung));
                
                kennung++;
            }
            
            ls.resetKennungZu();
            
            EMH.merge(ls);
            
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
        }
    }
    
    /**
     * Loescht alle Aufgaben die ein lernStatus noch zu bearbeiten hat.
     * 
     * @param ls Der entsprechende LernStatus
     * @throws java.lang.Exception
     */
    public static void loescheZuAufgaben(LernStatus ls) throws Exception {
        
        Query q = EMH.getEntityManager().createQuery(LOESCHE_ZUAUFGABEN);
        
        q.setParameter("ID", ls.getBenutzer().getId());
        q.setParameter("THEMA", ls.getThema().getId());
        
        q.executeUpdate();
           
    }
    
    //------------ Methoden zur Auslese der Datenbank --------------------------
    
    /**
     * Die methode erstellt eine Liste von Unis.
     * 
     * @return Gibt eine Liste von Unis zurueck.
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
     * Die Methode gibt eine bestimmte Uni zurueck.
     * 
     * @param id Eideutige Id der Uni.
     * @return 
     */
    public static Uni gibUni(short id){
        
        return EMH.getEntityManager().find(Uni.class, id);
        
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

//    /**
//     * Hier wird die Note des Benutzer im System gespeichert.
//     * ------------------------------------------DAFUQ?
//     * @param id Id der Plattform.
//     * @param plattform Plattform, die der Benutzer nutzt.
//     * @param note Die Note, die der Benutzer geschrieben hat.
//     */
//    public static void speichereNote(long id, short plattform, short note) {
//
//        Query q = EMH.getEntityManager().createQuery(GIB_NOTE);
//        
//        q.setParameter("ID", id);
//        q.setParameter("Plattform", plattform);
//        
//        Teilnahme t = (Teilnahme) q.getResultList().get(0);
//        
//        try{
//            EMH.beginTransaction();
//            t.setNote(note);
//            EMH.getEntityManager().merge(t);
//            EMH.commit();  
//        } catch (Exception e){
//            EMH.rollback();
//        }
//    }

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
   
}
