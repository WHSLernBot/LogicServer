package Controller;

import DAO.DAO;
import Entitys.Aufgabe;
import Entitys.LernStatus;
import Entitys.Uni;
import Message.MessageCreator;
import Message.Nachricht;
import com.google.gson.JsonObject;
import java.util.Collection;
import main.CBBenutzer;
import main.CBPlattform;
import main.ChatBotManager;
import static java.lang.Thread.sleep;

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
    private static final String METHODE_GIBMODULE = "gibModule";
    private static final String METHODE_MELDEMODULAN = "meldeFuerModulAn";

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
    
    private static final String MODUL = "modul";
    
    private static final String THEMA_OBJEKT = "thema";
    private static final String THEMA_ID = "id";
    private static final String THEMA_TOKEN = "token";
    
    
    /**
     * Diese Methode wird verwendet um das uebergebene JsonObject auzulesen
     * und entsprechende Aktionen auszufuehren.
     * @param json In dem Json sollten alle relevanten Informationen vorhanden sein,
     *      die fuer das weitere vorgehen benoetigt werden.
     * @return Nachricht mit den geforderten Informationen.
     */
    public static Nachricht loese(JsonObject json) {
        ChatBotManager manager = ChatBotManager.getInstance();
        
        JsonObject userOb = json.getAsJsonObject(USER_OBJEKT);

        CBPlattform pt = new CBPlattform(userOb.get(USER_ID).getAsString(),
                userOb.get(USER_PLATTFORM).getAsShort());

        CBBenutzer benutzer = manager.gibBenutzer(pt, json.get(NAME).getAsString() ,userOb.get(USER_SESSION).getAsString());
        
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
                    Aufgabe aufgabe;
                    if(!json.getAsJsonObject(THEMA_OBJEKT).get(THEMA_ID).isJsonNull()) {
                        LernStatus ls = DAO.gibLernstatus(benutzer, 
                            json.getAsJsonObject(THEMA_OBJEKT).get(THEMA_ID).getAsLong());

                        aufgabe = DAO.gibAufgabe(ls);
                    } else {
                        
                        aufgabe = DAO.gibAufgabe(benutzer, json.getAsJsonObject(THEMA_OBJEKT).get(THEMA_TOKEN).getAsString());
                        
                    }
 
                    //Provisorisch .. aber kommt das vor?
                    if(aufgabe == null) {
                        manager.gibBotPool().berechneNeu(benutzer);
                        sleep(1000 * 30);
                    } else {
                        MessageCreator.erstelleAufgabenJson(nachricht.getJson(), aufgabe,benutzer);
                    }

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
//                    --------- nicht mehr vorgesehen -----------
//                case METHODE_NEUEAUFGABE:
//                    long thema = json.getAsJsonObject(THEMA_OBJEKT).get(THEMA_ID).getAsLong();
//
//                    DAO.neueAufgabe(benutzer,thema,json.getAsJsonObject(AUFGABE_OBJEKT));
//                    break;
                case METHODE_GIBKLAUSURINFOS:
//                    Klausur klausur = DAO.gibKlausur(id, plattform, 
//                            json.get("modul").getAsString());
//
//                    nachricht = MessageCreator.erstelleKlausurInfoJson(id, 
//                            plattform,witSession, klausur, null);
                    break;
                case METHODE_BEWERTEAUFGABE:
                    DAO.bewerteAufgabe(benutzer,json.get(AUFGABE).getAsLong(),
                                json.get("bewerte").getAsBoolean());
                    
                    break;
                case METHODE_GIBMODULE:
                    DAO.gibNichtangemeldete(json, benutzer);
                    break;
                case METHODE_MELDEMODULAN:
                    DAO.meldeAn(benutzer,json.get(MODUL).getAsString());
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