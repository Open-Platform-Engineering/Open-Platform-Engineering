package codes.showme.domain.escalation;

import codes.showme.domain.AbstractIntegrationTest;
import codes.showme.techlib.pagination.Pagination;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class EscalationPolicyIntegrationTest extends AbstractIntegrationTest {


    @Before
    public void setUpEscalationPolicy() throws Exception {
        EscalationPolicyRepository ticketRepository = new EscalationPolicyRepositoryImpl();
        Mockito.when(instanceProvider.getInstance(EscalationPolicyRepository.class)).thenReturn(ticketRepository);
    }

    @Test
    public void name() throws IOException {
        EscalationPolicy escalationPolicy = new EscalationPolicy();
        escalationPolicy.addTag("ticket", "12376");

        EscalationRule rule = new EscalationRule();
        rule.setEscalatesTimeoutMin(30);
        rule.setRoundRobinSchedulingEnabled(false);

        EscalationScheduleTarget escalationScheduleTarget = new EscalationScheduleTarget();
        escalationScheduleTarget.setSchedule(12, "sre");
        rule.addTarget(escalationScheduleTarget);

        EscalationUserTarget userTarget = new EscalationUserTarget();
        userTarget.setUserId(22l);
        userTarget.setDisplayName("Jack");
        rule.addTarget(userTarget);

        escalationPolicy.addRule(rule);
        long id = escalationPolicy.save();
        assertEquals(1l, id);

        EscalationPolicy escalationPolicy2 = new EscalationPolicy();
        escalationPolicy2.save();
        assertEquals(2l, escalationPolicy2.getId().longValue());

        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(escalationPolicy));

        codes.showme.techlib.pagination.Pagination<EscalationPolicy> pagination = EscalationPolicy.list(1, 20);
        List<EscalationPolicy> results = pagination.getResults();
        assertEquals(2, results.size());

        Pagination<EscalationPolicy> listExistsByScheduleTarget = EscalationPolicy.listExistsByScheduleTarget(1, 10, 12);
        List<EscalationPolicy> listExistsByScheduleTargetResults = listExistsByScheduleTarget.getResults();
        assertEquals(1, listExistsByScheduleTarget.getTotalRecord());
        assertEquals(escalationPolicy.getId(), listExistsByScheduleTargetResults.get(0).getId());

    }

}
