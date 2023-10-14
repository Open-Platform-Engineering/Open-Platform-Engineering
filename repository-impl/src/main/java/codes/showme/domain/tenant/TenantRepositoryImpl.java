package codes.showme.domain.tenant;

import codes.showme.techlib.ioc.InstanceFactory;
import io.ebean.Database;

import java.util.UUID;

public class TenantRepositoryImpl implements TenantRepository{
    @Override
    public UUID save(Tenant tenant) {
        Database database = InstanceFactory.getInstance(Database.class);
        database.save(tenant);
        return tenant.getId();
    }
}
