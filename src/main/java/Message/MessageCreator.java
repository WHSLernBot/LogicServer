package Message;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.HashMap;


/**
 *
 * @author Seve
 */
public class MessageCreator {
    
    public JsonObject erstelleText(HashMap hashmap){
        JsonObject jResponse = new JsonObject();
        JsonObject jUser = new JsonObject();

        if(hashmap.containsKey("id")){
            jUser.addProperty("id", hashmap.get("id").toString());
        }
        if(hashmap.containsKey("plattform")){
            jUser.addProperty("plattform", hashmap.get("plattform").toString());
        }
        jResponse.add("user",jUser);
        
        return jResponse;
    }
    
    private String gibText(String methode){
        String text = "";
        return text;
    }
}
