package codes.showme.domain.schedule;

import codes.showme.domain.repository.EbeanConfig;
import codes.showme.techlib.ioc.InstanceFactory;
import codes.showme.techlib.pagination.Pagination;
import io.ebean.Database;
import io.ebean.PagedList;
import org.springframework.stereotype.Component;

import java.util.List;
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

    @Override
    public Pagination<ScheduleRule> list(int pageNum, int limit) {
        Database database = InstanceFactory.getInstance(Database.class, EbeanConfig.BEAN_NAME_READ_BEONLY);
        int limitInner = Pagination.buildDbQueryLimit(limit);
        int firstRow =  Pagination.buildDbQueryFirstRow(pageNum, limit);
        PagedList<ScheduleRule> pagedList = database.find(ScheduleRule.class).setFirstRow(firstRow).setMaxRows(limitInner).findPagedList();
        return new Pagination<>(pageNum, limitInner, pagedList.getTotalCount(), pagedList.getList());
    }
}
