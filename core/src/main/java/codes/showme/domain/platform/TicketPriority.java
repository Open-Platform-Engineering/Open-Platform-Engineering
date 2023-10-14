package codes.showme.domain.platform;

import codes.showme.domain.tenant.TenantAbility;
import jakarta.persistence.*;

/**
 * https://support.pagerduty.com/docs/incident-priority
 */
@Table(name = "cp_ticket_priorities")
@Entity
public class TicketPriority extends TenantAbility {
    @Id
    @GeneratedValue
    private Long id;


    @Column(name = "name", length = 16)
    private String name;

    @Column(name = "p_order", length = 4)
    private int order;
    @Column(name = "color", length = 16)
    private String color;

}
