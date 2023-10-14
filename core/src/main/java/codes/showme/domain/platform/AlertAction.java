package codes.showme.domain.platform;

/**
 * Whether a service creates only incidents, or both alerts and incidents. A service must create alerts in order to enable incident merging.
 */
public enum AlertAction {
    /**
     * "create_incidents" - The service will create one incident and zero alerts for each incoming event.
     */
    create_incidents,
    /**
     * "create_alerts_and_incidents" - The service will create one incident and one associated alert for each incoming event.
     */
    create_alerts_and_incidents
}
