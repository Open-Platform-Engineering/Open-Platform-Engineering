package codes.showme.domain.observability.incident;

import codes.showme.domain.incident.Incident;
import codes.showme.domain.incident.IncidentEvent;
import codes.showme.domain.incident.IncidentRepository;
import codes.showme.domain.incident.IncidentStatus;
import codes.showme.domain.observability.AbstractIntegrationTest;
import codes.showme.techlib.ioc.InstanceProvider;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class IncidentTest extends AbstractIntegrationTest {

    @Test
    public void statusTest() {

        InstanceProvider instanceProvider = setupInstanceProvider();

        Mockito.when(instanceProvider.getInstance(IncidentRepository.class)).thenReturn(new IncidentRepositoryMock());

        IncidentEvent incidentEvent = new IncidentEvent();

        Incident incident = Incident.triggeredBy(incidentEvent);

        Assert.assertEquals(IncidentStatus.TRIGGERED, incident.getStatus());

        incident.acknowledging(new IncidentAcknowledgeEventMock());

        Assert.assertEquals(IncidentStatus.ACKNOWLEDGED, incident.getStatus());

        incident.resolved(new IncidentResolveEventMock());

    }
}