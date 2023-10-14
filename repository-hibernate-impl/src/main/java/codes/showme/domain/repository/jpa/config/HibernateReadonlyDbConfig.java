package codes.showme.domain.repository.jpa.config;


import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties("e_i_codeplanet.db.readonly")
public class HibernateReadonlyDbConfig implements InitializingBean {


    /**
     * settings.put("dialect", "org.hibernate.dialect.MySQL8Dialect");
     *         settings.put("hibernate.connection.url",
     *                 "jdbc:mysql://localhost/hibernate_examples");
     *         settings.put("hibernate.connection.username", "root");
     *         settings.put("hibernate.connection.password", "password");
     *         settings.put("hibernate.jdbc.time_zone", "UTC");
     *         settings.put("hibernate.current_session_context_class", "thread");
     *         settings.put("hibernate.show_sql", "true");
     *         settings.put("hibernate.format_sql", "true");
     */

    private String driver_class;
    private String url;
    private String dialect;
    private String time_zone;
    private String username;
    private String password;
    private String current_session_context_class = "thread";
    private boolean show_sql = true;
    private boolean format_sql = true;

    @Value("${packages}")
    private String packagesSplitByComma;

    public Map<String, Object> exportProperties(){
        Map<String, Object> result = new HashMap<>();
        result.put("jakarta.persistence.jdbc.driver", getDriver_class());
        result.put("jakarta.persistence.jdbc.url", getUrl());
        result.put("jakarta.persistence.jdbc.user", getUsername());
        result.put("jakarta.persistence.jdbc.password", getPassword());
        result.put(Environment.DIALECT, getDialect());
        result.put(Environment.JDBC_TIME_ZONE, getTime_zone());
        result.put(Environment.SHOW_SQL, isShow_sql());
        result.put(Environment.FORMAT_SQL, isFormat_sql());
        result.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, getCurrent_session_context_class());
        return result;
    }
    public String getPackagesSplitByComma() {
        return packagesSplitByComma;
    }

    public void setPackagesSplitByComma(String packagesSplitByComma) {
        this.packagesSplitByComma = packagesSplitByComma;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public String getDriver_class() {
        return driver_class;
    }

    public void setDriver_class(String driver_class) {
        this.driver_class = driver_class;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public String getTime_zone() {
        return time_zone;
    }

    public void setTime_zone(String time_zone) {
        this.time_zone = time_zone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCurrent_session_context_class() {
        return current_session_context_class;
    }

    public void setCurrent_session_context_class(String current_session_context_class) {
        this.current_session_context_class = current_session_context_class;
    }

    public boolean isShow_sql() {
        return show_sql;
    }

    public void setShow_sql(boolean show_sql) {
        this.show_sql = show_sql;
    }

    public boolean isFormat_sql() {
        return format_sql;
    }

    public void setFormat_sql(boolean format_sql) {
        this.format_sql = format_sql;
    }
}
