package codes.showme.domain.observability.incident;

import codes.showme.domain.incident.IncidentAcknowledgeEvent;

import java.util.Date;

public class IncidentAcknowledgeEventMock implements IncidentAcknowledgeEvent {
    private Date time;

    @Override
    public Date getTime() {
        return time;
    }
}
