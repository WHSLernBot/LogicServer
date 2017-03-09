package Controller;

import DAO.DAO;
import Entitys.Aufgabe;
import Entitys.Klausur;
import Entitys.LernStatus;
import Entitys.Uni;
import Message.MessageCreator;
import Message.Nachricht;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Collection;
import java.util.LinkedList;
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
    private static final String METHODE_GIB_NAME = "gibName";
    private static final String METHODE_SPEICHERE_ANTWORT = "speichereAntwort";
    private static final String METHODE_GIB_SELEKTOREN = "gibSelektoren";
    private static final String METHODE_SPEICHERE_NOTE = "speichereNote";
    private static final String METHODE_GIB_INFOS = "gibInfos";
    private static final String METHODE_SETZE_UNI = "setzeUni";
    private static final String METHODE_GIB_UNIS = "gibUnis";
    private static final String METHODE_PRUEFUNG_ANMELDEN = "setzePruefung";
    private static final String METHODE_PRUEFUNG_ABMELDEN = "pruefungAbmelden";
//    private static final String METHODE_NEUE_AUFGABE = "neueAufgabe";
    private static final String METHODE_GIB_KLAUSUR_INFOS = "gibKlausurInfos";
    private static final String METHODE_GIB_MODUL_INFOS = "gibModulInfos";
    private static final String METHODE_BEWERTE_AUFGABE = "bewerteAufgabe";
    private static final String METHODE_GIB_NICHT_ANGEMELDETE_MODULE = "gibNAModule";
    private static final String METHODE_GIB_ANGEMELDETE_MOULE = "gibAnModule";
    private static final String METHODE_MELDE_MODUL_AN = "meldeFuerModulAn";
    private static final String METHODE_SETZE_INAKTIV = "setzeInaktiv";
    private static final String METHODE_GIB_PRUEFUNGEN = "gibPruefungen";
    private static final String METHODE_NEUE_VERBINDUNG = "neueVerbindung";
    
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
    
    private static final String ADRESSE_OBJEKT = "adresse";
    private static final String ADRESSE_URL = "url";
    private static final String ADRESSE_NUMMER = "nummer";
    
    private static final String AUFGABE = "aufgabenID";
    
    private static final String LIKE = "likeAufgabe";
    
    private static final String MODUL = "modul";
    private static final String MODUL_ARRAY = "module";
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
        String methode = json.get(METHODE).getAsString();
        
        ChatBotManager manager = ChatBotManager.getInstance();
        
        
        
        JsonObject userOb = json.getAsJsonObject(USER_OBJEKT);

        CBPlattform pt = new CBPlattform(userOb.get(USER_ID).getAsString(),
                userOb.get(USER_PLATTFORM).getAsShort());
        
        

        CBBenutzer benutzer = manager.gibBenutzer(pt,userOb.get(USER_SESSION).getAsString());
        
       
        
        Nachricht nachricht = new Nachricht(benutzer);
        
                
        /*Es wird kontrolliert welche Methode im Json uebergeben wurde und
        dem entsprechend ausgefuehrt.*/
        try {
            switch (methode) {
                case METHODE_GIB_AUFGABE:
                    Aufgabe aufgabe;
                    if(!json.getAsJsonObject(THEMA_OBJEKT).get(THEMA_ID).isJsonNull()) {
                        LernStatus ls = DAO.gibLernstatus(benutzer, 
                            json.getAsJsonObject(THEMA_OBJEKT).get(THEMA_ID).getAsLong());

                        aufgabe = DAO.gibAufgabe(ls);
                    } else {
                        
                        LinkedList<String> token = new LinkedList<>();
                        
                        JsonArray jt = json.getAsJsonObject(THEMA_OBJEKT).getAsJsonArray(THEMA_TOKEN);
                        for(JsonElement js : jt) {
                            token.add(js.getAsString());
                        }
                        
                        aufgabe = DAO.gibAufgabe(benutzer, token);
                        
                    }
 
                    if(aufgabe == null) {
                        throw new Exception("Es konnte keine entsprechende Aufgabe gefunden werden.");
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
                    MessageCreator.erstelleResultFeld(nachricht.getJson(), 200);
                    break;
                case METHODE_GIB_NAME:
                    MessageCreator.erstelleNameJson(nachricht.getJson(),benutzer);
                    break;
                case METHODE_SPEICHERE_ANTWORT:
                    
                    JsonObject ant = json.getAsJsonObject(ANTWORT_OBJEKT);
                    DAO.speichereAntwort(benutzer,
                            ant.get(AUFGABE).getAsLong(),
                            ant.get(ANTWORT_KENNUNG).getAsInt(),
                            ant.get(ANTWORT_NUMMER).getAsShort(),
                            ant.get(ANTWORT_HINWEIS).getAsBoolean());
                    
                    MessageCreator.erstelleResultFeld(nachricht.getJson(), 200);
                    break;
                case METHODE_SPEICHERE_NOTE:
                    DAO.speichereNote(benutzer, json.get(MODUL).getAsString(), json.get(NOTE).getAsShort());
                    MessageCreator.erstelleResultFeld(nachricht.getJson(), 200);
                    break;
                case METHODE_GIB_INFOS:
                    MessageCreator.erstelleBenutzerInfoNachricht(nachricht.getJson(),benutzer);
                    break;
                case METHODE_SETZE_UNI:  
                    DAO.setzeUni(benutzer, json.get(UNI_ID).getAsShort());
                    
                    MessageCreator.erstelleResultFeld(nachricht.getJson(), 200);
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
                    
                    MessageCreator.erstelleResultFeld(nachricht.getJson(), 200);
                    break;
                case METHODE_PRUEFUNG_ABMELDEN:
                    DAO.meldePruefungAb(benutzer, json.get(MODUL).getAsString());
                    MessageCreator.erstelleResultFeld(nachricht.getJson(), 200);
                    break;
//                    --------- nicht mehr vorgesehen -----------
//                case METHODE_NEUEAUFGABE:
//                    long thema = json.getAsJsonObject(THEMA_OBJEKT).get(THEMA_ID).getAsLong();
//
//                    DAO.neueAufgabe(benutzer,thema,json.getAsJsonObject(AUFGABE_OBJEKT));
//                    break;
                case METHODE_GIB_KLAUSUR_INFOS:
                    Klausur klausur = DAO.gibKlausur(benutzer,
                            json.get(MODUL).getAsString());

                    MessageCreator.erstelleKlausurInfoJson(nachricht.getJson(), klausur);
                    break;
                case METHODE_GIB_MODUL_INFOS:
                    
                    MessageCreator.erstelleModulInfoNachricht(nachricht.getJson(), benutzer, json.get(MODUL).getAsString());
                    break;
                case METHODE_BEWERTE_AUFGABE:
                    DAO.bewerteAufgabe(benutzer,json.get(AUFGABE).getAsLong(),
                                json.get(LIKE).getAsBoolean());
                    
                    MessageCreator.erstelleResultFeld(nachricht.getJson(), 200);
                    break;
                case METHODE_GIB_NICHT_ANGEMELDETE_MODULE:
                    MessageCreator.erstlleModulListe(nachricht.getJson(), DAO.gibNichtangemeldete(benutzer));
                    break;
                case METHODE_MELDE_MODUL_AN:
                    
                    
                    String fehler = "";
                    for(JsonElement je : json.get(MODUL_ARRAY).getAsJsonArray()) {
                        
                        try {
                            
                            DAO.meldeAn(benutzer,je.getAsString());
                        } catch (Exception e) {
                            fehler += e.getMessage();
                        }
                        
                    }
                    
                    if(!fehler.equals("")) {
                        throw new Exception(fehler);
                    }
                    
                    MessageCreator.erstelleResultFeld(nachricht.getJson(), 200);
                    
                    break;
                case METHODE_SETZE_INAKTIV:
                    DAO.setzeInaktiv(benutzer,json.get(MODUL).getAsString());
                    
                    MessageCreator.erstelleResultFeld(nachricht.getJson(), 200);
                    break;
                case METHODE_GIB_ANGEMELDETE_MOULE:
                    MessageCreator.erstlleModulListe(nachricht.getJson(), DAO.gibAngemeldete(benutzer));
                    break;
                case METHODE_GIB_PRUEFUNGEN:   
                    MessageCreator.erstlleKlausurListe(nachricht.getJson(), 
                            DAO.gibPruefungen(benutzer, json.get(MODUL).getAsString()));
                    break;
                case METHODE_NEUE_VERBINDUNG:
                    JsonObject ad = json.get(ADRESSE_OBJEKT).getAsJsonObject();
                    
                    String s = DAO.neueVerbindung(ad.get(ADRESSE_NUMMER).getAsShort(),ad.get(ADRESSE_URL).getAsString());
                    
                    MessageCreator.erstlleTextNachricht(nachricht.getJson(), s);
                    break;
                default:
                    throw new Exception("Methode konnte nicht ausgewertet werden.");
            }     
        } catch (Exception e) {
            MessageCreator.erstelleResultFeld(nachricht.getJson(), 500);
            MessageCreator.exception(nachricht.getJson(),e);
        }
        benutzer.release();
    
        return nachricht;
    }
    
}