package codes.showme.domain.platform;

//https://ownership.pagerduty.com/functions/#service-tiers

import jakarta.persistence.*;


@Entity
@Table(name = "cp_tier")
public class Tier {

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
