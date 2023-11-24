package codes.showme.domain.incident;

import codes.showme.domain.repository.EbeanConfig;
import codes.showme.techlib.ioc.InstanceFactory;
import io.ebean.Database;
import org.springframework.stereotype.Component;

@Component
public class IncidentRepositoryImpl implements IncidentRepository{

    @Override
    public long save(Incident incident) {
        Database instance = InstanceFactory.getInstance(Database.class, EbeanConfig.BEAN_NAME_WRITE_BEONLY);
        instance.save(incident);
        return incident.getId();
    }

}
