package main;

import DAO.DAO;
import DAO.EMH;
import Entitys.Uni;

/**
 *
 * @author Seve
 */
public class Tester {
    
    public static void main(String[] args) {
        
        System.out.println("start");
        
        try {
        EMH.beginTransaction();
        
        EMH.getEntityManager().persist(new Uni("WHS"));
        
        EMH.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        System.out.println(DAO.gibUnis().iterator().next().toString());
        
    }
    
}
