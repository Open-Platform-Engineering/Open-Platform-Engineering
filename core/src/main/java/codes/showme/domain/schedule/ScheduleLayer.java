package codes.showme.domain.schedule;

import codes.showme.domain.notification.NotifiedUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.google.common.collect.TreeRangeMap;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduleLayer implements Serializable {

    private static final long serialVersionUID = -3311024415427511491L;

    @JsonProperty("notified_users")
    private List<NotifiedUser> notifiedUsers = Lists.newArrayList();

    @JsonProperty("rotation")
    private RotationType rotation;

    /**
     * The start time of this layer.
     */
    @JsonProperty("start_time")
    private LocalDate startDate;

    public ZonedDateTime getStartTime(ZoneId zoneId) {
        return ZonedDateTime.of(startDate, rotation.getHandoffTime(), zoneId);
    }

    @JsonIgnore
    public TreeRangeMap<ZonedDateTime, NotifiedUser> getSchedule(ZonedDateTime endTime, ZoneId zoneId) {
        return rotation.getRangeTo(notifiedUsers, startDate,endTime, zoneId );
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public RotationType getRotation() {
        return rotation;
    }

    public void setRotation(RotationType rotation) {
        this.rotation = rotation;
    }

    public List<NotifiedUser> getNotifiedUsers() {
        return notifiedUsers;
    }

    public void setNotifiedUsers(List<NotifiedUser> notifiedUsers) {
        this.notifiedUsers = notifiedUsers;
    }

}
