package codes.showme.domain.platform;

import codes.showme.domain.tenant.TenantAbility;
import codes.showme.techlib.ioc.InstanceFactory;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "cp_tickets")
public class Ticket extends TenantAbility {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "service_id")
    private long serviceId;

    @Column(name = "name", length = 128)
    private String name;
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

    @Enumerated
    @Column(name = "ticket_status")
    private TicketStatus status = TicketStatus.TRIGGERED;

    /**
     * https://support.pagerduty.com/docs/incidents#2-assignment-via-escalation-policies-and-schedules
     * notify targets according to Escalation Policies related by the service
     */
    public void triggerNotification() {
        

    }

    public long save() {
        TicketRepository ticketRepository = InstanceFactory.getInstance(TicketRepository.class);
        return ticketRepository.save(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }
}
