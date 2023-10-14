package internal;

import codes.showme.domain.tenant.CurrentTenant;
import com.zaxxer.hikari.HikariDataSource;
import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import io.ebean.config.TenantMode;
import io.ebean.dbmigration.DbMigration;
import io.ebean.migration.MigrationConfig;
import io.ebean.migration.MigrationRunner;
import io.ebean.platform.postgres.PostgresPlatform;
import org.testcontainers.containers.PostgreSQLContainer;
import org.apache.commons.cli.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DdlGeneratorCommand {
    public static final String TESTS_DB = "ebean_ddlgeneratorcommand";
    public static final String DB_SCHEMA = "public";
    public static final String META_TABLE = "cp_db_migrations";
    public static final String MIGRATION_PATH = "domain";

    private static Options OPTIONS = new Options();
    private static CommandLine commandLine;
    private static String HELP_STRING = null;

    public static void main(String[] args) throws ParseException {

        CommandLineParser commandLineParser = new DefaultParser();
        // help
        OPTIONS.addOption("help","usage help");
        // port
        OPTIONS.addOption(Option.builder("p").required().hasArg(true).longOpt("path")
                .type(String.class).desc("the path of where model is").build());
        OPTIONS.addOption(Option.builder("g").required().type(String.class)
                .hasArgs().longOpt("packages").desc("the packages would be scan by Ebean").build());
        try {
            commandLine = commandLineParser.parse(OPTIONS, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage() + "\n" + getHelpString());
            System.exit(0);
        }

        try (PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:12.8")
                .withDatabaseName(TESTS_DB)
                .withUsername("postgres")
                .withPassword("postgres")

        ) {

            postgreSQLContainer.withStartupTimeoutSeconds(60).start();
            String sqlBasePath = commandLine.getOptionValue("path");
            String path = sqlBasePath;

            String dbmigrationFolder = path + File.separator + "dbmigration";
            String modelFolder = path + File.separator + "dbmigration" + File.separator + "model";
//            if (Files.exists(Path.of(path))) {
//                System.out.println(path);
//                throw new IllegalArgumentException("dbmigration path is required");
//            } else {
//                Files.createDirectory(Path.of(path));
//                Files.createDirectory(Path.of(dbmigrationFolder));
//                Files.createDirectory(Path.of(modelFolder));
//                if (params.containsKey("migration_model")) {
//                    for (String migrationModel : params.get("migration_model").split(",")) {
//                        String filename = migrationModel.substring(migrationModel.lastIndexOf(File.separator));
//                        String content = Files.readString(Path.of(migrationModel));
//                        Files.writeString(Path.of(modelFolder + File.separator + filename), content);
//                    }
//                }
//
//                if (params.containsKey("migration_sql")) {
//                    for (String sql : params.get("migration_sql").split(",")) {
//                        String filename = sql.substring(sql.lastIndexOf(File.separator));
//                        String content = Files.readString(Path.of(sql));
//                        Files.writeString(Path.of(dbmigrationFolder + File.separator + filename), content);
//                    }
//                }
//            }
//            System.setProperty("ddl.migration.version", "1.3");
//            System.setProperty("ddl.migration.name", "support end dating");

            HikariDataSource datasource = new HikariDataSource();
            datasource.setJdbcUrl(postgreSQLContainer.getJdbcUrl());

            datasource.setUsername(postgreSQLContainer.getUsername());
            datasource.setPassword(postgreSQLContainer.getPassword());
            datasource.setAutoCommit(true);

            DatabaseConfig config = new DatabaseConfig();
            config.setName("domain-sql");
            config.setRegister(true);
//            config.setDdlInitSql("/Users/jack/codebase/everything-in-code-planet/repository-impl/src/main/sql/dbmigration/1.1.sql");
            config.setDefaultServer(true);
            config.setDdlExtra(false);
            config.setPackages(Arrays.asList(commandLine.getOptionValues("packages")));

            config.setTenantMode(TenantMode.PARTITION);
            config.setCurrentTenantProvider(new CurrentTenant());
            config.setDdlCreateOnly(false);
            config.setDdlGenerate(true);
            config.setDdlRun(false);
            config.setDbSchema(DB_SCHEMA);
            config.setDataSource(datasource);

            // important!!! this step
            Database database = DatabaseFactory.create(config);
            DbMigration dbMigration = DbMigration.create();
            dbMigration.setServerConfig(config);
            dbMigration.setStrictMode(false);
            dbMigration.setPlatform(new PostgresPlatform());

            dbMigration.setMigrationPath(MIGRATION_PATH);
            dbMigration.setPathToResources(sqlBasePath);
            String generateInitMigration = dbMigration.generateInitMigration();
            String reuslt = dbMigration.generateMigration();


            MigrationConfig migrationConfig = new MigrationConfig();

            migrationConfig.setDbSchema(DB_SCHEMA);
            migrationConfig.setMetaTable(META_TABLE);
            migrationConfig.setMigrationPath("filesystem:" + sqlBasePath + File.separator + MIGRATION_PATH);
//            // run it ...
            MigrationRunner runner = new MigrationRunner(migrationConfig);
            runner.run(datasource);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static String getHelpString() {
        if (HELP_STRING == null) {
            HelpFormatter helpFormatter = new HelpFormatter();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PrintWriter printWriter = new PrintWriter(byteArrayOutputStream);
            helpFormatter.printHelp(printWriter, HelpFormatter.DEFAULT_WIDTH, "scp -help", null,
                    OPTIONS, HelpFormatter.DEFAULT_LEFT_PAD, HelpFormatter.DEFAULT_DESC_PAD, null);
            printWriter.flush();
            HELP_STRING = new String(byteArrayOutputStream.toByteArray());
            printWriter.close();
        }
        return HELP_STRING;
    }
}
