package Controller;

import com.google.gson.JsonObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Diese Klasse stellt Methoden zur Verf�gung, um einen ThreadPool zu erzeugen 
 * und auszuf�hren. Allerdings wird die Poolgr��e anfangs festgelegt.
 */
public class ControllerPool {
    /**
     * Gibt die Gr��e des Threadpools an.
     */
    private static final int POOL_SIZE = 30;
    
    /**
     * Erzeugt ein ExecutorService Object, welches den ThreadPool verwaltet.
     */
    private ExecutorService pool;
    
    /**
     * Der Konstruktor initialisiert ein neues Executor Object, 
     * mit einer fixen Poolgr��e.
     */
    public void controller(){
        
        pool = Executors.newFixedThreadPool(POOL_SIZE);
        
    }
    
    /**
     * F�hrt einen ControllerThread aus.
     * @param json Enth�lt alle wichtigen informationen, die vom Server kommen.
     */
    public void loese(JsonObject json) {
        
        pool.submit(new ControllerThread(json));
        
    }

}
