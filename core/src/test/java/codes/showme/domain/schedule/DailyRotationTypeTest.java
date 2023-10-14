package codes.showme.domain.schedule;

import com.google.common.collect.*;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DailyRotationTypeTest {

    @Test
    public void add() {
        DailyRotation dailyRotationType = new DailyRotation();
        dailyRotationType.addDailyRestriction(LocalTime.of(10, 2), LocalTime.of(19, 1));
        Assert.assertTrue(dailyRotationType.hasRestrictions());
    }

    @Test
    public void theRangeBetweenStartAndEnd() {

        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        ZonedDateTime startTime = LocalDateTime.of(2023, Month.JULY, 1, 6, 30, 0).atZone(zoneId);
        ZonedDateTime endTime = startTime.plus(1, ChronoUnit.DAYS);

        DailyRotation dailyRotationType = new DailyRotation().addDailyRestriction(LocalTime.of(21, 0), LocalTime.of(7, 0));
        RangeSet<ZonedDateTime> rangeSet = dailyRotationType.getRange(Range.openClosed(startTime, endTime), zoneId);

        String rangePrint = """
                2023-07-01T06:30+08:00[Asia/Shanghai]__2023-07-01T07:00+08:00[Asia/Shanghai]
                2023-07-01T21:00+08:00[Asia/Shanghai]__2023-07-02T06:30+08:00[Asia/Shanghai]
                """;
        ScheduleTestHelper.validateFinalSchedule(rangeSet, rangePrint);


    }

    @Test
    public void testCase3() {
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        ZonedDateTime startTime = LocalDateTime.of(2023, Month.JULY, 1, 6, 30, 0).atZone(zoneId);
        ZonedDateTime endTime = startTime.plus(1, ChronoUnit.DAYS);

        DailyRotation dailyRotationType = new DailyRotation()
                .addDailyRestriction(LocalTime.of(5, 0),
                        LocalTime.of(13, 1));
        RangeSet<ZonedDateTime> rangeSet = dailyRotationType.getRange(Range.openClosed(startTime, endTime), zoneId);
        String rangeSetString = """
                2023-07-01T06:30+08:00[Asia/Shanghai]__2023-07-01T13:01+08:00[Asia/Shanghai]
                2023-07-02T05:00+08:00[Asia/Shanghai]__2023-07-02T06:30+08:00[Asia/Shanghai]
                """;
        ScheduleTestHelper.validateFinalSchedule(rangeSet, rangeSetString);
    }

    @Test
    public void case3() {
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        ZonedDateTime startTime = LocalDateTime.of(2023, Month.JULY, 1, 6, 30, 0).atZone(zoneId);
        ZonedDateTime endTime = startTime.plus(1, ChronoUnit.DAYS);

        DailyRotation dailyRotationType = new DailyRotation();
        dailyRotationType.addDailyRestriction(LocalTime.of(8, 40), LocalTime.of(12, 0));
        dailyRotationType.addDailyRestriction(LocalTime.of(13, 30), LocalTime.of(18, 0));
        dailyRotationType.addDailyRestriction(LocalTime.of(20, 40), LocalTime.of(6, 40));
        RangeSet<ZonedDateTime> rangeSet = dailyRotationType.getRange(Range.openClosed(startTime, endTime), zoneId);
        String rangeSetStr = """
                2023-07-01T06:30+08:00[Asia/Shanghai]__2023-07-01T06:40+08:00[Asia/Shanghai]
                2023-07-01T08:40+08:00[Asia/Shanghai]__2023-07-01T12:00+08:00[Asia/Shanghai]
                2023-07-01T13:30+08:00[Asia/Shanghai]__2023-07-01T18:00+08:00[Asia/Shanghai]
                2023-07-01T20:40+08:00[Asia/Shanghai]__2023-07-02T06:30+08:00[Asia/Shanghai]
                """;
        ScheduleTestHelper.validateFinalSchedule(rangeSet, rangeSetStr);
    }

    @Test
    public void getFullRangesWithoutRestrictionTest() {
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        LocalDate startDate = LocalDate.of(2023, Month.AUGUST, 1);
        ZonedDateTime endTime = startDate.atTime(LocalTime.of(11,30)).atZone(zoneId).plus(7, ChronoUnit.DAYS);
        DailyRotation dailyRotation = new DailyRotation();
        dailyRotation.setHandoffTime(10, 20);
        List<Range<ZonedDateTime>> fullRangesWithoutRestriction =
                dailyRotation.getFullRangesWithoutRestriction(startDate, endTime, zoneId);
        System.out.println(ScheduleTestHelper.print(fullRangesWithoutRestriction));
        String rangeStr = """
                2023-08-01T10:20+08:00[Asia/Shanghai]__2023-08-02T10:20+08:00[Asia/Shanghai]
                2023-08-02T10:20+08:00[Asia/Shanghai]__2023-08-03T10:20+08:00[Asia/Shanghai]
                2023-08-03T10:20+08:00[Asia/Shanghai]__2023-08-04T10:20+08:00[Asia/Shanghai]
                2023-08-04T10:20+08:00[Asia/Shanghai]__2023-08-05T10:20+08:00[Asia/Shanghai]
                2023-08-05T10:20+08:00[Asia/Shanghai]__2023-08-06T10:20+08:00[Asia/Shanghai]
                2023-08-06T10:20+08:00[Asia/Shanghai]__2023-08-07T10:20+08:00[Asia/Shanghai]
                2023-08-07T10:20+08:00[Asia/Shanghai]__2023-08-08T10:20+08:00[Asia/Shanghai]
                2023-08-08T10:20+08:00[Asia/Shanghai]__2023-08-08T11:30+08:00[Asia/Shanghai]
                """;
        Assert.assertEquals(8, fullRangesWithoutRestriction.size());
        ScheduleTestHelper.validateFinalSchedule(fullRangesWithoutRestriction, rangeStr);
    }
}