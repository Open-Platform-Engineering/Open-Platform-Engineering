package codes.showme.domain.platform;

import codes.showme.domain.AbstractIntegrationTest;
import codes.showme.domain.platform.scheduledaction.*;
import codes.showme.techlib.ioc.InstanceFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

public class ServiceRepositoryImplTest extends AbstractIntegrationTest {

    Service service = new Service();

    @Before
    public void setupService() throws Exception {
        TicketRepositoryImpl ticketRepository = new TicketRepositoryImpl();
        Mockito.when(instanceProvider.getInstance(TicketRepository.class)).thenReturn(ticketRepository);
        ServiceRepositoryImpl serviceRepository = new ServiceRepositoryImpl();
        Mockito.when(instanceProvider.getInstance(ServiceRepository.class)).thenReturn(serviceRepository);

        service.setTenantId(UUID.randomUUID());
        service.setDescription("Random");
        service.setAcknowledgementTimeoutSeconds(3000);
        service.setAutoResolveTimeoutSeconds(60);
        ScheduledActionList scheduledActions = new ScheduledActionList();
        ScheduledAction action = new ScheduledAction();
        action.setAt(ScheduledActionAtType.named_time, ScheduledActionName.support_hours_start);
        action.setType(ScheduledActionType.urgency_change);
        scheduledActions.addAction(action);
        service.setScheduledActions(scheduledActions);
        long id = service.save();
        Assert.assertEquals(1l, id);
    }

    @Test
    public void receiveEvent() {
        Optional<Ticket> optionalTicket = service.receive(new SampleServiceEvent());

        Assert.assertTrue(optionalTicket.isPresent());

        Ticket ticket = optionalTicket.get();

        Assert.assertEquals(service.getId().longValue(), ticket.getServiceId());
        Assert.assertEquals(1l, ticket.getId().longValue());
    }
}
