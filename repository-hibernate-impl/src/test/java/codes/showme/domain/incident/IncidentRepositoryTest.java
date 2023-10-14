package codes.showme.domain.incident;

import codes.showme.domain.AbstractHibernateIntegrationTest;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.Mockito;

public class IncidentRepositoryTest extends AbstractHibernateIntegrationTest {

    @Before
    public void setUp() throws Exception {
        IncidentRepositoryHbnImpl incidentRepositoryHbn = new IncidentRepositoryHbnImpl();
        Mockito.when(instanceProvider.getInstance(IncidentRepository.class)).thenReturn(incidentRepositoryHbn);
    }

    @org.junit.Test
    public void save() {
        Incident incident = new Incident();
        long id = incident.save();
        Assert.assertEquals(1l, id);
    }
}
