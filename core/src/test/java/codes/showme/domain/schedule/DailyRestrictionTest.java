package codes.showme.domain.schedule;

import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import org.junit.Test;

import java.time.*;
import java.time.temporal.ChronoUnit;

public class DailyRestrictionTest {

    @Test
    public void oneDayRangeTest() {
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");

        ZonedDateTime startTime = LocalDate.of(2023, Month.AUGUST, 1).atTime(7, 10).atZone(zoneId);
        ZonedDateTime endTime = startTime.plus(1, ChronoUnit.DAYS).minus(1, ChronoUnit.MILLIS);

        DailyRestriction dailyRestriction = new DailyRestriction(LocalTime.of(7, 10), LocalTime.of(23, 59));
        RangeSet<ZonedDateTime> timeRangeSet = dailyRestriction.restrict(Range.openClosed(startTime, endTime), zoneId);
        System.out.println(timeRangeSet);

    }

    @Test
    public void less24HoursRangeTest() {
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");

        ZonedDateTime startTime = LocalDate.of(2023, Month.AUGUST, 1).atTime(7, 10).atZone(zoneId);
        ZonedDateTime endTime = startTime.plus(1, ChronoUnit.DAYS).minus(30, ChronoUnit.MINUTES);

        DailyRestriction dailyRestriction = new DailyRestriction(LocalTime.of(7, 10), LocalTime.of(23, 59));
        RangeSet<ZonedDateTime> timeRangeSet = dailyRestriction.restrict(Range.openClosed(startTime, endTime), zoneId);
        System.out.println(timeRangeSet);

    }

    @Test
    public void multipleDaysRangeTest() {
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        LocalDate startDate = LocalDate.of(2023, Month.AUGUST, 1);
        ZonedDateTime startTime = startDate.atTime(7, 10).atZone(zoneId);
        ZonedDateTime endTime = startDate.atTime(LocalTime.of(11, 30)).atZone(zoneId).plus(7, ChronoUnit.DAYS);

        DailyRestriction dailyRestriction = new DailyRestriction(LocalTime.of(7, 10), LocalTime.of(23, 59));
        RangeSet<ZonedDateTime> timeRangeSet = dailyRestriction.restrict(Range.openClosed(startTime, endTime), zoneId);
        System.out.println(timeRangeSet);

    }
}
