package DAO;

import javax.persistence.EntityManager;

/**
 *
 * @author Seve
 */
public class DAO {
    
    private EntityManager em;
    
    public DAO(EntityManager em) {
        this.em = em;
        
    }
    
}
