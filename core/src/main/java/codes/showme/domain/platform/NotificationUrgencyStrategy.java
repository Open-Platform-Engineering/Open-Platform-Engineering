package codes.showme.domain.platform;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;

/**
 * Having a high priority is not present that having to do it right now.
 * https://support.pagerduty.com/docs/configurable-service-settings#notification-urgency
 * High-urgency notifications, escalate as needed
 * Low-urgency notifications, do not escalate
 * Dynamic notifications based on alert severity
 * Based on support hours: Once selected, select the days of the week and the hours and time zone for your support hours.
 *  Next, under During support hours, use, select what type of notification urgency you would like during support hours. Under Outside support hours, use, select what type of notification urgency you would like outside of support hours. You may also optionally choose to check the box next to Raise urgency of unacknowledged incidents to high when support hours start.
 */

public enum NotificationUrgencyStrategy {
    HIGH,
    LOW,
    DYNAMIC,
    BASED_ON_SUPPORT_HOURS






}
