package codes.showme.domain.platform;

/**
 * The current state of the Service. Valid statuses are:
 *
 * active: The service is enabled and has no open incidents. This is the only status a service can be created with.
 * warning: The service is enabled and has one or more acknowledged incidents.
 * critical: The service is enabled and has one or more triggered incidents.
 * maintenance: The service is under maintenance, no new incidents will be triggered during maintenance mode.
 * disabled: The service is disabled and will not have any new triggered incidents.
 */
public enum ServiceStatus {
    ACTIVE,
    WARNING,
    CRITICAL,
    MAINTENANCE,
    DISABLED,
}
