package Message;

import com.google.gson.JsonObject;
import java.sql.Timestamp;

/**
 *
 * @author Seve
 */
public class Nachricht {
    
    private JsonObject json;
    
    private Timestamp zeit;

    public Nachricht(JsonObject json, Timestamp start) {
        this.json = json;
        this.zeit = start;
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
    
    
    
}
