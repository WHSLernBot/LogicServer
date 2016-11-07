package DAO;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

/**
 *
 * @author Seve
 */
public class DAOCreate {
    
    private static DAO dao;
    
    public static DAO gibDAO() {
        
        if(dao == null) {
            
            EntityManager em = Persistence.createEntityManagerFactory("LernServletPU").createEntityManager();
            
            dao = new DAO(em);
            
        }
        
        return dao;
        
    }
    
}
