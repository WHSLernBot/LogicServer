package spark;

import Controller.Controller;
import Message.Nachricht;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import static spark.Service.ignite;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import spark.velocity.controller.*;
import spark.velocity.util.Path;

public class Main {
    
    //Anzahl gleichzeitiger Zugriffe
    private static final int POOL_SIZE = 20;
   

    public static void main(String[] args) {
        try {
            DAO.DAO.erstelleEintraege();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        igniteFirstSpark();  
       
    }
    

    /**
     * Methode, welche einen Pool von Routen erstellt und gleichzeitige Zugriffe 
     * verarbeitet.
     */
    static void igniteFirstSpark() {
        
        Service http = ignite()
                      .port(getHerokuAssignedPort())
                      .threadPool(POOL_SIZE)
                      .staticFileLocation("/public");
        
        http.get("/", (req, res) -> "Diese Seite gehört ");
        
        http.get("/messageBot", (req, res) -> {
            
            Gson gson = new Gson();
            short i = 1;
            JsonParser parser = new JsonParser();
            
            JsonElement je = gson.fromJson(req.body(), JsonElement.class);
            JsonObject jb = je.getAsJsonObject();
            
            try {
                
                Nachricht na = Controller.loese(jb);
                return na.getJson();
                
            } catch(Exception e) {
                
                return "Fehler";
            }
        });
        
        /**
         * Name ist Programm
         */
        http.get("/setzeUni", (req, res) -> {
            
            
            short i = 1;
            DAO.DAO.neueVerbindung(i, "immense-journey-49192");
            
            
            //Json Body
            JsonObject body = new JsonObject();
            
            
            
            //User Object
            JsonObject a = new JsonObject();
            a.addProperty("userID", "1234");
            a.addProperty("plattformID", 1);
            a.addProperty("witSession", "12345");
            
            
            //Füllen des Body
            body.add("user", a);
            body.addProperty("methode", "setzeUni");
            body.addProperty("uniID", 1);
            
            
            System.out.println(body.toString());   
            try {
                
                System.out.println("Versuch wird gestartet");
                Nachricht na = Controller.loese(body);
                return na.getJson();
                
            } catch(Exception e) {
                
                System.out.println("FEHLER NACHRICHT = " + e.getMessage());
                return e.getMessage() + "";
                
            }

        });
        
        http.get("/setzeModul", (req, res) -> {
            //Json Body
            JsonObject body = new JsonObject();
            
            //User Object
            JsonObject a = new JsonObject();
            a.addProperty("userID", 1234);
            a.addProperty("plattformID", 1);
            a.addProperty("witSession", 12345);
            
            //Module Array Object
            JsonArray ja = new JsonArray();
            ja.add("INS");
            
            //Füllen des Body
            body.add("user", a);
            body.addProperty("methode", "meldeFuerModulAn");
            body.add("module", ja);
            
            
                
            try {
                
                System.out.println("Versuch wird gestartet");
                Nachricht na = Controller.loese(body);
                return na;
                
            } catch(Exception e) {
                
                System.out.println("FEHLER NACHRICHT = " + e.getMessage());
                return e.getMessage() + "";
                
            }

        });

        
        /**
         * Erstellt Test Einträge!
         * 
         */
        http.get("/createTest", (q, a) -> {
            
            try {
                
                DAO.DAO.erstelleEintraege();
                return "Einträge erstellt";
                
            } catch (Exception e) {
                
                
                return e.getMessage();
            }
            
            
        });
        
        
        //Routen für die Datenbankverwaltung
        
        http.get(Path.W_INDEX , IndexController.serveIndexPage);
        http.get(Path.W_ADMIN, AdminController.serveAdminPage);
        http.get(Path.W_USER, UserController.serveUserPage);
        http.post(Path.W_INDEX, IndexController.handleLoginPost);
        http.post(Path.W_ADMIN, AdminController.handleRegPost);
        http.post(Path.W_USER, UserController.handleModulPost);
        
        
        
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