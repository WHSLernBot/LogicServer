package Controller;

import DAO.DAO;
import Entitys.Aufgabe;
import Entitys.Klausur;
import Entitys.Uni;
import Message.MessageCreator;
import com.google.gson.JsonObject;
import java.util.Collection;
import main.CBBenutzer;
import main.CBPlattform;
import main.ChatBotManager;

/**
 * Diese Klasse stellt Methoden zur Verfügung, um ein JsonObject zu verarbeiten.
 * 
 */
public class ControllerThread implements Runnable {

    private final JsonObject json;

    private final ChatBotManager manager;

    private CBBenutzer benutzer;

    private final MessageCreator messCreator;

    private final String witSession;

    /**
     * Konstruktor, dem ein JsonObjekt übergeben wird.
     * @param json Enthält alle wichtigen Informationen.
     */
    public ControllerThread(JsonObject json) {
        this.json = json;
        manager = ChatBotManager.getInstance();
        messCreator = new MessageCreator();
        witSession = "";
    }
    /**
     * Führt den Thread aus.
     */
    @Override
    public void run() {
        long id = json.get("id").getAsLong();
        short plattform = json.get("plattform").getAsShort();

        CBPlattform pt = new CBPlattform(json.get("id").getAsString(),
                json.get("plattform").getAsShort());

        benutzer = manager.gibBenutzer(pt);

        if (benutzer == null) {
            benutzer = sucheBenutzer(pt);
        }

        //manager.setzeBenutzer(benutzer); Benutzer bei dem Manager als Aktiv melden        
        
        //Es wird kontrolliert welche Methode im Json übergeben wurde und
        //dem entsprächend ausgeführt.
        switch (json.get("methode").getAsString()) {
            case "gibSession":
                //witSession
                //essCreator.creat(id,plattform,witSession);
                break;
            case "gibAufgabe":
                Aufgabe aufgabe = DAO.gibAufgabe(benutzer,
                         json.get("modul").getAsString(),
                         json.get("thema").getAsString());

                messCreator.erstelleAufgabenJson(id, plattform, aufgabe);
                break;
            case "setzeName":
                DAO.setzeName(benutzer, json.get("name").getAsString());
                break;
            case "speichereAntwort":
                DAO.speichereAntwort(id, plattform, json.get("antwort").getAsString());
                break;
            case "speichereNote":
                DAO.speichereNote(id, plattform, json.get("note").getAsShort());
                break;
            case "gibInfos":

                break;
            case "setzeUni":
                DAO.setzeUni(benutzer, json.get("uni").getAsShort());
                break;
            case "gibUnis":
                Collection<Uni> colUnis = DAO.gibUnis();
                this.messCreator.erstelleUniJason(id, plattform, colUnis);
                break;
            case "setzePruefung":
                DAO.setzePruefung(id, plattform, json.get("modul").getAsString(), json.get("thema").getAsString());
                break;
            case "neueAufgabe":
                DAO.neueAufgabe(id, plattform, witSession, witSession);
                break;
            case "neuerBenutzer":
                //DAO.neuerBenutzer(pt);
                break;
            case "gibKlausurInfos":
                Klausur klausur = DAO.gibKlausur(id, plattform, json.get("modul").getAsString(), json.get("thema").getAsString());
                this.messCreator.erstelleKlausurInfoJson(id, plattform, klausur);
                break;
            case "bewerteAufgabe":
                DAO.bewerteAufgabe(id, json.get("bewerte").getAsBoolean());
                break;

        }
    }

    /**
     * Diese Methode kontrolliert, ob ein Benutzer in der Datenbank vorhanden
     * ist.
     *
     * @param pt Plattformid zum suchen des Benutzers.
     * @return Es wird ein CBBenutzer zurück gegeben.
     */
    private CBBenutzer sucheBenutzer(CBPlattform pt) {
        return new CBBenutzer(DAO.sucheBenutzer(pt));
    }
}
