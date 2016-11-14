package Controller;

import DAO.DAO;
import Message.MessageCreator;
import com.google.gson.JsonObject;
import main.CBBenutzer;
import main.CBPlattform;
import main.ChatBotManager;

/**
 *
 * @author Sebastian
 */
public class ControllerThread implements Runnable {

    private final JsonObject json;

    private final ChatBotManager manager;

    private final DAO dao;

    private CBBenutzer benutzer;
    
    private final MessageCreator messCreator;

    public ControllerThread(JsonObject json) {
        this.json = json;
        manager = ChatBotManager.getInstance();
        dao = DAO.getInstance();
        messCreator = new MessageCreator();
    }

    @Override
    public void run() {
        long id = json.get("id").getAsLong();
        short plattform = json.get("plattform").getAsShort();

        CBPlattform pt = new CBPlattform(json.get("id").getAsLong(),
                json.get("plattform").getAsShort());

        benutzer = manager.gibBenutzer(pt);

        if (benutzer == null) {
            benutzer = sucheBenutzer(pt);
        }

        //manager.setzeBenutzerAktiv(benutzer);
        switch (json.get("methode").getAsString()) {
            case "gibSession":
                String witSession;
                witSession = dao.gibSession(id, plattform);
                messCreator.creat(id,plattform,witSession);
                break;
            case "gibAufgabe": 
                dao.gibAufgabe(id,plattform, json.get("modul").getAsString(), json.get("thema").getAsString());
                break;
            case "setzeName":
                dao.setzeName(id, plattform, json.get("name").getAsString());
                break;
            case "speichereAntwort":
                dao.speichereAntwort(id,plattform, json.get("antwort").getAsString());
                break;
            case "speichereNote":
                dao.speichereNote(id,plattform, json.get("note").getAsShort());
                break;
            case "gibInfos":
                dao.gibInfos(id,plattform, json.get("modul").getAsString(), json.get("thema").getAsString());
                break;
            case "setzeUni":
                dao.setzeUni(id,plattform, json.get("uni").getAsShort());
                break;
            case "gibUnis":
                dao.gibUnis();
                break;
            case "setzePruefung":
                dao.setzePruefung(id, plattform, json.get("modul").getAsString(), json.get("datum").getAsString());
                break;
            case "neueAufgabe":
                dao.neueAufgabe(id,plattform, json.get("modul").getAsString(), json.get("thema").getAsString());
                break;
            case "neuerBenutzer":
                dao.neuerBenutzer(id,plattform);
                break;
            case "gibKlausurInfos":
                dao.gibKlausurInfos(id, plattform, json.get("modul").getAsString(), json.get("datum").getAsString());
                break;
            case "bewerteAufgabe":
                dao.bewerteAufgabe(id, plattform, json.get("aufgabenID").getAsShort(),json.get("like").getAsBoolean());
                break;
                
        }
           
    }

    private CBBenutzer sucheBenutzer(CBPlattform pt) {
        return null; //dao.gibBenutzer(pt);       
    }
}
