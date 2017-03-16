package message;


import dao.DAO;
import entitys.Antwort;
import entitys.Klausur;
import entitys.Aufgabe;
import entitys.LernStatus;
import entitys.Modul;
import entitys.Thema;
import entitys.Uni;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import main.CBBenutzer;
/**
 * Die Klasse MessageCreator stellt Methoden zur Verfuegung, um aus Objekten,
 * ein JsonObject zu erzeugen.
 */
public class MessageCreator {

    // Wird in Nachricht eingefuegt
//    private static final String USER_OBJEKT = "user";
//    private static final String USER_ID = "id";
//    private static final String USER_PLATTFORM = "plattformID"; //Sinnvoll? Oder Adresse?
//    private static final String USER_SESSION = "witSession";
    
    private static final String AUFGABE_OBJEKT = "aufgabe";
    private static final String AUFGABE_FRAGE = "frage";
    private static final String AUFGABE_VERWEIS = "verweis";
    private static final String AUFGABE_HINWEIS = "hinweis";
    private static final String AUFGABE_ID = "id";
    private static final String AUFGABE_BEWERTEN = "bewerten";
    private static final String AUFGABE_DARFBEWERTEN = "darfBewerten";
    private static final String AUFGABE_ANTWORT_ARRAY = "antworten";
    
    private static final String AUFGABE_ANTWORT_ANTWORT = "antwort";
    private static final String AUFGABE_ANTWORT_RICHTIG = "richtig";
    private static final String AUFGABE_ANTWORT_NUMMER = "nummer";
    
    private static final String UNIS_ARRAY = "unis";
    private static final String UNIS_NAME = "name";
    private static final String UNIS_ID = "uniID";
    
    
    private static final String MODULE_ARRAY = "module";
    private static final String MODULE_NAME = "name";
    private static final String MODULE_KUERZEL = "kuerzel";
    private static final String MODULE_THEMEN_ARRAY = "themen";
    private static final String MODULE_THEMEN_NAME = "name";
    private static final String MODULE_THEMEN_ID = "id";
    
    private static final String PRUEFUNG_ARRAY = "pruefungen";
    private static final String PRUEFUNG_PERIODE = "periode";
    private static final String PRUEFUNG_JAHR = "jahr";
    private static final String PRUEFUNG_DATUM = "datum";
    private static final String PRUEFUNG_UHRZEIT = "uhrzeit";
    private static final String PRUEFUNG_TYP = "typ";
    private static final String KLAUSUR_OBJEKT = "klausur";
    private static final String KLAUSUR_MODUL = "modul";
            
    private static final String NAME = "username";
    
    private static final String NACHRICHT = "nachricht";
    private static final String FEHLER = "fehler";
    
    private static final String RESULT = "result";
    
    private static final int BEWERTUNG_ABFRAGE = 8;
    
    public static enum TEXTE {
        AUFGABE,KLAUSUR,UNI,INFO
    };

    
    /**
     * Die Methode erstellt ein JsonObject, fuer eine uebergebene Aufgabe, mit 
     * entsprechenen Aufgabentext.
     * 
     * @param nachricht
     * @param aufgabe Aufgabe fuer den Nutzer.
     * @param b Der Benutzer, der die Aufgabe bekommt.
     */
    public static void erstelleAufgabenJson(JsonObject nachricht, Aufgabe aufgabe, CBBenutzer b) {
        
        JsonObject jAufgabe = new JsonObject();
        
        boolean darfBew = DAO.darfBewerten(aufgabe, b);
        
        jAufgabe.addProperty(AUFGABE_FRAGE, aufgabe.getFrage());
        jAufgabe.addProperty(AUFGABE_VERWEIS, aufgabe.getVerweis());
        jAufgabe.addProperty(AUFGABE_HINWEIS, aufgabe.getHinweis());
        jAufgabe.addProperty(AUFGABE_ID, aufgabe.getAufgabenID());
        jAufgabe.addProperty(AUFGABE_DARFBEWERTEN, darfBew);
        
        jAufgabe.addProperty(AUFGABE_BEWERTEN, (darfBew && (aufgabe.getBewertung() <= BEWERTUNG_ABFRAGE)));
        
        Collection<Antwort> antworten = aufgabe.getAntworten();
        
        JsonArray jAnt = new JsonArray();
        LinkedList<JsonObject> antObj = new LinkedList<>();
        
        for(Antwort antwort: antworten){
            
            JsonObject a = new JsonObject();
            a.addProperty(AUFGABE_ANTWORT_ANTWORT,antwort.getAntwort());
            a.addProperty(AUFGABE_ANTWORT_RICHTIG,antwort.getRichtig());
            a.addProperty(AUFGABE_ANTWORT_NUMMER,antwort.getNummer());
            antObj.add(a);
        }
        
        //Durcheinander machen
        while(!antObj.isEmpty()) {
            
            int i = (int) (Math.random() * (antObj.size() - 1));
            jAnt.add(antObj.get(i));
            antObj.remove(i);
            
        }
            
        jAufgabe.add(AUFGABE_ANTWORT_ARRAY, jAnt);
        
        nachricht.add(AUFGABE_OBJEKT,jAufgabe);
//        nachricht.addProperty(NACHRICHT, gibText(TEXTE.AUFGABE));
    }
    
