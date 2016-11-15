package DAO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerHelper {

    private static final EntityManagerFactory EMF;
    private static final ThreadLocal<EntityManager> THREAD_LOCAL;

    static {
        EMF = Persistence.createEntityManagerFactory("Persistent_Name");
        THREAD_LOCAL = new ThreadLocal<>();
    }

    public static EntityManager getEntityManager() {
        EntityManager em = THREAD_LOCAL.get();

        if (em == null) {
            em = EMF.createEntityManager();
            // set your flush mode here
            THREAD_LOCAL.set(em);
        }
        return em;
    }

    public static void closeEntityManager() {
        EntityManager em = THREAD_LOCAL.get();
        if (em != null) {
            em.close();
            THREAD_LOCAL.set(null);
        }
    }

    public static void closeEntityManagerFactory() {
        EMF.close();
    }

    public static void beginTransaction() {
        getEntityManager().getTransaction().begin();
    }

    public static void rollback() {
        getEntityManager().getTransaction().rollback();
    }

    public static void commit() {
        getEntityManager().getTransaction().commit();
    }
}
