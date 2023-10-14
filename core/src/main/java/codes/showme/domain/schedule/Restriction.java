package codes.showme.domain.schedule;

import codes.showme.domain.notification.NotifiedUser;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeMap;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = WeeklyRestriction.class, name = "weekly"),
        @JsonSubTypes.Type(value = DailyRestriction.class, name = "daily")})
public interface Restriction {

    RangeSet<ZonedDateTime> restrict(Range<ZonedDateTime> aTimeRange, ZoneId zoneId);

    TreeRangeMap<ZonedDateTime, NotifiedUser> generateRangesFrom(Range<ZonedDateTime> aTimeRange, ZoneId zoneId, TimeRangeShift shift, List<NotifiedUser> notifiedUsers);

    TreeRangeMap<ZonedDateTime, NotifiedUser> generateRangeUserMap(List<NotifiedUser> notifiedUsers, ZonedDateTime startTime, ZonedDateTime endTime, ZoneId zoneId);
}