    /**
     * Diese Methode fuegt den Namen des Benutzers in die Nachricht ein.
     * 
     * @param nachricht Das Json.
     * @param benutzer Der Benutzer.
     */
    public static void erstelleNameJson(JsonObject nachricht, CBBenutzer benutzer) {

        
        synchronized(benutzer) {
            
            nachricht.addProperty(NAME, benutzer.getBenutzer().getName());
            
        }
        
    }
    
    public static void erstelleResultFeld(JsonObject nachricht, int nummer) {
        nachricht.addProperty(RESULT, nummer);
    }
    
    public static void erstlleTextNachricht(JsonObject nachricht, String text) {

        nachricht.addProperty(NACHRICHT, text);
        
    }
     
    /**
     * Die Methode erstellt ein JsonObject, mit allen Vorhandenen Unis, die uebergeben werden.
     * 
     * @param nachricht
     * @param unis Enthaelt alle Unis, die zur auswahl stehen.
     */
    public static void erstelleUniJson(JsonObject nachricht,Collection<Uni> unis) {
        
        
        JsonArray jUnis = new JsonArray();
        
        for (Uni u : unis) {
            JsonObject uni = new JsonObject();
            
            uni.addProperty(UNIS_NAME, u.getName());
            uni.addProperty(UNIS_ID, u.getId());
            
            jUnis.add(uni);
        }
           
        nachricht.add(UNIS_ARRAY, jUnis);
//        nachricht.addProperty(NACHRICHT, gibText(TEXTE.UNI));
    }
    
    /**
     * Die Methode erstellt ein JsonObject, mit allen Informationen, 
     * die zur Klausur vorhanden sind.
     * 
     * 
     * @param nachricht
     * @param klausur Enthaelt alle Informatonen zur Klausur.
     */
    public static void erstelleKlausurInfoJson(JsonObject nachricht, Klausur klausur) {
        
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
        
        String text = klausur.getModul().getKuerzel() + " Klausur in der " 
                + klausur.getPruefungsperiode().getPhase() + ". Pruefungsperiode :\n\n";
        
        text += "Datum: " + sdfDate.format(klausur.getDatum()) + "\n";
        text += "Uhrzeit: " + sdfTime.format(klausur.getUhrzeit()) + "\n";
        text += "Raum: " + klausur.getOrt() + "\n";
        text += "Dauer: " + klausur.getDauer() + "\n";
        text += "Typ: " + klausur.getTyp() + "\n";
        text += "Hilfsmittel: " + klausur.getHilfsmittel();
  
        nachricht.addProperty(NACHRICHT, text);
    }
    
    /**
     * Die Methode erstellt ein JsonObject, mit allen Informationen, 
     * die zu dem Benutzer vorhanden sind.
     * --------------------Vielleicht den text dynamischer erstellen
     *
     * @param nachricht
     * @param benutzer
     */
    public static void erstelleBenutzerInfoNachricht(JsonObject nachricht, CBBenutzer benutzer) {
        
        String text = "Angemeldet fuer die Module:\n\n";
        
        LinkedList<String> module = new LinkedList<>();
        
        boolean komma = false;
        
        for(Object o : DAO.gibAngemeldete(benutzer)) {
            
            if(komma) {
                text += ", ";
            } else {
                komma = true;
            }
            
            String krz =((Modul) o).getKuerzel();
            
            module.add(krz);
            
            text += krz;
            
        }
        
        text += "\n angemeldet fuer die Klausuren:\n\n";
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        for(String s : module) {
            
            Klausur k = DAO.gibKlausur(benutzer, s);
            
            if(k != null) {
                text += s + " : " + sdf.format(k.getDatum()) + "\n";
            }
            
        }

        nachricht.addProperty(NACHRICHT, text);
    }
    
