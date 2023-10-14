package codes.showme.domain.notification;

import jakarta.persistence.*;

/**
 * on-call人员对于告警通知方式的喜好
 */
@Entity
@Table(name = "cp_alert_notification_prefers")
public class AlertNotificationPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
