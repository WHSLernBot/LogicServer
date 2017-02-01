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

/**
 * Diese Klasse stellt Methoden zur Verfuegung, um ein JsonObject zu verarbeiten.
 * 
 */
public class Controller {
    
    private static final String METHODE = "methode";
    private static final String METHODE_GIB_AUFGABE = "gibAufgabe";
    private static final String METHODE_SETZE_NAME = "setzeName";
    private static final String METHODE_SPEICHERE_ANTWORT = "speichereAntwort";
    private static final String METHODE_GIB_SELEKTOREN = "gibSelektoren";
    private static final String METHODE_SPEICHERE_NOTE = "speichereNote";
    private static final String METHODE_GIB_INFOS = "gibInfos";
    private static final String METHODE_SETZE_UNI = "setzeUni";
    private static final String METHODE_GIB_UNIS = "gibUnis";
    private static final String METHODE_PRUEFUNG_ANMELDEN = "setzePruefung";
    private static final String METHODE_PRUEFUNG_ABMELDEN = "pruefungAbmelden";
    private static final String METHODE_NEUE_AUFGABE = "neueAufgabe";
    private static final String METHODE_GIB_KLAUSUR_INFOS = "gibKlausurInfos";
    private static final String METHODE_BEWERTE_AUFGABE = "bewerteAufgabe";
    private static final String METHODE_GIB_NICHT_ANGEMELDETE_MODULE = "gibNAModule";
    private static final String METHODE_GIB_ANGEMELDETE_MOULE = "gibAnModule";
    private static final String METHODE_MELDE_MODUL_AN = "meldeFuerModulAn";
    private static final String METHODE_SETZE_INAKTIV = "setzeInaktiv";
    private static final String METHODE_GIB_PRUEFUNGEN = "gibPruefungen";
    
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
    private static final String PERIODE = "pruefungsperiode";
    private static final String PERIODE_JAHR = "pruefungsperiodeJahr";
    
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
                case METHODE_GIB_AUFGABE:
                    Aufgabe aufgabe;
                    if(!json.getAsJsonObject(THEMA_OBJEKT).get(THEMA_ID).isJsonNull()) {
                        LernStatus ls = DAO.gibLernstatus(benutzer, 
                            json.getAsJsonObject(THEMA_OBJEKT).get(THEMA_ID).getAsLong());

                        aufgabe = DAO.gibAufgabe(ls);
                    } else {
                        
                        aufgabe = DAO.gibAufgabe(benutzer, json.getAsJsonObject(THEMA_OBJEKT).get(THEMA_TOKEN).getAsString());
                        
                    }
 
                    //Provisorisch .. aber kommt das vor?
//                    if(aufgabe == null) {
//                        manager.gibBotPool().berechneNeu(benutzer);
//                        sleep(1000 * 30);
//                    } else {
                        MessageCreator.erstelleAufgabenJson(nachricht.getJson(), aufgabe,benutzer);
//                    }

                    break;
                case METHODE_SETZE_NAME:
                    DAO.setzeName(benutzer, json.get(NAME).getAsString());
                    break;
                case METHODE_SPEICHERE_ANTWORT:
                    
                    JsonObject ant = json.getAsJsonObject(ANTWORT_OBJEKT);
                    DAO.speichereAntwort(id,
                            json.get(AUFGABE).getAsLong(),
                            ant.get(ANTWORT_KENNUNG).getAsInt(),
                            ant.get(ANTWORT_NUMMER).getAsShort(),
                            ant.get(ANTWORT_HINWEIS).getAsBoolean());
                    break;
                case METHODE_SPEICHERE_NOTE:
                    DAO.speichereNote(benutzer, json.get(MODUL).getAsString(), json.get(NOTE).getAsShort());
                    break;
                case METHODE_GIB_INFOS:
                    MessageCreator.erstelleBenutzerInfoNachricht(nachricht.getJson(),benutzer);
                    break;
                case METHODE_SETZE_UNI:
                    DAO.setzeUni(benutzer, json.get(UNI_ID).getAsShort());
                    break;
                case METHODE_GIB_UNIS:
                    Collection<Uni> cUnis = DAO.gibUnis();
                    MessageCreator.erstelleUniJson(nachricht.getJson(), cUnis);
                    break;
                case METHODE_GIB_SELEKTOREN:
                    DAO.gibSelektoren(nachricht.getJson(),benutzer);
                    break;
                case METHODE_PRUEFUNG_ANMELDEN:
                    DAO.meldePruefungAn(benutzer, json.get(MODUL).getAsString(),
                            json.get(PERIODE).getAsShort(),
                            json.get(PERIODE_JAHR).getAsShort());
                    break;
                case METHODE_PRUEFUNG_ABMELDEN:
                    DAO.meldePruefungAb(benutzer, json.get(MODUL).getAsString());
                    break;
//                    --------- nicht mehr vorgesehen -----------
//                case METHODE_NEUEAUFGABE:
//                    long thema = json.getAsJsonObject(THEMA_OBJEKT).get(THEMA_ID).getAsLong();
//
//                    DAO.neueAufgabe(benutzer,thema,json.getAsJsonObject(AUFGABE_OBJEKT));
//                    break;
                case METHODE_GIB_KLAUSUR_INFOS:
//                    Klausur klausur = DAO.gibKlausur(id, plattform, 
//                            json.get("modul").getAsString());
//
//                    nachricht = MessageCreator.erstelleKlausurInfoJson(id, 
//                            plattform,witSession, klausur, null);
                    break;
                case METHODE_BEWERTE_AUFGABE:
                    DAO.bewerteAufgabe(benutzer,json.get(AUFGABE).getAsLong(),
                                json.get("bewerte").getAsBoolean());
                    
                    break;
                case METHODE_GIB_NICHT_ANGEMELDETE_MODULE:
                    MessageCreator.erstlleModulListe(json, DAO.gibNichtangemeldete(benutzer));
                    break;
                case METHODE_MELDE_MODUL_AN:
                    DAO.meldeAn(benutzer,json.get(MODUL).getAsString());
                    break;
                case METHODE_SETZE_INAKTIV:
                    DAO.setzeInaktiv(benutzer,json.get(MODUL).getAsString());
                    break;
                case METHODE_GIB_ANGEMELDETE_MOULE:
                    MessageCreator.erstlleModulListe(json, DAO.gibAngemeldete(benutzer));
                    break;
                case METHODE_GIB_PRUEFUNGEN:   
                    MessageCreator.erstlleKlausurListe(json, 
                            DAO.gibPruefungen(benutzer, json.get(MODUL).getAsString()));
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