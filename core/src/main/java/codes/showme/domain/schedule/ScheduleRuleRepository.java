package codes.showme.domain.schedule;

import java.util.Optional;
import java.util.UUID;

public interface ScheduleRuleRepository {

    long save(ScheduleRule scheduleRule);

    Optional<ScheduleRule> findByIdAndTenantId(long scheduleId, UUID tenantId);
}
