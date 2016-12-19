package spark;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import main.ChatBotManager;
import static spark.Spark.*;


public class Main {
    
    

    public static void main(String[] args) {
        
        
        get("/", (req, res) -> {
            
           
            return "Das ist meine Hauptseite";
            
            
        });
      
        get("/db", (req, res) -> {
            
            
//            JsonParser jp = new JsonParser();
//            
//            JsonElement je = jp.parse(req.body());
//            
//            JsonObject jo = je.getAsJsonObject();
//            
//            res.status(200);
//            res.type("application/json");

            return "Laeuft etwas";
        });
    
    
  }
    

}
