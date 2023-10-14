package codes.showme.domain.platform.scheduledaction;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents when scheduled action will occur.
 */
public class ScheduledActionAt {
    /**
     * The type of schedule action. Must be set to urgency_change.
     */
    @JsonProperty("type")
    private ScheduledActionAtType type;

    @JsonProperty("name")
    private ScheduledActionName name;

    public ScheduledActionAt(ScheduledActionAtType type, ScheduledActionName name) {
        this.name = name;
        this.type = type;
    }

    public ScheduledActionAtType getType() {
        return type;
    }

    public void setType(ScheduledActionAtType type) {
        this.type = type;
    }

    public ScheduledActionName getName() {
        return name;
    }

    public void setName(ScheduledActionName name) {
        this.name = name;
    }
}
