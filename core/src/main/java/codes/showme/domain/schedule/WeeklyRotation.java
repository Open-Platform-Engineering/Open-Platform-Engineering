package codes.showme.domain.schedule;

import codes.showme.domain.notification.NotifiedUser;
import codes.showme.techlib.time.TimeRangeHelper;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.*;

import java.io.Serializable;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeeklyRotation implements RotationType, Serializable {

    private static final long serialVersionUID = 2069580708390457616L;

    @JsonProperty("restrictions")
    private List<Restriction> restrictions = Lists.newArrayList();

    @JsonProperty("handoff_day_of_week")
    private DayOfWeek handoffDayOfWeek;

    @JsonProperty("handoff_time")
    private LocalTime handoffTime;

    @Override
    public boolean hasRestrictions() {
        return restrictions != null && !restrictions.isEmpty();
    }

    public TreeRangeMap<ZonedDateTime, NotifiedUser> getRangeTo(List<NotifiedUser> notifiedUsers, LocalDate startDate, ZonedDateTime endTime, ZoneId zoneId) {
        TreeRangeMap<ZonedDateTime, NotifiedUser> finalSchedule = TreeRangeMap.create();
        int userIndex = 0;
        ZonedDateTime start = ZonedDateTime.of(startDate, getHandoffTime(), zoneId);

        for (Range<ZonedDateTime> fullRange : TimeRangeHelper.splitARangeToWeekByWeekRanges(Range.openClosed(start, endTime))) {
            NotifiedUser user = notifiedUsers.get(userIndex % notifiedUsers.size());
            if (!hasRestrictions()) {
                finalSchedule.put(fullRange, user);
                userIndex++;
            } else {
                RangeSet<ZonedDateTime> timeRangeSet = getRange(fullRange, zoneId);
                for (Range<ZonedDateTime> eachRange : timeRangeSet.asRanges()) {
                    finalSchedule.put(eachRange, user);
                }
                boolean existsTimeRange = !timeRangeSet.isEmpty();
                if (existsTimeRange) {
                    userIndex++;
                }
            }
        }

        return finalSchedule;
    }


    public RangeSet<ZonedDateTime> getRange(Range<ZonedDateTime> aTimeRange, ZoneId zoneId) {
        RangeSet<ZonedDateTime> result = TreeRangeSet.create();
        for (Restriction restrictionType : restrictions) {
            result.addAll(restrictionType.restrict(aTimeRange, zoneId));
        }
        return result;
    }

    @Override
    public LocalTime getHandoffTime() {
        return handoffTime;
    }

    @Override
    public List<Range<ZonedDateTime>> getFullRangesWithoutRestriction(LocalDate startDate, ZonedDateTime endTime, ZoneId zoneId) {
        ZonedDateTime startTime = ZonedDateTime.of(startDate, getHandoffTime(), zoneId);
        int daysSpan = 7;
        List<Range<ZonedDateTime>> result = Lists.newArrayList();
        for (ZonedDateTime indexPoint = startTime; indexPoint.isBefore(endTime); indexPoint = indexPoint.plus(daysSpan, ChronoUnit.DAYS)) {
            ZonedDateTime aPoint = indexPoint;
            ZonedDateTime bPoint = aPoint.plus(daysSpan, ChronoUnit.DAYS);
            if (bPoint.isAfter(endTime)) {
                bPoint = endTime;
            }
            result.add(Range.openClosed(aPoint, bPoint));
        }
        return result;
    }


    public List<Restriction> getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(List<Restriction> restrictions) {
        this.restrictions = restrictions;
    }

    public DayOfWeek getHandoffDayOfWeek() {
        return handoffDayOfWeek;
    }

    public void setHandoffDayOfWeek(DayOfWeek handoffDayOfWeek) {
        this.handoffDayOfWeek = handoffDayOfWeek;
    }

    public void setHandoffTime(LocalTime handoffTime) {
        this.handoffTime = handoffTime;
    }
}
