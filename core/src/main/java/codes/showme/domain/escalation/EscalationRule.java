package codes.showme.domain.escalation;

import codes.showme.techlib.ioc.InstanceFactory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import io.ebean.annotation.DbJsonB;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EscalationRule implements Comparable<EscalationRule> {

    public static final String COLUMN_ESCALATION_TARGETS = "escalation_targets";
    @JsonProperty(COLUMN_ESCALATION_TARGETS)
    private Set<EscalationTarget> escalationTargets = new HashSet<>();

    @JsonProperty("escalates_timeout_min")
    private int escalatesTimeoutMin;

    @JsonProperty("round_robin_scheduling_enabled")
    private boolean roundRobinSchedulingEnabled;

    public EscalationRule addTarget(EscalationTarget target) {
        if (Objects.isNull(target)) {
            return this;
        }
        escalationTargets.add(target);
        return this;
    }


    public Set<EscalationTarget> getEscalationTargets() {
        return Collections.unmodifiableSet(escalationTargets);
    }

    public void setEscalatesTimeoutMin(int escalatesTimeoutMin) {
        if (escalatesTimeoutMin <= 0) {
            throw new UnsupportedOperationException("escalatesTimeoutMin must be > 0");
        }
        this.escalatesTimeoutMin = escalatesTimeoutMin;
    }

    public boolean isRoundRobinSchedulingEnabled() {
        return roundRobinSchedulingEnabled;
    }

    public void setRoundRobinSchedulingEnabled(boolean roundRobinSchedulingEnabled) {
        this.roundRobinSchedulingEnabled = roundRobinSchedulingEnabled;
    }

    public int getEscalatesTimeoutMin() {
        return escalatesTimeoutMin;
    }


    @Override
    public int compareTo(EscalationRule o) {
        return 0;
    }
}
