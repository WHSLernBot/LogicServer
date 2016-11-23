package Message;


import Entitys.Klausur;
import Entitys.Aufgabe;
import Entitys.Uni;
import com.google.gson.JsonObject;
import java.util.Collection;
/**
 * Die Klasse MessageCreator stellt Methoden zur Verf�gung, um aus Objekten,
 * ein JsonObject zu erzeugen.
 */
public class MessageCreator {

    private final JsonObject jResponse;
    
    private final JsonObject jUser;
    
    private final JsonObject jAufgabe;
    
    private final JsonObject jInfo;
    
    private final JsonObject jUni;
    
    private final JsonObject jKlausurinfo;
    
    /**
     * Initialisiert alle Objekte.
     */
    public MessageCreator() {
        jResponse = new JsonObject();
        jUser = new JsonObject();
        jAufgabe = new JsonObject();
        jInfo = new JsonObject();
        jUni = new JsonObject();
        jKlausurinfo = new JsonObject();
    }
    
    /**
     * Die Methode erstellt ein JsonObject, f�r eine �bergebene Aufgabe, mit 
     * entsprechenen Aufgabentext.
     * @param id Id des Nutzers.
     * @param plattform Plattform des Nutzers.
     * @param aufgabe Aufgabe f�r den Nutzer.
     * @return JsonObject mit informationen �ber den Benutzer und der Aufgabenstellung.
     */
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
    
    /**
     * Die Methode erstellt ein JsonObject, mit allen Vorhandenen Unis, die �bergeben werden.
     * @param id Id des Nutzers.
     * @param plattform Plattform des Nutzers.
     * @param unis Enth�lt alle Unis, die zur auswahl stehen.
     * @return JsonObject mit allen Unis.
     */
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
    
    /**
     * Die Methode erstellt ein JsonObject, mit allen Informationen, 
     * die zur Klausur vorhanden sind.
     * @param id Id des Nutzers.
     * @param plattform Plattform des Nutzers.     
     * @param klausur Enth�lt alle Informatonen zur Klausur.
     * @return JsonObject, welches alle Informationen zur Klausur enth�lt.
     */
    public JsonObject erstelleKlausurInfoJson(long id, long plattform, Klausur klausur) {
        jUser.addProperty("id", id);
        jUser.addProperty("plattform", plattform);
        
        jKlausurinfo.addProperty("klausurinfo",klausur.getHilfsmittel());
        
        jResponse.add("klausurinfo", jKlausurinfo);
        jResponse.add("user", jUser);
        jResponse.addProperty("nachricht", gibText("info"));
        return jResponse;        
    }
    
    /**
     * Erzeugt einen Text, f�r die entsprechende Me.thode
     * @param methode Gibt an, f�r welche Methode der Text erzeugt werden soll.
     * @return Gibt den entsprechenden Text zur�ck.
     */
    private String gibText(String methode) {
        String text = "Ups... da ist ein fehler unterlaufen!";
        switch(methode) {
            case "aufgabe":
                text = "Hier ist eine neue Aufage: ";
                break;
            case "uni":
                text = "Das sind alle verf�gbaren Uni's";
                break;
            case "info":
                text = "Hier hast du die gew�nschten Info's!";
                break;
        }
        return text;
    }
}