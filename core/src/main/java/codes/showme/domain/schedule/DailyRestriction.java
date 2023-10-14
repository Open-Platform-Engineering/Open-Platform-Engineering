package codes.showme.domain.schedule;

import codes.showme.domain.notification.NotifiedUser;
import codes.showme.techlib.time.TimeRangeHelper;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeMap;
import com.google.common.collect.TreeRangeSet;

import java.io.Serializable;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

/**
 * An array of restrictions for the layer. A restriction is a limit on which period of the day or week the schedule layer can accept assignments.
 */
public class DailyRestriction implements Serializable, Restriction {

    private static final long serialVersionUID = -2143039074868842713L;

    @JsonProperty("from")
    private LocalTime from;

    @JsonProperty("to")
    private LocalTime to;

    public DailyRestriction() {
    }

    @Override
    public TreeRangeMap<ZonedDateTime, NotifiedUser> generateRangesFrom(Range<ZonedDateTime> aTimeRange, ZoneId zoneId, TimeRangeShift shift, List<NotifiedUser> notifiedUsers) {
        TreeRangeMap<ZonedDateTime, NotifiedUser> result = TreeRangeMap.create();
        LocalTime aLocalTime = getFrom();
        LocalTime bLocalTime = getTo();
        int userIndex = 0;
        for (ZonedDateTime indexStart = aTimeRange.lowerEndpoint(); indexStart.isBefore(aTimeRange.upperEndpoint()); indexStart = indexStart.plus(shift.getValue(), shift.getUnit())) {
            ZonedDateTime indexEnd = indexStart.plus(shift.getValue(), shift.getUnit());
            if (isIn24HoursAndInOneDay(indexStart, indexEnd)) {


            } else if (isIn24HoursAndInTwoDay(indexStart, indexEnd)) {

            } else if (isGreaterThan24Hours(indexStart, indexEnd)) {

            }
            userIndex++;
        }
        return result;
    }

    private boolean isRestrictionInOneDay() {
        return getTo().isBefore(getFrom());
    }

    private boolean isGreaterThan24Hours(ZonedDateTime indexStart, ZonedDateTime indexEnd) {
        return false;
    }

    private boolean isIn24HoursAndInTwoDay(ZonedDateTime indexStart, ZonedDateTime indexEnd) {
        return false;
    }

    private boolean isIn24HoursAndInOneDay(ZonedDateTime indexStart, ZonedDateTime indexEnd) {
        return Math.abs(indexEnd.toEpochSecond() - indexStart.toEpochSecond()) <= 24 * 60 * 60;
    }

    @Override
    public RangeSet<ZonedDateTime> restrict(Range<ZonedDateTime> aTimeRange, ZoneId zoneId) {
        if (TimeRangeHelper.isIn24HoursBasedSeconds(aTimeRange)) {
            RangeSet<ZonedDateTime> restrictionTimeRange = TimeRangeHelper.calculateADayRestrictionRange(aTimeRange, getFrom(), getTo());
            if (restrictionTimeRange.isEmpty()) {
                return TreeRangeSet.create();
            }
            RangeSet<ZonedDateTime> result = TreeRangeSet.create();
            for (Range<ZonedDateTime> range : restrictionTimeRange.asRanges()) {
                result.add(aTimeRange.intersection(range));
            }
            return result;
        } else {
            RangeSet<ZonedDateTime> result = TreeRangeSet.create();
            for (ZonedDateTime startIndex = aTimeRange.lowerEndpoint(); startIndex.isBefore(aTimeRange.upperEndpoint()); startIndex = startIndex.plus(1, ChronoUnit.DAYS)) {
                ZonedDateTime endIndex = startIndex.plus(1, ChronoUnit.DAYS);
                if (endIndex.isAfter(aTimeRange.upperEndpoint())) {
                    endIndex = aTimeRange.upperEndpoint();
                }
                RangeSet<ZonedDateTime> restrictionTimeRange = TimeRangeHelper.calculateADayRestrictionRange(Range.openClosed(startIndex, endIndex), getFrom(), getTo());
                for (Range<ZonedDateTime> range : restrictionTimeRange.asRanges()) {
                    result.add(range);
                }
            }
            return result;
        }


    }


    @Override
    public TreeRangeMap<ZonedDateTime, NotifiedUser> generateRangeUserMap(List<NotifiedUser> notifiedUsers, ZonedDateTime startTime, ZonedDateTime endTime, ZoneId zoneId) {
        ZonedDateTime from = getFrom().atDate(startTime.toLocalDate()).atZone(zoneId);
        ZonedDateTime to = getTo().atDate(startTime.toLocalDate()).atZone(zoneId);
        if (to.isBefore(from)) {
            to = to.plus(1, ChronoUnit.DAYS);
        }
        TreeRangeMap<ZonedDateTime, NotifiedUser> result = TreeRangeMap.create();
        int userIndex = 0;
        for (; to.isBefore(endTime); from = from.plus(1, ChronoUnit.DAYS), to = to.plus(1, ChronoUnit.DAYS)) {
            result.put(Range.openClosed(from, to), notifiedUsers.get(userIndex % notifiedUsers.size()));
            userIndex++;
        }
        return result;
    }

    public DailyRestriction(LocalTime from, LocalTime to) {
        this.from = from;
        this.to = to;
    }

    public LocalTime getFrom() {
        return from;
    }

    public void setFrom(LocalTime from) {
        this.from = from;
    }

    public LocalTime getTo() {
        return to;
    }

    public void setTo(LocalTime to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "DailyRestriction{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }


}
