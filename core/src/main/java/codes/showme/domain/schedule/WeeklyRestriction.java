package codes.showme.domain.schedule;

import codes.showme.domain.notification.NotifiedUser;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class WeeklyRestriction implements Restriction {

    @JsonProperty("week_from")
    private DayOfWeek weekFrom;

    @JsonProperty("time_from")
    private LocalTime timeFrom;

    @JsonProperty("week_to")
    private DayOfWeek weekTo;

    @JsonProperty("time_to")
    private LocalTime timeTo;


    public WeeklyRestriction() {
    }

    public WeeklyRestriction from(DayOfWeek weekFrom, LocalTime timeFrom) {
        this.weekFrom = weekFrom;
        this.timeFrom = timeFrom;
        return this;
    }

    public WeeklyRestriction to(DayOfWeek weekTo, LocalTime timeTo) {
        this.weekTo = weekTo;
        this.timeTo = timeTo;
        return this;
    }

    @Override
    public RangeSet<ZonedDateTime> restrict(Range<ZonedDateTime> aTimeRange, ZoneId zoneId) {
        RangeSet<ZonedDateTime> result = TreeRangeSet.create();

        List<Range<ZonedDateTime>> originals = Lists.newArrayList();
        List<Range<ZonedDateTime>> restrictions = Lists.newArrayList();
        for (ZonedDateTime startIndex = aTimeRange.lowerEndpoint(); startIndex.isBefore(aTimeRange.upperEndpoint()); startIndex = startIndex.plus(1, ChronoUnit.WEEKS)) {
            ZonedDateTime endIndex = startIndex.plus(1, ChronoUnit.WEEKS);
            if (endIndex.isAfter(aTimeRange.upperEndpoint())) {
                endIndex = aTimeRange.upperEndpoint();
            }
            originals.add(Range.openClosed(startIndex, endIndex));
        }


        if (getWeekFrom().getValue() > getWeekTo().getValue()) {

            ZonedDateTime start1 = aTimeRange.lowerEndpoint().minus(aTimeRange.lowerEndpoint().getDayOfWeek().getValue(), ChronoUnit.DAYS)
                    .plus(getWeekFrom().getValue(), ChronoUnit.DAYS)
                    .withHour(getTimeFrom().getHour()).withMinute(getTimeFrom().getMinute()).withSecond(getTimeFrom().getSecond());
            ZonedDateTime end1 = start1.plusDays(7 - start1.getDayOfWeek().getValue()).truncatedTo(ChronoUnit.HOURS);
                    aTimeRange.lowerEndpoint().minus(aTimeRange.lowerEndpoint().getDayOfWeek().getValue(), ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS).plus(7, ChronoUnit.DAYS);
            ZonedDateTime start2 = end1;
            ZonedDateTime end2 = end1.plus(getWeekTo().getValue(), ChronoUnit.DAYS).withHour(getTimeTo().getHour()).withMinute(getTimeTo().getMinute()).withSecond(getTimeTo().getSecond());
            for (; end2.isBefore(aTimeRange.upperEndpoint().plus(7, ChronoUnit.DAYS)); ) {
                restrictions.add(Range.openClosed(start1, end1));
                restrictions.add(Range.openClosed(start2, end2));
                start1 = start1.plus(1, ChronoUnit.WEEKS);
                end1 = end1.plus(1, ChronoUnit.WEEKS);
                start2 = start2.plus(1, ChronoUnit.WEEKS);
                end2 = end2.plus(1, ChronoUnit.WEEKS);
            }
        } else {
            ZonedDateTime startWeekTime = aTimeRange.lowerEndpoint().minus(aTimeRange.lowerEndpoint().getDayOfWeek().getValue(), ChronoUnit.DAYS);
            ZonedDateTime start = startWeekTime.plus(getWeekFrom().getValue(), ChronoUnit.DAYS).withHour(getTimeFrom().getHour()).withMinute(getTimeFrom().getMinute()).withSecond(getTimeFrom().getSecond());
            ZonedDateTime end = startWeekTime.plus(getWeekTo().getValue(), ChronoUnit.DAYS).withHour(getTimeTo().getHour()).withMinute(getTimeTo().getMinute()).withSecond(getTimeTo().getSecond());
            for (; start.isBefore(aTimeRange.upperEndpoint().plus(7 - aTimeRange.upperEndpoint().getDayOfWeek().getValue(), ChronoUnit.DAYS)); ) {
                restrictions.add(Range.openClosed(start, end));
                start = start.plus(1, ChronoUnit.WEEKS);
                end = end.plus(1, ChronoUnit.WEEKS);
            }
        }


        for (Range<ZonedDateTime> original : originals) {
            for (Range<ZonedDateTime> restriction : restrictions) {
                try {
                    result.add(restriction.intersection(original));
                } catch (IllegalArgumentException e) {
                    // ignore
                }
            }
        }

        return result;

    }

    @Override
    public TreeRangeMap<ZonedDateTime, NotifiedUser> generateRangesFrom(Range<ZonedDateTime> aTimeRange, ZoneId zoneId, TimeRangeShift shift, List<NotifiedUser> notifiedUsers) {
        return null;
    }

    @Override
    public TreeRangeMap<ZonedDateTime, NotifiedUser> generateRangeUserMap(List<NotifiedUser> notifiedUsers, ZonedDateTime startTime, ZonedDateTime endTime, ZoneId zoneId) {

        ZonedDateTime actualStartTime = startTime.plus(getWeekFrom().getValue() - startTime.getDayOfWeek().getValue(), ChronoUnit.DAYS).withHour(getTimeFrom().getHour()).withMinute(getTimeFrom().getMinute());
        ZonedDateTime actualEndTime = actualStartTime.plus(Math.abs(getWeekTo().getValue() - getWeekFrom().getValue()), ChronoUnit.DAYS).withHour(getTimeTo().getHour()).withMinute(getTimeTo().getMinute());

        int userIndex = 0;
        TreeRangeMap<ZonedDateTime, NotifiedUser> result = TreeRangeMap.create();
        for (; actualEndTime.isBefore(endTime); actualStartTime = actualStartTime.plus(1, ChronoUnit.WEEKS),
                actualEndTime = actualEndTime.plus(1, ChronoUnit.WEEKS)) {
            result.put(Range.openClosed(actualStartTime, actualEndTime), notifiedUsers.get(userIndex % notifiedUsers.size()));
            userIndex++;
        }

        return result;
    }


//    public RangeSet<ZonedDateTime> buildRange(Range<ZonedDateTime> aDayTimeRange, ZoneId zoneId) {
//
//        ZonedDateTime lowerEndpoint = aDayTimeRange.lowerEndpoint();
//        ZonedDateTime upperEndpoint = aDayTimeRange.upperEndpoint();
//
//        int lowerEndpointDayOfWeek = lowerEndpoint.getDayOfWeek().getValue();
//        int upperEndpointDayOfWeek = upperEndpoint.getDayOfWeek().getValue();
//
//        LocalTime lowerEndpointLocalTime = lowerEndpoint.toLocalTime();
//        LocalTime upperEndpointLocalTime = upperEndpoint.toLocalTime();
//
//        // 入参星期之间的对比维度
//        // case 1
//        if (upperEndpointDayOfWeek > lowerEndpointDayOfWeek
//                && getWeekFrom().getValue() < lowerEndpointDayOfWeek
//                && getWeekTo().getValue() < lowerEndpointDayOfWeek
//        ) {
//            return TreeRangeSet.create();
//        }
//
//        // case 2
//        if (upperEndpointDayOfWeek > lowerEndpointDayOfWeek
//                && getWeekFrom().getValue() == lowerEndpointDayOfWeek
//                && getWeekTo().getValue() == lowerEndpointDayOfWeek
//        ) {
//            TreeRangeSet<ZonedDateTime> result = TreeRangeSet.create();
//
//            if (getTimeFrom().isAfter(lowerEndpointLocalTime) && getTimeTo().isAfter(lowerEndpointLocalTime)) {
//
//                result.add(Range.openClosed(lowerEndpoint.withHour(getTimeFrom().getHour()).withMinute(getTimeFrom().getMinute()),
//                        lowerEndpoint.withHour(getTimeTo().getHour()).withMinute(getTimeTo().getMinute())
//                ));
//            } else if (getTimeFrom().isBefore(lowerEndpointLocalTime) && getTimeTo().isAfter(lowerEndpointLocalTime)) {
//                result.add(Range.openClosed(lowerEndpoint,
//                        lowerEndpoint.withHour(getTimeTo().getHour()).withMinute(getTimeTo().getMinute())
//                ));
//            }
//            return result;
//        }
//
//        // case 3
//        if (upperEndpointDayOfWeek > lowerEndpointDayOfWeek
//                && getWeekFrom().getValue() == lowerEndpointDayOfWeek
//                && getWeekTo().getValue() == upperEndpointDayOfWeek
//        ) {
//            TreeRangeSet<ZonedDateTime> result = TreeRangeSet.create();
//            ZonedDateTime aPoint = null;
//            ZonedDateTime bPoint = null;
//            if (getTimeFrom().isAfter(lowerEndpointLocalTime) || getTimeFrom().equals(lowerEndpointLocalTime)) {
//                aPoint = lowerEndpoint.withHour(getTimeFrom().getHour()).withMinute(getTimeFrom().getMinute());
//            } else {
//                aPoint = lowerEndpoint;
//            }
//
//            if (getTimeTo().isAfter(upperEndpointLocalTime) || getTimeTo().equals(upperEndpointLocalTime)) {
//                bPoint = upperEndpoint;
//            } else {
//                bPoint = upperEndpoint.withHour(getTimeTo().getHour()).withMinute(getTimeFrom().getMinute());
//            }
//            result.add(Range.openClosed(aPoint, bPoint));
//            return result;
//        }
//
//        // case 4
//        if (upperEndpointDayOfWeek > lowerEndpointDayOfWeek
//                && getWeekFrom().getValue() == lowerEndpointDayOfWeek
//                && getWeekTo().getValue() > upperEndpointDayOfWeek
//        ) {
//            TreeRangeSet<ZonedDateTime> result = TreeRangeSet.create();
//            ZonedDateTime aPoint = null;
//            ZonedDateTime bPoint = null;
//            if (getTimeFrom().isAfter(lowerEndpointLocalTime)) {
//                aPoint = lowerEndpoint.withHour(getTimeFrom().getHour()).withMinute(getTimeFrom().getMinute());
//            } else {
//                aPoint = lowerEndpoint;
//            }
//            bPoint = upperEndpoint;
//            result.add(Range.openClosed(aPoint, bPoint));
//            return result;
//
//        }
//
//        // case 5
//        if (upperEndpointDayOfWeek > lowerEndpointDayOfWeek
//                && getWeekTo().getValue() == lowerEndpointDayOfWeek
//                && getWeekFrom().getValue() < lowerEndpointDayOfWeek
//        ) {
//            TreeRangeSet<ZonedDateTime> result = TreeRangeSet.create();
//            if (getTimeTo().isBefore(lowerEndpointLocalTime)) {
//                return result;
//            }
//
//            result.add(Range.openClosed(lowerEndpoint, lowerEndpoint.withHour(getTimeTo().getHour()).withMinute(getTimeTo().getMinute())));
//            return result;
//        }
//
//        // case 6
//        if (upperEndpointDayOfWeek < lowerEndpointDayOfWeek
//                && getWeekTo().getValue() == lowerEndpointDayOfWeek
//                && getWeekFrom().getValue() < lowerEndpointDayOfWeek
//        ) {
//            TreeRangeSet<ZonedDateTime> result = TreeRangeSet.create();
//            if (getTimeTo().isBefore(lowerEndpointLocalTime)) {
//                return result;
//            }
//            result.add(Range.openClosed(lowerEndpoint, lowerEndpoint.withHour(getTimeTo().getHour()).withMinute(getTimeTo().getMinute())));
//            return result;
//        }
//
//        // case 7
//        if (upperEndpointDayOfWeek < lowerEndpointDayOfWeek
//                && getWeekTo().getValue() < lowerEndpointDayOfWeek
//                && getWeekFrom().getValue() < lowerEndpointDayOfWeek
//        ) {
//            return TreeRangeSet.create();
//        }
//
//        // case 8
//        if (upperEndpointDayOfWeek < lowerEndpointDayOfWeek
//                && getWeekFrom().getValue() == lowerEndpointDayOfWeek
//                && getWeekTo().getValue() == lowerEndpointDayOfWeek
//        ) {
//
//            TreeRangeSet<ZonedDateTime> result = TreeRangeSet.create();
//            if (getTimeFrom().isAfter(lowerEndpointLocalTime) &&
//                    getTimeTo().isAfter(lowerEndpointLocalTime)) {
//                result.add(Range.openClosed(lowerEndpoint.withHour(getTimeFrom().getHour()).withMinute(getTimeFrom().getMinute()),
//                        lowerEndpoint.withHour(getTimeFrom().getHour()).withMinute(getTimeTo().getMinute())));
//                return result;
//            }
//
//            if (getTimeFrom().isBefore(lowerEndpointLocalTime) &&
//                    getTimeTo().isAfter(lowerEndpointLocalTime)) {
//                result.add(Range.openClosed(
//                        lowerEndpoint, lowerEndpoint.withHour(getTimeTo().getHour()).withMinute(getTimeTo().getMinute())
//                ));
//                return result;
//            }
//
//            if (getTimeFrom().isBefore(lowerEndpointLocalTime) &&
//                    getTimeTo().isBefore(lowerEndpointLocalTime)) {
//                return result;
//            }
//
//        }
//
//        //case 9
//        if (upperEndpointDayOfWeek < lowerEndpointDayOfWeek
//                && getWeekFrom().getValue() == upperEndpointDayOfWeek
//                && getWeekTo().getValue() == upperEndpointDayOfWeek
//        ) {
//            TreeRangeSet<ZonedDateTime> result = TreeRangeSet.create();
//            if (getTimeFrom().isBefore(upperEndpointLocalTime) &&
//                    getTimeTo().isBefore(upperEndpointLocalTime)
//            ) {
//                result.add(Range.openClosed(
//                        upperEndpoint.withHour(getTimeFrom().getHour()).withMinute(getTimeFrom().getMinute()),
//                        upperEndpoint.withHour(getTimeFrom().getHour()).withMinute(getTimeTo().getMinute())
//                ));
//                return result;
//            }
//
//            if (getTimeFrom().isBefore(upperEndpointLocalTime) &&
//                    getTimeTo().isAfter(upperEndpointLocalTime)) {
//                result.add(Range.openClosed(
//                        upperEndpoint.withHour(getTimeFrom().getHour()).withMinute(getTimeFrom().getMinute()),
//                        upperEndpoint
//                ));
//                return result;
//            }
//
//            if (getTimeFrom().isAfter(upperEndpointLocalTime) &&
//                    getTimeTo().isAfter(upperEndpointLocalTime)) {
//                return result;
//            }
//
//        }
//
//        // case 10
//        if (upperEndpointDayOfWeek < lowerEndpointDayOfWeek
//                && getWeekFrom().getValue() == upperEndpointDayOfWeek
//                && getWeekTo().getValue() > upperEndpointDayOfWeek
//                && getWeekTo().getValue() < lowerEndpointDayOfWeek
//        ) {
//
//            TreeRangeSet<ZonedDateTime> result = TreeRangeSet.create();
//            if (getTimeFrom().isBefore(upperEndpointLocalTime)) {
//                result.add(Range.openClosed(upperEndpoint.withHour(getTimeFrom().getHour()).withMinute(getTimeFrom().getMinute()),
//                        upperEndpoint)
//                );
//                return result;
//            }
//            if (getTimeFrom().isAfter(upperEndpointLocalTime)) {
//                return result;
//            }
//        }
//
//        // case 11
//        if (upperEndpointDayOfWeek < lowerEndpointDayOfWeek
//                && getWeekFrom().getValue() > upperEndpointDayOfWeek
//                && getWeekTo().getValue() > upperEndpointDayOfWeek
//                && getWeekTo().getValue() < lowerEndpointDayOfWeek
//        ) {
//
//            return TreeRangeSet.create();
//
//        }
//
//        // case 12
//        if (upperEndpointDayOfWeek < lowerEndpointDayOfWeek
//                && getWeekFrom().getValue() > upperEndpointDayOfWeek
//                && getWeekTo().getValue() == lowerEndpointDayOfWeek
//        ) {
//            TreeRangeSet<ZonedDateTime> result = TreeRangeSet.create();
//
//            if (getTimeTo().isAfter(lowerEndpointLocalTime)) {
//                result.add(Range.openClosed(
//                        lowerEndpoint,
//                        lowerEndpoint.withHour(getTimeTo().getHour()).withMinute(getTimeTo().getMinute())
//                ));
//                return result;
//            }
//
//            if (getTimeTo().isBefore(lowerEndpointLocalTime)) {
//                return result;
//            }
//
//
//            return result;
//        }
//
//        // case 13
//        if (upperEndpointDayOfWeek < lowerEndpointDayOfWeek
//                && getWeekFrom().getValue() == upperEndpointDayOfWeek
//                && getWeekTo().getValue() == lowerEndpointDayOfWeek
//        ) {
//            TreeRangeSet<ZonedDateTime> result = TreeRangeSet.create();
//            if (getTimeTo().isAfter(lowerEndpointLocalTime)) {
//                result.add(Range.openClosed(
//                        lowerEndpoint,
//                        lowerEndpoint.withHour(getTimeTo().getHour()).withMinute(getTimeTo().getMinute())));
//            }
//
//            if (getTimeFrom().isBefore(upperEndpointLocalTime)) {
//                result.add(Range.openClosed(
//                        upperEndpoint.withHour(getTimeFrom().getHour()).withMinute(getTimeFrom().getMinute()),
//                        upperEndpoint
//
//                ));
//            }
//            return result;
//        }
//
//        // case 14
//        if (upperEndpointDayOfWeek > lowerEndpointDayOfWeek
//                && getWeekFrom().getValue() < lowerEndpointDayOfWeek
//                && getWeekTo().getValue() == upperEndpointDayOfWeek
//
//        ) {
//            TreeRangeSet<ZonedDateTime> result = TreeRangeSet.create();
//
//            if (getTimeTo().isBefore(upperEndpointLocalTime)) {
//                result.add(Range.openClosed(lowerEndpoint, upperEndpoint.withHour(getTimeTo().getHour()).withMinute(getTimeTo().getMinute())));
//            }
//
//            return result;
//        }
//
//        // case 15
//        if (upperEndpointDayOfWeek > lowerEndpointDayOfWeek
//                && getWeekFrom().getValue() < lowerEndpointDayOfWeek
//                && getWeekTo().getValue() > upperEndpointDayOfWeek
//        ) {
//            TreeRangeSet<ZonedDateTime> result = TreeRangeSet.create();
//            result.add(aDayTimeRange);
//            return result;
//        }
//
//        // case 16
//        if (upperEndpointDayOfWeek == lowerEndpointDayOfWeek
//                && getWeekFrom().getValue() == lowerEndpointDayOfWeek
//                && getWeekTo().getValue() == lowerEndpointDayOfWeek
//        ) {
//            TreeRangeSet<ZonedDateTime> result = TreeRangeSet.create();
//            ZonedDateTime aPoint = null;
//            ZonedDateTime bPoint = null;
//            if (getTimeFrom().isAfter(lowerEndpointLocalTime)) {
//                aPoint = lowerEndpoint.withHour(getTimeFrom().getHour()).withMinute(getTimeFrom().getMinute());
//            } else {
//                aPoint = lowerEndpoint;
//            }
//            if (getTimeTo().isAfter(upperEndpointLocalTime)) {
//                bPoint = upperEndpoint.withHour(getTimeFrom().getHour()).withMinute(getTimeFrom().getMinute());
//            } else {
//                bPoint = upperEndpoint;
//            }
//            result.add(Range.openClosed(aPoint, bPoint));
//            return result;
//        }
//        // case 17
//        if (upperEndpointDayOfWeek == lowerEndpointDayOfWeek
//                && getWeekFrom().getValue() < lowerEndpointDayOfWeek
//                && getWeekTo().getValue() < lowerEndpointDayOfWeek
//        ) {
//            return TreeRangeSet.create();
//        }
//
//        // case 18
//        if (upperEndpointDayOfWeek == lowerEndpointDayOfWeek
//                && getWeekFrom().getValue() == lowerEndpointDayOfWeek
//                && getWeekTo().getValue() > upperEndpointDayOfWeek
//        ) {
//            TreeRangeSet<ZonedDateTime> result = TreeRangeSet.create();
//            ZonedDateTime aPoint = null;
//            if (getTimeFrom().isAfter(lowerEndpointLocalTime)) {
//                aPoint = lowerEndpoint.withHour(getTimeFrom().getHour()).withMinute(getTimeFrom().getMinute());
//            } else {
//                aPoint = lowerEndpoint;
//            }
//
//            result.add(Range.openClosed(aPoint, upperEndpoint));
//            return result;
//        }
//
//        // case 19
//        // from:FRIDAY,09:00; to: SUNDAY,23:59;
//        // range(2023-07-10T08:00+08:00[Asia/Shanghai]..2023-07-10T20:00+08:00[Asia/Shanghai]],lower week:MONDAY,upper week MONDAY
//        if (upperEndpointDayOfWeek == lowerEndpointDayOfWeek
//                && getWeekFrom().getValue() < getWeekTo().getValue()
//                && getWeekTo().getValue() > upperEndpointDayOfWeek
//                && getWeekFrom().getValue() > upperEndpointDayOfWeek
//        ) {
//            return TreeRangeSet.create();
//        }
//
//        // case 20
//        // from:FRIDAY,09:00; to: SUNDAY,23:59;
//        // range(2023-07-10T20:00+08:00[Asia/Shanghai]..2023-07-11T08:00+08:00[Asia/Shanghai]],lower week:MONDAY,upper week TUESDAY
//        if (upperEndpointDayOfWeek > lowerEndpointDayOfWeek
//                && getWeekFrom().getValue() < getWeekTo().getValue()
//                && getWeekFrom().getValue() > upperEndpointDayOfWeek
//                && getWeekTo().getValue() > upperEndpointDayOfWeek
//        ) {
//            return TreeRangeSet.create();
//        }
//
//        // case 21
//        // from:FRIDAY,09:00; to: SUNDAY,23:59;
//        // range(2023-07-13T20:00+08:00[Asia/Shanghai]..2023-07-14T08:00+08:00[Asia/Shanghai]],lower week:THURSDAY,upper week FRIDAY
//        if (upperEndpointDayOfWeek > lowerEndpointDayOfWeek
//                && getWeekFrom().getValue() < getWeekTo().getValue()
//                && getWeekFrom().getValue() == upperEndpointDayOfWeek
//                && getWeekTo().getValue() > upperEndpointDayOfWeek
//        ) {
//            TreeRangeSet<ZonedDateTime> result = TreeRangeSet.create();
//
//            if (getTimeFrom().isAfter(lowerEndpointLocalTime)) {
//                result.add(Range.openClosed(lowerEndpoint, getTimeFrom().atDate(lowerEndpoint.toLocalDate()).atZone(zoneId)));
//            }
//
//            return result;
//        }
//
//
////        return TreeRangeSet.create();
//        throw new UnsupportedOperationException("not support case,from:" + getWeekFrom() + "," + getTimeFrom() + "; to: " + weekTo + "," + getTimeTo() +
//                ";\n range" + aDayTimeRange + ",lower week:" + lowerEndpoint.getDayOfWeek() + ",upper week " + upperEndpoint.getDayOfWeek());
//    }

    public DayOfWeek getWeekFrom() {
        return weekFrom;
    }

    public void setWeekFrom(DayOfWeek weekFrom) {
        this.weekFrom = weekFrom;
    }

    public LocalTime getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(LocalTime timeFrom) {
        this.timeFrom = timeFrom;
    }

    public DayOfWeek getWeekTo() {
        return weekTo;
    }

    public void setWeekTo(DayOfWeek weekTo) {
        this.weekTo = weekTo;
    }

    public LocalTime getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(LocalTime timeTo) {
        this.timeTo = timeTo;
    }

    @Override
    public String toString() {
        return "WeeklyRestriction{" +
                "weekFrom=" + weekFrom +
                ", timeFrom=" + timeFrom +
                ", weekTo=" + weekTo +
                ", timeTo=" + timeTo +
                '}';
    }


}
