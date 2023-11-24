package codes.showme.domain.platform;

import codes.showme.domain.platform.scheduledaction.ScheduledActionList;
import codes.showme.techlib.ioc.InstanceFactory;
import io.ebean.annotation.DbJson;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Optional;

@Entity
@Table(name = "cp_services")
public class Service {

    private static final Logger logger = LoggerFactory.getLogger(Service.class);

    public static final String COLUMN_ONCALL_SCHEDULE_RULE_ID = "oncall_s_rule_id";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", length = 64)
    private String name;

    @Column(name = "escalation_policy_id")
    private Long escalationPolicyId;

    /**
     * A short-form, server-generated string that provides succinct,
     * important information about an object suitable for primary labeling of an entity in a client.
     * In many cases, this will be identical to name, though it is not intended to be an identifier.
     */
    @Column(name = "summary", length = 128)
    private String summary;

    /**
     * The user-provided description of the service.
     */
    @Column(name = "descr", columnDefinition = "text")
    private String description;

    /**
     * Time in seconds that an incident is automatically resolved if left open for that long.
     * Value is null if the feature is disabled. Value must not be negative.
     * Setting this field to 0, null (or unset in POST request) will disable the feature.
     * Default: 14400
     */
    @Column(name = "auto_resolve_timeout_sec")
    private int autoResolveTimeoutSeconds = 14400;

    /**
     * Time in seconds that an incident changes to the Triggered State after being Acknowledged.
     * Value is null if the feature is disabled. Value must not be negative.
     * Setting this field to 0, null (or unset in POST request) will disable the feature.
     */
    @Column(name = "acknowledgement_timeout")
    private int acknowledgementTimeoutSeconds = 1800;

    /**
     * The date/time when the most recent incident was created for this service.
     */
    @Column(name = "last_incident_timestamp")
    private Date lastIncidentTimestamp;

    @Column(name = "svc_status")
    @Enumerated(EnumType.STRING)
    private ServiceStatus serviceStatus = ServiceStatus.ACTIVE;

    @DbJson
    @Column(name = "support_hour")
    private SupportHour supportHour;

    @Column(name = "scheduled_actions")
    private ScheduledActionList scheduledActions = new ScheduledActionList();

    @Column(name = "alert_creation")
    private AlertAction alertAction;

    @Enumerated(EnumType.STRING)
    @Column(name = "urgency_strategy")
    private NotificationUrgencyStrategy notificationUrgencyStrategy;

    @Column(name = COLUMN_ONCALL_SCHEDULE_RULE_ID, nullable = true)
    private Long scheduleRuleId;

    @Column(name = "enabled", columnDefinition = "boolean")
    private boolean enabled = true;

    @DbJson
    @Column(name = "maintenance_windows")
    private ServiceMaintenanceWindows maintenanceWindows;

    public Optional<Ticket> receive(ServiceEvent serviceEvent) {
        if (!enabled) {
            throw new UnsupportedOperationException();
        }
        try {
            Ticket ticket = serviceEvent.createTicket(this);
            ticket.save();

            ticket.triggerNotification();
            return Optional.of(ticket);
        }catch (Exception e){
//            InstanceFactory.getInstance(Observation.class)
//                    .counter("receive.serviceevent","tenant", UserContext.get().getTenantId());
            logger.error("service receive a event:{}, but got an error", serviceEvent, e);
            return Optional.empty();
        }

    }

    public long save(){
        ServiceRepository serviceRepository = InstanceFactory.getInstance(ServiceRepository.class);
        return serviceRepository.save(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAutoResolveTimeoutSeconds() {
        return autoResolveTimeoutSeconds;
    }

    public void setAutoResolveTimeoutSeconds(int autoResolveTimeoutSeconds) {
        this.autoResolveTimeoutSeconds = autoResolveTimeoutSeconds;
    }

    public int getAcknowledgementTimeoutSeconds() {
        return acknowledgementTimeoutSeconds;
    }

    public void setAcknowledgementTimeoutSeconds(int acknowledgementTimeoutSeconds) {
        this.acknowledgementTimeoutSeconds = acknowledgementTimeoutSeconds;
    }

    public Date getLastIncidentTimestamp() {
        return lastIncidentTimestamp;
    }

    public ServiceStatus getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(ServiceStatus serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public SupportHour getSupportHour() {
        return supportHour;
    }

    public void setSupportHour(SupportHour supportHour) {
        this.supportHour = supportHour;
    }

    public ScheduledActionList getScheduledActions() {
        return scheduledActions;
    }

    public void setScheduledActions(ScheduledActionList scheduledActions) {
        this.scheduledActions = scheduledActions;
    }

    public AlertAction getAlertAction() {
        return alertAction;
    }

    public void setAlertAction(AlertAction alertAction) {
        this.alertAction = alertAction;
    }

    public NotificationUrgencyStrategy getNotificationUrgencyStrategy() {
        return notificationUrgencyStrategy;
    }

    public void setNotificationUrgencyStrategy(NotificationUrgencyStrategy notificationUrgencyStrategy) {
        this.notificationUrgencyStrategy = notificationUrgencyStrategy;
    }

    public Long getScheduleRuleId() {
        return scheduleRuleId;
    }

    public void setScheduleRuleId(Long scheduleRuleId) {
        this.scheduleRuleId = scheduleRuleId;
    }

    public Long getEscalationPolicyId() {
        return escalationPolicyId;
    }

    public void setEscalationPolicyId(Long escalationPolicyId) {
        this.escalationPolicyId = escalationPolicyId;
    }

    public ServiceMaintenanceWindows getMaintenanceWindows() {
        return maintenanceWindows;
    }

    public void setMaintenanceWindows(ServiceMaintenanceWindows maintenanceWindows) {
        this.maintenanceWindows = maintenanceWindows;
    }
}
