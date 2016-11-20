package Controller;

import DAO.DAO;
import Entitys.Benutzer;
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

    private CBBenutzer benutzer;
    
    private final MessageCreator messCreator;
    
    private final String witSession;

    public ControllerThread(JsonObject json) {
        this.json = json;
        manager = ChatBotManager.getInstance();
        messCreator = new MessageCreator();
        witSession = "";
    }

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

        //manager.setzeBenutzerAktiv(benutzer);
        switch (json.get("methode").getAsString()) {
            case "gibSession":
                //witSession = dao.gibSession(id, plattform);
                //essCreator.creat(id,plattform,witSession);
                break;
            case "gibAufgabe":               
                DAO.gibAufgabe(benutzer, witSession, witSession);
                break;
            case "setzeName":
                DAO.setzeName(benutzer, witSession);
                break;
            case "speichereAntwort":
                DAO.speichereAntwort(id, plattform, witSession);
                break;
            case "speichereNote":
                DAO.speichereNote(id, plattform, plattform);
                break;
            case "gibInfos":
                
                break;
            case "setzeUni":
                DAO.setzeUni(benutzer, plattform);
                break;
            case "gibUnis":
                DAO.gibUnis();
                break;
            case "setzePruefung":
                DAO.setzePruefung(id, plattform, witSession, witSession);
                break;
            case "neueAufgabe":
                DAO.neueAufgabe(id, plattform, witSession, witSession);
                break;
            case "neuerBenutzer":
                DAO.neuerBenutzer(pt);
                break;
            case "gibKlausurInfos":
                DAO.gibKlausur(id, plattform, witSession, witSession);
                break;
            case "bewerteAufgabe":
                DAO.bewerteAufgabe(id, true);
                break;
                
        }
           
    }
    private CBBenutzer sucheBenutzer(CBPlattform pt) {      
        return DAO.sucheBenutzer(pt);       
    }
}
