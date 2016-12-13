package Controller;

import DAO.DAO;
import Entitys.Aufgabe;
import Entitys.Klausur;
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
 * Diese Klasse stellt Methoden zur Verfügung, um ein JsonObject zu verarbeiten.
 * 
 */
public class Controller {

    private final ChatBotManager manager;

    private CBBenutzer benutzer;

    private final MessageCreator messCreator;
    /**
     * Initialisiert alle Objekte.
     */
    public Controller() {
        manager = ChatBotManager.getInstance();
        messCreator = new MessageCreator();
    }
    /**
     * Diese Methode wird verwendet um das übergebene JsonObject auzulesen
     * und entsprechende Aktionen auszuführen.
     * @param json In dem Json sollten alle relevanten Informationen vorhanden sein,
     *      die für das weitere vorgehen benötigt werden.
     * @return Nachricht mit den geforderten Informationen.
     */
    public Nachricht loese(JsonObject json) {
        Nachricht nachricht = null;
        

        CBPlattform pt = new CBPlattform(json.get("id").getAsString(),
                json.get("plattform").getAsShort());

        benutzer = manager.gibBenutzer(pt);

        //Hier nochmal mit synchronitaet gucken!----------------
        
        long id = benutzer.getBenutzer().getId();
        short plattform = benutzer.getBenutzer().getPlattform().getAdresse().getId();
        String witSession = benutzer.getBenutzer().getPlattform().getWitSession();
                
        /*Es wird kontrolliert welche Methode im Json übergeben wurde und
        dem entsprächend ausgeführt.*/
        try {
            switch (json.get("methode").getAsString()) {
                case "gibAufgabe":

                    LernStatus ls = DAO.gibLernstatus(benutzer, json.get("thema").getAsLong());

    //                Aufgabe aufgabe = DAO.gibAufgabe(benutzer,
    //                         json.get("modul").getAsString(),
    //                         json.get("thema").getAsString());

                    Aufgabe aufgabe = DAO.gibAufgabe(ls);

                    nachricht = messCreator.erstelleAufgabenJson(id, 
                            plattform,witSession, aufgabe, null);
                    break;
                case "setzeName":
                    DAO.setzeName(benutzer, json.get("name").getAsString());
                    break;
                case "speichereAntwort":
                    DAO.speichereAntwort(id, plattform, 
                            json.get("antwort").getAsString());
                    break;
                case "speichereNote":
                    DAO.speichereNote(id, plattform, json.get("note").getAsShort());
                    break;
                case "gibInfos":
                    nachricht = this.messCreator.erstelleBenutzerInfoNachricht(benutzer, null);
                    break;
                case "setzeUni":
                    DAO.setzeUni(benutzer, json.get("uni").getAsShort());
                    break;
                case "gibUnis":
                    Collection<Uni> cUnis = DAO.gibUnis();
                    nachricht = this.messCreator.erstelleUniJason(id, 
                            plattform,witSession, cUnis, null);
                    break;
                case "gibSelektoren":
                    nachricht = new Nachricht(DAO.gibSelektoren(benutzer),null);
                    break;
                case "setzePruefung":
                    DAO.setzePruefung(id, plattform, 
                            json.get("modul").getAsString());
                    break;
                case "neueAufgabe":
    //                DAO.neueAufgabe(id, plattform, witSession, witSession);
                    break;
                case "gibKlausurInfos":
                    Klausur klausur = DAO.gibKlausur(id, plattform, 
                            json.get("modul").getAsString());

                    nachricht = this.messCreator.erstelleKlausurInfoJson(id, 
                            plattform,witSession, klausur, null);
                    break;
                case "bewerteAufgabe":
                    DAO.bewerteAufgabe(id, json.get("bewerte").getAsInt());
                    break;
                    
                    
                default:
                    throw new Exception("Methode konnte nicht ausgewertet werden.");
            }     
        } catch (Exception e) {
//            nachricht = messCreator.
        }
        benutzer.release();
        
        return nachricht;
    }
}