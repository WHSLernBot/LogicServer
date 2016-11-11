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
public class ControllerThread implements Runnable{
    
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
            sucheBenutzer(pt);
        }
        
    }
    
    private void sucheBenutzer(CBPlattform pt) {
        
        
        
    }
}
