package Controller;

import DAO.DAO;
import Entitys.Aufgabe;
import Entitys.Uni;
import Message.MessageCreator;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
        HashMap jsonAttribute = new HashMap();
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
                //witSession
                //essCreator.creat(id,plattform,witSession);
                break;
            case "gibAufgabe":               
                Aufgabe aufgabe = DAO.gibAufgabe(benutzer, json.get("modul").getAsString(), json.get("thema").getAsString());
                messCreator.erstelleAufgabenText(id,plattform, aufgabe);
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
                Collection<Uni> alUnis = DAO.gibUnis();
                
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
                DAO.gibKlausur(id, plattform, json.get("modul").getAsString(), json.get("thema").getAsString());
                break;
            case "bewerteAufgabe":
                DAO.bewerteAufgabe(id, json.get("bewerte").getAsBoolean());
                break;
                
        }         
    }
    private CBBenutzer sucheBenutzer(CBPlattform pt) {      
        return DAO.sucheBenutzer(pt);       
    }
}
