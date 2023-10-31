package codes.showme.domain.repository;


import com.google.common.base.Objects;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties("codeplanet.db.writeonly")
public class WriteonlyDbConfig  {

    private String driver_class;
    private String url;
    private String dialect;
    private String time_zone="UTC";

    private String schema = "everythingincode";

    private String db_name;
    private String username;
    private String password;
    private String current_session_context_class = "thread";
    private boolean show_sql = true;
    private boolean format_sql = true;
    private List<String> packages;

    public List<String> getPackages() {
        return packages;
    }

    public void setPackages(List<String> packages) {
        this.packages = packages;
    }

    public String getDb_name() {
        return db_name;
    }

    public void setDb_name(String db_name) {
        this.db_name = db_name;
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

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    @Override
    public String toString() {
        return "WriteonlyDbConfig{" +
                "driver_class='" + driver_class + '\'' +
                ", url='" + url + '\'' +
                ", dialect='" + dialect + '\'' +
                ", time_zone='" + time_zone + '\'' +
                ", schema='" + schema + '\'' +
                ", db_name='" + db_name + '\'' +
                ", current_session_context_class='" + current_session_context_class + '\'' +
                ", show_sql=" + show_sql +
                ", format_sql=" + format_sql +
                ", packages=" + packages +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WriteonlyDbConfig that = (WriteonlyDbConfig) o;
        return show_sql == that.show_sql && format_sql == that.format_sql && Objects.equal(driver_class, that.driver_class) && Objects.equal(url, that.url) && Objects.equal(dialect, that.dialect) && Objects.equal(time_zone, that.time_zone) && Objects.equal(schema, that.schema) && Objects.equal(db_name, that.db_name) && Objects.equal(username, that.username) && Objects.equal(password, that.password) && Objects.equal(current_session_context_class, that.current_session_context_class) && Objects.equal(packages, that.packages);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(driver_class, url, dialect, time_zone, schema, db_name, username, password, current_session_context_class, show_sql, format_sql, packages);
    }
}
