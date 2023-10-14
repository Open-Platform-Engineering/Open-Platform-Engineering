package codes.showme.domain.incident;

import codes.showme.domain.AbstractIntegrationTest;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class IncidentRepositoryTest extends AbstractIntegrationTest {
    @Test
    public void name() throws IOException {
        Incident incident = new Incident();
        long id = incident.save();
        Assert.assertEquals(1l, id);
    }
}
