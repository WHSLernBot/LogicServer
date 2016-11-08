/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.util.concurrent.ExecutorService;
import static java.util.concurrent.Executors.newCachedThreadPool;

/**
 *
 * @author Seve
 */
public class ControllerPool {
    private ExecutorService exeSer = newCachedThreadPool();
    public void controller(String id){
        Runnable con = new ControllerThread(id);
        exeSer.submit(con);
        
    }
    public void beenden(){
        exeSer.shutdown();
    }
    
    public static void main(String[] args) {
        ControllerPool conpo = new ControllerPool();
        for (int i = 0; i < 10; i++) {
            conpo.controller("" + i);
        }
        
        conpo.beenden();
    }
}
