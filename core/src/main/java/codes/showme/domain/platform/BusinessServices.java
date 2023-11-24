package codes.showme.domain.platform;

import jakarta.persistence.*;

/**
 * Create business services to model how technical services support your business infrastructure and to communicate incident status to non-technical stakeholders
 */
@Entity
@Table(name = "cp_biz_services")
public class BusinessServices {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", length = 64)
    private String name;

}
