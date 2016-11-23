package Controller;

import com.google.gson.JsonObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Diese Klasse stellt Methoden zur Verfügung, um einen ThreadPool zu erzeugen 
 * und auszuführen. Allerdings wird die Poolgröße anfangs festgelegt.
 */
public class ControllerPool {
    /**
     * Gibt die Größe des Threadpools an.
     */
    private static final int POOL_SIZE = 30;
    
    /**
     * Erzeugt ein ExecutorService Object, welches den ThreadPool verwaltet.
     */
    private ExecutorService pool;
    
    /**
     * Der Konstruktor initialisiert ein neues Executor Object, 
     * mit einer fixen Poolgröße.
     */
    public void controller(){
        
        pool = Executors.newFixedThreadPool(POOL_SIZE);
        
    }
    
    /**
     * Führt einen ControllerThread aus.
     * @param json Enthält alle wichtigen informationen, die vom Server kommen.
     */
    public void loese(JsonObject json) {
        
        pool.submit(new ControllerThread(json));
        
    }

}
