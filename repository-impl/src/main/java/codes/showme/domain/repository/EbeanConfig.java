package codes.showme.domain.repository;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;
import java.util.Arrays;

@Configuration
public class EbeanConfig {


    private static final Logger log = LoggerFactory.getLogger(EbeanConfig.class);
    public static final String BEAN_NAME_WRITE_BEONLY = "write_only";
    public static final String BEAN_NAME_READ_BEONLY = "read_only";


    @Bean(name = BEAN_NAME_WRITE_BEONLY)
    public io.ebean.Database database(WriteonlyDbConfig writeonlyDbConfig){

        HikariDataSource datasource =  new HikariDataSource();
        datasource.setJdbcUrl(writeonlyDbConfig.getUrl());
        datasource.setUsername(writeonlyDbConfig.getUsername());
        datasource.setPassword(writeonlyDbConfig.getPassword());
        try {
            datasource.setLoginTimeout(12);
            datasource.setIdleTimeout(20000);
            datasource.setMinimumIdle(1);
            datasource.setConnectionTimeout(2000);
            datasource.setMaximumPoolSize(20);
            datasource.setPoolName("ebean_write_only");
            datasource.setReadOnly(false);
            datasource.setDriverClassName(writeonlyDbConfig.getDriver_class());
            datasource.setConnectionTestQuery("SELECT 1");
        } catch (SQLException e) {
            log.error("write_only datasource init failed", e);
            throw new RuntimeException(e);
        }

        DatabaseConfig config = new DatabaseConfig();
        config.setName(writeonlyDbConfig.getDb_name());
        config.setRegister(true);
        config.setDefaultServer(false);
        config.setPackages(writeonlyDbConfig.getPackages());
        config.setDdlCreateOnly(false);
        config.setDdlGenerate(false);
        config.setDdlRun(false);
        config.setDbSchema(writeonlyDbConfig.getSchema());
        config.setDataSource(datasource);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.disable(MapperFeature.AUTO_DETECT_CREATORS,
                MapperFeature.AUTO_DETECT_FIELDS,
                MapperFeature.AUTO_DETECT_GETTERS,
                MapperFeature.AUTO_DETECT_SETTERS,
                MapperFeature.AUTO_DETECT_IS_GETTERS);
        config.setObjectMapper(objectMapper);
        return DatabaseFactory.create(config);
    }

    @Bean(name = BEAN_NAME_READ_BEONLY)
    public io.ebean.Database readOnlyDatabaseConfig(ReadonlyDbConfig readonlyDbConfig){
        HikariDataSource datasource =  new HikariDataSource();
        datasource.setJdbcUrl(readonlyDbConfig.getUrl());
        datasource.setUsername(readonlyDbConfig.getUsername());
        datasource.setPassword(readonlyDbConfig.getPassword());
        try {
            datasource.setLoginTimeout(12);
            datasource.setIdleTimeout(20000);
            datasource.setMinimumIdle(1);
            datasource.setConnectionTimeout(2000);
            datasource.setMaximumPoolSize(20);
            datasource.setPoolName("ebean_read_only");
            datasource.setReadOnly(true);
            datasource.setDriverClassName(readonlyDbConfig.getDriver_class());
            datasource.setConnectionTestQuery("SELECT 1");
        } catch (SQLException e) {
            log.error("write_only datasource init failed", e);
            throw new RuntimeException(e);
        }

        DatabaseConfig config = new DatabaseConfig();
        config.setName(readonlyDbConfig.getDb_name());
        config.setRegister(true);
        config.setDefaultServer(false);
        config.setPackages(readonlyDbConfig.getPackages());
        config.setDdlCreateOnly(false);
        config.setDdlGenerate(false);
        config.setDdlRun(false);
        config.setDbSchema(readonlyDbConfig.getSchema());
        config.setDataSource(datasource);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.disable(MapperFeature.AUTO_DETECT_CREATORS,
                MapperFeature.AUTO_DETECT_FIELDS,
                MapperFeature.AUTO_DETECT_GETTERS,
                MapperFeature.AUTO_DETECT_SETTERS,
                MapperFeature.AUTO_DETECT_IS_GETTERS);
        config.setObjectMapper(objectMapper);
        return DatabaseFactory.create(config);
    }
}
