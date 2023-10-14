package codes.showme.domain.schedule;

import codes.showme.domain.schedule.ScheduleRule;
import codes.showme.domain.schedule.ScheduleRuleRepository;
import codes.showme.techlib.ioc.InstanceFactory;
import io.ebean.Database;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

@Component
public class ScheduleRuleRepositoryImpl implements ScheduleRuleRepository {

    @Override
    public long save(ScheduleRule scheduleRule) {
        Database database = InstanceFactory.getInstance(Database.class);
        database.save(scheduleRule);
        return scheduleRule.getId();
    }

    @Override
    public Optional<ScheduleRule> findByIdAndTenantId(long scheduleId, String tenantId) {
        Database database = InstanceFactory.getInstance(Database.class);
        return database.find(ScheduleRule.class).where().eq("id", scheduleId).and().eq(ScheduleRule.COLUMN_TENANT_ID, tenantId).findOneOrEmpty();
    }
}
