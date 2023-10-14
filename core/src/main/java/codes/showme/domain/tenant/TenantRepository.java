package codes.showme.domain.tenant;

import java.util.UUID;

public interface TenantRepository {
    UUID save(Tenant tenant);
}
