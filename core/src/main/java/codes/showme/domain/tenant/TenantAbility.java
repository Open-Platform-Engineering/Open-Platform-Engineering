package codes.showme.domain.tenant;


import io.ebean.annotation.TenantId;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.util.UUID;

@MappedSuperclass
public abstract class TenantAbility {
    public static final String COLUMN_TENANT_ID = "tenant_id";

    @TenantId
    @Column(name = COLUMN_TENANT_ID)
    private UUID tenantId;

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }
}
