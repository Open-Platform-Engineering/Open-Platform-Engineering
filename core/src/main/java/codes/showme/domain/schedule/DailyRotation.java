package codes.showme.domain.schedule;

import codes.showme.domain.notification.NotifiedUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.*;

import java.io.Serializable;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class DailyRotation implements RotationType, Serializable {

    private static final long serialVersionUID = -511678136937193938L;

    @JsonProperty("restrictions")
    private List<Restriction> restrictions = Lists.newArrayList();

    @JsonProperty("handoff_time")
    private LocalTime handoffTime;


    public DailyRotation addDailyRestriction(LocalTime from, LocalTime to) {
        restrictions.add(new DailyRestriction(from, to));
        return this;
    }

    public DailyRotation addWeeklyRestriction(WeeklyRestriction weeklyRestriction) {
        restrictions.add(weeklyRestriction);
        return this;
    }

    @JsonIgnore
    public boolean hasRestrictions() {
        return !restrictions.isEmpty();
    }

    @Override
    public TreeRangeMap<ZonedDateTime, NotifiedUser> getRangeTo(List<NotifiedUser> notifiedUsers, LocalDate startDate, ZonedDateTime endTime, ZoneId zoneId) {
        TreeRangeMap<ZonedDateTime, NotifiedUser> finalSchedule = TreeRangeMap.create();
        int userIndex = 0;
        for (Range<ZonedDateTime> fullRange : getFullRangesWithoutRestriction(startDate, endTime, zoneId)) {
            if (!hasRestrictions()) {
                finalSchedule.put(fullRange, notifiedUsers.get(userIndex % (notifiedUsers.size())));
                userIndex++;
            } else {
                RangeSet<ZonedDateTime> timeRangeSet = getRange(fullRange, zoneId);
                if (!timeRangeSet.isEmpty()) {
                    for (Range<ZonedDateTime> eachRange : timeRangeSet.asRanges()) {
                        finalSchedule.put(eachRange, notifiedUsers.get(userIndex % notifiedUsers.size()));
                    }
                    userIndex++;
                }
            }
        }
        return finalSchedule;
    }

    public List<Restriction> getRestrictions() {
        return restrictions;
    }


    @JsonIgnore
    public RangeSet<ZonedDateTime> getRange(Range<ZonedDateTime> aTimeRange, ZoneId zoneId) {
        RangeSet<ZonedDateTime> result = TreeRangeSet.create();
        for (Restriction restriction : restrictions) {
            result.addAll(restriction.restrict(aTimeRange,zoneId));
        }
        return result;
    }

    public LocalTime getHandoffTime() {
        return handoffTime;
    }

    @Override
    public List<Range<ZonedDateTime>> getFullRangesWithoutRestriction(LocalDate startDate, ZonedDateTime endTime, ZoneId zoneId) {
        List<Range<ZonedDateTime>> result = Lists.newArrayList();
        ZonedDateTime startTime = ZonedDateTime.of(startDate, getHandoffTime(), zoneId);
        int daysSpan = 1;
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

    public void setHandoffTime(int hour, int minute) {
        this.handoffTime = LocalTime.of(hour, minute, 0, 0);
    }


}
