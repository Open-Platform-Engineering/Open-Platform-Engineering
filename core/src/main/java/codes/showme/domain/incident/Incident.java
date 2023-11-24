package codes.showme.domain.incident;

import codes.showme.domain.team.Reassignable;
import codes.showme.techlib.ioc.InstanceFactory;
import jakarta.persistence.*;


import java.util.Date;

/**
 * https://support.pagerduty.com/docs/incidents
 */
@Entity
@Table(name = "cp_incidents")
public class Incident {

    @Id
    @GeneratedValue
    private Long id;

    @Version
    private int version;


    @Enumerated
    @Column(name = "incident_status")
    private IncidentStatus status = IncidentStatus.TRIGGERED;

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "opened_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date openedTime;

    @Column(name = "ack_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date acknowledgedTime;

    @Column(name = "reopened_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reopenedTime;

    @Column(name = "resolved_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date resolvedTime;

    @Column(name = "duration_ms")
    private long durationMs;



    /**
     * 被一个事件触发
     *
     * @param event
     */
    public static Incident triggeredBy(IncidentEvent event) {
        Incident incident = new Incident();
        incident.eventId = event.getId();
        return incident;
    }

    /**
     * https://support.pagerduty.com/docs/incident-priority#prioritize-an-incident
     */
    public void prioritize() {

    }

    /**
     * https://support.pagerduty.com/docs/incidents#4-acknowledging-and-resolving
     */
    public void acknowledging(IncidentAcknowledgeEvent incidentAcknowledgeEvent) {
        this.status = IncidentStatus.ACKNOWLEDGED;
        acknowledgedTime = incidentAcknowledgeEvent.getTime();
        InstanceFactory.getInstance(IncidentRepository.class).save(this);
    }

    /**
     * https://support.pagerduty.com/docs/reassign-incidents#reassign-an-incident
     *
     * @param reassignable
     */
    public void reassign(Reassignable reassignable) {
        System.out.println(reassignable);
    }

    public void resolved(IncidentResolveEvent incidentResolveEvent) {
        this.status = IncidentStatus.RESOLVED;
        InstanceFactory.getInstance(IncidentRepository.class).save(this);
    }

    public long save() {
        IncidentRepository repository = InstanceFactory.getInstance(IncidentRepository.class);
        return repository.save(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public IncidentStatus getStatus() {
        return status;
    }

    public void setStatus(IncidentStatus status) {
        this.status = status;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Date getOpenedTime() {
        return openedTime;
    }

    public void setOpenedTime(Date openedTime) {
        this.openedTime = openedTime;
    }

    public Date getAcknowledgedTime() {
        return acknowledgedTime;
    }

    public void setAcknowledgedTime(Date acknowledgedTime) {
        this.acknowledgedTime = acknowledgedTime;
    }

    public Date getReopenedTime() {
        return reopenedTime;
    }

    public void setReopenedTime(Date reopenedTime) {
        this.reopenedTime = reopenedTime;
    }

    public Date getResolvedTime() {
        return resolvedTime;
    }

    public void setResolvedTime(Date resolvedTime) {
        this.resolvedTime = resolvedTime;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(long durationMs) {
        this.durationMs = durationMs;
    }
}
