package codes.showme.techlib.time;

import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TimeRangeHelperTest {
    public static ZoneId zoneId = ZoneId.of("Asia/Shanghai");

    @Test
    public void localTimeTest() {
        LocalTime from = LocalTime.of(12, 0);
        LocalTime to = LocalTime.of(2, 0);
        assertTrue(to.isBefore(from));
    }

    @Test
    public void splitARangeToWeeklyRangesTest() {
        ZonedDateTime start = ZonedDateTime.of(2023, 1, 1, 0, 0, 0, 0, zoneId);
        ZonedDateTime end = start.plus(23, ChronoUnit.WEEKS);
        List<Range<ZonedDateTime>> ranges = TimeRangeHelper.splitARangeToWeekByWeekRanges(Range.openClosed(start, end));
        assertEquals(23, ranges.size());
        assertEquals(start, ranges.get(0).lowerEndpoint());
        assertEquals(start.plus(1, ChronoUnit.WEEKS), ranges.get(0).upperEndpoint());
        assertEquals(end, ranges.get(22).upperEndpoint());

    }

    @Test
    public void calculateADayRestrictionRangeInOneDayTest() {
        ZonedDateTime start = ZonedDateTime.of(2023, 1, 1, 0, 0, 0, 0, zoneId);
        ZonedDateTime end = start.plus(23, ChronoUnit.HOURS);
        LocalTime from = LocalTime.of(2, 10);
        LocalTime to = LocalTime.of(12, 22);
        RangeSet<ZonedDateTime> restrictionRange = TimeRangeHelper.calculateADayRestrictionRange(Range.open(start, end),
                from, to);
        Assert.assertFalse(restrictionRange.isEmpty());
        List<Range<ZonedDateTime>> rangeArrayList = Lists.newArrayList(restrictionRange.asRanges());
        Range<ZonedDateTime> firstRange = rangeArrayList.get(0);
        assertEquals(start.toLocalDate(), firstRange.lowerEndpoint().toLocalDate());
        assertEquals(start.toLocalDate(), firstRange.upperEndpoint().toLocalDate());
        assertEquals(from, firstRange.lowerEndpoint().toLocalTime());
        assertEquals(to, firstRange.upperEndpoint().toLocalTime());
    }

    @Test
    public void calculateADayRestrictionRangeCrossDayTest() {
        ZonedDateTime start = ZonedDateTime.of(2023, 1, 1, 3, 0, 0, 0, zoneId);
        ZonedDateTime end = start.plus(23, ChronoUnit.HOURS);
        LocalTime from = LocalTime.of(2, 10);
        LocalTime to = LocalTime.of(12, 22);
        RangeSet<ZonedDateTime> restrictionRange = TimeRangeHelper.calculateADayRestrictionRange(Range.open(start, end),
                from, to);
        Assert.assertFalse(restrictionRange.isEmpty());
        ArrayList<Range<ZonedDateTime>> rangeArrayList = Lists.newArrayList(restrictionRange.asRanges());
        Range<ZonedDateTime> firstRange = rangeArrayList.get(0);

        assertEquals(firstRange.lowerEndpoint().toLocalDate(), firstRange.upperEndpoint().toLocalDate());
        assertEquals(LocalTime.of(3, 0), firstRange.lowerEndpoint().toLocalTime());
        assertEquals(to, firstRange.upperEndpoint().toLocalTime());

    }

    @Test
    public void calculateADayRestrictionRangeCrossDay1Test() {
        ZonedDateTime start = ZonedDateTime.of(2023, 1, 1, 3, 0, 0, 0, zoneId);
        ZonedDateTime end = start.plus(23, ChronoUnit.HOURS).plus(59, ChronoUnit.MINUTES);
        LocalTime from = LocalTime.of(11, 10);
        LocalTime to = LocalTime.of(2, 22);
        RangeSet<ZonedDateTime> restrictionRange = TimeRangeHelper.calculateADayRestrictionRange(Range.open(start, end),
                from, to);
        Assert.assertFalse(restrictionRange.isEmpty());
        ArrayList<Range<ZonedDateTime>> rangeArrayList = Lists.newArrayList(restrictionRange.asRanges());
        for (Range<ZonedDateTime> zonedDateTimeRange : rangeArrayList) {
            System.out.println(zonedDateTimeRange);
        }

        Range<ZonedDateTime> firstRange = rangeArrayList.get(0);

        assertEquals(firstRange.lowerEndpoint().toLocalDate().plus(1, ChronoUnit.DAYS),
                firstRange.upperEndpoint().toLocalDate());
        assertEquals(LocalTime.of(11, 10), firstRange.lowerEndpoint().toLocalTime());
        assertEquals(end, firstRange.upperEndpoint());

    }

    @Test
    public void isCrossADayTest() {
        ZonedDateTime start = ZonedDateTime.of(2023, 1, 1, 0, 0, 0, 0, zoneId);
        Assert.assertTrue(TimeRangeHelper.isCrossADay(Range.openClosed(start, start.plus(1, ChronoUnit.DAYS))));
        Assert.assertFalse(TimeRangeHelper.isCrossADay(Range.openClosed(start, start.plus(1, ChronoUnit.DAYS).minus(1, ChronoUnit.SECONDS))));
    }

    @Test
    public void isCrossADayTest1() {
        ZonedDateTime start = ZonedDateTime.of(2023, 1, 1, 23, 59, 59, 0, zoneId);
        Assert.assertFalse(TimeRangeHelper.isCrossADay(Range.openClosed(start, start.plus(1, ChronoUnit.MILLIS))));
        Assert.assertTrue(TimeRangeHelper.isCrossADay(Range.openClosed(start, start.plus(1, ChronoUnit.SECONDS))));

    }

    @Test
    public void isIn24HoursTest() {
        ZonedDateTime start = ZonedDateTime.of(2023, 1, 1, 0, 0, 0, 0, zoneId);
        Assert.assertTrue(TimeRangeHelper.isIn24HoursBasedSeconds(Range.openClosed(start, start.plus(59, ChronoUnit.MINUTES))));
        Assert.assertTrue(TimeRangeHelper.isIn24HoursBasedSeconds(Range.openClosed(start, start.plus(1, ChronoUnit.DAYS).minus(1, ChronoUnit.SECONDS))));
        Assert.assertFalse(TimeRangeHelper.isNotIn24HoursBasedSeconds(Range.openClosed(start, start.plus(1, ChronoUnit.DAYS))));
        Assert.assertTrue(TimeRangeHelper.isIn24HoursBasedSeconds(Range.openClosed(start, start.plus(1, ChronoUnit.DAYS))));
        Assert.assertTrue(TimeRangeHelper.isNotIn24HoursBasedSeconds(Range.openClosed(start, start.plus(1, ChronoUnit.DAYS).plus(1, ChronoUnit.SECONDS))));
        Assert.assertTrue(TimeRangeHelper.isIn24HoursBasedSeconds(Range.openClosed(start, start.plus(1, ChronoUnit.DAYS).minus(1, ChronoUnit.MILLIS))));
    }

    @Test
    public void isIn1WeekTest() {
        ZonedDateTime start = ZonedDateTime.of(2023, 1, 1, 0, 0, 0, 0, zoneId);
        Assert.assertTrue(TimeRangeHelper.isIn1Week(Range.openClosed(start, start.plus(1, ChronoUnit.DAYS))));
        Assert.assertTrue(TimeRangeHelper.isIn1Week(Range.openClosed(start, start.plus(7, ChronoUnit.DAYS).minus(1, ChronoUnit.SECONDS))));
        Assert.assertTrue(TimeRangeHelper.isIn1Week(Range.openClosed(start, start.plus(7, ChronoUnit.DAYS).minus(1, ChronoUnit.NANOS))));
        Assert.assertFalse(TimeRangeHelper.isIn1Week(Range.openClosed(start, start.plus(7, ChronoUnit.DAYS).plus(1, ChronoUnit.SECONDS))));
        Assert.assertTrue(TimeRangeHelper.isIn1Week(Range.openClosed(start, start.plus(7, ChronoUnit.DAYS).plus(1, ChronoUnit.NANOS))));
    }

    @Test
    public void splitARangeToDayByDayRangesTest() {
        ZonedDateTime start = ZonedDateTime.of(2023, 1, 1, 0, 0, 0, 0, zoneId);
        ZonedDateTime end = start.plus(7, ChronoUnit.DAYS);

        List<Range<ZonedDateTime>> ranges = TimeRangeHelper.splitARangeToDayByDayRanges(Range.openClosed(start, end));
        assertEquals(7, ranges.size());
        assertEquals(start.plus(1, ChronoUnit.DAYS), ranges.get(0).upperEndpoint());
        assertEquals(start.plus(2, ChronoUnit.DAYS), ranges.get(1).upperEndpoint());
        assertEquals(start.plus(3, ChronoUnit.DAYS), ranges.get(2).upperEndpoint());
        assertEquals(start.plus(4, ChronoUnit.DAYS), ranges.get(3).upperEndpoint());
        assertEquals(start.plus(5, ChronoUnit.DAYS), ranges.get(4).upperEndpoint());
        assertEquals(start.plus(6, ChronoUnit.DAYS), ranges.get(5).upperEndpoint());
        assertEquals(start.plus(7, ChronoUnit.DAYS), ranges.get(6).upperEndpoint());


    }


}
