package codes.showme.techlib.time;

import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class TimeRangeHelper {

    public static boolean isCrossADay(Range<ZonedDateTime> aRange) {
        return aRange.upperEndpoint().toLocalDate().isAfter(aRange.lowerEndpoint().toLocalDate());
    }

    public static boolean isNotCrossADay(Range<ZonedDateTime> aRange) {
        return !isCrossADay(aRange);
    }

    public static boolean isIn24HoursBasedSeconds(Range<ZonedDateTime> aRange) {
        long millisecond = aRange.upperEndpoint().toInstant().toEpochMilli() - aRange.lowerEndpoint().toInstant().toEpochMilli();
        return millisecond <= 24 * 60 * 60 * 1000 && millisecond > 0;
    }

    public static boolean isIn1Week(Range<ZonedDateTime> aRange) {
        long millisecond = aRange.upperEndpoint().toInstant().toEpochMilli() - aRange.lowerEndpoint().toInstant().toEpochMilli();
        return millisecond <= 7 * 24 * 60 * 60 * 1000 && millisecond > 0;
    }

    public static boolean isNotIn24HoursBasedSeconds(Range<ZonedDateTime> aRange) {
        return !isIn24HoursBasedSeconds(aRange);
    }

    public static List<Range<ZonedDateTime>> splitARangeToDayByDayRanges(Range<ZonedDateTime> aRange) {
        List<Range<ZonedDateTime>> result = Lists.newArrayList();
        if (isIn24HoursBasedSeconds(aRange)) {
            result.add(aRange);
            return result;
        }
        ZonedDateTime from = aRange.lowerEndpoint();
        ZonedDateTime to = aRange.upperEndpoint();
        int daysSpan = 1;
        for (ZonedDateTime indexPoint = from; indexPoint.isBefore(to); indexPoint = indexPoint.plus(daysSpan, ChronoUnit.DAYS)) {
            ZonedDateTime aPoint = indexPoint;
            ZonedDateTime bPoint = aPoint.plus(daysSpan, ChronoUnit.DAYS);
            if (bPoint.isAfter(to)) {
                bPoint = to;
            }
            result.add(Range.openClosed(aPoint, bPoint));
        }
        return result;
    }

    public static List<Range<ZonedDateTime>> splitARangeToWeeklyRanges(Range<ZonedDateTime> aRange) {
        List<Range<ZonedDateTime>> result = Lists.newArrayList();
        ZonedDateTime from = aRange.lowerEndpoint();
        ZonedDateTime to = aRange.upperEndpoint();
        int daysSpan = 7;
        for (ZonedDateTime indexPoint = from; indexPoint.isBefore(to); indexPoint = indexPoint.plus(daysSpan, ChronoUnit.DAYS)) {
            ZonedDateTime aPoint = indexPoint;
            ZonedDateTime bPoint = aPoint.plus(daysSpan, ChronoUnit.DAYS);
            if (bPoint.isAfter(to)) {
                bPoint = to;
            }
            result.add(Range.openClosed(aPoint, bPoint));
        }
        return result;
    }

    public static List<Range<ZonedDateTime>> splitARangeToWeekByWeekRanges(Range<ZonedDateTime> aRange) {
        List<Range<ZonedDateTime>> result = Lists.newArrayList();
        if (isIn24HoursBasedSeconds(aRange)) {
            result.add(aRange);
            return result;
        }
        ZonedDateTime from = aRange.lowerEndpoint();
        ZonedDateTime to = aRange.upperEndpoint();
        int daysSpan = 7;
        for (ZonedDateTime indexPoint = from; indexPoint.isBefore(to); indexPoint = indexPoint.plus(daysSpan, ChronoUnit.DAYS)) {
            ZonedDateTime aPoint = indexPoint;
            ZonedDateTime bPoint = aPoint.plus(daysSpan, ChronoUnit.DAYS);
            if (bPoint.isAfter(to)) {
                bPoint = to;
            }
            result.add(Range.openClosed(aPoint, bPoint));
        }
        return result;
    }


    public static RangeSet<ZonedDateTime> calculateADayRestrictionRange(Range<ZonedDateTime> aTimeRange, LocalTime from, LocalTime to) {
        if (isNotIn24HoursBasedSeconds(aTimeRange)) {
            throw new UnsupportedOperationException("the timerange must be within 24 hours, current:" + aTimeRange);
        }


        List<Range<ZonedDateTime>> originalRanges = Lists.newArrayList();
        List<Range<ZonedDateTime>> restrictionRanges = Lists.newArrayList();
        ZonedDateTime lowerEndpoint = aTimeRange.lowerEndpoint();
        ZonedDateTime upperEndpoint = aTimeRange.upperEndpoint();
        if (isCrossADay(aTimeRange)) {
            originalRanges.add(Range.openClosed(lowerEndpoint, lowerEndpoint.truncatedTo(ChronoUnit.DAYS).plus(1, ChronoUnit.DAYS)));
            originalRanges.add(Range.openClosed(lowerEndpoint.truncatedTo(ChronoUnit.DAYS).plus(1, ChronoUnit.DAYS), upperEndpoint));
        } else {
            originalRanges.add(aTimeRange);
        }

        if (to.isBefore(from)) {
            // lowerEndpoint
            restrictionRanges.add(Range.openClosed(lowerEndpoint.truncatedTo(ChronoUnit.DAYS), to.atDate(lowerEndpoint.toLocalDate()).atZone(lowerEndpoint.getZone())));
            restrictionRanges.add(Range.openClosed(from.atDate(lowerEndpoint.toLocalDate()).atZone(lowerEndpoint.getZone()), lowerEndpoint.truncatedTo(ChronoUnit.DAYS).plus(1, ChronoUnit.DAYS)));
            // upperEndpoint
            restrictionRanges.add(Range.openClosed(upperEndpoint.truncatedTo(ChronoUnit.DAYS), from.atDate(upperEndpoint.toLocalDate()).atZone(upperEndpoint.getZone())));
            restrictionRanges.add(Range.openClosed(to.atDate(upperEndpoint.toLocalDate()).atZone(upperEndpoint.getZone()), upperEndpoint.truncatedTo(ChronoUnit.DAYS).plus(1, ChronoUnit.DAYS)));
        } else {
            // lowerEndpoint
            restrictionRanges.add(Range.openClosed(from.atDate(lowerEndpoint.toLocalDate()).atZone(lowerEndpoint.getZone()), to.atDate(lowerEndpoint.toLocalDate()).atZone(lowerEndpoint.getZone())));
            restrictionRanges.add(Range.openClosed(from.atDate(upperEndpoint.toLocalDate()).atZone(upperEndpoint.getZone()), to.atDate(upperEndpoint.toLocalDate()).atZone(upperEndpoint.getZone())));
        }

        RangeSet<ZonedDateTime> result = TreeRangeSet.create();

        for (Range<ZonedDateTime> originalRange : originalRanges) {
            for (Range<ZonedDateTime> restrictionRange : restrictionRanges) {
                try {
                    Range<ZonedDateTime> intersection = originalRange.intersection(restrictionRange);
                    result.add(intersection);
                }catch (IllegalArgumentException e){
                    // ignore intersection is undefined for disconnected ranges
                }

            }
        }

        return result;


    }
}
