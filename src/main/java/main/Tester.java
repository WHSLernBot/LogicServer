package main;

import static Controller.Controller.loese;
import DAO.EMH;
import Entitys.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Map.Entry;

/**
 *
 * @author Seve
 */
public class Tester {
    
    public static void main(String[] args) {
        
        System.out.println("start");
        
        long id = 0;
        ChatBotManager manager = ChatBotManager.getInstance();
        
        try {
            EMH.beginTransaction();

            Uni u = new Uni("WHS");
            
            Modul m = new Modul(u,"INS", "Internetsprachen");
            
            Thema t = new Thema(m,"XML",100);
            
            id = t.getId();
            Aufgabe a = new Aufgabe(t,"Wie gros bin ich?",1,"NE","nope");
            
            a.addAntwort("184", true);
            a.addAntwort("180", false);
            a.addAntwort("190", false);
            
            EMH.getEntityManager().persist(new Adresse(0,"54234"));
            EMH.getEntityManager().persist(u);
            EMH.getEntityManager().persist(m);
            EMH.getEntityManager().persist(t);
            EMH.getEntityManager().persist(a);

            EMH.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        JsonObject obj = new JsonObject();
        JsonObject user = new JsonObject();
        user.addProperty("userID", 124242324);
        user.addProperty("plattformID", 0);
        user.addProperty("witSession", 124242324);
        
        obj.add("user", user);
        obj.addProperty("methode", "gibAufgabe");
        
        JsonObject thema = new JsonObject();
        thema.addProperty("id", id);
        
        
        JsonObject loesung = loese(obj).getJson();
        
        for(Entry<String,JsonElement> en : loesung.entrySet()) {
            System.out.println(en.getKey() + " : " + en.getValue());
        }
        
        System.out.println(loese(obj).getJson().entrySet());
        
    }
    
}
