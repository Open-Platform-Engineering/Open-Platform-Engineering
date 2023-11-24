package codes.showme.domain;

import codes.showme.domain.incident.IncidentRepository;
import codes.showme.domain.incident.IncidentRepositoryImpl;
import codes.showme.domain.platform.ServiceRepository;
import codes.showme.domain.platform.ServiceRepositoryImpl;
import codes.showme.domain.platform.TicketRepository;
import codes.showme.domain.platform.TicketRepositoryImpl;
import codes.showme.domain.repository.EbeanConfig;
import codes.showme.techlib.ioc.InstanceFactory;
import codes.showme.techlib.ioc.InstanceProvider;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;
import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import io.ebean.migration.MigrationConfig;
import io.ebean.migration.MigrationRunner;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.mockito.Mockito;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Arrays;

public abstract class AbstractIntegrationTest {

    public static final String TESTS_DB = "ebean-test1";
    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:12.8")
            .withDatabaseName(TESTS_DB)
            .withUsername("sa")
            .withPassword("sa");

    public static final InstanceProvider instanceProvider = Mockito.mock(InstanceProvider.class);

    public Database database;

    @Before
    public void setUp() throws Exception {
        HikariDataSource datasource =  new HikariDataSource();
        datasource.setJdbcUrl(postgreSQLContainer.getJdbcUrl());
        datasource.setUsername(postgreSQLContainer.getUsername());
        datasource.setPassword(postgreSQLContainer.getPassword());
        datasource.setAutoCommit(false);

        DatabaseConfig config = new DatabaseConfig();
        config.setName(postgreSQLContainer.getDatabaseName());
        config.setRegister(true);
        config.setDefaultServer(true);
        config.setPackages(Arrays.asList("codes.showme.domain"));
        config.setDdlCreateOnly(true);
        config.setDdlGenerate(true);
        config.setDdlRun(false);
        config.setDbSchema("public");
        config.setDataSource(datasource);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.disable(MapperFeature.AUTO_DETECT_CREATORS,
                MapperFeature.AUTO_DETECT_FIELDS,
                MapperFeature.AUTO_DETECT_GETTERS,
                MapperFeature.AUTO_DETECT_SETTERS,
                MapperFeature.AUTO_DETECT_IS_GETTERS);
        config.setObjectMapper(objectMapper);

        database = DatabaseFactory.create(config);

        MigrationConfig migrationConfig = new MigrationConfig();
        migrationConfig.setDbSchema("public");
        migrationConfig.setMetaTable("sxs");
        migrationConfig.setMigrationPath("classpath:main/sql");
//            // run it ...
        MigrationRunner runner = new MigrationRunner(migrationConfig);
        runner.run(datasource);

        Mockito.when(instanceProvider.getInstance(Database.class, EbeanConfig.BEAN_NAME_WRITE_BEONLY)).thenReturn(database);
        Mockito.when(instanceProvider.getInstance(Database.class, EbeanConfig.BEAN_NAME_READ_BEONLY)).thenReturn(database);
        InstanceFactory.setInstanceProvider(instanceProvider);
    }

    public Database getDatabase() {
        return database;
    }
}
