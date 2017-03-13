package DAO;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EMH {
    
    private static final String JDBC_URL = "javax.persistence.jdbc.url";
    private static final String JDBC_DRIVER = "javax.persistence.jdbc.driver";
    private static final String JDBC_USERNAME = "javax.persistence.jdbc.user";
    private static final String JDBC_PASSWORD = "javax.persistence.jdbc.password";
    private static final String JDBC_DIALECT = "hibernate.dialect";
    private static final String JDBC_POOL = "connection.pool_size";
    private static final String POOL_SIZE = "10";
    private static final String JDBC_HBM = "hbm2ddl.auto";
    private static final String HBM_TYPE = "update";
    private static final String JDBC_SESSION_CONTEXT = "current_session_context_class";
    private static final String SESSION_CONTEXT_TYPE = "thread";
    
    private static final EntityManagerFactory EMF;
    private static final ThreadLocal<EntityManager> THREAD_LOCAL;

    static {
        
        String databaseUrl = System.getenv("DATABASE_URL");
        StringTokenizer st = new StringTokenizer(databaseUrl, ":@/");
        String dbVendor = st.nextToken(); //if DATABASE_URL is set
        String userName = st.nextToken();
        String password = st.nextToken();
        String host = st.nextToken();
        String port = st.nextToken();
        String databaseName = st.nextToken();
        String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory", host, port, databaseName);
        Map<String, String> properties = new HashMap<>();
        properties.put(JDBC_URL, jdbcUrl );
        properties.put(JDBC_DRIVER, "org.postgresql.Driver");
        properties.put(JDBC_DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        properties.put(JDBC_USERNAME, userName);
        properties.put(JDBC_PASSWORD, password);
//        properties.put(JDBC_POOL, POOL_SIZE);
        properties.put(JDBC_HBM, HBM_TYPE);
        properties.put(JDBC_SESSION_CONTEXT, SESSION_CONTEXT_TYPE);
        EMF = Persistence.createEntityManagerFactory("LernServletPU", properties);
        
        
//        EMF = Persistence.createEntityManagerFactory("LernServletPU");
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
    
    public static void persist(Object ent) {
        getEntityManager().persist(ent);
    }
    
    public static void merge(Object ent) {
        getEntityManager().merge(ent);
    }
    
    public static void remove(Object ent) {
        getEntityManager().remove(ent);
    }
    
}
