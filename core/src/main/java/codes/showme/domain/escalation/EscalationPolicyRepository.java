package codes.showme.domain.escalation;

import codes.showme.techlib.pagination.Pagination;

import java.util.List;

public interface EscalationPolicyRepository {

    long save(EscalationPolicy escalationPolicy);

    codes.showme.techlib.pagination.Pagination<EscalationPolicy> list(int pageIndex, int pageSize);



    Pagination<EscalationPolicy> listExistsByScheduleTarget(int pageIndex, int pageSize, long scheduleId);
}
