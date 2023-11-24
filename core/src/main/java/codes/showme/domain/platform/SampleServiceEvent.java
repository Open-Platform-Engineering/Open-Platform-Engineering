package codes.showme.domain.platform;

import java.util.Date;

public class SampleServiceEvent implements ServiceEvent{

    @Override
    public ServiceEventType getType() {
        return ServiceEventType.ALERT;
    }

    @Override
    public Ticket createTicket(Service service) {
        Ticket result = new Ticket();
        AlertAction alertAction = service.getAlertAction();
        result.setServiceId(service.getId());
        result.setOpenedTime(new Date());
        result.setStatus(TicketStatus.TRIGGERED);
        return result;
    }


    @Override
    public String toString() {
        return "SampleServiceEvent{}";
    }
}
