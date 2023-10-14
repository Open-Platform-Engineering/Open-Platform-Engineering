package codes.showme.domain.repository.jpa.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class JpaConfig {
    @Bean(name = "hibernate_datasource_writeonly", destroyMethod = "close")
    public DataSource writeDatasource(HibernateWriteonlyDbConfig writeonlyDbConfig) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(writeonlyDbConfig.getUrl());
        config.setUsername(writeonlyDbConfig.getUsername());
        config.setPassword(writeonlyDbConfig.getPassword());
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        return new HikariDataSource(config);
    }

    @Bean
    public EntityManagerFactory entityManagerFactory(HibernateWriteonlyDbConfig writeonlyDbConfig) {
        return Persistence.createEntityManagerFactory("showmecodes-persistence-unit", writeonlyDbConfig.exportProperties());
    }

    @Bean
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory){
        return entityManagerFactory.createEntityManager();
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(HibernateWriteonlyDbConfig writeonlyDbConfig, DataSource writeDatasource) {
        final LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(writeDatasource);
        sessionFactory.setPackagesToScan(writeonlyDbConfig.getPackagesSplitByComma().split(","));
        sessionFactory.setHibernateProperties(hibernateProperties(writeonlyDbConfig));

        return sessionFactory;
    }

    private Properties hibernateProperties(HibernateWriteonlyDbConfig writeonlyDbConfig) {
        final Properties settings = new Properties();
        settings.put(Environment.DRIVER, writeonlyDbConfig.getDriver_class());
        settings.put(Environment.DIALECT, writeonlyDbConfig.getDialect());
        settings.put(Environment.USER, writeonlyDbConfig.getUsername());
        settings.put(Environment.PASS, writeonlyDbConfig.getPassword());
        settings.put(Environment.JDBC_TIME_ZONE, writeonlyDbConfig.getTime_zone());
        settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, writeonlyDbConfig.getCurrent_session_context_class());
        settings.put(Environment.SHOW_SQL, writeonlyDbConfig.isShow_sql());
        settings.put(Environment.FORMAT_SQL, writeonlyDbConfig.isFormat_sql());
        settings.put(Environment.CONNECTION_PROVIDER, "com.zaxxer.hikari.hibernate.HikariConnectionProvider");
        return settings;
    }

    @Bean
    public PlatformTransactionManager hibernateTransactionManager(HibernateWriteonlyDbConfig writeonlyDbConfig, DataSource writeDatasource) {
        final HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory(writeonlyDbConfig, writeDatasource).getObject());
        return transactionManager;
    }


//    /**
//     * Get transaction manager.
//     */
//    @Bean
//    public JpaTransactionManager transactionManager() {
//        var transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
//        return transactionManager;
//    }
}
