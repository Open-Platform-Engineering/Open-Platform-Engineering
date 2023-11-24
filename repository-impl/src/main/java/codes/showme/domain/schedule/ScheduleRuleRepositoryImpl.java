package codes.showme.domain.schedule;

import codes.showme.domain.repository.EbeanConfig;
import codes.showme.techlib.ioc.InstanceFactory;
import io.ebean.Database;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ScheduleRuleRepositoryImpl implements ScheduleRuleRepository {
    @Override
    public long save(ScheduleRule scheduleRule) {
        Database database = InstanceFactory.getInstance(Database.class, EbeanConfig.BEAN_NAME_WRITE_BEONLY);
        database.save(scheduleRule);
        return scheduleRule.getId();
    }

    @Override
    public Optional<ScheduleRule> findById(long scheduleId) {
        Database database = InstanceFactory.getInstance(Database.class, EbeanConfig.BEAN_NAME_READ_BEONLY);
        return database.find(ScheduleRule.class).where().eq("id", scheduleId).findOneOrEmpty();
    }
}
