package Message;


import Entitys.Antwort;
import Entitys.Klausur;
import Entitys.Aufgabe;
import Entitys.LernStatus;
import Entitys.Uni;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.LinkedList;
import main.CBBenutzer;
/**
 * Die Klasse MessageCreator stellt Methoden zur Verfügung, um aus Objekten,
 * ein JsonObject zu erzeugen.
 */
public class MessageCreator {

    
//    private static final String USER_OBJEKT = "user";
//    private static final String USER_ID = "id";
//    private static final String USER_PLATTFORM = "plattformID"; //Sinnvoll? Oder Adresse?
//    private static final String USER_SESSION = "witSession";
    
    private static final String AUFGABE_OBJEKT = "aufgabe";
    private static final String AUFGABE_FRAGE = "frage";
    private static final String AUFGABE_VERWEIS = "verweis";
    private static final String AUFGABE_HINWEIS = "hinweis";
    private static final String AUFGABE_ID = "id";
    private static final String AUFGABE_ANTWORT_ARRAY = "antworten";
    
    private static final String AUFGABE_ANTWORT_ANTWORT = "antwort";
    private static final String AUFGABE_ANTWORT_RICHTIG = "richtig";
    private static final String AUFGABE_ANTWORT_NUMMER = "nummer";
    
    private static final String UNIS_ARRAY = "unis";
    private static final String UNIS_NAME = "name";
    private static final String UNIS_ID = "uniID";
    
    private static final String KLAUSUR_OBJEKT = "klausur";
    private static final String KLAUSUR_HILFSMITTEL = "hilfsmittel";
    private static final String KLAUSUR_ORT = "ort";
    private static final String KLAUSUR_DAUER = "dauer";
    private static final String KLAUSUR_PERIODE = "periode";
    private static final String KLAUSUR_UHRZEIT = "uhrzeit";
    
    private static final String NACHRICHT = "nachricht";
    private static final String FEHLER = "nachricht";
    
    private static enum TEXTE {
        AUFGABE,KLAUSUR,UNI,INFO
    };
    
    /**
     * Die Methode erstellt ein JsonObject, für eine übergebene Aufgabe, mit 
     * entsprechenen Aufgabentext.
     * 
     * @param nachricht
     * @param aufgabe Aufgabe für den Nutzer.
     */
    public static void erstelleAufgabenJson(JsonObject nachricht, Aufgabe aufgabe) {
        
        JsonObject jAufgabe = new JsonObject();
        
        jAufgabe.addProperty(AUFGABE_FRAGE, aufgabe.getFrage());
        jAufgabe.addProperty(AUFGABE_VERWEIS, aufgabe.getVerweis());
        jAufgabe.addProperty(AUFGABE_HINWEIS, aufgabe.getHinweis());
        jAufgabe.addProperty(AUFGABE_ID, aufgabe.getAufgabenID());
        
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
        nachricht.addProperty(NACHRICHT, gibText(TEXTE.AUFGABE));
    }
    
    /**
     * Die Methode erstellt ein JsonObject, mit allen Vorhandenen Unis, die übergeben werden.
     * 
     * @param nachricht
     * @param unis Enthält alle Unis, die zur auswahl stehen.
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
        nachricht.addProperty(NACHRICHT, gibText(TEXTE.UNI));
    }
    
    /**
     * Die Methode erstellt ein JsonObject, mit allen Informationen, 
     * die zur Klausur vorhanden sind.
     * ---------------------------Das alles nicht schoener als fertiegn text?
     * 
     * @param nachricht
     * @param klausur Enthält alle Informatonen zur Klausur.
     */
    public static void erstelleKlausurInfoJson(JsonObject nachricht, Klausur klausur) {
        JsonObject jKlausurinfo = new JsonObject();
        
        
        jKlausurinfo.addProperty(KLAUSUR_HILFSMITTEL,klausur.getHilfsmittel());
        jKlausurinfo.addProperty(KLAUSUR_ORT,klausur.getOrt());
        jKlausurinfo.addProperty(KLAUSUR_DAUER,klausur.getDauer());
        //Periode vielleicht als Objet mit infos
        jKlausurinfo.addProperty(KLAUSUR_PERIODE,klausur.getPruefungsperiode().getAnfang().toString());
        jKlausurinfo.addProperty(KLAUSUR_UHRZEIT,klausur.getUhrzeit().toString());
        
        nachricht.add(KLAUSUR_OBJEKT, jKlausurinfo);     
        nachricht.addProperty(NACHRICHT, gibText(TEXTE.INFO));
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
        JsonObject jInfo = new JsonObject();
        JsonObject jUni = new JsonObject();
        
        Collection<LernStatus> stadi = benutzer.getBenutzer().getLernStadi();
        int i = 0;
        for (LernStatus status : stadi) {
            jUni.addProperty("status" + i, status.getSumPunkte());
            i++;
        }
        nachricht.add("info", jInfo);     
        nachricht.addProperty(NACHRICHT, gibText(TEXTE.INFO));
    }
    
    
    /**
     * Erzeugt einen Text, für die entsprechende Methode.
     * @param methode Gibt an, für welche Methode der Text erzeugt werden soll.
     * @return Gibt den entsprechenden Text zurück.
     */
    private static String gibText(TEXTE methode) {
        String text = "Ups... da ist ein fehler unterlaufen!";
        switch(methode) {
            case AUFGABE:
                text = "Hier ist eine neue Aufage: ";
                break;
            case UNI:
                text = "Das sind alle verfügbaren Uni's";
                break;
            case INFO:
                text = "Hier hast du die gewünschten Info's!";
                break;
        }
        return text;
    }
    
    public static void exception(JsonObject nachricht, Exception e) {
        JsonObject jResponse = new JsonObject();
        
        nachricht.addProperty(FEHLER, "Fehler: " + e.getMessage());
    }
    
    /**
     * Fügt eine Nachricht dem ChatBotManager hinzu.
     * @param json Enthält alle wichtigen Informationen.
     * @param time Gibt an, wann eine Nachricht abgeschickt werden soll.
     */
//    private static Nachricht erzeugeNachricht(JsonObject json,Timestamp time){
//        /*
//        time ist wichtig, damit die Nachricht auch noch zu einem späteren 
//        Zeitpunkt abgeschickt werden kann.
//        */
//        Nachricht nachricht = new Nachricht(json,time);
//        return nachricht;
//    }
    
    
}