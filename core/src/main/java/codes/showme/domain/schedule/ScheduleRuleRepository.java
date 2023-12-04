package codes.showme.domain.schedule;

import codes.showme.techlib.pagination.Pagination;

import java.util.Optional;

public interface ScheduleRuleRepository {

    long save(ScheduleRule scheduleRule);

    Optional<ScheduleRule> findById(long scheduleId);

    Pagination<ScheduleRule> list(int pageNum, int limit);
}
