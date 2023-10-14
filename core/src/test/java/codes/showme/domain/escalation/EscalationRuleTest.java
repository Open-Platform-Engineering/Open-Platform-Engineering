package codes.showme.domain.escalation;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class EscalationRuleTest {


    @Test(expected = UnsupportedOperationException.class)
    public void timeoutTest() {
        EscalationRule escalationRule = new EscalationRule();
        escalationRule.setEscalatesTimeoutMin(-10);
    }

    @Test
    public void addTarget() {
        EscalationRule escalationRule = new EscalationRule();
        EscalationUserTarget target = new EscalationUserTarget();
        target.setUser(1l,"Jack");
        escalationRule.addTarget(target);

        EscalationScheduleTarget scheduleTarget = new EscalationScheduleTarget();
        scheduleTarget.setSchedule(2l, "sre_schedule");
        escalationRule.addTarget(scheduleTarget);

    }
}
