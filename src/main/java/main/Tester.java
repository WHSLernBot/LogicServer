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
        
        EMH.getEntityManager().persist(new Uni("WHS"));
        
        System.out.println(DAO.gibUnis().iterator().next().toString());
        
    }
    
}
