package codes.showme.domain.incident;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.List;

/**
 * https://support.pagerduty.com/docs/incidents#incident-timeline
 */
@Entity
@Table(name = "cp_incident_timelines")
public class IncidentTimeline {

    @Id
    private long id;

    public List<IncidentEvent> list(){
        return null;
    }
}
