package variantA.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class JPAUtil {
    private static EntityManagerFactory emf;

    static {
        try {
            // 1) Essaye avec persistence.xml (perfPU)
            emf = Persistence.createEntityManagerFactory("perfPU");
            System.out.println("[JPA] EntityManagerFactory created from persistence.xml (perfPU)");
        } catch (Throwable ex) {
            System.err.println("[JPA] Failed to init from persistence.xml: " + ex);
            // 2) Fallback: propriétés en dur (au cas où persistence.xml ne serait pas chargé)
            Map<String, Object> props = new HashMap<>();
            props.put("jakarta.persistence.jdbc.driver", "org.postgresql.Driver");
            props.put("jakarta.persistence.jdbc.url", "jdbc:postgresql://localhost:5432/perfdb");
            props.put("jakarta.persistence.jdbc.user", "perf");
            props.put("jakarta.persistence.jdbc.password", "perf");

            // Hibernate core
            props.put("hibernate.hbm2ddl.auto", "validate");
            props.put("hibernate.show_sql", "false");
            props.put("hibernate.format_sql", "false");
            props.put("hibernate.jdbc.time_zone", "UTC");

            // HikariCP provider
            props.put("hibernate.connection.provider_class", "org.hibernate.hikaricp.internal.HikariCPConnectionProvider");
            props.put("hibernate.hikari.maximumPoolSize", "10");
            props.put("hibernate.hikari.minimumIdle", "2");
            props.put("hibernate.hikari.idleTimeout", "30000");
            props.put("hibernate.hikari.connectionTimeout", "20000");

            emf = Persistence.createEntityManagerFactory("perfPU", props);
            System.out.println("[JPA] EntityManagerFactory created from programmatic props (fallback).");
        }
    }

    public static EntityManager em() {
        return emf.createEntityManager();
    }
}
