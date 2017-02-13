package DAO;

import DBBot.aufgabenItem;
import Entitys.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import main.CBBenutzer;
import main.CBPlattform;
import main.ChatBotManager;

/**
 * Diese Klasse stellt Methoden zur verfuegung, die zum Zugriff auf einzelne persistente Entitaeten
 * erlauben. 
 */
public class DAO {
    
    private static final String AUFGABE_OBJEKT = "aufgabe";
    private static final String AUFGABE_FRAGE = "frage";
    private static final String AUFGABE_VERWEIS = "verweis";
    private static final String AUFGABE_HINWEIS = "hinweis";
    private static final String AUFGABE_ID = "id";
    private static final String AUFGABE_TOKEN_ARRAY = "token";
    private static final String AUFGABE_ANTWORT_ARRAY = "antworten";
    
    private static final String MODULE_ARRAY = "module";
    private static final String MODULE_NAME = "name";
    private static final String MODULE_KUERZEL = "kuerzel";
    private static final String MODULE_THEMEN_ARRAY = "themen";
    private static final String MODULE_THEMEN_NAME = "name";
    private static final String MODULE_THEMEN_ID = "id";
    
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
            + "from Aufgabe a, Token o, LernStatus l, Thema t "
            + "where l.BENUTZER_ID = :BID and l.THEMA_THEMENID = a.THEMA_THEMENID "
            + "and o.AUFGABE_AUFGABENID = a.AUFGABENID and t.THEMENID = l.THEMA_THEMENID "
            + "and o.TOKEN = :TOK";
    
    private static final String GIB_AUFGABEN_AUFGABENTEXT = "select a.AUFGABENID "
            + "from Aufgabe a, LernStatus l, Thema t "
            + "where l.BENUTZER_ID = :BID and l.THEMA_THEMENID = a.THEMA_THEMENID "
            + "and t.THEMENID = l.THEMA_THEMENID and (LOWER(a.FRAGE) like :TOK)";
    
    private static final String GIB_AUFGABEN_ZUSATZTEXTE = "select a.AUFGABENID "
            + "from Aufgabe a, LernStatus l, Thema t "
            + "where l.BENUTZER_ID = :BID and l.THEMA_THEMENID = a.THEMA_THEMENID "
            + "and t.THEMENID = l.THEMA_THEMENID "
            + "and (LOWER(a.HINWEIS) like :TOK or LOWER(a.VERWEIS) like :TOK)";
    
    private static final String GIB_THEMA_THEMA = "select object(l) "
            + "from Thema t, LernStatus l "
            + "where l.BENUTZER_ID = :BID and l.THEMA_THEMENID = t.THEMENID and "
            + "t.THEMENID = a.THEMA_THEMENID and LOWER(t.NAME) like :TOK";
    
    private static final String GIB_THEMA_MODUL_THEMA = "select object(l) "
            + "from Thema t, LernStatus l "
            + "where l.BENUTZER_ID = :BID and l.THEMA_THEMENID = t.THEMENID and "
            + "t.MODUL_UNI_ID = :UID and t.MODUL_KUERZEL = :KRZ and "
            + "t.LOWER(t.NAME) like :TOK";
    
    private static final String ABFRAGE_LERNSTATUS = "and l.THEMA_THEMENID = :TID";
    
    private static final String ABFRAGE_MODUL = "and t.MODUL_UNI_ID = :UID and t.MODUL_KUERZEL = :KRZ";

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
            + "from ZuAufgabe "
            + "where LERNSTATUS_BENUTZER_ID = :BID "
            + "order by KENNUNG ASC";
    
    private static final String GIB_LERNSTADI = "select object(l) "
            + "from Benutzer b, LernStatus l, Thema t, Modul m "
            + "where b.ID = :BID and l.BENUTZER_ID = b.ID and "
            + "l.THEMA_THEMENID = t.THEMENID and t.MODUL_KUERZEL = :KRZ and "
            + "t.MODUL_UNI_ID = b.UNI_ID";
    
    private static final String GIB_MODULE = "select object(m) "
            + "from Modul m, LernStatus ls, Thema t "
            + "where ls.BENUTZER_ID = :BID, and ls.AKTIV = true "
            + "and ls.Beendet = false and ls.THEMA_THEMENID = t.THEMENID and "
            + "t.Modul_Kuerzel = m.KUERZEL and t.MODUL_UNI_ID = m.UNI_ID";
    
