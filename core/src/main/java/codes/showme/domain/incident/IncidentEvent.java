package codes.showme.domain.incident;

import jakarta.persistence.*;

import java.util.Date;


@Entity
@Table(name = "cp_incident_events")
public class IncidentEvent {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type")
    private IncidentEventSourceType eventChannelType;

    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime ;

    public Long getId() {
        return id;
    }

    public IncidentEventSourceType getEventChannelType() {
        return eventChannelType;
    }
}
