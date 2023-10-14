package codes.showme.domain.observability;

import codes.showme.domain.observability.repository.RunbookRepository;
import codes.showme.techlib.ioc.InstanceFactory;
import jakarta.persistence.*;

/*
 * https://www.pagerduty.com/resources/learn/what-is-a-runbook/
 * A runbook is a detailed “how-to” guide for completing a commonly repeated task or procedure within a company’s IT operations process (e.g. provisioning, software updates / deployment, change configs, and opening ports)
 */
@Entity
@Table(name = "cp_runbooks")
public class Runbook {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public long persist() {
        RunbookRepository runbookRepository = InstanceFactory.getInstance(RunbookRepository.class);
        return runbookRepository.save(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
