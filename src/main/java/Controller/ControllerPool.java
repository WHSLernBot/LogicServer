package Controller;

import com.google.gson.JsonObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Seve
 */
public class ControllerPool {
      
    private static final int POOL_SIZE = 30;
    
    private ExecutorService pool;
    
    public void controller(String id){
        
        pool = Executors.newFixedThreadPool(POOL_SIZE);
        
    }
    
    public void loese(JsonObject json) {
        
        pool.submit(new ControllerThread(json));
        
    }

}
