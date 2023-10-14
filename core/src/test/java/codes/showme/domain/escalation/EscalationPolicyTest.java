package codes.showme.domain.escalation;

import org.junit.Assert;
import org.junit.Test;

public class EscalationPolicyTest {


    @Test
    public void create() {
        EscalationPolicy escalationPolicy = new EscalationPolicy();
        escalationPolicy.setName("sre-team");
        escalationPolicy.setHandoffNotificationEnabled(true);
        EscalationRule firstRule = new EscalationRule();
        firstRule.setEscalatesTimeoutMin(20);
        escalationPolicy.addRule(firstRule);
        escalationPolicy.addTag("jira","abc-123");

        Assert.assertEquals("abc-123", escalationPolicy.getTags().get("jira"));

    }

    @Test
    public void noOneAckThenRepeatTimesEnabled() {
        EscalationPolicy escalationPolicy = new EscalationPolicy();
        escalationPolicy.setNoOneAckThenRepeatTimes(9);
        Assert.assertTrue(escalationPolicy.noOneAckThenRepeatTimesEnabled());
    }

    @Test
    public void noOneAckThenRepeatTimesDisabled() {
        EscalationPolicy escalationPolicy = new EscalationPolicy();
        escalationPolicy.setNoOneAckThenRepeatTimes(0);
        Assert.assertFalse(escalationPolicy.noOneAckThenRepeatTimesEnabled());
    }
}