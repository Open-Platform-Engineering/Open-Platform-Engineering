package codes.showme.domain.incident;


/**
 * doc https://support.pagerduty.com/docs/incidents#incident-statuses
 */
public enum IncidentStatus {
    /**
     * An active service (i.e., someone is on call and the service is not disabled or in maintenance mode) will
     * trigger an incident when it receives an event. The incident will escalate according to the service's
     * escalation policy. By default, PagerDuty sends notifications when an incident is triggered,
     * but not when it is acknowledged or resolved. Users create their own notification
     * rules — or they can use webhooks — to receive notifications when an incident is acknowledged or resolved.
     */
    TRIGGERED,
    /**
     * An acknowledged incident is being worked on, but is not yet resolved.
     * The user that acknowledges an incident claims ownership of the issue, and halts the escalation process.
     * While an incident is acknowledged, notifications are not sent until the acknowledgement timeout is reached.
     * If the acknowledgement timeout is reached, the incident goes from the acknowledged status back to the triggered status.
     * The escalation process also resumes.
     */
    ACKNOWLEDGED,
    /**
     * A resolved incident has been fixed. Once an incident is resolved,
     * no additional notifications are sent and the incident cannot be triggered again.
     */
    RESOLVED
}
