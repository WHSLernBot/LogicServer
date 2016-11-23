package Message;


import Entitys.Klausur;
import Entitys.Aufgabe;
import Entitys.Uni;
import com.google.gson.JsonObject;
import java.util.Collection;

public class MessageCreator {

    private final JsonObject jResponse;
    private final JsonObject jUser;
    private final JsonObject jAufgabe;
    private final JsonObject jInfo;
    private final JsonObject jUni;
    private final JsonObject jKlausurinfo;

    public MessageCreator() {
        jResponse = new JsonObject();
        jUser = new JsonObject();
        jAufgabe = new JsonObject();
        jInfo = new JsonObject();
        jUni = new JsonObject();
        jKlausurinfo = new JsonObject();
    }

    public JsonObject erstelleAufgabenJson(long id,long plattform, Aufgabe aufgabe) {
        jUser.addProperty("id", id);
        jUser.addProperty("plattform", plattform);
        
        jAufgabe.addProperty("frage", aufgabe.getFrage());
        jAufgabe.addProperty("hinweis", aufgabe.getHinweis());
        jAufgabe.addProperty("verweis", aufgabe.getVerweis());
        
        jResponse.add("user", jUser);
        jResponse.add("aufgabe",jAufgabe);
        jResponse.addProperty("nachricht", gibText("aufgabe"));
        
        return jResponse;
    }
   
    public JsonObject erstelleUniJason(long id, long plattform,Collection<Uni> unis){
        jUser.addProperty("id", id);
        jUser.addProperty("plattform", plattform);
        int i = 0;
        for (Uni name : unis) {
            this.jUni.addProperty("name" + i, name.getName());
            this.jUni.addProperty(name.getName()+"id", name.getId());
            i++;
        }
        jResponse.add("user", jUser);
        jResponse.add("uni", jUni);
        jResponse.addProperty("nachricht", gibText("uni"));
        return jResponse;
    }

    public JsonObject erstelleInfoJson(long id, long plattform, Klausur klausur) {
        jUser.addProperty("id", id);
        jUser.addProperty("plattform", plattform);
        
        jKlausurinfo.addProperty("klausurinfo",klausur.getHilfsmittel());
        
        jResponse.add("klausuindo", jKlausurinfo);
        jResponse.add("user", jUser);
        jResponse.addProperty("nachricht", gibText("info"));
        return jResponse;        
    }

    private String gibText(String methode) {
        String text = "Ups... da ist ein fehler unterlaufen!";
        switch(methode) {
            case "aufgabe":
                text = "Hier ist eine neue Aufage: ";
                break;
            case "uni":
                text = "Das sind alle verfügbaren Uni's";
                break;
            case "info":
                text = "Hier hast du die gewünschten Info's!";
                break;
        }
                
        
        return text;
    }
}
