package codes.showme.domain.schedule;

import codes.showme.domain.notification.NotifiedUser;
import codes.showme.domain.notification.NotifiedUserExample;
import com.google.common.collect.TreeRangeMap;
import org.junit.Before;
import org.junit.Test;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class ScheduleLayerTest {
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
    public void oneDailyRestrictionTest() {
        scheduleLayer.setNotifiedUsers(notifiedUsers);
        DailyRotation rotation = new DailyRotation();

        rotation.addDailyRestriction(LocalTime.of(9, 0), LocalTime.of(12, 0));
        rotation.addDailyRestriction(LocalTime.of(13, 0), LocalTime.of(18, 0));
        rotation.setHandoffTime(16, 0);
        scheduleLayer.setRotation(rotation);
        scheduleLayer.setStartDate(LocalDate.of(2023, 7, 10));
        ZonedDateTime endTime = scheduleLayer.getStartTime(zoneId).plus(4, ChronoUnit.WEEKS);
        TreeRangeMap<ZonedDateTime, NotifiedUser> layerSchedule = scheduleLayer.getSchedule(endTime, zoneId);


        String exceptStr = """
                Jack,2023-07-10T16:00+08:00[Asia/Shanghai]__2023-07-10T18:00+08:00[Asia/Shanghai]
                Jack,2023-07-11T09:00+08:00[Asia/Shanghai]__2023-07-11T12:00+08:00[Asia/Shanghai]
                Jack,2023-07-11T13:00+08:00[Asia/Shanghai]__2023-07-11T16:00+08:00[Asia/Shanghai]
                Jane,2023-07-11T16:00+08:00[Asia/Shanghai]__2023-07-11T18:00+08:00[Asia/Shanghai]
                Jane,2023-07-12T09:00+08:00[Asia/Shanghai]__2023-07-12T12:00+08:00[Asia/Shanghai]
                Jane,2023-07-12T13:00+08:00[Asia/Shanghai]__2023-07-12T16:00+08:00[Asia/Shanghai]
                Shawn,2023-07-12T16:00+08:00[Asia/Shanghai]__2023-07-12T18:00+08:00[Asia/Shanghai]
                Shawn,2023-07-13T09:00+08:00[Asia/Shanghai]__2023-07-13T12:00+08:00[Asia/Shanghai]
                Shawn,2023-07-13T13:00+08:00[Asia/Shanghai]__2023-07-13T16:00+08:00[Asia/Shanghai]
                Jack,2023-07-13T16:00+08:00[Asia/Shanghai]__2023-07-13T18:00+08:00[Asia/Shanghai]
                Jack,2023-07-14T09:00+08:00[Asia/Shanghai]__2023-07-14T12:00+08:00[Asia/Shanghai]
                Jack,2023-07-14T13:00+08:00[Asia/Shanghai]__2023-07-14T16:00+08:00[Asia/Shanghai]
                Jane,2023-07-14T16:00+08:00[Asia/Shanghai]__2023-07-14T18:00+08:00[Asia/Shanghai]
                Jane,2023-07-15T09:00+08:00[Asia/Shanghai]__2023-07-15T12:00+08:00[Asia/Shanghai]
                Jane,2023-07-15T13:00+08:00[Asia/Shanghai]__2023-07-15T16:00+08:00[Asia/Shanghai]
                Shawn,2023-07-15T16:00+08:00[Asia/Shanghai]__2023-07-15T18:00+08:00[Asia/Shanghai]
                Shawn,2023-07-16T09:00+08:00[Asia/Shanghai]__2023-07-16T12:00+08:00[Asia/Shanghai]
                Shawn,2023-07-16T13:00+08:00[Asia/Shanghai]__2023-07-16T16:00+08:00[Asia/Shanghai]
                Jack,2023-07-16T16:00+08:00[Asia/Shanghai]__2023-07-16T18:00+08:00[Asia/Shanghai]
                Jack,2023-07-17T09:00+08:00[Asia/Shanghai]__2023-07-17T12:00+08:00[Asia/Shanghai]
                Jack,2023-07-17T13:00+08:00[Asia/Shanghai]__2023-07-17T16:00+08:00[Asia/Shanghai]
                Jane,2023-07-17T16:00+08:00[Asia/Shanghai]__2023-07-17T18:00+08:00[Asia/Shanghai]
                Jane,2023-07-18T09:00+08:00[Asia/Shanghai]__2023-07-18T12:00+08:00[Asia/Shanghai]
                Jane,2023-07-18T13:00+08:00[Asia/Shanghai]__2023-07-18T16:00+08:00[Asia/Shanghai]
                Shawn,2023-07-18T16:00+08:00[Asia/Shanghai]__2023-07-18T18:00+08:00[Asia/Shanghai]
                Shawn,2023-07-19T09:00+08:00[Asia/Shanghai]__2023-07-19T12:00+08:00[Asia/Shanghai]
                Shawn,2023-07-19T13:00+08:00[Asia/Shanghai]__2023-07-19T16:00+08:00[Asia/Shanghai]
                Jack,2023-07-19T16:00+08:00[Asia/Shanghai]__2023-07-19T18:00+08:00[Asia/Shanghai]
                Jack,2023-07-20T09:00+08:00[Asia/Shanghai]__2023-07-20T12:00+08:00[Asia/Shanghai]
                Jack,2023-07-20T13:00+08:00[Asia/Shanghai]__2023-07-20T16:00+08:00[Asia/Shanghai]
                Jane,2023-07-20T16:00+08:00[Asia/Shanghai]__2023-07-20T18:00+08:00[Asia/Shanghai]
                Jane,2023-07-21T09:00+08:00[Asia/Shanghai]__2023-07-21T12:00+08:00[Asia/Shanghai]
                Jane,2023-07-21T13:00+08:00[Asia/Shanghai]__2023-07-21T16:00+08:00[Asia/Shanghai]
                Shawn,2023-07-21T16:00+08:00[Asia/Shanghai]__2023-07-21T18:00+08:00[Asia/Shanghai]
                Shawn,2023-07-22T09:00+08:00[Asia/Shanghai]__2023-07-22T12:00+08:00[Asia/Shanghai]
                Shawn,2023-07-22T13:00+08:00[Asia/Shanghai]__2023-07-22T16:00+08:00[Asia/Shanghai]
                Jack,2023-07-22T16:00+08:00[Asia/Shanghai]__2023-07-22T18:00+08:00[Asia/Shanghai]
                Jack,2023-07-23T09:00+08:00[Asia/Shanghai]__2023-07-23T12:00+08:00[Asia/Shanghai]
                Jack,2023-07-23T13:00+08:00[Asia/Shanghai]__2023-07-23T16:00+08:00[Asia/Shanghai]
                Jane,2023-07-23T16:00+08:00[Asia/Shanghai]__2023-07-23T18:00+08:00[Asia/Shanghai]
                Jane,2023-07-24T09:00+08:00[Asia/Shanghai]__2023-07-24T12:00+08:00[Asia/Shanghai]
                Jane,2023-07-24T13:00+08:00[Asia/Shanghai]__2023-07-24T16:00+08:00[Asia/Shanghai]
                Shawn,2023-07-24T16:00+08:00[Asia/Shanghai]__2023-07-24T18:00+08:00[Asia/Shanghai]
                Shawn,2023-07-25T09:00+08:00[Asia/Shanghai]__2023-07-25T12:00+08:00[Asia/Shanghai]
                Shawn,2023-07-25T13:00+08:00[Asia/Shanghai]__2023-07-25T16:00+08:00[Asia/Shanghai]
                Jack,2023-07-25T16:00+08:00[Asia/Shanghai]__2023-07-25T18:00+08:00[Asia/Shanghai]
                Jack,2023-07-26T09:00+08:00[Asia/Shanghai]__2023-07-26T12:00+08:00[Asia/Shanghai]
                Jack,2023-07-26T13:00+08:00[Asia/Shanghai]__2023-07-26T16:00+08:00[Asia/Shanghai]
                Jane,2023-07-26T16:00+08:00[Asia/Shanghai]__2023-07-26T18:00+08:00[Asia/Shanghai]
                Jane,2023-07-27T09:00+08:00[Asia/Shanghai]__2023-07-27T12:00+08:00[Asia/Shanghai]
                Jane,2023-07-27T13:00+08:00[Asia/Shanghai]__2023-07-27T16:00+08:00[Asia/Shanghai]
                Shawn,2023-07-27T16:00+08:00[Asia/Shanghai]__2023-07-27T18:00+08:00[Asia/Shanghai]
                Shawn,2023-07-28T09:00+08:00[Asia/Shanghai]__2023-07-28T12:00+08:00[Asia/Shanghai]
                Shawn,2023-07-28T13:00+08:00[Asia/Shanghai]__2023-07-28T16:00+08:00[Asia/Shanghai]
                Jack,2023-07-28T16:00+08:00[Asia/Shanghai]__2023-07-28T18:00+08:00[Asia/Shanghai]
                Jack,2023-07-29T09:00+08:00[Asia/Shanghai]__2023-07-29T12:00+08:00[Asia/Shanghai]
                Jack,2023-07-29T13:00+08:00[Asia/Shanghai]__2023-07-29T16:00+08:00[Asia/Shanghai]
                Jane,2023-07-29T16:00+08:00[Asia/Shanghai]__2023-07-29T18:00+08:00[Asia/Shanghai]
                Jane,2023-07-30T09:00+08:00[Asia/Shanghai]__2023-07-30T12:00+08:00[Asia/Shanghai]
                Jane,2023-07-30T13:00+08:00[Asia/Shanghai]__2023-07-30T16:00+08:00[Asia/Shanghai]
                Shawn,2023-07-30T16:00+08:00[Asia/Shanghai]__2023-07-30T18:00+08:00[Asia/Shanghai]
                Shawn,2023-07-31T09:00+08:00[Asia/Shanghai]__2023-07-31T12:00+08:00[Asia/Shanghai]
                Shawn,2023-07-31T13:00+08:00[Asia/Shanghai]__2023-07-31T16:00+08:00[Asia/Shanghai]
                Jack,2023-07-31T16:00+08:00[Asia/Shanghai]__2023-07-31T18:00+08:00[Asia/Shanghai]
                Jack,2023-08-01T09:00+08:00[Asia/Shanghai]__2023-08-01T12:00+08:00[Asia/Shanghai]
                Jack,2023-08-01T13:00+08:00[Asia/Shanghai]__2023-08-01T16:00+08:00[Asia/Shanghai]
                Jane,2023-08-01T16:00+08:00[Asia/Shanghai]__2023-08-01T18:00+08:00[Asia/Shanghai]
                Jane,2023-08-02T09:00+08:00[Asia/Shanghai]__2023-08-02T12:00+08:00[Asia/Shanghai]
                Jane,2023-08-02T13:00+08:00[Asia/Shanghai]__2023-08-02T16:00+08:00[Asia/Shanghai]
                Shawn,2023-08-02T16:00+08:00[Asia/Shanghai]__2023-08-02T18:00+08:00[Asia/Shanghai]
                Shawn,2023-08-03T09:00+08:00[Asia/Shanghai]__2023-08-03T12:00+08:00[Asia/Shanghai]
                Shawn,2023-08-03T13:00+08:00[Asia/Shanghai]__2023-08-03T16:00+08:00[Asia/Shanghai]
                Jack,2023-08-03T16:00+08:00[Asia/Shanghai]__2023-08-03T18:00+08:00[Asia/Shanghai]
                Jack,2023-08-04T09:00+08:00[Asia/Shanghai]__2023-08-04T12:00+08:00[Asia/Shanghai]
                Jack,2023-08-04T13:00+08:00[Asia/Shanghai]__2023-08-04T16:00+08:00[Asia/Shanghai]
                Jane,2023-08-04T16:00+08:00[Asia/Shanghai]__2023-08-04T18:00+08:00[Asia/Shanghai]
                Jane,2023-08-05T09:00+08:00[Asia/Shanghai]__2023-08-05T12:00+08:00[Asia/Shanghai]
                Jane,2023-08-05T13:00+08:00[Asia/Shanghai]__2023-08-05T16:00+08:00[Asia/Shanghai]
                Shawn,2023-08-05T16:00+08:00[Asia/Shanghai]__2023-08-05T18:00+08:00[Asia/Shanghai]
                Shawn,2023-08-06T09:00+08:00[Asia/Shanghai]__2023-08-06T12:00+08:00[Asia/Shanghai]
                Shawn,2023-08-06T13:00+08:00[Asia/Shanghai]__2023-08-06T16:00+08:00[Asia/Shanghai]
                Jack,2023-08-06T16:00+08:00[Asia/Shanghai]__2023-08-06T18:00+08:00[Asia/Shanghai]
                Jack,2023-08-07T09:00+08:00[Asia/Shanghai]__2023-08-07T12:00+08:00[Asia/Shanghai]
                Jack,2023-08-07T13:00+08:00[Asia/Shanghai]__2023-08-07T16:00+08:00[Asia/Shanghai]
                """;
        ScheduleTestHelper.validateFinalSchedule(layerSchedule, exceptStr);

    }


    @Test
    public void oneWeeklyRestrictionTest() {
        scheduleLayer.setNotifiedUsers(notifiedUsers);

        WeeklyRestriction weeklyRestriction0 = new WeeklyRestriction();
        weeklyRestriction0.from(DayOfWeek.MONDAY, LocalTime.of(9, 0))
                .to(DayOfWeek.MONDAY, LocalTime.of(9, 13));
        WeeklyRestriction weeklyRestriction1 = new WeeklyRestriction();
        weeklyRestriction1.from(DayOfWeek.MONDAY, LocalTime.of(21, 20))
                .to(DayOfWeek.THURSDAY, LocalTime.of(11, 10));

        DailyRotation rotation = new DailyRotation();
        rotation.addWeeklyRestriction(weeklyRestriction0);
        rotation.addWeeklyRestriction(weeklyRestriction1);
        rotation.setHandoffTime(1, 11);
        scheduleLayer.setRotation(rotation);
        scheduleLayer.setStartDate(LocalDate.of(2023, 7, 12));
        ZonedDateTime endTime = scheduleLayer.getStartTime(zoneId).plus(4, ChronoUnit.WEEKS);
        TreeRangeMap<ZonedDateTime, NotifiedUser> scheduleLayerSchedule = scheduleLayer.getSchedule(endTime, zoneId);
        System.out.println(ScheduleTestHelper.print(scheduleLayerSchedule));
        String exceptStr = """
                Jack,2023-07-12T01:11+08:00[Asia/Shanghai]__2023-07-13T01:11+08:00[Asia/Shanghai]
                Jane,2023-07-13T01:11+08:00[Asia/Shanghai]__2023-07-13T11:10+08:00[Asia/Shanghai]
                Shawn,2023-07-17T09:00+08:00[Asia/Shanghai]__2023-07-17T09:13+08:00[Asia/Shanghai]
                Shawn,2023-07-17T21:20+08:00[Asia/Shanghai]__2023-07-18T01:11+08:00[Asia/Shanghai]
                Jack,2023-07-18T01:11+08:00[Asia/Shanghai]__2023-07-19T01:11+08:00[Asia/Shanghai]
                Jane,2023-07-19T01:11+08:00[Asia/Shanghai]__2023-07-20T01:11+08:00[Asia/Shanghai]
                Shawn,2023-07-20T01:11+08:00[Asia/Shanghai]__2023-07-20T11:10+08:00[Asia/Shanghai]
                Jack,2023-07-24T09:00+08:00[Asia/Shanghai]__2023-07-24T09:13+08:00[Asia/Shanghai]
                Jack,2023-07-24T21:20+08:00[Asia/Shanghai]__2023-07-25T01:11+08:00[Asia/Shanghai]
                Jane,2023-07-25T01:11+08:00[Asia/Shanghai]__2023-07-26T01:11+08:00[Asia/Shanghai]
                Shawn,2023-07-26T01:11+08:00[Asia/Shanghai]__2023-07-27T01:11+08:00[Asia/Shanghai]
                Jack,2023-07-27T01:11+08:00[Asia/Shanghai]__2023-07-27T11:10+08:00[Asia/Shanghai]
                Jane,2023-07-31T09:00+08:00[Asia/Shanghai]__2023-07-31T09:13+08:00[Asia/Shanghai]
                Jane,2023-07-31T21:20+08:00[Asia/Shanghai]__2023-08-01T01:11+08:00[Asia/Shanghai]
                Shawn,2023-08-01T01:11+08:00[Asia/Shanghai]__2023-08-02T01:11+08:00[Asia/Shanghai]
                Jack,2023-08-02T01:11+08:00[Asia/Shanghai]__2023-08-03T01:11+08:00[Asia/Shanghai]
                Jane,2023-08-03T01:11+08:00[Asia/Shanghai]__2023-08-03T11:10+08:00[Asia/Shanghai]
                Shawn,2023-08-07T09:00+08:00[Asia/Shanghai]__2023-08-07T09:13+08:00[Asia/Shanghai]
                Shawn,2023-08-07T21:20+08:00[Asia/Shanghai]__2023-08-08T01:11+08:00[Asia/Shanghai]
                Jack,2023-08-08T01:11+08:00[Asia/Shanghai]__2023-08-09T01:11+08:00[Asia/Shanghai]
                """;
        ScheduleTestHelper.validateFinalSchedule(scheduleLayerSchedule, exceptStr);

    }
}
