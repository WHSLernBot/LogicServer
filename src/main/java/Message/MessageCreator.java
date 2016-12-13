package Message;


import Entitys.Antwort;
import Entitys.Klausur;
import Entitys.Aufgabe;
import Entitys.LernStatus;
import Entitys.Uni;
import com.google.gson.JsonObject;
import java.sql.Timestamp;
import java.util.Collection;
import main.CBBenutzer;
/**
 * Die Klasse MessageCreator stellt Methoden zur Verfügung, um aus Objekten,
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
     * Die Methode erstellt ein JsonObject, für eine übergebene Aufgabe, mit 
     * entsprechenen Aufgabentext.
     * @param id Id des Nutzers.
     * @param plattform Plattform des Nutzers.
     * @param witSession
     * @param aufgabe Aufgabe für den Nutzer.
     * @param time Gibt an, wann eine Nachricht abgeschickt werden soll.
     * @return Gibt ein Nachrichtenobject, mit Aufgabe und Antwort zurueck.
     */
    public Nachricht erstelleAufgabenJson(long id,long plattform,String witSession, Aufgabe aufgabe,Timestamp time) {
        
        jUser.addProperty("id", id);
        jUser.addProperty("plattform", plattform);
        jUser.addProperty("witsession", witSession);
        
        jAufgabe.addProperty("frage", aufgabe.getFrage());
        jAufgabe.addProperty("hinweis", aufgabe.getHinweis());
        jAufgabe.addProperty("verweis", aufgabe.getVerweis());
        
        Collection<Antwort> ant = aufgabe.getAntworten();
        int i=0;
        for(Antwort antworten: ant){
            jAufgabe.addProperty("antwort"+i,antworten.getAntwort());
            jAufgabe.addProperty("richtigeAntwort"+i,antworten.getRichtig());
            i++;
        }
            
        jResponse.add("user", jUser);
        jResponse.add("aufgabe",jAufgabe);
        jResponse.addProperty("nachricht", gibText("aufgabe"));
        return erzeugeNachricht(jResponse, time);
    }
    
    /**
     * Die Methode erstellt ein JsonObject, mit allen Vorhandenen Unis, die übergeben werden.
     * @param id Id des Nutzers.
     * @param plattform Plattform des Nutzers.
     * @param witSession
     * @param unis Enthält alle Unis, die zur auswahl stehen.
     * @param time Gibt an, wann eine Nachricht abgeschickt werden soll.
     * @return Gibt eine Nachricht mit allen Unis zurueck;
     */
    public Nachricht erstelleUniJason(long id, long plattform,String witSession,Collection<Uni> unis,Timestamp time){
        jUser.addProperty("id", id);
        jUser.addProperty("plattform", plattform);
        jUser.addProperty("witsession", witSession);
        int i = 0;
        for (Uni name : unis) {
            this.jUni.addProperty("name" + i, name.getName());
            this.jUni.addProperty("nameid", name.getId());
            i++;
        }
        jResponse.add("user", jUser);
        jResponse.add("uni", jUni);
        jResponse.addProperty("nachricht", gibText("uni"));
        return erzeugeNachricht(jResponse, time);
    }
    
    /**
     * Die Methode erstellt ein JsonObject, mit allen Informationen, 
     * die zur Klausur vorhanden sind.
     * @param id Id des Nutzers.
     * @param plattform Plattform des Nutzers.     
     * @param witSession     
     * @param klausur Enthält alle Informatonen zur Klausur.
     * @param time Gibt an, wann eine Nachricht abgeschickt werden soll.
     * @return Gibt ein Nachrichtenobjekt mit allen Klausurinfos zurueck.
     */
    public Nachricht erstelleKlausurInfoJson(long id, long plattform,String witSession, Klausur klausur,Timestamp time) {
        jUser.addProperty("id", id);
        jUser.addProperty("plattform", plattform);
        jUser.addProperty("witsession", witSession);
        
        jKlausurinfo.addProperty("hilfsmittel",klausur.getHilfsmittel());
        jKlausurinfo.addProperty("ort",klausur.getOrt());
        jKlausurinfo.addProperty("dauer",klausur.getDauer());
        jKlausurinfo.addProperty("periode",klausur.getPruefungsperiode().getAnfang().toString());
        jKlausurinfo.addProperty("uhrzeit",klausur.getUhrzeit().toString());
        
        jResponse.add("user", jUser);
        jResponse.add("klausurinfo", jKlausurinfo);     
        jResponse.addProperty("nachricht", gibText("info"));
        return erzeugeNachricht(jResponse, time);
    }
    
    /**
     * Die Methode erstellt ein JsonObject, mit allen Informationen, 
     * die zu dem Benutzer vorhanden sind.
     * @param benutzer 
     * @param time Gibt an, wann eine Nachricht abgeschickt werden soll.
     * @return Gibt ein Nachrichtenobjekt mit allen Benutzerinfos zurueck.
     */
    public Nachricht erstelleBenutzerInfoNachricht(CBBenutzer benutzer,Timestamp time) {
        jUser.addProperty("id", benutzer.getBenutzer().getId());
        jUser.addProperty("plattform", benutzer.getBenutzer().getPlattform().getPfID());
        jUser.addProperty("witsession", benutzer.getBenutzer().getPlattform().getWitSession());
        Collection<LernStatus> stadi = benutzer.getBenutzer().getLernStadi();
        int i = 0;
        for (LernStatus status : stadi) {
            this.jUni.addProperty("status" + i, status.getSumPunkte());
            i++;
        }
        jResponse.add("user", jUser);
        jResponse.add("info", jInfo);     
        jResponse.addProperty("nachricht", gibText("info"));
        return erzeugeNachricht(jResponse, time);
    }
    
    
    /**
     * Erzeugt einen Text, für die entsprechende Methode.
     * @param methode Gibt an, für welche Methode der Text erzeugt werden soll.
     * @return Gibt den entsprechenden Text zurück.
     */
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
    
    public Nachricht exception(Exception e) {
        jResponse.addProperty("Exception", "Fehler: " + e.getMessage());
        return erzeugeNachricht(jResponse, null);
    }
    
    /**
     * Fügt eine Nachricht dem ChatBotManager hinzu.
     * @param json Enthält alle wichtigen Informationen.
     * @param time Gibt an, wann eine Nachricht abgeschickt werden soll.
     */
    private Nachricht erzeugeNachricht(JsonObject json,Timestamp time){
        /*
        time ist wichtig, damit die Nachricht auch noch zu einem späteren 
        Zeitpunkt abgeschickt werden kann.
        */
        Nachricht nachricht = new Nachricht(json,time);
        return nachricht;
    }
    
    
}