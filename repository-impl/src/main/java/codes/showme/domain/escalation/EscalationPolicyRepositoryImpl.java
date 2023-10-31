package codes.showme.domain.escalation;

import codes.showme.techlib.ioc.InstanceFactory;
import codes.showme.techlib.pagination.Pagination;
import io.ebean.Database;
import io.ebean.PagedList;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EscalationPolicyRepositoryImpl implements EscalationPolicyRepository {
    @Override
    public long save(EscalationPolicy escalationPolicy) {
        Database instance = InstanceFactory.getInstance(Database.class);
        instance.save(escalationPolicy);
        return escalationPolicy.getId();
    }

    @Override
    public Pagination<EscalationPolicy> list(int pageIndex, int pageSize) {
        Database database = InstanceFactory.getInstance(Database.class);
        PagedList<EscalationPolicy> pagedList = database.find(EscalationPolicy.class).setFirstRow(pageIndex - 1)
                .setMaxRows(pageSize).orderBy().desc(EscalationPolicy.COLUMN_CREATED_TIME).findPagedList();
        return new Pagination(pageIndex, pageSize, pagedList.getTotalCount(), pagedList.getList());
    }

    @Override
    public Pagination<EscalationPolicy> listExistsByScheduleTarget(int pageIndex, int pageSize, long scheduleId) {
        Database database = InstanceFactory.getInstance(Database.class);

        PagedList<EscalationPolicy> pagedList = database.find(EscalationPolicy.class)
                .where()
                .raw("rule_set::jsonb @@ '$.rules[*].escalation_targets[*].schedule_id == " + scheduleId + "'")
                .setFirstRow(pageIndex - 1).setMaxRows(pageSize)
//                .jsonEqualTo(
//                        EscalationPolicy.COLUMN_RULE_SET,
//                        "rules[*],escalation_targets[*],schedule_id",
//                        scheduleId)
                .orderBy().desc(EscalationPolicy.COLUMN_CREATED_TIME).findPagedList();
        return new Pagination(pageIndex, pageSize, pagedList.getTotalCount(), pagedList.getList());
    }

}
