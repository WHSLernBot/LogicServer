/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;
/**
 *
 * @author Sebastian
 */
public class ControllerThread implements Runnable{
    private String id;
    public ControllerThread(String id){
        this.id = id;
    }
    
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+ "####Ich bin User: " + id);
    }
    
}
