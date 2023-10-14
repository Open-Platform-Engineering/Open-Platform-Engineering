package codes.showme.domain.observability.schedule;

import codes.showme.domain.schedule.ScheduleRule;
import codes.showme.domain.schedule.ScheduleRuleRepository;
import codes.showme.techlib.ioc.InstanceFactory;
import jakarta.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class ScheduleRuleHbnRepositoryImpl implements ScheduleRuleRepository {

    @Override
    public long save(codes.showme.domain.schedule.ScheduleRule scheduleRule) {
        EntityManager entityManager = InstanceFactory.getInstance(EntityManager.class);
        entityManager.persist(scheduleRule);
        return scheduleRule.getId();

    }

    @Override
    public Optional<ScheduleRule> findByIdAndTenantId(long scheduleId, String tenantId) {
        return Optional.empty();
    }
}
