package codes.showme.domain.incident;

import codes.showme.domain.AbstractIntegrationTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

public class IncidentRepositoryTest extends AbstractIntegrationTest {

    @Before
    public void init() throws Exception {
        IncidentRepositoryImpl incidentRepository = new IncidentRepositoryImpl();
        Mockito.when(instanceProvider.getInstance(IncidentRepository.class)).thenReturn(incidentRepository);
    }

    @Test
    public void name() throws IOException {
        Incident incident = new Incident();
        long id = incident.save();
        Assert.assertEquals(1l, id);
    }
}
