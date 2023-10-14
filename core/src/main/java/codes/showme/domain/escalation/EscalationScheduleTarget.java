package codes.showme.domain.escalation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EscalationScheduleTarget implements EscalationTarget, Serializable {

    private static final long serialVersionUID = 7355906446826118962L;
    @JsonProperty("schedule_id")
    private long scheduleId;

    @JsonProperty("display_name")
    private String displayName;

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public void setSchedule(long scheduleId, String displayName) {
        this.scheduleId = scheduleId;
        this.displayName = displayName;
    }

    public long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
