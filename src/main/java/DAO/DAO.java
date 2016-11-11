package DAO;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

/**
 *
 * @author Seve
 */
public class DAO {
    
    private static DAO dao;
    
    private EntityManager em;
    
    private DAO() {

        em = Persistence.createEntityManagerFactory("LernServletPU").createEntityManager();
        
    }
    
    public static DAO getInstance() {
        
        if(dao == null) {
            dao = new DAO();
        }
        
        return dao;
    }
    
}
