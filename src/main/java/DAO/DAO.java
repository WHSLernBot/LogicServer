package DAO;

import DBBot.aufgabenItem;
import Entitys.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.sql.Date;
import java.sql.Time;
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
    
    private static final int BEWERTUNS_GRENZE = 0;
    private static final int AUFGABEN_HINZUFUEG_GRENZE = 30;
    
    private static final String ALLE_UNIS = "select object(u) from Uni u";
    
    private static final String GIB_ANTWORT = "select object(aw) "
            + "from Antwort aw, Aufgabe a "
            + "where a = aw.aufgabe and NUMMER = :NR "
            + "and AUFGABENID = :ID";
   
    private static final String GIB_AUFGABEN_TOKEN = "select object(a) "
            + "from Aufgabe a, Token o, LernStatus l, Thema t "
            + "where BENUTZER_ID = :BID and l.thema = a.thema "
            + "and a = o.aufgabe and t = a.thema "
            + "and TOKEN = :TOK ";
    
    private static final String GIB_AUFGABEN_AUFGABENTEXT = "select object(a) "
            + "from Aufgabe a, LernStatus l, Thema t "
            + "where BENUTZER_ID = :BID and l.thema = a.thema "
            + "and t = a.thema and LOWER(FRAGE) like :TOK ";
    
    private static final String GIB_AUFGABEN_ZUSATZTEXTE = "select object(a) "
            + "from Aufgabe a, LernStatus l, Thema t "
            + "where BENUTZER_ID = :BID and l.thema = a.thema "
            + "and t = a.thema "
            + "and (LOWER(HINWEIS) like :TOK or LOWER(VERWEIS) like :TOK)";
    
    private static final String GIB_THEMA_THEMA = "select object(l) "
            + "from Thema t, LernStatus l "
            + "where BENUTZER_ID = :BID and THEMA_THEMENID = THEMENID and "
            + "LOWER(NAME) like :TOK ";
    
    private static final String GIB_THEMA_MODUL_THEMA = "select object(l) "
            + "from Thema t, LernStatus l "
            + "where BENUTZER_ID = :BID and THEMA_THEMENID = THEMENID and "
            + "MODUL_UNI_ID = :UID and MODUL_KUERZEL = :KRZ and "
            + "LOWER(NAME) like :TOK";
    
    private static final String ABFRAGE_LERNSTATUS = " and l.thema.themenID = :TID";
    
    private static final String ABFRAGE_MODUL = " and MODUL_UNI_ID = :UID and MODUL_KUERZEL = :KRZ";

    private static final String GIB_BEAUFGABE = "select object(b) "
            + "from BeAufgabe b, Aufgabe a "
            + "where AufgabenID = :AID and a = b.aufgabe "
            + "and LERNSTATUS_THEMA_THEMENID = THEMA_THEMENID "
            + "and LERNSTATUS_BENUTZER_ID = :BID "
            + "and KENNUNG = :K";
    
    private static final String ALLE_XGAUFGAEBN = "select object(x) "
            + "from XGAufgabe x "
            + "where LERNSTATUS_BENUTZER_ID = :BID";
    
    private static final String ALLE_ZUAUFGAEBN = "select object(z) "
            + "from ZuAufgabe z "
            + "where LERNSTATUS_BENUTZER_ID = :BID "
            + "order by KENNUNG ASC";
    
    private static final String GIB_LERNSTADI = "select object(l) "
            + "from Benutzer b, LernStatus l, Thema t, Modul m "
            + "where ID = :BID and BENUTZER_ID = ID and "
            + "THEMA_THEMENID = THEMENID and MODUL_KUERZEL = :KRZ and "
            + "b.uni = m.uni and t.modul = m";
    
    private static final String GIB_MODULE = "select DISTINCT object(m) "
            + "from Modul m, LernStatus ls, Thema t "
            + "where BENUTZER_ID = :BID and AKTIV = true "
            + "and Beendet = false and THEMA_THEMENID = THEMENID and "
            + "Modul_Kuerzel = KUERZEL and MODUL_UNI_ID = UNI_ID";
    
    private static final String GIB_NOT_MODULE = 
//            "select DISTINCT object(mod) "
//            + "from Modul mod "
//            + "where UNI_ID = :UID and NOT EXISTS ( "
            "select DISTINCT object(m) "
            + "from Modul m, Thema t, LernStatus ls "
            + "where BENUTZER_ID = :BID and THEMA_THEMENID = THEMENID "
            + "and KUERZEL = MODUL_KUERZEL and UNI_ID = :UID and Beendet = false";
    
    private static final String GIB_PRUEFUNGSPERIODEN = "select object(p) "
            + "from Pruefungsperiode p "
            + "where UNI_ID = :UID and ANFANG > :HEUTE";
    
    private static final String GIB_TEILNAHME = "select object (t) "
            + "from Teilnahme t "
            + "where Benutzer_ID = :BID and "
