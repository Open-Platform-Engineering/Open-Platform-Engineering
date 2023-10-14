package codes.showme.domain.schedule;

import java.util.Optional;

public interface ScheduleRuleRepository {

    long save(ScheduleRule scheduleRule);

    Optional<ScheduleRule> findByIdAndTenantId(long scheduleId, String tenantId);
}
