package codes.showme.domain.incident;

import io.ebean.Database;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class IncidentRepositoryImpl implements IncidentRepository{
    
    @Resource
    private Database database;

    @Override
    public long save(Incident incident) {
        database.save(incident);
        return incident.getId();
    }

    public void setDatabase(Database database) {
        this.database = database;
    }
}
