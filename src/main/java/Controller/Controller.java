package Controller;

import DAO.DAO;
import Entitys.Aufgabe;
import Entitys.LernStatus;
import Entitys.Uni;
import Message.MessageCreator;
import Message.Nachricht;
import com.google.gson.JsonObject;
import static java.lang.Thread.sleep;
import java.util.Collection;
import main.CBBenutzer;
import main.CBPlattform;
import main.ChatBotManager;

/**
 * Diese Klasse stellt Methoden zur Verfuegung, um ein JsonObject zu verarbeiten.
 * 
 */
public class Controller {
    
    private static final String METHODE = "methode";
    private static final String METHODE_GIBAUFGABE = "gibAufgabe";
    private static final String METHODE_SETZENAME = "setzeName";
    private static final String METHODE_SPEICHEREANTWORT = "speichereAntwort";
    private static final String METHODE_GIBSELEKTOREN = "gibSelektoren";
    private static final String METHODE_SPEICHERENOTE = "speichereNote";
    private static final String METHODE_GIBINFOS = "gibInfos";
    private static final String METHODE_SETZEUNI = "setzeUni";
    private static final String METHODE_GIBUNIS = "gibUnis";
    private static final String METHODE_SETZEPRUEFUNG = "setzePruefung";
    private static final String METHODE_NEUEAUFGABE = "neueAufgabe";
    private static final String METHODE_GIBKLAUSURINFOS = "gibKlausurInfos";
    private static final String METHODE_BEWERTEAUFGABE = "bewerteAufgabe";

    private static final String USER_OBJEKT = "user";
    private static final String USER_ID = "userID";
    private static final String USER_PLATTFORM = "plattformID";
    private static final String USER_SESSION = "witSession";
    
    private static final String ANTWORT_OBJEKT = "antwort";
    private static final String ANTWORT_TEXT = "text";
    private static final String ANTWORT_KENNUNG = "kennung";
    private static final String ANTWORT_NUMMER = "nummer";
    private static final String ANTWORT_HINWEIS = "hinweis";
    
    private static final String AUFGABE_OBJEKT = "aufgabe";
    
    private static final String UNI_ID = "uniID";
    
    private static final String NOTE = "userNote";
    
    private static final String NAME = "username";
    
    private static final String AUFGABE = "aufgabenID";
    
    private static final String THEMA_OBJEKT = "thema";
    private static final String THEMA_ID = "id";
    
    
    /**
     * Diese Methode wird verwendet um das uebergebene JsonObject auzulesen
     * und entsprechende Aktionen auszufuehren.
     * @param json In dem Json sollten alle relevanten Informationen vorhanden sein,
     *      die fuer das weitere vorgehen benoetigt werden.
     * @return Nachricht mit den geforderten Informationen.
     */
    public static Nachricht loese(JsonObject json) {
        ChatBotManager manager = ChatBotManager.getInstance();
        
         

        CBPlattform pt = new CBPlattform(json.getAsJsonObject(USER_OBJEKT).get(USER_ID).getAsString(),
                json.getAsJsonObject(USER_OBJEKT).get(USER_PLATTFORM).getAsShort());

        CBBenutzer benutzer = manager.gibBenutzer(pt);
        Nachricht nachricht = new Nachricht(benutzer);
        
        
        long id;
        synchronized(benutzer) {
            id = benutzer.getBenutzer().getId();
        }
        
                
        /*Es wird kontrolliert welche Methode im Json uebergeben wurde und
        dem entsprechend ausgefuehrt.*/
        try {
            switch (json.get(METHODE).getAsString()) {
                case METHODE_GIBAUFGABE:

                    LernStatus ls = DAO.gibLernstatus(benutzer, 
                            json.getAsJsonObject(THEMA_OBJEKT).get(THEMA_ID).getAsLong());

                    Aufgabe aufgabe = DAO.gibAufgabe(ls);
                    
                    //Provisorisch
                    if(aufgabe == null) {
                        manager.gibBotPool().berechneNeu(benutzer);
                        sleep(1000 * 30);
                    }

                    MessageCreator.erstelleAufgabenJson(nachricht.getJson(), aufgabe);
                    break;
                case METHODE_SETZENAME:
                    DAO.setzeName(benutzer, json.get(NAME).getAsString());
                    break;
                case METHODE_SPEICHEREANTWORT:
                    
                    JsonObject ant = json.getAsJsonObject(ANTWORT_OBJEKT);
                    DAO.speichereAntwort(id,
                            json.get(AUFGABE).getAsLong(),
                            ant.get(ANTWORT_KENNUNG).getAsInt(),
                            ant.get(ANTWORT_NUMMER).getAsShort(),
                            ant.get(ANTWORT_HINWEIS).getAsBoolean());
                    break;
                case METHODE_SPEICHERENOTE:
//                    DAO.speichereNote(id, plattform, json.get(NOTE).getAsShort());
                    break;
                case METHODE_GIBINFOS:
                    MessageCreator.erstelleBenutzerInfoNachricht(nachricht.getJson(),benutzer);
                    break;
                case METHODE_SETZEUNI:
                    DAO.setzeUni(benutzer, json.get(UNI_ID).getAsShort());
                    break;
                case METHODE_GIBUNIS:
                    Collection<Uni> cUnis = DAO.gibUnis();
                    MessageCreator.erstelleUniJson(nachricht.getJson(), cUnis);
                    break;
                case METHODE_GIBSELEKTOREN:
                    DAO.gibSelektoren(nachricht.getJson(),benutzer);
                    break;
                case METHODE_SETZEPRUEFUNG:
//                    DAO.setzePruefung(id, plattform, 
//                            json.get("modul").getAsString());
                    break;
                case METHODE_NEUEAUFGABE:
                    //Hier gucken wer darf und wer nicht
                    long thema = json.getAsJsonObject(THEMA_OBJEKT).get(THEMA_ID).getAsLong();
                    
                    DAO.neueAufgabe(thema,json.getAsJsonObject(AUFGABE_OBJEKT));
                    break;
                case METHODE_GIBKLAUSURINFOS:
//                    Klausur klausur = DAO.gibKlausur(id, plattform, 
//                            json.get("modul").getAsString());
//
//                    nachricht = MessageCreator.erstelleKlausurInfoJson(id, 
//                            plattform,witSession, klausur, null);
                    break;
                case METHODE_BEWERTEAUFGABE:
                    //Es gilt zu pruefen ob der benutzer das auch darf
                    
                    DAO.bewerteAufgabe(json.get(AUFGABE).getAsLong(), json.get("bewerte").getAsBoolean());
                    break;
                default:
                    throw new Exception("Methode konnte nicht ausgewertet werden.");
            }     
        } catch (Exception e) {
            MessageCreator.exception(nachricht.getJson(),e);
        }
        benutzer.release();
        
        return nachricht;
    }
}