package spark;

import Message.Nachricht;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.ChatBotManager;
import static spark.Service.ignite;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class Main {
    
    private static final int POOL_SIZE = 20;
   

    public static void main(String[] args) {
        
        igniteFirstSpark();  
       
    }
    
//    static void igniteSecondSpark() {     
//        
//        Service http = ignite();
//        
//        
//        http.get("/basic", (req, res) -> "Hello World 2");
//        
//        
//    }
    
    static void igniteFirstSpark() {
        
        Service http = ignite()
                      .port(getHerokuAssignedPort())
                      .threadPool(POOL_SIZE);
        
        
        http.get("/", (req, res) -> "Ja es geht zumindest.");
        
        http.post("/messageBot", (req, res) -> {
            
            JsonParser parser = new JsonParser();
            
            String s = req.body();
            
            JsonObject obj= new JsonObject();
            
            JsonObject objBody;
            
            obj.add("body", parser.parse(s));
            objBody = obj.get("body").getAsJsonObject();
            
            
            Rand rd = new Rand(objBody.get("key").getAsInt());
                
            
            System.out.println(rd.toString());
            return rd.toString() + ". Zeit: " + ChatBotManager.getInstance().jetzt();
      
        });
        
        
        http.get("/hello2", (q, a) -> "Hello from port 1234!");
        
        
        http.get("/hello3", (q, a) -> "Hello from port 5678!");
        
        
        
    }
    
    public void sendMessage(String route, Nachricht nachricht) throws UnsupportedEncodingException {
        
        Nachricht message = nachricht;
        
        JsonObject jo = message.getJson();
        Gson gson = new Gson();
        String postUrl = route;
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(postUrl);
        StringEntity postingString = new StringEntity(gson.toJson(jo));
        post.setEntity(postingString);
        post.setHeader("Content-type", "application/json");
        try {
            
            HttpResponse response = httpClient.execute(post);
            
            StatusLine sl = response.getStatusLine();
            
            if(sl.getStatusCode() != 200) {
                
                //I.wie Sammmeln wenn was schiefgegangen ist und erneut versuchen.
                HashMap<String, String> m = new HashMap<>();
                 
            }
            
        } catch (IOException ex) {
            
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //Port welcher Standardmäßig verwendet werden soll.
    }

}