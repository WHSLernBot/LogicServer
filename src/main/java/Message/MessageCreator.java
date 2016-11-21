package Message;

import Entitys.Aufgabe;
import Entitys.Benutzer;
import Entitys.Modul;
import Entitys.Thema;
import Entitys.Uni;
import com.google.gson.JsonObject;
import main.CBBenutzer;

public class MessageCreator {

    private JsonObject jResponse;
    private JsonObject jUser;
    private JsonObject jAufgabe;
    private JsonObject jInfo;
    private JsonObject jUni;
    private JsonObject jKlausurinfo;

    public MessageCreator() {
        jResponse = new JsonObject();
        jUser = new JsonObject();
        jAufgabe = new JsonObject();
        jInfo = new JsonObject();
        jUni = new JsonObject();
        jKlausurinfo = new JsonObject();
    }

    public JsonObject erstelleAufgabenText(long id,long plattform,Aufgabe aufgabe) {
        jUser.addProperty("id", id);
        jUser.addProperty("plattform", plattform);
        
        jAufgabe.addProperty("frage", aufgabe.getFrage());
        jAufgabe.addProperty("hinweis", aufgabe.getHinweis());
        jAufgabe.addProperty("verweis", aufgabe.getVerweis());
        
        jResponse.add("user", jUser);
        jResponse.add("aufgabe",jAufgabe);
        
        return jResponse;
    }

    private String gibText(String methode) {
        String text = "";
        return text;
    }
    
    public static void main(String[] args) {
        MessageCreator mc = new MessageCreator();
        System.out.println(mc.erstelleAufgabenText(14, 523, new Aufgabe(
                new Thema(
                        new Modul(
                                new Uni("uni")
                                ,"modul1","modul2")
                        ,"",1)
                ,"frage",3,"Besser lernen","Seite 34")));
    }
}
