package codes.showme.domain.schedule;

import codes.showme.domain.notification.NotifiedUser;
import codes.showme.domain.notification.NotifiedUserExample;
import codes.showme.domain.schedule.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeMap;
import org.junit.Before;
import org.junit.Test;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class WeeklyRotationTest {
    public static ZoneId zoneId = ZoneId.of("Asia/Shanghai");

    private ScheduleLayer scheduleLayer;

    private ArrayList<NotifiedUser> notifiedUsers = new ArrayList<>();
    private NotifiedUserExample jack = new NotifiedUserExample("Jack");
    private NotifiedUserExample jane = new NotifiedUserExample("Jane");
    private NotifiedUserExample shawn = new NotifiedUserExample("Shawn");

    @Before
    public void setUp() throws Exception {
        scheduleLayer = new ScheduleLayer();
        notifiedUsers.add(jack);
        notifiedUsers.add(jane);
        notifiedUsers.add(shawn);
        scheduleLayer.setNotifiedUsers(notifiedUsers);
    }

    @Test
    public void noRestrictions() {
        WeeklyRotation rotation = new WeeklyRotation();
        rotation.setHandoffDayOfWeek(DayOfWeek.TUESDAY);
        rotation.setHandoffTime(LocalTime.of(8, 30));
        ZonedDateTime start = ZonedDateTime.of(2023, 6, 12, 0, 0, 0, 0, zoneId);
        RangeSet<ZonedDateTime> range = rotation.getRange(Range.openClosed(start, start.plus(3, ChronoUnit.WEEKS)), zoneId);
        System.out.println(range);

    }

    @Test
    public void getRange() {
        WeeklyRotation rotation = new WeeklyRotation();
        rotation.setHandoffDayOfWeek(DayOfWeek.TUESDAY);
        rotation.setHandoffTime(LocalTime.of(8, 30));
        List<Restriction> restrictionList = Lists.newArrayList();
        DailyRestriction dailyRestriction = new DailyRestriction();
        dailyRestriction.setFrom(LocalTime.of(9, 0));
        dailyRestriction.setTo(LocalTime.of(12, 0));
        DailyRestriction dailyRestriction1 = new DailyRestriction();
        dailyRestriction1.setFrom(LocalTime.of(13, 0));
        dailyRestriction1.setTo(LocalTime.of(19, 0));
        restrictionList.add(dailyRestriction);
        restrictionList.add(dailyRestriction1);
        rotation.setRestrictions(restrictionList);

        LocalDate startDate = LocalDate.of(2023, 7, 14);
        scheduleLayer.setStartDate(startDate);
        scheduleLayer.setRotation(rotation);

        ZonedDateTime endTime = ZonedDateTime.of(startDate.plus(1, ChronoUnit.WEEKS), LocalTime.of(12, 3), zoneId);
        TreeRangeMap<ZonedDateTime, NotifiedUser> schedule = scheduleLayer.getSchedule(endTime, zoneId);


        String exceptedSchedule = """
                Jack,2023-07-14T09:00+08:00[Asia/Shanghai]__2023-07-14T12:00+08:00[Asia/Shanghai]
                Jack,2023-07-14T13:00+08:00[Asia/Shanghai]__2023-07-14T19:00+08:00[Asia/Shanghai]
                Jack,2023-07-15T09:00+08:00[Asia/Shanghai]__2023-07-15T12:00+08:00[Asia/Shanghai]
                Jack,2023-07-15T13:00+08:00[Asia/Shanghai]__2023-07-15T19:00+08:00[Asia/Shanghai]
                Jack,2023-07-16T09:00+08:00[Asia/Shanghai]__2023-07-16T12:00+08:00[Asia/Shanghai]
                Jack,2023-07-16T13:00+08:00[Asia/Shanghai]__2023-07-16T19:00+08:00[Asia/Shanghai]
                Jack,2023-07-17T09:00+08:00[Asia/Shanghai]__2023-07-17T12:00+08:00[Asia/Shanghai]
                Jack,2023-07-17T13:00+08:00[Asia/Shanghai]__2023-07-17T19:00+08:00[Asia/Shanghai]
                Jack,2023-07-18T09:00+08:00[Asia/Shanghai]__2023-07-18T12:00+08:00[Asia/Shanghai]
                Jack,2023-07-18T13:00+08:00[Asia/Shanghai]__2023-07-18T19:00+08:00[Asia/Shanghai]
                Jack,2023-07-19T09:00+08:00[Asia/Shanghai]__2023-07-19T12:00+08:00[Asia/Shanghai]
                Jack,2023-07-19T13:00+08:00[Asia/Shanghai]__2023-07-19T19:00+08:00[Asia/Shanghai]
                Jack,2023-07-20T09:00+08:00[Asia/Shanghai]__2023-07-20T12:00+08:00[Asia/Shanghai]
                Jack,2023-07-20T13:00+08:00[Asia/Shanghai]__2023-07-20T19:00+08:00[Asia/Shanghai]
                Jane,2023-07-21T09:00+08:00[Asia/Shanghai]__2023-07-21T12:00+08:00[Asia/Shanghai]
                Jane,2023-07-21T13:00+08:00[Asia/Shanghai]__2023-07-21T19:00+08:00[Asia/Shanghai]
                Jane,2023-07-22T09:00+08:00[Asia/Shanghai]__2023-07-22T12:00+08:00[Asia/Shanghai]
                Jane,2023-07-22T13:00+08:00[Asia/Shanghai]__2023-07-22T19:00+08:00[Asia/Shanghai]
                Jane,2023-07-23T09:00+08:00[Asia/Shanghai]__2023-07-23T12:00+08:00[Asia/Shanghai]
                Jane,2023-07-23T13:00+08:00[Asia/Shanghai]__2023-07-23T19:00+08:00[Asia/Shanghai]
                Jane,2023-07-24T09:00+08:00[Asia/Shanghai]__2023-07-24T12:00+08:00[Asia/Shanghai]
                Jane,2023-07-24T13:00+08:00[Asia/Shanghai]__2023-07-24T19:00+08:00[Asia/Shanghai]
                Jane,2023-07-25T09:00+08:00[Asia/Shanghai]__2023-07-25T12:00+08:00[Asia/Shanghai]
                Jane,2023-07-25T13:00+08:00[Asia/Shanghai]__2023-07-25T19:00+08:00[Asia/Shanghai]
                Jane,2023-07-26T09:00+08:00[Asia/Shanghai]__2023-07-26T12:00+08:00[Asia/Shanghai]
                Jane,2023-07-26T13:00+08:00[Asia/Shanghai]__2023-07-26T19:00+08:00[Asia/Shanghai]
                Jane,2023-07-27T09:00+08:00[Asia/Shanghai]__2023-07-27T12:00+08:00[Asia/Shanghai]
                Jane,2023-07-27T13:00+08:00[Asia/Shanghai]__2023-07-27T19:00+08:00[Asia/Shanghai]
                Shawn,2023-07-28T09:00+08:00[Asia/Shanghai]__2023-07-28T12:00+08:00[Asia/Shanghai]
                Shawn,2023-07-28T13:00+08:00[Asia/Shanghai]__2023-07-28T19:00+08:00[Asia/Shanghai]
                Shawn,2023-07-29T09:00+08:00[Asia/Shanghai]__2023-07-29T12:00+08:00[Asia/Shanghai]
                Shawn,2023-07-29T13:00+08:00[Asia/Shanghai]__2023-07-29T19:00+08:00[Asia/Shanghai]
                Shawn,2023-07-30T09:00+08:00[Asia/Shanghai]__2023-07-30T12:00+08:00[Asia/Shanghai]
                Shawn,2023-07-30T13:00+08:00[Asia/Shanghai]__2023-07-30T19:00+08:00[Asia/Shanghai]
                Shawn,2023-07-31T09:00+08:00[Asia/Shanghai]__2023-07-31T12:00+08:00[Asia/Shanghai]
                Shawn,2023-07-31T13:00+08:00[Asia/Shanghai]__2023-07-31T19:00+08:00[Asia/Shanghai]
                Shawn,2023-08-01T09:00+08:00[Asia/Shanghai]__2023-08-01T12:00+08:00[Asia/Shanghai]
                Shawn,2023-08-01T13:00+08:00[Asia/Shanghai]__2023-08-01T19:00+08:00[Asia/Shanghai]
                Shawn,2023-08-02T09:00+08:00[Asia/Shanghai]__2023-08-02T12:00+08:00[Asia/Shanghai]
                Shawn,2023-08-02T13:00+08:00[Asia/Shanghai]__2023-08-02T19:00+08:00[Asia/Shanghai]
                Shawn,2023-08-03T09:00+08:00[Asia/Shanghai]__2023-08-03T12:00+08:00[Asia/Shanghai]
                Shawn,2023-08-03T13:00+08:00[Asia/Shanghai]__2023-08-03T19:00+08:00[Asia/Shanghai]
                Jack,2023-08-04T09:00+08:00[Asia/Shanghai]__2023-08-04T12:00+08:00[Asia/Shanghai]
                Jack,2023-08-04T13:00+08:00[Asia/Shanghai]__2023-08-04T19:00+08:00[Asia/Shanghai]
                Jack,2023-08-05T09:00+08:00[Asia/Shanghai]__2023-08-05T12:00+08:00[Asia/Shanghai]
                Jack,2023-08-05T13:00+08:00[Asia/Shanghai]__2023-08-05T19:00+08:00[Asia/Shanghai]
                Jack,2023-08-06T09:00+08:00[Asia/Shanghai]__2023-08-06T12:00+08:00[Asia/Shanghai]
                Jack,2023-08-06T13:00+08:00[Asia/Shanghai]__2023-08-06T19:00+08:00[Asia/Shanghai]
                Jack,2023-08-07T09:00+08:00[Asia/Shanghai]__2023-08-07T12:00+08:00[Asia/Shanghai]
                Jack,2023-08-07T13:00+08:00[Asia/Shanghai]__2023-08-07T19:00+08:00[Asia/Shanghai]
                Jack,2023-08-08T09:00+08:00[Asia/Shanghai]__2023-08-08T12:00+08:00[Asia/Shanghai]
                Jack,2023-08-08T13:00+08:00[Asia/Shanghai]__2023-08-08T19:00+08:00[Asia/Shanghai]
                Jack,2023-08-09T09:00+08:00[Asia/Shanghai]__2023-08-09T12:00+08:00[Asia/Shanghai]
                Jack,2023-08-09T13:00+08:00[Asia/Shanghai]__2023-08-09T19:00+08:00[Asia/Shanghai]
                Jack,2023-08-10T09:00+08:00[Asia/Shanghai]__2023-08-10T12:00+08:00[Asia/Shanghai]
                Jack,2023-08-10T13:00+08:00[Asia/Shanghai]__2023-08-10T19:00+08:00[Asia/Shanghai]
                Jane,2023-08-11T09:00+08:00[Asia/Shanghai]__2023-08-11T12:00+08:00[Asia/Shanghai]
                Jane,2023-08-11T13:00+08:00[Asia/Shanghai]__2023-08-11T19:00+08:00[Asia/Shanghai]
                """;
        ScheduleTestHelper.validateFinalSchedule(schedule, exceptedSchedule);

    }

    @Test
    public void getRange2() {
        WeeklyRotation rotation = new WeeklyRotation();
        rotation.setHandoffDayOfWeek(DayOfWeek.TUESDAY);
        rotation.setHandoffTime(LocalTime.of(8, 30));
        List<Restriction> restrictionList = Lists.newArrayList();
        DailyRestriction dailyRestriction = new DailyRestriction();
        dailyRestriction.setFrom(LocalTime.of(9, 0));
        dailyRestriction.setTo(LocalTime.of(12, 0));
        DailyRestriction dailyRestriction1 = new DailyRestriction();
        dailyRestriction1.setFrom(LocalTime.of(11, 0));
        dailyRestriction1.setTo(LocalTime.of(19, 0));
        restrictionList.add(dailyRestriction);
        restrictionList.add(dailyRestriction1);
        rotation.setRestrictions(restrictionList);

        LocalDate startDate = LocalDate.of(2023, 7, 14);
        scheduleLayer.setStartDate(startDate);
        scheduleLayer.setRotation(rotation);

        ZonedDateTime endTime = ZonedDateTime.of(startDate.plus(1, ChronoUnit.WEEKS), LocalTime.of(12, 3), zoneId);
        TreeRangeMap<ZonedDateTime, NotifiedUser> schedule = scheduleLayer.getSchedule(endTime, zoneId);

        System.out.println(ScheduleTestHelper.print(schedule));

        String exceptedSchedule = """
                Jack,2023-07-14T09:00+08:00[Asia/Shanghai]__2023-07-14T19:00+08:00[Asia/Shanghai]
                Jack,2023-07-15T09:00+08:00[Asia/Shanghai]__2023-07-15T19:00+08:00[Asia/Shanghai]
                Jack,2023-07-16T09:00+08:00[Asia/Shanghai]__2023-07-16T19:00+08:00[Asia/Shanghai]
                Jack,2023-07-17T09:00+08:00[Asia/Shanghai]__2023-07-17T19:00+08:00[Asia/Shanghai]
                Jack,2023-07-18T09:00+08:00[Asia/Shanghai]__2023-07-18T19:00+08:00[Asia/Shanghai]
                Jack,2023-07-19T09:00+08:00[Asia/Shanghai]__2023-07-19T19:00+08:00[Asia/Shanghai]
                Jack,2023-07-20T09:00+08:00[Asia/Shanghai]__2023-07-20T19:00+08:00[Asia/Shanghai]
                Jane,2023-07-21T09:00+08:00[Asia/Shanghai]__2023-07-21T12:03+08:00[Asia/Shanghai]
                """;
        ScheduleTestHelper.validateFinalSchedule(schedule, exceptedSchedule);

    }

    @Test
    public void getFullRangesWithoutRestrictionTest() {
        LocalDate startDate = LocalDate.of(2023, Month.AUGUST, 1);
        ZonedDateTime endTime = startDate.atTime(LocalTime.of(11, 30)).atZone(zoneId).plus(2, ChronoUnit.WEEKS);
        WeeklyRotation rotation = new WeeklyRotation();
        rotation.setHandoffDayOfWeek(DayOfWeek.TUESDAY);
        rotation.setHandoffTime(LocalTime.of(8, 30));
        List<Range<ZonedDateTime>> fullRangesWithoutRestriction = rotation.getFullRangesWithoutRestriction(startDate, endTime, zoneId);
//        System.out.println(ScheduleTestHelper.print(fullRangesWithoutRestriction));
        String rangeStr = """
                2023-08-01T08:30+08:00[Asia/Shanghai]__2023-08-08T08:30+08:00[Asia/Shanghai]
                2023-08-08T08:30+08:00[Asia/Shanghai]__2023-08-15T08:30+08:00[Asia/Shanghai]
                2023-08-15T08:30+08:00[Asia/Shanghai]__2023-08-15T11:30+08:00[Asia/Shanghai]
                """;
        ScheduleTestHelper.validateFinalSchedule(fullRangesWithoutRestriction, rangeStr);

    }
}