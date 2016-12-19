package DAO;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import static spark.Spark.port;

public class EMH {

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
                properties.put("javax.persistence.jdbc.url", jdbcUrl );
                properties.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
                properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
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
}