    private static final String GIB_NOT_MODULE = "select object(mod) "
            + "from Modul mod "
            + "where mod.UNI_ID = :UID and (NOT EXISTS "
            + "select m "
            + "from Modul m, Thema t, LernStatus ls "
            + "where ls.BENUTZER_ID = :BID and ls.THEMA_THEMENID = t.THEMENID "
            + "and m.KUERZEL = t.MODUL_KUERZEL)";
    
    private static final String GIB_PRUEFUNGSPERIODEN = "select object(p) "
            + "from Pruefungsperiode p "
            + "where p.UNI_ID = :UID and p.ANFANG > :HEUTE";
    
    private static final String GIB_TEILNAHME = "select object (t) "
            + "from Teilnahme t, Benutzer b "
            + "where b.ID = :BID and t.Benutzer_ID = b.ID and "
            + "t.MODUL_UNI_ID = b.UNI_ID and t.KLAUSUR_PERIODE_UNI_ID = b.UNI_ID and "
            + "t.KLAUSUR_PERIODE_PHASE = :PHA and t.KLAUSUR_PERIODE_JAHR = :JAHR and "
            + "t.KLAUSUR_PERIODE_KUERZEL = :KRZ and t.NOTE < 50";
    
    private static final String SUCHE_TEILNAHME = "select object (t) "
            + "from Teilnahme t, Benutzer b "
            + "where b.ID = :BID and t.Benutzer_ID = b.ID and "
            + "t.MODUL_UNI_ID = b.UNI_ID and t.KLAUSUR_PERIODE_UNI_ID = b.UNI_ID and "
            + "t.KLAUSUR_PERIODE_KUERZEL = :KRZ and t.NOTE = 0";
    
    private static final String GIB_INAKTIVE_MODULE = "select object(m) "
            + "from Modul m, LernStatus ls, Thema t "
            + "where ls.BENUTZER_ID = :BID, and ls.AKTIV = false "
            + "and ls.Beendet = false and ls.THEMA_THEMENID = t.THEMENID and "
            + "t.Modul_Kuerzel = m.KUERZEL and t.MODUL_UNI_ID = m.UNI_ID";
    
    private static final String LOESCHE_ZUAUFGABEN = "delete from ZuAufgabe "
            + "where LERNSTATUS_BENUTZER_ID = :ID "
            + "and LERNSTATUS_THEMA_THEMENID = :THEMA";
    
    private static final String GIB_SELEKTOREN = "select object(t) "
            + "from LernStatus l, Thema t "
            + "where l.BENUTZER_ID = :BID and l.AKTIV "
            + "and l.THEMA_ThemenID = t.THEMENID order by t.MODUL_KUERZEL";
    
    private static final String ALLE_SELEKTOREN = "select object(t) "
            + "from Thema t order by t.MODUL_KUERZEL ASC";
    
    private static final String WURDE_KLAUSUR_AKT = "selekt (k) "
            + "from Klausur k "
            + "where k.MODUL_KUERZEL = :KRZ and k.MODUL_UNI_ID = :UID "
            + "and k.VERAENDERT";
    
