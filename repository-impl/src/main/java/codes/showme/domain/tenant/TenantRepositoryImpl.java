package codes.showme.domain.tenant;

import codes.showme.domain.repository.EbeanConfig;
import codes.showme.techlib.ioc.InstanceFactory;
import io.ebean.Database;

import java.util.UUID;

public class TenantRepositoryImpl implements TenantRepository{
    @Override
    public UUID save(Tenant tenant) {
        Database database = InstanceFactory.getInstance(Database.class, EbeanConfig.BEAN_NAME_WRITE_BEONLY);
        database.save(tenant);
        return tenant.getId();
    }
}
