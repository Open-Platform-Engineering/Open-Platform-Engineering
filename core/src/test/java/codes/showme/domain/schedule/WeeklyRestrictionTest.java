package codes.showme.domain.schedule;

import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import org.junit.Test;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class WeeklyRestrictionTest {
    private static final ZoneId zoneId = ZoneId.of("Asia/Shanghai");

    @Test
    public void buildOneWeekRangeTest() {
        ZonedDateTime startTime = LocalDate.of(2023, Month.JULY, 10).atTime(7, 10).atZone(zoneId);
        ZonedDateTime endTime = startTime.plus(1, ChronoUnit.WEEKS).minus(1, ChronoUnit.MILLIS);

        WeeklyRestriction weeklyRestriction0 = new WeeklyRestriction();
        weeklyRestriction0.from(DayOfWeek.MONDAY, LocalTime.of(9, 0))
                .to(DayOfWeek.THURSDAY, LocalTime.of(9, 13));
        RangeSet<ZonedDateTime> rangeSet = weeklyRestriction0.restrict(Range.openClosed(startTime, endTime), zoneId);

        List<Range<ZonedDateTime>> ranges = Lists.newArrayList(rangeSet.asRanges());
        assertEquals(1, ranges.size());
        assertEquals(startTime.withHour(9).withMinute(0), ranges.get(0).lowerEndpoint());
        assertEquals(LocalDate.of(2023, Month.JULY, 13).atTime(9, 13).atZone(zoneId), ranges.get(0).upperEndpoint());
    }

    @Test
    public void buildOneWeekRange1Test() {
        ZonedDateTime startTime = LocalDate.of(2023, Month.JULY, 12).atTime(7, 10).atZone(zoneId);
        ZonedDateTime endTime = startTime.plus(1, ChronoUnit.WEEKS).minus(1, ChronoUnit.MILLIS);

        WeeklyRestriction weeklyRestriction0 = new WeeklyRestriction();
        weeklyRestriction0.from(DayOfWeek.MONDAY, LocalTime.of(9, 0))
                .to(DayOfWeek.THURSDAY, LocalTime.of(9, 13));
        RangeSet<ZonedDateTime> rangeSet = weeklyRestriction0.restrict(Range.openClosed(startTime, endTime), zoneId);

        List<Range<ZonedDateTime>> ranges = Lists.newArrayList(rangeSet.asRanges());

        assertEquals(2, ranges.size());
        assertEquals(startTime.withHour(7).withMinute(10), ranges.get(0).lowerEndpoint());
        assertEquals(LocalDate.of(2023, Month.JULY, 13).atTime(9, 13).atZone(zoneId), ranges.get(0).upperEndpoint());
        assertEquals(LocalDate.of(2023, Month.JULY, 19).atTime(7, 9, 59, 999 * 1000000).toLocalTime(), ranges.get(1).upperEndpoint().toLocalTime());
    }

    @Test
    public void buildOneWeekRangeMultiWeeksTest() {
        ZonedDateTime startTime = LocalDate.of(2023, Month.JULY, 12).atTime(7, 10).atZone(zoneId);
        ZonedDateTime endTime = startTime.plus(3, ChronoUnit.WEEKS).minus(1, ChronoUnit.MILLIS);

        WeeklyRestriction weeklyRestriction0 = new WeeklyRestriction();
        weeklyRestriction0.from(DayOfWeek.MONDAY, LocalTime.of(9, 0))
                .to(DayOfWeek.THURSDAY, LocalTime.of(9, 13));

        RangeSet<ZonedDateTime> rangeSet = weeklyRestriction0.restrict(Range.openClosed(startTime, endTime), zoneId);

        List<Range<ZonedDateTime>> ranges = Lists.newArrayList(rangeSet.asRanges());
        assertEquals(4, ranges.size());
        assertEquals(startTime.withHour(7).withMinute(10), ranges.get(0).lowerEndpoint());
        assertEquals(LocalDate.of(2023, Month.JULY, 13).atTime(9, 13).atZone(zoneId), ranges.get(0).upperEndpoint());
        assertEquals(LocalDate.of(2023, Month.JULY, 17).atTime(9, 0).atZone(zoneId), ranges.get(1).lowerEndpoint());
        assertEquals(LocalDate.of(2023, Month.JULY, 20).atTime(9, 13).atZone(zoneId), ranges.get(1).upperEndpoint());
        assertEquals(LocalDate.of(2023, Month.JULY, 24).atTime(9, 0).atZone(zoneId), ranges.get(2).lowerEndpoint());
        assertEquals(LocalDate.of(2023, Month.AUGUST, 2).atTime(7, 9, 59, 999 * 1000000).atZone(zoneId), ranges.get(3).upperEndpoint());
    }

    @Test
    public void buildOneWeekRangeUpperBeforeLowerTest() {
        ZonedDateTime startTime = LocalDate.of(2023, Month.JULY, 16).atTime(10, 10).atZone(zoneId);
        ZonedDateTime endTime = startTime.plus(3, ChronoUnit.WEEKS).minus(1, ChronoUnit.MILLIS);

        WeeklyRestriction weeklyRestriction0 = new WeeklyRestriction();
        weeklyRestriction0.from(DayOfWeek.SUNDAY, LocalTime.of(9, 0))
                .to(DayOfWeek.THURSDAY, LocalTime.of(9, 13));

        RangeSet<ZonedDateTime> rangeSet = weeklyRestriction0.restrict(Range.openClosed(startTime, endTime), zoneId);

//        System.out.println(ScheduleTestHelper.print(rangeSet));
        // 2023-07-16T10:10+08:00[Asia/Shanghai]__2023-07-20T09:13+08:00[Asia/Shanghai]
        //2023-07-23T09:00+08:00[Asia/Shanghai]__2023-07-27T09:13+08:00[Asia/Shanghai]
        //2023-07-30T09:00+08:00[Asia/Shanghai]__2023-08-03T09:13+08:00[Asia/Shanghai]
        //2023-08-06T09:00+08:00[Asia/Shanghai]__2023-08-06T10:09:59.999+08:00[Asia/Shanghai]

        List<Range<ZonedDateTime>> ranges = Lists.newArrayList(rangeSet.asRanges());
        assertEquals(4, ranges.size());

        assertEquals(LocalDate.of(2023, Month.JULY, 16).atTime(10, 10).atZone(zoneId), ranges.get(0).lowerEndpoint());
        assertEquals(LocalDate.of(2023, Month.JULY, 20).atTime(9, 13).atZone(zoneId), ranges.get(0).upperEndpoint());
        assertEquals(LocalDate.of(2023, Month.JULY, 23).atTime(9, 0).atZone(zoneId), ranges.get(1).lowerEndpoint());
        assertEquals(LocalDate.of(2023, Month.JULY, 27).atTime(9, 13).atZone(zoneId), ranges.get(1).upperEndpoint());
        assertEquals(LocalDate.of(2023, Month.JULY, 30).atTime(9, 0).atZone(zoneId), ranges.get(2).lowerEndpoint());
        assertEquals(LocalDate.of(2023, Month.AUGUST, 3).atTime(9, 13).atZone(zoneId), ranges.get(2).upperEndpoint());
        assertEquals(LocalDate.of(2023, Month.AUGUST, 6).atTime(9, 0).atZone(zoneId), ranges.get(3).lowerEndpoint());
        assertEquals(LocalDate.of(2023, Month.AUGUST, 6).atTime(10, 9, 59, 999 * 1000000).atZone(zoneId), ranges.get(3).upperEndpoint());
    }

}
