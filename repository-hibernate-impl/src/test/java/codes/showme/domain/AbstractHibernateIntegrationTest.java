package codes.showme.domain;


import codes.showme.domain.repository.jpa.config.HibernateWriteonlyDbConfig;
import codes.showme.domain.repository.jpa.config.JpaConfig;
import codes.showme.techlib.ioc.InstanceFactory;
import codes.showme.techlib.ioc.InstanceProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.mockito.Mockito;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

public abstract class AbstractHibernateIntegrationTest {

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:12.8")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    public static final InstanceProvider instanceProvider = Mockito.mock(InstanceProvider.class);

    @BeforeClass
    public static void beforeClass() throws Exception {


        Flyway flyway = Flyway.configure()
//                .initSql(text)
                .locations("main/sql")
                .dataSource(postgreSQLContainer.getJdbcUrl(),
                        postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword()).load();
        MigrateResult migrate = flyway.migrate();
        Assert.assertEquals(1, migrate.migrationsExecuted);
        Assert.assertTrue(migrate.success);

        HibernateWriteonlyDbConfig jpaWriteonlyDbConfig = new HibernateWriteonlyDbConfig();
        jpaWriteonlyDbConfig.setUrl(postgreSQLContainer.getJdbcUrl());
        jpaWriteonlyDbConfig.setUsername(postgreSQLContainer.getUsername());
        jpaWriteonlyDbConfig.setPassword(postgreSQLContainer.getPassword());
        jpaWriteonlyDbConfig.setTime_zone("UTC");
        jpaWriteonlyDbConfig.setDriver_class("org.postgresql.Driver");
        jpaWriteonlyDbConfig.setDialect("org.hibernate.dialect.PostgreSQLDialect");
        jpaWriteonlyDbConfig.setPackagesSplitByComma("codes.showme.domain");



        JpaConfig jpaConfig = new JpaConfig();
        DataSource dataSource = jpaConfig.writeDatasource(jpaWriteonlyDbConfig);
        EntityManagerFactory entityManagerFactory = jpaConfig.entityManagerFactory(jpaWriteonlyDbConfig);
        Mockito.when(instanceProvider.getInstance(EntityManager.class)).thenReturn(entityManagerFactory.createEntityManager());
        InstanceFactory.setInstanceProvider(instanceProvider);
    }

}