    /**
     * Diese Methode erstellt einen genauen ueberblick ueber alle Themen eines 
     * Moduls und wie gut sie der Benutzer bearbeitet hat.
     * 
     * 
     * @param nachricht
     * @param b
     * @param modul 
     */
    public static void erstelleModulInfoNachricht(JsonObject nachricht, CBBenutzer b, String modul) {
        
        List stadi = DAO.gibLernstadi(b, modul);
        
        String textmodul = " -- " + modul + " -- \n";
         
        String text = "";
        int prozent = 0;
        
        for(Object o : stadi) {
            
            LernStatus s = (LernStatus) o;
            Thema t = s.getThema();
            
            text = text + t.getName() + ":\n\t" + s.getRichtige() 
                    + "/" + s.getGeloest() + " richtig geloest\n\t"
                    + "zu " + s.getSumPunkte() + "% das Thema abgeschlossen\n\n";
            
            prozent = s.getSumPunkte() * t.getAnteil();
            
        }
        
        short note = DAO.gibErgebniss(b, modul,prozent);
        
        textmodul += "Unseren Berechnungen nach haben sie das Modul zu" 
                + prozent + "% abgeschlossen.";
        
        if(note != 0) {
            textmodul += " Die Klausurnote aehnlicher Benutzer war :" + note / 10 + "," + note % 10;
        }
        
        nachricht.addProperty(NACHRICHT, textmodul + "\n\n" + text);
    }
    
    
    /**
     * Erstellt aus einer Liste aus Modulen alle Module fuer das JSON.
     * 
     * @param nachricht
     * @param mo 
     */
    public static void erstlleModulListe(JsonObject nachricht, List mo) {
        JsonArray module = new JsonArray();
        for(Object o : mo) {
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
     * @param nachricht
     * @param klausuren
     */
    public static void erstlleKlausurListe(JsonObject nachricht, List<Klausur> klausuren) {
        JsonArray pruefungen = new JsonArray();
        
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
        
        String kuerzel = "";
        
        for(Klausur k : klausuren) {
            
            if(kuerzel.equals("")) {
                kuerzel = k.getModul().getKuerzel();
            }
            
            JsonObject p = new JsonObject();
            
            p.addProperty(PRUEFUNG_PERIODE, k.getPruefungsperiode().getPhase());
            p.addProperty(PRUEFUNG_JAHR, k.getPruefungsperiode().getJahr());
            p.addProperty(PRUEFUNG_DATUM, sdfDate.format(k.getDatum()));
            p.addProperty(PRUEFUNG_UHRZEIT, sdfTime.format(k.getUhrzeit()));
            p.addProperty(PRUEFUNG_TYP, k.getTyp());
            
            pruefungen.add(p);
        }
        
        JsonObject klausur = new JsonObject();
        klausur.addProperty(KLAUSUR_MODUL, kuerzel);
        klausur.add(PRUEFUNG_ARRAY, pruefungen);
        
        nachricht.add(KLAUSUR_OBJEKT, klausur);
        
    }
    
//    /**
//     * Erzeugt einen Text, fuer die entsprechende Methode.
//     * @param methode Gibt an, fuer welche Methode der Text erzeugt werden soll.
//     * @return Gibt den entsprechenden Text zurueck.
//     */
//    private static String gibText(TEXTE methode) {
//        String text = "Ups... da ist ein fehler unterlaufen!";
//        switch(methode) {
//            case AUFGABE:
//                text = "Hier ist eine neue Aufage: ";
//                break;
//            case UNI:
//                text = "Das sind alle verfuegbaren Uni's";
//                break;
//            case INFO:
//                text = "Hier hast du die gewuenschten Info's!";
//                break;
//        }
//        return text;
//    }
    
    public static void exception(JsonObject nachricht, Exception e) {
        
        nachricht.addProperty(FEHLER, "Fehler: " + e.getMessage());
    }
    
    /**
     * Fuegt eine Nachricht dem ChatBotManager hinzu.
     * @param json Enthaelt alle wichtigen Informationen.
     * @param time Gibt an, wann eine Nachricht abgeschickt werden soll.
     */
//    private static Nachricht erzeugeNachricht(JsonObject json,Timestamp time){
//        /*
//        time ist wichtig, damit die Nachricht auch noch zu einem spaeteren 
//        Zeitpunkt abgeschickt werden kann.
//        */
//        Nachricht nachricht = new Nachricht(json,time);
//        return nachricht;
//    }
    
    
}