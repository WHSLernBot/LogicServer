package DBBot;

import Message.MessageCreator;
import Message.Nachricht;
import java.util.LinkedList;
import main.CBBenutzer;

/**
 * Diese Klasse erstllt aus vielen einzel Informationen eine zusammenhaengende 
 * Nachricht fuer einen Benutzer.
 * 
 * @author Seve
 */
public class PersonaleNachricht {
    
    private static final int OPTIONEN = 8;
    public static final int MORGEN_KLAUSUR = 0;
    public static final int ANMELDEBEGINN = 1;
    public static final int NOTEEINTRAGEN = 2;
    public static final int AKTIVITAET = 3;
    public static final int PROGNOSE_SCHLECHT = 4;
    public static final int PROGNOSE_MITTEL = 5;
    public static final int PROGNOSE_GUT = 6;
    public static final int KLAUSUR_TEILNAME = 7;
    
    private final LinkedList<String>[] nachrichten;
    
    /**
     * Erstllt eine neue Personale Nachricht.
     */
    public PersonaleNachricht() {
        
        nachrichten = new LinkedList[OPTIONEN];
        
        for(int i = 0;i < nachrichten.length; i++) {
            nachrichten[i] = new LinkedList<>();
        }
        
    }
    
    /**
     * Fuegt eine Nachrichteninformation zu einer Option hinzu.
     * 
     * @param nachricht
     * @param option 
     */
    public void addNachricht(String nachricht, int option) {
        
        nachrichten[option].add(nachricht);
        
    }
    
    /**
     * Erstllt aus allen Opionen eine Nachricht.
     * @param b Der Benutzer an den die Nachricht gehen soll.
     */
    public void erstelleNachrichten(CBBenutzer b) {
        
        Nachricht n = new Nachricht(b);
        
        String nachricht = "";
        
        for(int i = 0; i < OPTIONEN; i++) {
            nachricht = nachricht + gibOptionsnachricht(i);
        }
        
        //Nachricht senden
        if(!nachricht.equals("")) {
            MessageCreator.erstlleTextNachricht(n.getJson(),nachricht);
            
            
        }

    }
    
    private String gibOptionsnachricht(int option) {
        LinkedList<String> n = nachrichten[option];
        if(n.isEmpty()) {
            return "";
        }
        
        String text = "";
        
        if(n.size() == 1) {
            text = text + n.getFirst();
        } else if (n.size() > 1) {
            text = text + n.getFirst();
            
            for(int i = 1; i < n.size() - 1; i++) {
                text = text + ", " + n.get(i);
            }
            
            text = text + " und " + n.getLast();
        }
        
        switch(option) {
            case MORGEN_KLAUSUR:
                text = "Ich wuensch dir morgen viel Glueck bei deiner " + text + " Klausur :).";
                break;
            case ANMELDEBEGINN:
                text = "Ab heute kannst du dich fuer " + text + " Anmelden.";
                break;
            case NOTEEINTRAGEN:
                text = "Denk daran noch deine " + text + " Note einzutragen, damit wir unser System verbessern koennen und bessere Prognossen machen koennen.";
                break;
            case PROGNOSE_SCHLECHT:
                break;
            case PROGNOSE_MITTEL:
                break;
            case PROGNOSE_GUT:
                break;
            case KLAUSUR_TEILNAME:
                break;

        }
        
        
        return text;
    }
    
}
