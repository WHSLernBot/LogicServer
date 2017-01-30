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
import main.ChatBotManager;

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
    
    private static final int BEWERTUNS_GRENZE = 20;
    private static final int AUFGABEN_HINZUFUEG_GRENZE = 30;
    
    private static final String ALLE_UNIS = "select object(u) from Uni u";
    
    private static final String GIB_ANTWORT = "select object(aw) "
            + "from Antwort aw, Aufgabe a "
            + "where aw.AUFGABE_AUFGABENID = a.AUFGABENID and aw.NUMMER = :NR "
            + "and a.AUFGABENID = :ID";
   
    private static final String GIB_AUFGABEN_TOKEN = "select a.AUFGABENID "
            + "from Aufgabe a, Token o, LernStatus l "
            + "where l.BENUTZER_ID = :BID and l.THEMA_THEMENID = a.THEMA_THEMENID "
            + "and o.AUFGABE_AUFGABENID = a.AUFGABENID "
            + "and o.TOKEN = :TOK";
    
    private static final String GIB_AUFGABEN_AUFGABENTEXT = "select a.AUFGABENID "
            + "from Aufgabe a, LernStatus l "
            + "where l.BENUTZER_ID = :BID and l.THEMA_THEMENID = a.THEMA_THEMENID"
            + "and (LOWER(a.FRAGE) like :TOK)";
    
    private static final String GIB_AUFGABEN_TEXT = "select a.AUFGABENID "
            + "from Aufgabe a, LernStatus l "
            + "where l.BENUTZER_ID = :BID and l.THEMA_THEMENID = a.THEMA_THEMENID"
            + "and (LOWER(a.HINWEIS) like :TOK or LOWER(a.VERWEIS) like :TOK)";
    
    private static final String GIB_AUFGABEN_THEMA = "select object(l) "
            + "from Aufgabe a, Thema t, LernStatus l"
            + "where l.BENUTZER_ID = :BID and l.THEMA_THEMENID = t.THEMENID and "
            + "t.THEMENID = a.THEMA_THEMENID and o.AUFGABE_AUFGABENID = a.AUFGABENID "
            + "and (LOWER(t.NAME) like :TOK";
    
    private static final String GIB_BEAUFGABE = "select object(b) "
            + "from Beaufgabe b, Aufgabe a "
            + "where a.AufgabenID = :AID and b.AUFGABE_AUFGABENID = a.AUFGABENID "
            + "and b.LERNSTATUS_THEMA_THEMENID = a.THEMA_THEMENID "
            + "and a.LERNSTATUS_BENUTZER_ID = :BID "
            + "and b.KENNUNG = :K";
    
    private static final String ALLE_XGAUFGAEBN = "select AUFGABE_AUFGABENID "
            + "from XGAufgabe "
            + "where LERNSTATUS_BENUTZER_ID = :BID";
    
    private static final String ALLE_ZUAUFGAEBN = "select AUFGABE_AUFGABENID "
            + "from ZuAufgabe"
            + "where LERNSTATUS_BENUTZER_ID = :BID "
            + "order by KENNUNG ASC";
    
    private static final String GIB_NOTE = "select object(t) "
            + "from Teilnahme t, Klausur k, Benutzer b where t.benutzer = b "
            + "and l.klausur = k and b.id = :ID";
    
    private static final String GIB_KLAUSUR = "select object(k) "
            + "from Klausur k, Modul m where k.kuerzel = m";
    
    private static final String GIB_MODUL = "select object(m) "
            + "from Modul m where m.kuerzel";
    
    private static final String GIB_NOT_MODULE = "select object(mod) "
            + "from Modul mod "
            + "where mod.UNI_ID = :UID and NOT EXISTS( "
            + "select m "
            + "from Modul m, Thema t, LernStatus ls "
            + "where ls.BENUTZER_ID = :BID and ls.THEMA_THEMENID = t.THEMENID "
            + "and m.KUERZEL = t.MODUL_KUERZEL)";
    
    private static final String LOESCHE_ZUAUFGABEN = "delete from ZuAufgabe "
            + "where LERNSTATUS_BENUTZER_ID = :ID "
            + "and LERNSTATUS_THEMA_THEMENID = :THEMA";
    
    private static final String GIB_SELEKTOREN = "select object(t) "
            + "from LernStatus l, Thema t "
            + "where l.BENUTZER_ID = :BID and l.AKTIV "
            + "and l.THEMA_ThemenID = t.THEMENID order by t.MODUL_KUERZEL";
    
    private static final String ALLE_SELEKTOREN = "select object(t) "
            + "from Thema t order by t.MODUL_KUERZEL ASC";
    
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
     * in die bearebeitenden Aufgaben ein. Außerdem berechnet er die Aufgaben
     * neu, falls keine mehr vorliegen.
     * 
     * @param ls
     * @return 
     * @throws java.lang.Exception 
     */
    public static Aufgabe gibAufgabe(LernStatus ls) throws Exception {
        
        Aufgabe a = null;
        
        long bid = ls.getBenutzer().getId();
        long tid = ls.getThema().getId();
        LernStatusPK pk = new LernStatusPK(bid,tid);
        
        ZuAufgabe zu = EMH.getEntityManager().find(ZuAufgabe.class, new ZuAufgabePK(pk,ls.getKennungZu()));
        
        if(zu == null) {
            throw new Exception("Es liegen keine zu bearbeitenden Aufgaben vor.");
        }
        Collection<XGAufgabe> xgs = ls.getXgAufgaben();
        
        try {
            
            EMH.beginTransaction();
            
            boolean notinXG = true;
            
            while(notinXG) {
                
                a = zu.getAufgabe();

                ls.gotZuAufgabe();
                
                EMH.remove(zu);
                
                notinXG = false;
                for(XGAufgabe xg : xgs) {
                    if(xg.getAufgabe().getAufgabenID() == a.getAufgabenID()) {
                        notinXG = true;
                        
                        zu = EMH.getEntityManager().find(ZuAufgabe.class, new ZuAufgabePK(pk,ls.getKennungZu()));
                        break;
                    }
                }
            }
            
            EMH.persist(new BeAufgabe(a,ls,ls.getKennungBe(),false,gibDatum(),false,false));
            ls.addedBeAufgabe();
            EMH.merge(ls);
               
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
            throw new Exception("Zu bearbeitende Aufgabe konnte nicht gefunden werden.");
        }
        
        //Pruefen auf naechste Aufgabe
        zu = EMH.getEntityManager().find(ZuAufgabe.class, new ZuAufgabePK(pk,ls.getKennungZu()));
        
        if(zu == null) {
            ChatBotManager.getInstance().gibBotPool().berechneLS(ls);
        }
        
        return a;
        
    }
    
    /**
     * Gibt eine Aufgabe, die einen bestimmten token entaelt aus.
     * 
     * @param be
     * @param token
     * @return 
     * @throws java.lang.Exception 
     */
    public static Aufgabe gibAufgabe(CBBenutzer be, String token) throws Exception {
        
        Aufgabe a;
        long aid = -1;
        long bId = be.getBenutzer().getId();
        
        List result;
        
        token = "%" + token.toLowerCase() + "%";
        
        Query q = EMH.getEntityManager().createNamedQuery(GIB_AUFGABEN_TOKEN);
        
        q.setParameter("BID", bId);
        q.setParameter("TOK", token);
        
        result = q.getResultList();
        
        if(result.isEmpty()) {
            q = EMH.getEntityManager().createNamedQuery(GIB_AUFGABEN_AUFGABENTEXT);
        
            q.setParameter("BID", bId);
            q.setParameter("TOK", token);

            result = q.getResultList();
            
            if(result.isEmpty()) {
                q = EMH.getEntityManager().createNamedQuery(GIB_AUFGABEN_TEXT);

                q.setParameter("BID", bId);
                q.setParameter("TOK", token);

                result = q.getResultList();
                
                if(result.isEmpty()) {
                    q = EMH.getEntityManager().createNamedQuery(GIB_AUFGABEN_THEMA);

                    q.setParameter("BID", bId);
                    q.setParameter("TOK", token);

                    //Achtung! Hier in der liste jetzt lernStadi und nicht long
                    result = q.getResultList();
                    if(!result.isEmpty()) {
                        //Was bei mehreren moeglichkeiten?
                        return gibAufgabe((LernStatus) result.get(0));
                    }
                }
            }
        }
        
        if(result.isEmpty()) {
            return null;
        } else if(result.size() >= 1) {
            aid = (long) result.get(0);
        } else {
            
            Query qzu = EMH.getEntityManager().createNamedQuery(ALLE_ZUAUFGAEBN);
            Query qxg = EMH.getEntityManager().createNamedQuery(ALLE_XGAUFGAEBN);
            
            qxg.setParameter("BID", bId);
            qzu.setParameter("BID", bId);
            
            List rxg = qxg.getResultList();
            List rzu = qzu.getResultList();
            
            for(Object zu : rzu) {
          
                long id = (long) zu;
                
                if(result.contains(id)) {
                    if(!rxg.contains(id)) {
                        aid = id;
                        break;
                    }
                }
                
            }
            
            
        }
        if(aid == -1) {
            return null;
        }
        try {
            
            EMH.beginTransaction();
            
            a = EMH.getEntityManager().find(Aufgabe.class, aid);
            
            LernStatus ls = EMH.getEntityManager().find(LernStatus.class, new LernStatusPK(bId,a.getThema().getId()));
            
            EMH.persist(new BeAufgabe(a,ls,ls.getKennungBe(),false,gibDatum(),false,false));
            ls.addedBeAufgabe();
            
            EMH.persist(new XGAufgabe(a,ls,ls.getKennungXG()));
            ls.addedXGAufgabe();
            
            EMH.merge(ls);
            
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
            throw new Exception("Aufgabe konnte nicht gefunden werden.");
        }
        
        return a;
    }  
    
    /**
     * Diese Methode stellt eine uebergebene Aufgabe in die Datenbank ein.
     * 
     * @param b Der benutzer, der Die Aufgabe hiinzufuegen moechte
     * @param thema
     * @param aufgabe Die Aufgabe als Json Objekt in definiertem aussehen.
     * @throws java.lang.Exception Wird geworfen, falls der Benutzer keine neue
     * Aufgabe hinzufuegen darf.
     */
    public static void neueAufgabe(CBBenutzer b, long thema, JsonObject aufgabe) throws Exception {
 
        //moegliche sicherheitsluecke
        if(b != null) {
            LernStatus ls = DAO.gibLernstatus(b, thema);
        
            if(ls.getRichtige() <= AUFGABEN_HINZUFUEG_GRENZE) {
                throw new Exception("Aktuell duerfen sie noch keien Aufgaben hinzufuegen.");
            }
        }
        
        
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
                a.addToken(el.getAsString().toLowerCase());
            }
            
            EMH.persist(a); 
                 
            
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
        }    
    }
    
    /**
     * Diese Methode bewertet eine Aufgabe.
     * 
     * @param b Der Benutzer der Bewerten moechte.
     * @param id Aufgaben-ID
     * @param like falls true wurde die Aufgabe positiv bewertet
     * @throws java.lang.Exception Wird geworfen, 
     * falls der Benutzer die Aufgabe gar nicht bewerten darf.
     */
    public static void bewerteAufgabe(CBBenutzer b, long id, boolean like) throws Exception {
         
        Aufgabe aufgabe = EMH.getEntityManager().find(Aufgabe.class, id);
        
        LernStatus ls = DAO.gibLernstatus(b, aufgabe.getThema().getId());
            
        if(ls.getRichtige() < BEWERTUNS_GRENZE) {
            throw new Exception("Aktuell duerfen Sie in diesem Thema keine Aufgaben bewerten.");
        }
        
        if(darfBewerten(aufgabe,b)) {
            throw new Exception("Sie haben die Aufgabe schon bewertet.");
        }
        
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
                EMH.persist(new Bewertung(aufgabe,ls,like));
                
                EMH.merge(aufgabe);
            }      
            
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
            throw new Exception("Aufgabe konnte nicht bewertet werden.");
        }
        
    }
    
    /**
     * Gibt true aus, falls ein Benutzer eine Aufgabe bewerten darf.
     * 
     * @param a Die Aufgabe.
     * @param b Der Benutzer
     * @return true, falls bewertet werden darf.
     */
    public static boolean darfBewerten(Aufgabe a, CBBenutzer b) {
        
        return gibBewertung(a,b) == null;
    }
    
    /**
     * Gibt die Bewertung einer Aufgabe eines Benutzers aus.
     * 
     * @param a Die Aufgabe.
     * @param b Der Benutzer
     * @return Die entsprechende Bewertug, falls nicht vorhanden null.
     */
    public static Bewertung gibBewertung(Aufgabe a, CBBenutzer b) {
        
        Bewertung bew = EMH.getEntityManager().find(Bewertung.class,
                new BewertungPK(a.getAufgabenID(), 
                        new LernStatusPK(b.getBenutzer().getId(), a.getThema().getId())));
        
        return bew;
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
     * Diese Methode fuegt einem JSON alle Module hinzu, fuer die sich ein 
     * Benutzer noch NICHT(!) angemeldet hat.
     * 
     * @param nachricht 
     * @param b 
     */
    public static void gibNichtangemeldete(JsonObject nachricht, CBBenutzer b) {
        Query q = EMH.getEntityManager().createNamedQuery(GIB_NOT_MODULE);
        
        synchronized(b) {
            q.setParameter("UID", b.getBenutzer().getUni().getId());
            q.setParameter("BID", b.getBenutzer().getId());
        }
       
        List result = q.getResultList();
        
        JsonArray module = new JsonArray();
        for(Object o : result) {
            Modul m = (Modul) o;
            
            JsonObject mod = new JsonObject();
            
            mod.addProperty(MODULE_KUERZEL, m.getKuerzel());
            mod.addProperty(MODULE_NAME, m.getName());
            
            module.add(mod);
            
        }
        nachricht.add(MODULE_ARRAY, module);
    }
    
    /**
     * 
     * @param b
     * @return 
     */
//    public static boolean darfBearbeiten(CBBenutzer b) {
//        return true;
//    }
    
    /**
     * Diese Methode meldet einen Benutzer bei einem Modul an und erstellt dort
     * die entsprechenden LernStadi. Dannach berechnet er alle Aufgaben fuer
     * den Benutzer neu.
     * 
     * @param b Der Benutzer der angemeldet werden soll.
     * @param modul Das Kuerzel des Moduls.
     * @throws java.lang.Exception
     */
    public static void meldeAn(CBBenutzer b, String modul) throws Exception {
        
        try {
            EMH.beginTransaction();
            Modul m = EMH.getEntityManager().find(Modul.class, modul);

            synchronized(b) {
                Benutzer be = b.getBenutzer();
                Date datum = gibDatum();
                for(Thema t : m.getThemen()) {

                    EMH.persist(new LernStatus(be,t,datum));

                }
            }
            
            EMH.commit();
        } catch (Exception e) {
            EMH.rollback();
            throw new Exception("Das Modul konnte nicht angemeldet werden.");
        }
    
        ChatBotManager.getInstance().gibBotPool().berechneNeu(b);
        
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
