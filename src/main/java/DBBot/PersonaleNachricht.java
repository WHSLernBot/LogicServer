package DBBot;

import java.util.LinkedList;

/**
 *
 * @author Seve
 */
public class PersonaleNachricht {
    
    private static final int OPTIONEN = 2;
    public static final int MORGEN_KLAUSUR = 0;
    public static final int ANMELDEBEGINN = 1;
    
    private final LinkedList<String>[] nachrichten;
    
    public PersonaleNachricht() {
        
        nachrichten = new LinkedList[OPTIONEN];
        
        for(int i = 0;i < nachrichten.length; i++) {
            nachrichten[i] = new LinkedList<>();
        }
        
    }
    
    public void addNachricht(String nachricht, int option) {
        
        nachrichten[option].add(nachricht);
        
    }
    
    public void erstelleNachrichten() {
        
        //hier wird das ganze mit dem Message Creator erzeugt und verschickt
        
    }
    
}
