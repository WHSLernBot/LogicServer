package main;

import DAO.EMH;
import Entitys.*;
import com.google.gson.JsonObject;

/**
 *
 * @author Seve
 */
public class Tester {
    
    public static void main(String[] args) {
        
        System.out.println("start");
        
        ChatBotManager manager = ChatBotManager.getInstance();
        
        try {
            EMH.beginTransaction();

            Uni u = new Uni("WHS");
            
            Modul m = new Modul(u,"INS", "Internetsprachen");
            
            Thema t = new Thema(m,"XML",100);
            
            Aufgabe a = new Aufgabe(t,"Wie gros bin ich?",1,"NE","nope");
            
            a.addAntwort("184", true);
            a.addAntwort("180", false);
            a.addAntwort("190", false);
            
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

        System.out.println();
        
    }
    
}
