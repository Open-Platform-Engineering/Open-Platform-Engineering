package codes.showme.domain.platform;

import codes.showme.techlib.ioc.InstanceFactory;
import io.ebean.Database;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ServiceRepositoryImpl implements ServiceRepository{

    @Override
    public long save(Service service) {
        Database instance = InstanceFactory.getInstance(Database.class);
        instance.save(service);
        return service.getId();
    }

}
