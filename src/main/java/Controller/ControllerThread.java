package Controller;

import DAO.DAO;
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

    public ControllerThread(JsonObject json) {
        this.json = json;
        manager = ChatBotManager.getInstance();
        dao = DAO.getInstance();
    }

    @Override
    public void run() {
        System.out.println(json.get("id"));

        CBPlattform pt = new CBPlattform(json.get("id").getAsLong(),
                json.get("plattform").getAsInt());

        benutzer = manager.gibBenutzer(pt);

        if (benutzer == null) {
            benutzer = sucheBenutzer(pt);
        }

        //Manager.setzeBenutzerAktiv(benutzer);
        switch (json.get("Methode").toString()) {
            case "gibAufgabe()":
                //doa.gibAufgabe(pt);
                break;
            case "gibInfo()":
                //doa.gibInfo(pt);
                break;
            case "gibSession()":
                //doa.gibSession();
                break;
            case "setzeAntwort()":
                //doa.setzteAntwort(json.get("Antwort").toString());
                break;
            case "setzeNote()":
                //doa.setzteNote(json.get("Note").getAsShort());
                break;
            case "setzeName()":
                //doa.setzeName(json.get("Name").toString);
                break;
        }
        
        erstelleJSON();
    }

    private CBBenutzer sucheBenutzer(CBPlattform pt) {
        return null; //dao.gibBenutzer(pt);       
    }

    private JsonObject erstelleJSON() {
        JsonObject jsonNeu = null;

        return jsonNeu;
    }
}
