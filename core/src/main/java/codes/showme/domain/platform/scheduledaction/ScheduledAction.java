package codes.showme.domain.platform.scheduledaction;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ScheduledAction {
    @JsonProperty("type")
    private ScheduledActionType type = ScheduledActionType.urgency_change;

    @JsonProperty("at")
    private ScheduledActionAt at;

    @JsonProperty(value = "to_urgency")
    private String toUrgency = "high";

    public void setAt(ScheduledActionAtType type, ScheduledActionName name){
        this.at = new ScheduledActionAt(type, name);
    }

    public ScheduledActionType getType() {
        return type;
    }

    public void setType(ScheduledActionType type) {
        this.type = type;
    }

    public ScheduledActionAt getAt() {
        return at;
    }

    public String getToUrgency() {
        return toUrgency;
    }

    public void setToUrgency(String toUrgency) {
        this.toUrgency = toUrgency;
    }
}
