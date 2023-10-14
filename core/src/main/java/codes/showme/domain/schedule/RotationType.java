package codes.showme.domain.schedule;

import codes.showme.domain.notification.NotifiedUser;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeMap;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = WeeklyRotation.class, name = "weekly"),
        @JsonSubTypes.Type(value = CustomRotation.class, name = "custom"),
        @JsonSubTypes.Type(value = DailyRotation.class, name = "daily")})
public interface RotationType {

    boolean hasRestrictions();

    TreeRangeMap<ZonedDateTime, NotifiedUser> getRangeTo(List<NotifiedUser> notifiedUsers, LocalDate startDate, ZonedDateTime endTime, ZoneId zoneId);


    LocalTime getHandoffTime();

    List<Range<ZonedDateTime>> getFullRangesWithoutRestriction(LocalDate startDate, ZonedDateTime endTime, ZoneId zoneId);

}
