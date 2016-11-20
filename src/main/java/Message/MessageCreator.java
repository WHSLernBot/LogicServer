package Message;

import com.google.gson.JsonObject;
import java.util.HashMap;


/**
 *
 * @author Seve
 */
public class MessageCreator {
    
    public JsonObject erstelleText(HashMap hashmap){
        JsonObject json = new JsonObject();
        
        if(hashmap.containsKey("id")){
            json.addProperty("id", hashmap.get("id").toString());
        }
        
        if(hashmap.containsKey("methode")){
            json.addProperty("nachricht", gibText(hashmap.get("methode").toString()));
        }
        
        return json;
    }
    
    private String gibText(String methode){
        String text = "";
        return text;
    }
}