//            + "MODUL_UNI_ID = UNI_ID and KLAUSUR_PERIODE_UNI_ID = UNI_ID and "
            + "KLAUSUR_PERIODE_PHASE = :PHA and KLAUSUR_PERIODE_JAHR = :JAHR and "
            + "KLAUSUR_MODUL_KUERZEL = :KRZ and NOTE < 50";
    
    private static final String SUCHE_TEILNAHME = "select object (t) "
            + "from Teilnahme t "
            + "where Benutzer_ID = :BID and "
//            + "MODUL_UNI_ID = UNI_ID and KLAUSUR_PERIODE_UNI_ID = UNI_ID and "
            + "KLAUSUR_MODUL_KUERZEL = :KRZ and NOTE = 0";
    
    private static final String GIB_BEENDETE_MODULE = "select DISTINCT object(m) "
            + "from Modul m, LernStatus ls, Thema t "
            + "where BENUTZER_ID = :BID "
            + "and Beendet = true and THEMA_THEMENID = THEMENID and "
            + "Modul_Kuerzel = KUERZEL and MODUL_UNI_ID = UNI_ID";
    
    private static final String LOESCHE_ZUAUFGABEN = "Select object(z) from ZuAufgabe z "
            + "where LERNSTATUS_BENUTZER_ID = :ID "
            + "and LERNSTATUS_THEMA_THEMENID = :THEMA";
    
    private static final String GIB_SELEKTOREN = "select DISTINCT object(t) "
            + "from LernStatus l, Thema t "
            + "where BENUTZER_ID = :BID and AKTIV = true "
            + "and THEMA_ThemenID = THEMENID order by MODUL_KUERZEL ASC";
    
    private static final String ALLE_SELEKTOREN = "select object(t) "
            + "from Thema t order by MODUL_KUERZEL ASC";
    
    private static final String WURDE_KLAUSUR_AKT = "select (k) "
            + "from Klausur k "
            + "where MODUL_KUERZEL = :KRZ and MODUL_UNI_ID = :UID "
            + "and VERAENDERT = true";
    
    private static final String GIB_AEHNLICHE_ERGEBNISSE = "select object(t) "
            + "from Teilname t "
            + "where KLAUSUR_MODUL_KUERZEL = :KRZ and KLAUSUR_MODUL_UNI_ID = :UID "
            + "and PROZENT >= :MIN and PROZENT <= :MAX order by PROZENT ASC";
    
    private static final String GIB_UNI = "select object(u) "
            + "from Uni u "
            + "where LOWER(NAME) like :NA";
    
    private static final String GIB_THEMEN = "select object (t) "
            + "from Thema t "
            + "where MODUL_KUERZEL = :KRZ and MODUL_UNI_ID = :UID";
    
    private static final String ALLE_MODULE = "select object (m) "
            + "from Modul m "
            + "where UNI_ID = :UID";
    
    private static final String GIB_ALLE_ANGEMELDETEN = "select DISTINCT object(b) "
            + "from Benutzer b, LernStatus ls "
            + "where ID = BENUTZER_ID and THEMA_THEMENID = :TID";
    
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
        
        int kennung = -1;
        
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
            
            kennung = ls.getKennungBe();
            
            EMH.persist(new BeAufgabe(a,ls,kennung,false,gibDatum(),false,false));
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
        
        a.setBekennung(kennung);   
        
        
        return a;
        
    }
    
    private static boolean istAngemeldet(CBBenutzer b, Modul m) {
        LernStatus angemeldetLS = null;
        
        if(m != null) {
            angemeldetLS = EMH.getEntityManager().find(LernStatus.class, 
                new LernStatusPK(b.getBenutzer().getId(),DAO.getThemen(m).iterator().next().getId()));
        }
            
        return angemeldetLS != null;
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
        int kennung;
        
        //Herausfinden ob ein token ein Modul in der Datenbank ist.
        Modul m = null;
        
        Iterator it = token.iterator();
        while(m == null && it.hasNext()) {
            String s = ((String)it.next()).toUpperCase();
            
            m = EMH.getEntityManager().find(Modul.class, new ModulPK(be.getBenutzer().getUni().getId(),s));  
            
            if(m != null) {
                it.remove();
            }
        }
            
        if(m != null && !istAngemeldet(be,m)) {
            throw new Exception("Du bist noch garnicht fuer das entsprechende Modul angemeldet!");
        }
        
        //Herausfinden ob angegebenes Thema in der Datenbank liegenden Themen entsprechen.
        Query qu;
        
        List<LernStatus> stadi = new LinkedList<>();
        
        it = token.iterator();
        while(!token.isEmpty() && stadi.isEmpty() && it.hasNext()) {
            String s = ((String) it.next()).toLowerCase();
            if(m == null) {
                qu = EMH.getEntityManager().createQuery(GIB_THEMA_THEMA);

                qu.setParameter("BID", bId);
                qu.setParameter("TOK", "%" + s + "%");

            } else {
                qu = EMH.getEntityManager().createQuery(GIB_THEMA_MODUL_THEMA);

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
        
        //Restlichen tokens bewerten
        HashMap<Long,Integer> punkte = new HashMap<>();
        
        if(!token.isEmpty()) {
            for(LernStatus ls : stadi) {
                trageAufgabenEin(punkte,token,bId,ls,m);    
            }
            if(stadi.isEmpty()) {
                trageAufgabenEin(punkte,token,bId,null,m);
            }
        }
        
        //Jetzt wurden alle Token ausgewertet. Zeit auszuwerten:
        
        if(m != null && stadi.isEmpty() && punkte.isEmpty()) {
            //Es liegt nur ein Modul vor
            
            LernStatus ls = null;
            double mPunkte = 10001;
            for(Object o : gibLernstadi(be,m.getKuerzel())) {
                LernStatus l = (LernStatus) o;
                
                double p = l.getSumPunkte() / l.getThema().getAnteil();
                if(p < mPunkte) {
                    ls = l;
                    mPunkte = p;
                }
            }
            return gibAufgabe(ls);
            
        } else if(!stadi.isEmpty() && punkte.isEmpty()) {
            
            //Es liegen Lernstadi vor
            
            LernStatus ls = null;
            double mPunkte = 10001;
            for(Object o: stadi) {
                LernStatus l = (LernStatus) o;
                
                double p = l.getSumPunkte() / l.getThema().getAnteil();
                if(p < mPunkte) {
                    ls = l;
                    mPunkte = p;
                }
            }
            return gibAufgabe(ls);
            
        }
        
        //Es gibt auch noch gefundene Token
        
        Query qxg = EMH.getEntityManager().createQuery(ALLE_XGAUFGAEBN);
            
        qxg.setParameter("BID", bId);
            
        List<Long> rxg = new LinkedList<>();
        
        for(Object o : qxg.getResultList()) {
            rxg.add(((XGAufgabe) o).getAufgabe().getAufgabenID());
        }
  
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
            
            Query qzu = EMH.getEntityManager().createQuery(ALLE_ZUAUFGAEBN);
            
            qzu.setParameter("BID", bId);
            
            List<Long> rzu = new LinkedList<>();
        
            for(Object o : qxg.getResultList()) {
                rxg.add(((ZuAufgabe) o).getAufgabe().getAufgabenID());
            }
            
            for(long zu : rzu) {

                if(result.contains(zu)) {
                    aid = zu;
                    break;
                }
                
            }
            
            
        }
        
        try {
            
            EMH.beginTransaction();
            
            a = EMH.getEntityManager().find(Aufgabe.class, aid);
            
            LernStatus ls = EMH.getEntityManager().find(LernStatus.class, new LernStatusPK(bId,a.getThema().getId()));
            
            kennung = ls.getKennungBe();
            
            EMH.persist(new BeAufgabe(a,ls,kennung,false,gibDatum(),false,false));
            ls.addedBeAufgabe();
            
            EMH.persist(new XGAufgabe(a,ls,ls.getKennungXG()));
            ls.addedXGAufgabe();
            
            EMH.merge(ls);
            
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
            throw new Exception("Aufgabe konnte nicht gefunden werden.");
        }
        
        a.setBekennung(kennung);   
        
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
            Long aid = ((Aufgabe) o).getAufgabenID();

            int p = (punkte.containsKey(aid)) ? punkte.get(aid) : 0;
            p += wertung;
            
            punkte.put(aid, p);
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
        
        q = EMH.getEntityManager().createQuery(jpql);
        
        return q;
    }

    
    /*
    /**
     * Diese Methode stellt eine uebergebene Aufgabe in die Datenbank ein.
     * 
     * @param b Der benutzer, der Die Aufgabe hiinzufuegen moechte
     * @param thema
     * @param aufgabe Die Aufgabe als Json Objekt in definiertem aussehen.
     * @throws java.lang.Exception Wird geworfen, falls der Benutzer keine neue
     * Aufgabe hinzufuegen darf.
     *
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
    */
    
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
            
        System.out.println(id);
        
        if(ls == null) {
            throw new Exception("Sie sind garnicht am Modul angemeldet.");
        }
        
        if(ls.getRichtige() < BEWERTUNS_GRENZE) {
            throw new Exception("Aktuell duerfen Sie in diesem Thema keine Aufgaben bewerten.");
        }
        
        if(!darfBewerten(aufgabe,b)) {
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
            System.out.println("aa");
            q = EMH.getEntityManager().createQuery(GIB_BEAUFGABE);
            q.setParameter("AID", aufgabe);
            q.setParameter("BID", b.getBenutzer().getId());
            q.setParameter("K", kennung);
            
            BeAufgabe be = (BeAufgabe) q.getResultList().get(0);
            System.out.println("b");
            LernStatus ls = be.getLernStatus();
            
            ls.neueGeloest(gibDatum());
            
            be.setzeAntwort(richtig, hinweis, gibDatum());
            System.out.println("v");
            EMH.getEntityManager().merge(be);
            EMH.getEntityManager().merge(ls);
            
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
            throw new Exception("Die Antwort konnte nicht gespeichert werden." + e.getMessage());
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
            System.out.println("Adresse klappt!");
            be = new Benutzer(pt.getId(),ad,witSession,name,gibDatum());
            System.out.println("Neuer benutzer: " + be.toString());
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
        
        modul = modul.toUpperCase();
        
        Teilnahme t;
        Klausur k;
        LernStatus ls;

        //Pruefen ob anmeldent moeglich
        
        try {
            synchronized (b) {
                PruefungsperiodePK pPK = new PruefungsperiodePK(jahr, b.getBenutzer().getUni().getId(), periode);
                ModulPK mPK = new ModulPK(b.getBenutzer().getUni().getId(), modul);
                
                
                k = EMH.getEntityManager().find(Klausur.class, new KlausurPK(mPK, pPK));
                
                if(k == null) {
                    throw new Exception();
                }
                
                t = new Teilnahme(b.getBenutzer(), k);

                Modul m = EMH.getEntityManager().find(Modul.class, mPK);
                ls = EMH.getEntityManager().find(LernStatus.class,
                        new LernStatusPK(b.getBenutzer().getId(), DAO.getThemen(m).iterator().next().getId()));
            }
        } catch(Exception e) {
            throw new Exception("Fehlerhafte Angaben.");
        }

        if (ls == null) {
            throw new Exception("Sie haben sich fuer das entsprechende Modul noch garnicht angemeldet.");
        }
        
        //Pruefen ob schon teilnahme existiert
        
        Query q = EMH.getEntityManager().createQuery(GIB_TEILNAHME);
        
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
        
        modul = modul.toUpperCase();
        
        Query q = EMH.getEntityManager().createQuery(SUCHE_TEILNAHME);
        
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
        Query q = EMH.getEntityManager().createQuery(GIB_MODULE);
        
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
        
        Query qu = EMH.getEntityManager().createQuery(GIB_BEENDETE_MODULE);
        
        List res = new LinkedList<>();
        
        synchronized(b) {
            qu.setParameter("BID", b.getBenutzer().getId());
        }
       
        Collection<Modul> mod = DAO.getModule(b.getBenutzer().getUni().getId());
        
        List<Modul> ang = DAO.gibAngemeldete(b);
        
        List<Modul> be = qu.getResultList();
        
        for(Modul m : mod) {
            
            if(!ang.contains(m) && !be.contains(m)) {
                res.add(m);
            }
            
        }
        
        return res;

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
        
        modul = modul.toUpperCase();
        
        Query qPeriode = EMH.getEntityManager().createQuery(GIB_PRUEFUNGSPERIODEN);
        
        Query qTeilnahme = EMH.getEntityManager().createQuery(GIB_TEILNAHME);
        
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
                    new LernStatusPK(b.getBenutzer().getId(), DAO.getThemen(m).iterator().next().getId()));
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
        
        modul = modul.toUpperCase();
        
        Query q = EMH.getEntityManager().createQuery(SUCHE_TEILNAHME);
        
        synchronized(b) {
            q.setParameter("BID", b.getBenutzer().getId());
            q.setParameter("KRZ", modul);
        }
        
        List result = q.getResultList();
        
        try {

            EMH.beginTransaction();

            Teilnahme t = (Teilnahme) result.get(0);
            t.setNote(note);

            Klausur k = t.getKlausur();
            
            k.setVeraendert(true);
            
            EMH.merge(t);
            EMH.merge(k);
            EMH.commit();
        } catch(Exception e) {
            EMH.rollback();
            throw new Exception("Die Note konnte nicht gespeichert werden.");
        }
        
        //Modul als inaktiv kennzeichnen
        if(note == 50) {
            setzeInaktiv(b,modul);
        } else {
            setzeBeendet(b,modul);
        }
        
        
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
        
        modul = modul.toUpperCase();
        
        Query q = EMH.getEntityManager().createQuery(SUCHE_TEILNAHME);
        
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
        
        modul = modul.toUpperCase();
        
        Query q = EMH.getEntityManager().createQuery(SUCHE_TEILNAHME);
        
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
        
        System.out.println(b.getBenutzer().getUni().getId());
        
        modul = modul.toUpperCase();
        
        Modul m = EMH.getEntityManager().find(Modul.class, new ModulPK(b.getBenutzer().getUni().getId(), modul));
        System.out.println("Modul variable erstellt!");
        
        if(m == null) {
            throw new Exception(modul + " konnte nicht gefunden werden.");
        }
        try {
            EMH.beginTransaction();
            

            synchronized(b) {
                Benutzer be = b.getBenutzer();
                Date datum = gibDatum();
                
                Collection<Thema> themen = DAO.getThemen(m);
                
                for(Thema t : themen) {
                    LernStatus ls = EMH.getEntityManager().find(LernStatus.class, new LernStatusPK(be.getId(),t.getId()));
                    if(ls == null) {
                        ls = new LernStatus(be,t,datum);
                        
                        EMH.persist(ls);  
                    } else {
                        if(ls.isBeendet()) {
                            throw new Exception(modul + " haben Sie schon erfolgreich absolviert.");
                        }
                        ls.setAktiv(true);
                        EMH.merge(ls);
                    }

                }
            }
            EMH.commit();
        } catch (Exception e) {
            EMH.rollback();
            throw new Exception(modul + " konnte nicht angemeldet werden. " + e.getMessage());
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
        
        modul = modul.toUpperCase();
        
        try {
            EMH.beginTransaction();
            Modul m = EMH.getEntityManager().find(Modul.class, new ModulPK(b.getBenutzer().getUni().getId(),modul));

            synchronized(b) {
                Benutzer be = b.getBenutzer();
                for(Thema t : DAO.getThemen(m)) {

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
        
        modul = modul.toUpperCase();
        
        try {
            EMH.beginTransaction();
            Modul m = EMH.getEntityManager().find(Modul.class, new ModulPK(b.getBenutzer().getUni().getId(),modul));

            synchronized(b) {
                Benutzer be = b.getBenutzer();
                for(Thema t : DAO.getThemen(m)) {

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
        
        Plattform p = EMH.getEntityManager().find(Plattform.class, new PlattformPK(pt.getId(),pt.getPlattform()));
        
        if(p == null) {
            return null;
        }
        
        return new CBBenutzer(p.getBenutzer());
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
        
        List<Thema> ls = q.getResultList();
        
        String modul = "";
        String name = "";
        
        JsonArray module = new JsonArray();
        JsonArray themen = new JsonArray();
        
        for(Thema t: ls) {
            
            if(!t.getModul().getKuerzel().equals(modul)) {
                if(!modul.equals("")) {
                    JsonObject mod = new JsonObject();
                    mod.addProperty(MODULE_KUERZEL, modul);  
                    mod.addProperty(MODULE_NAME, name);
                    mod.add(MODULE_THEMEN_ARRAY, themen);
                    
                    themen = new JsonArray();
                }
                
                modul = t.getModul().getKuerzel();
                name = t.getModul().getName();
            }
            
            JsonObject thema = new JsonObject();
                
                thema.addProperty(MODULE_THEMEN_NAME, t.getName());
                thema.addProperty(MODULE_THEMEN_ID, t.getId());
                
                themen.add(thema);
             
        }
        JsonObject mod = new JsonObject();
        mod.addProperty(MODULE_KUERZEL, modul);  
        mod.addProperty(MODULE_NAME, name);
        mod.add(MODULE_THEMEN_ARRAY, themen);
        
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
        
        Collection<ZuAufgabe> zu = q.getResultList();
        
        for(ZuAufgabe z : zu) {
//            System.out.println(z.getKennung());
            EMH.remove(z);
        }
           
    }
    
    /**
     * Gibt alle Lernstadi eines Moduls zurueck, die zu einem Benutzer gehoehren.
     * 
     * @param b Der Benutzer.
     * @param modul Das entsprechende Modul.
     * @return Liste aller Lernstadi.
     */
    public static List gibLernstadi(CBBenutzer b, String modul) {
        modul = modul.toUpperCase();
      
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
    
    /**
     * Prueft ob bei einem Modul neue Klausurergebnisse dazukamen.
     * 
     * @param m Das Modul.
     * @return true, falls neue ergebnisse dazukamen.
     */
    public static boolean wurdeVeraendert(Modul m) {
        
        Query q = EMH.getEntityManager().createQuery(WURDE_KLAUSUR_AKT);
        
        q.setParameter("KRZ", m.getKuerzel());
        q.setParameter("UID", m.getUni().getId());
        
        return (!q.getResultList().isEmpty());
        
    }
    
    /**
     * Setzt den Anteil der Angegebenen Themen neu.
     * 
     * @param themen Array aller Themen IDs.
     * @param prozent Array der entsprechenden Prozente.
     */
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
    
    /**
     * Diese Methode gib die naechste note eines anderen Benutzers aus, der ein
     * Modul geschrieben hat und mit seinen berechneten Prozent +- 5 Prozent
     * mit der angabe des Benutzers uebereinstimmt.
     * 
     * @param b Der Benutzer.
     * @param modul Das Modul in dem die Klausur geschrieben wird.
     * @param prozent Der errechnete Prozentwert.
     * @return Die Note. Falls keine gefunden wurde wird -1 zurueckgegeben.
     */
    public static short gibErgebniss(CBBenutzer b, String modul, int prozent) {
        
        modul = modul.toUpperCase();
        short note = 0;
        
        Query q = EMH.getEntityManager().createQuery(GIB_AEHNLICHE_ERGEBNISSE);
        
        q.setParameter("UID", b.getBenutzer().getUni().getId());
        q.setParameter("KRZ", modul);
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

    public static String neueVerbindung(short nummer, String url) throws Exception {
        
        Adresse a = EMH.getEntityManager().find(Adresse.class, nummer);
        
        try {
            EMH.beginTransaction();
            if(a == null) {
                a = new Adresse(nummer,url);

                EMH.persist(a);
            } else {
                a.setAdresse(url);
                
                EMH.merge(a);
            }
            
            EMH.commit();
        } catch(Exception e) {
            EMH.rollback();
            
            throw new Exception("Verbindung konnte nicht hinzugefuegt werden.");
        }
        
        EMH.getEntityManager().find(Adresse.class, nummer);
        
        return a.toString();
        
    }
    
    //------------ Methoden fuer die Webseite ----------------------------------
   
//    /**
//     * Diese Methode gib den Namen des Benutzers.
//     * --------Versteh ich nicht .. so gar nicht
//     * @param b Der ChatBot-Benutzer.
//     * @param id id des Benutzers.
//     * @return 
//     */
//    public static Benutzer getUsername(CBBenutzer b, long id) {
//        
//        Benutzer be = EMH.getEntityManager().find(Benutzer.class, id);
//        
//        synchronized(b) {
//            be = b.getBenutzer();
//            be.getName();
//            
//        } 
//        return be;
//    }
    
    /**
     * Gibt die id der Uni zurueck fuer die sich ein Benutzer angemeldet hat.
     * 
     * @param name Der angegebene Name.
     * @return Die entsprechende id, falls nichts efunden wurde wird -1 zurueckgegeben.
     */
    public static short getUniID(String name) {
        
        Query q = EMH.getEntityManager().createQuery(GIB_UNI);
        
        q.setParameter("NA", name.toLowerCase());
        
        List result = q.getResultList();
        
        if(result.isEmpty()) {
            return -1;
        } else {
            
            return ((Uni) q.getResultList().get(0)).getId();
        }        
    }
    
    /**
     * Gibt den Benutzernamen der Uni zurueck.
     * 
     * @param id Die id der Uni.
     * @return Der name, falls die Uni nicht gefunden werden konnte wird 
     * "unbekannt" zurueckgegebn.
     */
    public static String getUsername(short id) {
        Uni u = EMH.getEntityManager().find(Uni.class, id);
        
        return (u == null) ? "unbekannt" : u.getName();
    }
    
    /**
     * Diese Methode gibt zurueck, ob das angegebene Password der Uni richtig war.
     * 
     * @param id Die ID der Uni.
     * @param password Das angegebene Password.
     * @return true, falls das Password richtig war.
     */
    public static boolean pruefePassword(short id, String password) {
        
        Uni u = EMH.getEntityManager().find(Uni.class, id);
        
        return (u == null) ? false : u.getPassword().equals(password);
        
    }
 
    /**
     * Diese Methode fuegt einer Uni ein entsprechendes Modul hinzu.
     * 
     * @param id ID der Uni.
     * @param name Name des Moduls.
     * @param kuerzel Kuerzel des Moduls.
     * @return 
     * @throws java.lang.Exception
     */
    public static Modul addModul(short id, String name, String kuerzel) throws Exception {
                
        kuerzel = kuerzel.toUpperCase();
        Modul m;
        Modul mod = EMH.getEntityManager().find(Modul.class, new ModulPK(id,kuerzel));
        
        if(mod != null) {
            throw new Exception(kuerzel + " exestiert schon an dieser Uni.");
        }
        
        try {
            EMH.beginTransaction();
            Uni uni = EMH.getEntityManager().find(Uni.class, id);
            
            m = new Modul(uni,kuerzel.toUpperCase(),name);
            
            EMH.persist(m);

            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
            throw new Exception("Das Modul konnte nicht hinzugefuegt werden");
        }
        return m;
    }
    
    /**
     * Diese Methode erstellt eine Liste von Modulen. 
     * 
     * @param id Id der Uni.
     * @return Gibt eine Liste von Module zurueck.
     */
    public static Collection<Modul> getModule(short id) {
        
        ArrayList<Modul> module = new ArrayList<>();
        
        Query q = EMH.getEntityManager().createQuery(ALLE_MODULE);
        q.setParameter("UID", id);
        
        List rs = q.getResultList();
        
        for(Object o : rs) {
            module.add((Modul) o);
        }
        
        return module;
    }
    
   
    /**
     * Diese Methode fuegt einem Modul ein Thema hinzu.
     * 
     * @param kuerzel Kuerzel des Moduls.
     * @param id Id der Uni. 
     * @param thema Das Thema das gesetzt werden soll.
     * @param anteil Anteil die das Thema zum modul insgesamt annimmt.
     * @return 
     * @throws java.lang.Exception
     */
    public static Thema addThema(String kuerzel, short id, String thema, int anteil) throws Exception {
               
        Thema t;
        try {
            EMH.beginTransaction();
            Modul modul = EMH.getEntityManager().find(Modul.class, new ModulPK(id,kuerzel));
            
            int summe = 100 + anteil;
            
            int rest = 0;
            
            for(Thema th : DAO.getThemen(modul)) {
                
                int ant = th.getAnteil();
                
                ant = ant * 100 / summe;
                
                rest += ant;
                
                th.setAnteil(ant);
                
                EMH.merge(th);
                
            }      
            
            t = new Thema(modul,thema,(100 - rest));
            
            Collection<Thema> themen = DAO.getThemen(modul);
            
            if(!themen.isEmpty()) {
            
                Query q = EMH.getEntityManager().createQuery(GIB_ALLE_ANGEMELDETEN);
            
                q.setParameter("TID", themen.iterator().next().getId());

                for(Object o : q.getResultList()) {

                    LernStatus ls = new LernStatus((Benutzer) o,t,DAO.gibDatum());
                    
                    EMH.persist(ls);

                }
            }
            EMH.persist(t);
            
           
            
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
            throw new Exception("Das Thema konnte nicht hinzugefuegt werden. " + e.getMessage());
        }
        
        return t;
    
    }
    
    /**
     * Diese Methode erstellt eine Liste von Themen. Eigentlich unnoetig. 
     * Besser ueber Modul.getThemen()
     * 
     * @param id Id der Uni.
     * @param kuerzel Name des Moduls.
     * @return 
     */
    public static Collection<Thema> getThemen(short id, String kuerzel) {
        
        ArrayList<Thema> themen = new ArrayList<>();
        
        Query q = EMH.getEntityManager().createQuery(GIB_THEMEN);
        q.setParameter("UID", id);
        q.setParameter("KRZ", kuerzel);
        
        List rs = q.getResultList();
        
        for(Object o : rs) {
            themen.add((Thema) o);
        }
        
        return themen;
    }
    
    public static Collection<Thema> getThemen(Modul m) {
        return getThemen(m.getUni().getId(),m.getKuerzel());
    }
    
    
    /**
     * Diese Methode speichert eine neue Aufgabe ab.
     * 
     * @param thema Zugehoeriges Thema der Frage.
     * @param frage Frage, die gesetzt werden soll.
     * @param hinweis Der Hinweistext zur Aufgabe.
     * @param verweis Der Verweistext zur Aufgabe.
     * @param punkte Die Punkte die die Aufgabe annehemen soll. Falls >= 0 standartwert 100.
     * @return Die neue Aufgabe.
     * @throws java.lang.Exception
     */
    public static Aufgabe addAufgabe(Thema thema, String frage, String hinweis, String verweis, int punkte) throws Exception {      
        
        Aufgabe a;
        
        try {

            EMH.beginTransaction();

            if(punkte <= 0) {
                punkte = 100;
            }
            a = new Aufgabe(thema,frage,punkte,hinweis,verweis);

            thema.addAufgabe();
            
            EMH.persist(thema);
            
            EMH.persist(a);
            EMH.commit();
        } catch(Exception e) {
            EMH.rollback();
            throw new Exception("Die Aufgabe konnte nicht gespeichert werden.");
        }
        
        return a;
    }
    
    
    /**
     * Fuegt eine neue Antwort einer Aufgabe hinzu.
     * 
     * @param aufgabe Die Id der Aufgabe.
     * @param antwort Antwort der Aufgabe.
     * @param richtig Falls true, ist die Antwort richtig.
     * @throws Exception 
     */
    public static void addAntwort(long aufgabe, String antwort, boolean richtig) throws Exception {
        
        Aufgabe a = EMH.getEntityManager().find(Aufgabe.class, aufgabe);
        if(a == null) {
            throw new Exception("Die Aufgabe wurde nicht gefunden.");
        }
        
        try {
            
            EMH.beginTransaction();
            
            Antwort aw = new Antwort(a,a.getAnzAntworten(),antwort,richtig);
            
            a.addAntwort();
            
            EMH.merge(a);
            EMH.persist(aw);
                        
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
            throw new Exception("Die Antwort konnte nicht gespeichert werden.");
        }
    }
    
    /**
     * Fuegt einer Aufgabe ein neues Token hinzu.
     * 
     * @param aufgabe Die AufgabeID.
     * @param token Das neue Token.
     * @throws Exception 
     */
    public static void addToken(long aufgabe, String token) throws Exception {
        
        Aufgabe a = EMH.getEntityManager().find(Aufgabe.class, aufgabe);
        if(a == null) {
            throw new Exception("Die Aufgabe wurde nicht gefunden.");
        }
        
        try {
            
            EMH.beginTransaction();
            
            Token t = new Token(a,token);
            
            EMH.persist(t);
                        
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
            throw new Exception("Die Antwort konnte nicht gespeichert werden.");
        }
        
    }
    
    /**
     * Aendert das Passwort einer uni.
     * 
     * @param id Die Id der uni.
     * @param neu Das alte Passwort.
     * @param alt Das neue Passwort.
     * @throws Exception 
     */
    public static void setPassword(short id, String neu, String alt) throws Exception {
        
        Uni u = EMH.getEntityManager().find(Uni.class, id);
        
        if(u == null || !pruefePassword(id,alt) ) {
            throw new Exception("Die angaben stimmen nicht.");
        }
        
        try {
            
            EMH.beginTransaction();
            
            u.setPassword(neu);
            
            EMH.merge(u);
            
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
            throw new Exception("Das Passwort konnte nicht gespeichert werden.");
        }
        
    }
    
    /**
     * Aendert den Namen der Uni.
     * 
     * @param id Die Id der uni.
     * @param name Der neue Name der Uni.
     * @throws Exception 
     */
    public static void setUsername(short id, String name) throws Exception {
        
        Uni u = EMH.getEntityManager().find(Uni.class, id);
        
        if(u == null) {
            throw new Exception("Die Uni konnte nicht gefunden werden.");
        }
        
        try {
            
            EMH.beginTransaction();
            
            u.setName(name);
            
            EMH.merge(u);
            
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
            throw new Exception("Der Name konnte nicht gespeichert werden.");
        }
        
    }
    
    /**
     * Erstellt eine neue Uni.
     * 
     * @param name Name der Uni.
     * @param passwort Passwort der Uni.
     * @throws Exception 
     */
    public static void neueUni(String name, String passwort) throws Exception {

        try {
            
            EMH.beginTransaction();
            
            Uni u = new Uni(name);
            
            u.setPassword(passwort);
            
            EMH.persist(u);
            
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
            throw new Exception("Der Name konnte nicht gespeichert werden.");
        }
        
    }
    
    /**
     * Fuegt eine neue Pruefungsperiode einer Uni hinzu.
     * 
     * @param id Id der Uni.
     * @param jahr Jahr der Pruefungsperiode.
     * @param phase Nummer der Phase.
     * @param anfang Angangsdatum der Pruefungen.
     * @param ende Enddatum der Pruefungen.
     * @param anmeldebeginn Anmelebeginn der Pruefungen.
     * @return 
     * @throws Exception 
     */
    public static Pruefungsperiode addPruefungsphase(short id, short jahr, short phase, Date anfang, Date ende, Date anmeldebeginn) throws Exception {
        Pruefungsperiode p;
        try {
            
            EMH.beginTransaction();
            
            Uni u = EMH.getEntityManager().find(Uni.class, id);
            
            p = new Pruefungsperiode(u,jahr,phase,anmeldebeginn, anfang,ende);
            
            EMH.persist(p);
            
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
            throw new Exception("Die Pruefungsphase konnte nicht erstellt werden.");
        }
        
        return p;
        
    }
    
    /**
     * Fuegt neiner Pruefungsperiode eine neue Klausur hinzu.
     * 
     * @param p Die Pruefungsperiode.
     * @param m Das Modul der Pruefung.
     * @param datum Das Datum der Klausur.
     * @param uhrzeit Die Uhrzeit der Klausur.
     * @param dauer Die Dauer der Klausur.
     * @param ort Der Ort der Klausur.
     * @param hilfsmittle Zugelassene Hilfsmittel.
     * @param typ Die Art der Klausur (muenlicht, schriftlich etc.)
     * @throws Exception 
     */
    public static void addKlausur(Pruefungsperiode p, Modul m, Date datum, 
            Time uhrzeit, short dauer, String ort, String hilfsmittle, String typ) throws Exception {

        try {
            
            EMH.beginTransaction();
            
            Klausur k = new Klausur(m, p, datum, uhrzeit, dauer, ort, hilfsmittle, typ);
            
            EMH.persist(k);
            
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
            throw new Exception("Die Pruefungsphase konnte nicht erstellt werden.");
        }
        
    }
    
    /**
     * Loescht eine Aufgabe und alle zugehoerigen Antworten und token.
     * 
     * @param id Die AufgabenID.
     * @throws Exception 
     */
    public static void loescheAufgabe(long id) throws Exception {
        
        try {
            
            EMH.beginTransaction();
            
            Aufgabe a = EMH.getEntityManager().find(Aufgabe.class, id);
            
            EMH.remove(a);
            
            EMH.commit();
            
        } catch (Exception e) {
            EMH.rollback();
            throw new Exception("Die Aufgabe konnte nicht geloescht werden.");
        }
        
    }
    
    
    //---------------- Test Funktionen ------------------
    
    /**
     * Erstellt ein paar Datenbankeintraege, falls diese noch nicht vorhanden sind.
     * Diese sind:
     * Eine Uni, ein Modul, ein Thema, eine Aufgabe mit 3 Antworten und 2 Token
     * 
     * @throws Exception 
     */
    public static void erstelleEintraege() throws Exception {
        
        String name = "WHS Informatik";
        
        if(DAO.getUniID(name) != -1) {
            return;
        }
        
        try {
            EMH.beginTransaction();
            
            Uni u = new Uni(name);
            
            EMH.persist(u);
            
            Modul m = new Modul(u,"INS", "Internetsprachen");
            
            EMH.persist(m);
            
            Thema t = new Thema(m,"HTML",100);
            
            t.addAufgabe();
            
            EMH.persist(t);
            
            Aufgabe a = new Aufgabe(t,"Mit welchem Tag kann man in HTML "
                    + "bereiche makieren?", 100, "Das Tag "
                            + "ist nur fuer die Darstellung da.", "Schau dir "
                                    + "nochmal alle TAGs auf den Folien an.");
            
            
            Antwort aw1 = new Antwort(a,a.getAnzAntworten(),"<span>",true);
            a.addAntwort();
            
            Antwort aw2 = new Antwort(a,a.getAnzAntworten(),"<pre>",false);
            a.addAntwort();
            
            Antwort aw3 = new Antwort(a,a.getAnzAntworten(),"<figure>",false);
            a.addAntwort();
            
            Token t1 = new Token(a,"tag");
            Token t2 = new Token(a,"html");
            
            EMH.persist(a);
            EMH.persist(aw1);
            EMH.persist(aw2);
            EMH.persist(aw3);
            EMH.persist(t1);
            EMH.persist(t2);
            
            EMH.commit();
        } catch (Exception e) {
            EMH.rollback();
            throw new Exception("Eintraege konnten nich erstellt werden.");
        }
        
    }
    
    
}
