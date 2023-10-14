package codes.showme.domain.tenant;

import io.ebean.config.CurrentTenantProvider;

public class CurrentTenant implements CurrentTenantProvider {
    @Override
    public String currentId() {
        return UserContext.get().getTenantId();
    }
}
