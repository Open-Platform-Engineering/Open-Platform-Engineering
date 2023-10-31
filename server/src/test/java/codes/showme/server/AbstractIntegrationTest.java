package codes.showme.server;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;
import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import io.ebean.migration.MigrationConfig;
import io.ebean.migration.MigrationRunner;
import org.junit.Before;
import org.junit.ClassRule;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Arrays;

public abstract class AbstractIntegrationTest {

    private static final String TESTS_DB = "ebean-test1";
    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:12.8")
            .withDatabaseName(TESTS_DB)
            .withUsername("sa")
            .withPassword("sa");


    private Database database;

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
    }

    public static PostgreSQLContainer getPostgreSQLContainer() {
        return postgreSQLContainer;
    }

    public static String getDBName(){
        return TESTS_DB;
    }

    public Database getDatabase() {
        return database;
    }
}
