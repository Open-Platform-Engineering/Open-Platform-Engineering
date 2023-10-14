package codes.showme.domain.tenant;

import codes.showme.domain.AbstractIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;

public class TenantRepositoryImplTest extends AbstractIntegrationTest {

    @Before
    public void aSetUp() throws Exception {
        TenantRepositoryImpl tenantRepository = new TenantRepositoryImpl();
        Mockito.when(instanceProvider.getInstance(TenantRepository.class)).thenReturn(tenantRepository);
    }

    @Test
    public void saveTest() {
        Tenant tenant = new Tenant();
        tenant.setName("abc-tenant");
        Assert.assertNotNull(tenant.save());
    }
}
