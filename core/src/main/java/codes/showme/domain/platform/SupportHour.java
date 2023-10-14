package codes.showme.domain.platform;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Set;


@JsonIgnoreProperties(ignoreUnknown = true)
public class SupportHour {
    /**
     * The time zone for the support hours
     */
    @JsonProperty(value = "time_zone")
    private String timeZone;

    @JsonProperty(value = "type")
    private SupportHourType type;

    /**
     * The support hours' starting time of day (date portion is ignored)
     */
    @JsonProperty(value = "start_time")
    private Date startTime;

    /**
     * The support hours' ending time of day (date portion is ignored)
     */
    @JsonProperty(value = "end_time")
    private Date endTime;

    @JsonProperty(value = "days_of_week")
    private Set<Integer> daysOfWeek;

    public SupportHourType getType() {
        return type;
    }

    public void setType(SupportHourType type) {
        this.type = type;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Set<Integer> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(Set<Integer> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}

