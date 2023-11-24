package codes.showme.domain.platform;

import codes.showme.domain.repository.EbeanConfig;
import codes.showme.techlib.ioc.InstanceFactory;
import io.ebean.Database;
import org.springframework.stereotype.Component;

@Component
public class ServiceRepositoryImpl implements ServiceRepository{

    @Override
    public long save(Service service) {
        Database instance = InstanceFactory.getInstance(Database.class, EbeanConfig.BEAN_NAME_WRITE_BEONLY);
        instance.save(service);
        return service.getId();
    }

}
