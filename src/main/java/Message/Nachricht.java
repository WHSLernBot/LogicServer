package Message;

import com.google.gson.JsonObject;
import java.sql.Timestamp;
import main.CBBenutzer;

/**
 * Diese Klasse stellt eine Nachricht da, die an den Benutzer gesendet wird.
 * Sie generiert automatisch das user Objekt im json und kann zu einer 
 * bestimmten zeit gesendet werden.
 * 
 * @author Seve
 */
public class Nachricht {
    
    private static final String USER_OBJEKT = "user";
    private static final String USER_ID = "id";
    private static final String USER_PLATTFORM = "plattformID";
    private static final String USER_SESSION = "witSession";
    
    private JsonObject json;
    
    private Timestamp zeit;
    
    private final String adresse;
    
    private int ttl;

    /**
     * Erstellt eine neue Nachricht.
     * 
     * @param benutzer Der Benutzer, der die Nachricht erhaten soll.
     */
    public Nachricht(CBBenutzer benutzer) {
        
        this.json = new JsonObject();
        
        JsonObject jUser = new JsonObject();
       
        
        if(benutzer != null) {
        synchronized(benutzer) {
            jUser.addProperty(USER_ID, benutzer.getBenutzer().getPlattform().getPfID());
            jUser.addProperty(USER_PLATTFORM, benutzer.getBenutzer().getPlattform().getAdresse().getId());
            jUser.addProperty(USER_SESSION, benutzer.getBenutzer().getPlattform().getWitSession());
            this.adresse = benutzer.getBenutzer().getPlattform().getAdresse().getAdresse();
        } 


        this.json.add(USER_OBJEKT, jUser);
        } else {
              adresse = null;   
                }
        this.zeit = null;
        ttl = 3;
        
    }

    public JsonObject getJson() {
        return json;
    }

    public void setJson(JsonObject json) {
        this.json = json;
    }

    public Timestamp getZeit() {
        return zeit;
    }

    public void setZeit(Timestamp start) {
        this.zeit = start;
    }
    
    public void gesendet() {
        ttl--;
    }
    
    /**
     * Sagt aus, ob die nachricht schon abgelaufen ist.
     * @return falls true, so soll die Nachricht nochmal versendet werden
     */
    public boolean alive() {
        return (ttl > 0);
    }
    
    public int getTTL() {
        return ttl;
    }

    public String getAdresse() {
        return adresse;
    }
    
}