    private static final String GIB_AEHNLICHE_ERGEBNISSE = "select object(t) "
            + "from Teilname t "
            + "where t.KLAUSUR_MODUL_KUERZEL = :KRZ and t.KLAUSUR_MODUL_UNI_ID = :UID "
            + "and t.PROZENT >= :MIN and t.PROZENT <= :MAX order by t.PROZENT ASC";
    
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
     * in die bearebeitenden Aufgaben ein. Ausserdem berechnet er die Aufgaben
     * neu, falls keine mehr vorliegen.
     * 
     * @param ls
     * @return 
     * @throws java.lang.Exception 
     */
    public static Aufgabe gibAufgabe(LernStatus ls) throws Exception {
        
        if(ls == null) {
            throw new Exception("Es wurde kein Thema angegeben.");
        }
        
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
     * Gibt eine Aufgabe aus, die aus einer Liste an tokens am sinnvollsten war.
     * 
     * @param be
     * @param token
     * @return 
     * @throws java.lang.Exception 
     */
    public static Aufgabe gibAufgabe(CBBenutzer be, List<String> token) throws Exception {
        
        Aufgabe a;
        long aid = -1;
        long bId = be.getBenutzer().getId();
        
        
        //Herausfinden ob ein token ein Modul in der Datenbank ist.
        Modul m = null;
        
        Iterator it = token.iterator();
        while(m == null && it.hasNext()) {
            String s = (String)it.next();
            
            m = EMH.getEntityManager().find(Modul.class, new ModulPK(be.getBenutzer().getUni().getId(),s));  
            
            if(m != null) {
                it.remove();
            }
        }
        
        //Schauen ob fuer modul angemeldet
        LernStatus angemeldetLS = null;
        
        if(m != null) {
            angemeldetLS = EMH.getEntityManager().find(LernStatus.class, 
                new LernStatusPK(bId,m.getThemen().iterator().next().getId()));
        }
            
        if(angemeldetLS == null) {
            throw new Exception("Du bist noch garnicht fuer das entsprechende Modul angemeldet!");
        }
        
        //Pruefen ob noch token uebrig
        if(token.isEmpty() && m != null) {
            LernStatus ls = null;
            int punkte = 10001;
            for(Object o: gibLernstadi(be,m.getKuerzel())) {
                LernStatus l = (LernStatus) o;
                
                int p = l.getSumPunkte() * l.getThema().getAnteil();
                if(p < punkte) {
                    ls = l;
                    punkte = p;
                }
            }
            return gibAufgabe(ls);
        }
          
        //Herausfinden ob angegebenes Thema in der Datenbank liegenden Themen entsprechen.
        Query qu;
        
        List<LernStatus> stadi = new LinkedList<>();
        
        it = token.iterator();
        while(stadi.isEmpty() && it.hasNext()) {
            String s = (String) it.next();
            if(m == null) {
                qu = EMH.getEntityManager().createNamedQuery(GIB_THEMA_THEMA);

                qu.setParameter("BID", bId);
                qu.setParameter("TOK", "%" + s + "%");

            } else {
                qu = EMH.getEntityManager().createNamedQuery(GIB_THEMA_MODUL_THEMA);

                qu.setParameter("BID", bId);
                qu.setParameter("TOK", "%" + s + "%");
                qu.setParameter("UID", m.getUni().getId());
                qu.setParameter("KRZ", m.getKuerzel());
            }

            for(Object o: qu.getResultList()) {
                stadi.add((LernStatus) o);
            }
            
            if(!stadi.isEmpty()) {
                it.remove();
            }

        }
        
        //Falls keine token mehr uebrig sind.
        if(token.isEmpty()) {
            LernStatus ls = null;
            int punkte = 10001;
            for(LernStatus l: stadi) {
                int p = l.getSumPunkte() * l.getThema().getAnteil();
                if(p < punkte) {
                    ls = l;
                    punkte = p;
                }
            }
            return gibAufgabe(ls);
        }
        
        //Restlichen tokens bewerten
        HashMap<Long,Integer> punkte = new HashMap<>();
        
        for(LernStatus ls : stadi) {
            trageAufgabenEin(punkte,token,bId,ls,m);    
        }
        if(stadi.isEmpty()) {
            trageAufgabenEin(punkte,token,bId,null,m);
        }
        
        
        Query qxg = EMH.getEntityManager().createNamedQuery(ALLE_XGAUFGAEBN);
            
        qxg.setParameter("BID", bId);
            
        List<Long> rxg = qxg.getResultList();
  
        List<Long> result = new LinkedList<>();
        int top = 0;
        for(Long key : punkte.keySet()) {
            
            if(!rxg.contains(key)) {
               int v = punkte.get(key);
            
                if(v > top) {
                    result.clear();
                    top = v;
                    result.add(key);
                } else if (v == top) {
                    result.add(key);
                }
            }
        }
        
        //Ergebniss auswerten
        
        if(result.isEmpty()) {
            return null;
        } else if(result.size() == 1) {
            aid = result.get(0);
        } else {
            
            Query qzu = EMH.getEntityManager().createNamedQuery(ALLE_ZUAUFGAEBN);
            
            qzu.setParameter("BID", bId);
            
            List rzu = qzu.getResultList();
            
            for(Object zu : rzu) {
          
                long id = (long) zu;
                
                if(result.contains(id)) {
                    aid = id;
                    break;
                }
                
            }
            
            
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
     * Diese Methode traegt ein eine Hash Map aus AufgabenID und einem wert 
     * einen wert ein, wie oft der token in Aufgabe war und wie gut die Stelle 
     * war, wo er gefunden wurde.
     * 
     * @param punkte Die HashMap, die die Aufgaben bewertet.
     * @param token Die Token die gesuch werden soll.
     * @param bId Die id des Benutzeres.
     * @param ls Ein theoretisch gefundener LernStatus, kann auch null sein.
     * @param m Ein theoretisch gefundener Modul, kann auch null sein.
     */
    private static void trageAufgabenEin(HashMap<Long,Integer> punkte, List<String> token, long bId, LernStatus ls, Modul m) {
        
        Query q1 = erstelleQuery(GIB_AUFGABEN_TOKEN,ls,m);
        Query q2 = erstelleQuery(GIB_AUFGABEN_AUFGABENTEXT,ls,m);
        Query q3 = erstelleQuery(GIB_AUFGABEN_ZUSATZTEXTE,ls,m);
        
        for(String t: token) {
            t = "%" + t.toLowerCase() + "%";
        

            tragePunkteEin(punkte, 20,q1,t,bId,ls,m);
            tragePunkteEin(punkte, 5,q2,t,bId,ls,m);
            tragePunkteEin(punkte, 3,q3,t,bId,ls,m);
        }
    }
    
    /**
     * Diese Methode fuert eine jpql abfrage aus und traegt in die Bewertung der 
     * Aufgaben die entsprechende Punktzahl ein.
     * 
     * @param punkte Die HashMap, die die Aufgaben bewertet.
     * @param wertung Der Wert, wie Stark das ergebniss gewichtet werden soll.
     * @param jpql Die JPQL Abfrage.
     * @param token Der Token der gesuch werden soll.
     * @param bId Die id des Benutzeres.
     * @param ls Ein theoretisch gefundener LernStatus, kann auch null sein.
     * @param m Ein theoretisch gefundener Modul, kann auch null sein.
     */
    private static void tragePunkteEin(HashMap<Long,Integer> punkte, int wertung, Query jpql, String token, long bId, LernStatus ls, Modul m) {
        
        jpql.setParameter("BID", bId);
        jpql.setParameter("TOK", token);
        if(ls != null) {
           jpql.setParameter("TID", ls.getThema().getId()); 
        } else if(m != null) {
            jpql.setParameter("UID", m.getUni().getId());
            jpql.setParameter("KRZ", m.getKuerzel());
        }
        for(Object o : jpql.getResultList()) {
            Long aid = (Long) o;

            punkte.put(aid, punkte.get(aid) + wertung);
        }
    }
    
    /**
     * Erstellt eine jpql abfrage, je nachdem ob ein modul oder LernStatus
     * angegeben ist.
     * 
     * @param jpql Die zu grunde liegende Abfrage.
     * @param ls Der LernStatus.
     * @param m Das Modul.
     * @return Die daraus resultierende Abfrage.
     */
    private static Query erstelleQuery(String jpql,LernStatus ls, Modul m) {
        Query q;
        
        if(ls != null) {
            jpql = jpql + " " + ABFRAGE_LERNSTATUS;
        } else if(m != null) {
            jpql = jpql + " " + ABFRAGE_MODUL;
        }
        
        q = EMH.getEntityManager().createNamedQuery(jpql);
        
        return q;
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
     * @param b
     * @param aufgabe id der Aufgabe
     * @param kennung kennummer der beAufgabe
     * @param antwort nummer der Antwort
     * @param hinweis true falls ein hinweis benoetigt wurde
     * @throws Exception 
     */
    public static void speichereAntwort(CBBenutzer b, long aufgabe, int kennung,
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
            q.setParameter("BID", b.getBenutzer().getId());
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
     * Diese Methode legt eine Klausurteilnahme an, falls der Benutzer 
     * nicht schon angemeldet ist.
     * 
     * @param b Der Benutzer.
     * @param modul Das entsprechende Modul.
     * @param periode Die Pruefungsperiode der Klausur.
     * @param jahr Das Jahr der Klausur.
     * @throws java.lang.Exception
     */
    public static void meldePruefungAn(CBBenutzer b, String modul, short periode, short jahr) throws Exception {
        
        Teilnahme t;
        Klausur k;
        LernStatus ls;

        //Pruefen ob anmeldent moeglich
        
        try {
            synchronized (b) {
                PruefungsperiodePK pPK = new PruefungsperiodePK(jahr, b.getBenutzer().getUni().getId(), periode);
                ModulPK mPK = new ModulPK(b.getBenutzer().getUni().getId(), modul);
                k = EMH.getEntityManager().find(Klausur.class, new KlausurPK(mPK, pPK));

                t = new Teilnahme(b.getBenutzer(), k);

                Modul m = EMH.getEntityManager().find(Modul.class, mPK);
                ls = EMH.getEntityManager().find(LernStatus.class,
                        new LernStatusPK(b.getBenutzer().getId(), m.getThemen().iterator().next().getId()));
            }
        } catch(Exception e) {
            throw new Exception("Fehlerhafte Angaben.");
        }

        if (ls == null) {
            throw new Exception("Sie haben sich fuer das entsprechende Modul noch garnicht angemeldet.");
        }
        
        //Pruefen ob schon teilnahme existiert
        
        Query q = EMH.getEntityManager().createNamedQuery(GIB_TEILNAHME);
        
        q.setParameter("BID", b.getBenutzer().getId());
        q.setParameter("KRZ", modul);
        q.setParameter("PHA", periode);
        q.setParameter("JAHR", jahr);
        
        if(!q.getResultList().isEmpty()) {
            throw new Exception("Sie sind schon an der Klausur angemeldet oder haben Sie schon bestanden.");
        }
        
        //Teilnahme speichern
        
        try {
            EMH.beginTransaction();
            
            EMH.persist(t); 
             
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
            throw new Exception("Die Klausurteilnahme konnte nicht angemldet werden.");
        }
    }
    
    /**
     * Diese Methode meldet einen Benutzer von einer Pruefungsteilnahme wieder ab.
     * 
     * @param b Der Benutzer.
     * @param modul Das Modul dessen Pruefung abgemeldet werden soll.
     * @throws java.lang.Exception 
     */
    public static void meldePruefungAb(CBBenutzer b, String modul) throws Exception {
        
        Query q = EMH.getEntityManager().createNamedQuery(SUCHE_TEILNAHME);
        
        synchronized(b) {
            q.setParameter("BID", b.getBenutzer().getId());
            q.setParameter("KRZ", modul);
        }
        
        List result = q.getResultList();
          
        try {

            EMH.beginTransaction();

            //Normalerweise immer nur ein ergebniss, aber sicherheitshalber
            for(Object o: result) {
                Teilnahme t = (Teilnahme) o;
                EMH.remove(t);  
            }

            EMH.commit();
        } catch(Exception e) {
            EMH.rollback();
            throw new Exception("Die Pruefung konnte nicht abgemeldet werden.");
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
     * Gibt alle Module als Liste aus, fuer die sich ein Benutzer angemeldet hat.
     * 
     * @param b 
     * @return  
     */
    public static List gibAngemeldete(CBBenutzer b) {
        Query q = EMH.getEntityManager().createNamedQuery(GIB_MODULE);
        
        synchronized(b) {
            q.setParameter("BID", b.getBenutzer().getId());
        }
       
        List result = q.getResultList();
        
        return result;
    }
    
    /**
     * Gibt alle Module als Liste aus, fuer die sich ein Benutzer NICHT 
     * angemeldet hat, oder die inaktiv sind.
     * 
     * @param b 
     * @return  
     */
    public static List gibNichtangemeldete(CBBenutzer b) {
        Query q = EMH.getEntityManager().createNamedQuery(GIB_NOT_MODULE);
        Query qu = EMH.getEntityManager().createNamedQuery(GIB_INAKTIVE_MODULE);
        
        synchronized(b) {
            q.setParameter("UID", b.getBenutzer().getUni().getId());
            q.setParameter("BID", b.getBenutzer().getId());
            qu.setParameter("BID", b.getBenutzer().getId());
        }
       
        List result = q.getResultList();
        result.addAll(qu.getResultList());
        
        return result;
    }
    
    /**
     * Diese Methode gib eine Liste aller Klausuren aus, fuer die sich ein Benutezr 
     * anmeldet kann.
     * 
     * @param b
     * @param modul
     * @return 
     * @throws java.lang.Exception 
     */
    public static List<Klausur> gibPruefungen(CBBenutzer b, String modul) throws Exception {
        
        Query qPeriode = EMH.getEntityManager().createNamedQuery(GIB_PRUEFUNGSPERIODEN);
        
        Query qTeilnahme = EMH.getEntityManager().createNamedQuery(GIB_TEILNAHME);
        
        ModulPK mPK;
        LernStatus ls;
        synchronized(b) {
            mPK = new ModulPK(b.getBenutzer().getUni().getId(),modul);   
            qPeriode.setParameter("UID", b.getBenutzer().getUni().getId());
            qPeriode.setParameter("HEUTE", gibDatum(),TemporalType.DATE);
            qTeilnahme.setParameter("BID", b.getBenutzer().getId());
            qTeilnahme.setParameter("KRZ", modul);
            Modul m = EMH.getEntityManager().find(Modul.class, mPK);
            ls = EMH.getEntityManager().find(LernStatus.class, 
                    new LernStatusPK(b.getBenutzer().getId(), m.getThemen().iterator().next().getId()));
        }
        
        if(ls == null) {
            throw new Exception("Sie sind garnicht fuer das Modul angemeldet.");
        }
        
        List result = qPeriode.getResultList();
        List<Klausur> klausuren = new LinkedList<>();
        
        for(Object o : result) {
            Pruefungsperiode p = (Pruefungsperiode) o;
            
            
            
            PruefungsperiodePK pPK = p.gibPK();
            
            Klausur k = EMH.getEntityManager().find(Klausur.class, new KlausurPK(mPK, pPK));
            
            qTeilnahme.setParameter("PHA", p.getPhase());
            qTeilnahme.setParameter("JAHR", p.getJahr());
            
            if(qTeilnahme.getResultList().isEmpty()) {
                klausuren.add(k);
            }
        }
        
        
        return klausuren;
        
    }
    
    /**
     * Diese Methode schreibt die Note eines Benutzers in die Datenbank.
     * 
     * @param b
     * @param modul
     * @param note
     * @throws Exception 
     */
    public static void speichereNote(CBBenutzer b, String modul, short note) throws Exception {
        
        Query q = EMH.getEntityManager().createNamedQuery(SUCHE_TEILNAHME);
        
        synchronized(b) {
            q.setParameter("BID", b.getBenutzer().getId());
            q.setParameter("KRZ", modul);
        }
        
        List result = q.getResultList();
        
        try {

            EMH.beginTransaction();

            Teilnahme t = (Teilnahme) result.get(0);
            t.setNote(note);

            Klausur k = gibKlausur(b,modul);
            
            k.setVeraendert(true);
            
            EMH.merge(t);
            EMH.merge(k);
            EMH.commit();
        } catch(Exception e) {
            EMH.rollback();
            throw new Exception("Die Note konnte nicht gespeichert werden.");
        }
        
        //Modul als inaktiv kennzeichnen
        setzeInaktiv(b,modul);
        
        //Sollten weitere Anmeldungen (weshalb auch immer) exestieren.
        meldePruefungAb(b,modul);
        
    }   
    
    /**
     * Diese Methode gibt die Klausur zurueck an der sich der Benutzer fuer das
     * Modul angemeldet hat.
     * 
     * @param b
     * @param modul
     * @return 
     */
    public static Klausur gibKlausur(CBBenutzer b, String modul) {
        
        Query q = EMH.getEntityManager().createNamedQuery(SUCHE_TEILNAHME);
        
        synchronized(b) {
            q.setParameter("BID", b.getBenutzer().getId());
            q.setParameter("KRZ", modul);
        }
        
        List result = q.getResultList();
        
        if(result.isEmpty()) {
            return null;
        }
        
        return ((Teilnahme) result.get(0)).getKlausur();
        
    }
    
    /**
     * Gibt die gueltige und erfolgreiche Klausurteilnahme eines Benutzers aus.
     * 
     * @param id Die id des Benutzers.
     * @param modul Das Modul das der Benutzer geschirben hat
     * @return 
     */
    public static Teilnahme gibTeilnahme(Long id, String modul) {
        
        Query q = EMH.getEntityManager().createNamedQuery(SUCHE_TEILNAHME);
        
        q.setParameter("BID", id);
        q.setParameter("KRZ", modul);

        List result = q.getResultList();
        
        for(Object o : result) {
            
            Teilnahme t = (Teilnahme) o;
            
            if(t.getNote() >= 50 && t.getNote() != 0) {
                return t;
            }
            
        }
        
        return null;
    }

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
                    LernStatus ls = EMH.getEntityManager().find(LernStatus.class, new LernStatusPK(be.getId(),t.getId()));
                    if(ls == null) {
                        EMH.persist(new LernStatus(be,t,datum));  
                    } else {
                        if(ls.isBeendet()) {
                            throw new Exception("Dieses Modul haben Sie schon erfolgreich absolviert.");
                        }
                        ls.setAktiv(true);
                        EMH.merge(ls);
                    }

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
     * Setzt alle Lernstadi eines Moduls des Benutzers auf inaktiv.
     * 
     * @param b Der Benutzer.
     * @param modul Das entsprechende Modul.
     * @throws Exception 
     */
    public static void setzeInaktiv(CBBenutzer b, String modul) throws Exception {
        
        try {
            EMH.beginTransaction();
            Modul m = EMH.getEntityManager().find(Modul.class, modul);

            synchronized(b) {
                Benutzer be = b.getBenutzer();
                for(Thema t : m.getThemen()) {

                    LernStatus ls = EMH.getEntityManager().find(LernStatus.class, new LernStatusPK(be.getId(),t.getId()));

                    ls.setAktiv(false);
                    EMH.merge(ls);
                }
            }
            
            EMH.commit();
        } catch (Exception e) {
            EMH.rollback();
            throw new Exception("Das Modul konnte nicht als inaktiv gekennzeichent werden.");
        }
        
    }
    
    /**
     * Setzt alle Lernstadi eines Moduls des Benutzers auf beendet.
     * 
     * @param b Der Benutzer.
     * @param modul Das entsprechende Modul.
     * @throws Exception 
     */
    public static void setzeBeendet(CBBenutzer b, String modul) throws Exception {
        
        try {
            EMH.beginTransaction();
            Modul m = EMH.getEntityManager().find(Modul.class, modul);

            synchronized(b) {
                Benutzer be = b.getBenutzer();
                for(Thema t : m.getThemen()) {

                    LernStatus ls = EMH.getEntityManager().find(LernStatus.class, new LernStatusPK(be.getId(),t.getId()));

                    ls.setAsBeendet();
                    EMH.merge(ls);
                }
            }
            
            EMH.commit();
        } catch (Exception e) {
            EMH.rollback();
            throw new Exception("Das Modul konnte nicht als beendet gekennzeichent werden.");
        }
        
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
    
    public static List gibLernstadi(CBBenutzer b, String modul) {
        
         Query q = EMH.getEntityManager().createQuery(GIB_LERNSTADI);
        
         synchronized(b) {
             q.setParameter("BID", b.getBenutzer().getId());
             q.setParameter("KRZ", modul);
         }
         
         return q.getResultList();
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
    
    public static boolean wurdeVeraendert(Modul m) {
        
        Query q = EMH.getEntityManager().createNamedQuery(WURDE_KLAUSUR_AKT);
        
        q.setParameter("KRZ", m.getKuerzel());
        q.setParameter("UID", m.getUni().getId());
        
        return (!q.getResultList().isEmpty());
        
    }
    
    public static void setzeAnteil(long[] themen, int[] prozent) {
        
        try {
            
            EMH.beginTransaction();
            
            for(int i = 0; i < themen.length; i++) {
                
                Thema t = EMH.getEntityManager().find(Thema.class, themen[i]);
                
                t.setAnteil(prozent[i]);
                
                EMH.merge(t);
                
            }
            
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
        }
        
    }
    
    public static short gibErgebniss(CBBenutzer b, String m, int prozent) {
        
        short note = 0;
        
        Query q = EMH.getEntityManager().createNamedQuery(GIB_AEHNLICHE_ERGEBNISSE);
        
        q.setParameter("UID", b.getBenutzer().getUni().getId());
        q.setParameter("KRZ", m);
        q.setParameter("MIN", prozent - 5);
        q.setParameter("MAX", prozent + 5);
        
        for(Object o : q.getResultList()) {
            
            Teilnahme t = (Teilnahme) o;
            note = t.getNote();
            if(t.getProzent() >= prozent) {
                break;
            }
            
        }
        
        return note;
        
    }

}
