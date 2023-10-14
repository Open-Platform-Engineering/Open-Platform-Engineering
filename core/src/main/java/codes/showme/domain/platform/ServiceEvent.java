package codes.showme.domain.platform;

public interface ServiceEvent {
    ServiceEventType getType();

    Ticket createTicket(Service service);
}
