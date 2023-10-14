package codes.showme.domain.platform.scheduledaction;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

public class ScheduledActionList {
    @JsonProperty("actions")
    private Set<ScheduledAction> actions = new HashSet<>();

    public ScheduledActionList addAction(ScheduledAction action){
        actions.add(action);
        return this;
    }

    public Set<ScheduledAction> getActions() {
        return actions;
    }

}
