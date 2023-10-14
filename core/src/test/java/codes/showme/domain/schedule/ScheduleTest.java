package codes.showme.domain.schedule;

import codes.showme.domain.notification.NotifiedUser;
import codes.showme.domain.notification.NotifiedUserExample;
import org.junit.Test;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class ScheduleTest {

    public static ZoneId zoneId = ZoneId.of("Asia/Shanghai");

    private NotifiedUserExample jack = new NotifiedUserExample("Jack");
    private NotifiedUserExample jane = new NotifiedUserExample("Jane");
    private NotifiedUserExample shawn = new NotifiedUserExample("Shawn");
    private NotifiedUserExample nike = new NotifiedUserExample("Nike");
    private NotifiedUserExample ben = new NotifiedUserExample("Ben");

    @Test
    public void testCalculateFinalSchedule() {
        ScheduleLayer scheduleLayer1 = new ScheduleLayer();
        ArrayList<NotifiedUser> notifiedUsers = new ArrayList<>();
        notifiedUsers.add(jack);
        notifiedUsers.add(jane);
        notifiedUsers.add(shawn);
        scheduleLayer1.setNotifiedUsers(notifiedUsers);
        DailyRotation rotation = new DailyRotation();
        rotation.addDailyRestriction(LocalTime.of(9, 0), LocalTime.of(12, 0));
        rotation.addDailyRestriction(LocalTime.of(13, 0), LocalTime.of(18, 0));
        rotation.setHandoffTime(10, 11);
        scheduleLayer1.setRotation(rotation);
        scheduleLayer1.setStartDate(LocalDate.of(2023, 7, 10));

        ScheduleLayer scheduleLayer2 = new ScheduleLayer();
        ArrayList<NotifiedUser> notifiedUsers2 = new ArrayList<>();
        notifiedUsers2.add(nike);
        notifiedUsers2.add(ben);
        scheduleLayer2.setNotifiedUsers(notifiedUsers2);
        WeeklyRestriction weeklyRestriction0 = new WeeklyRestriction();
        weeklyRestriction0.from(DayOfWeek.MONDAY, LocalTime.of(9, 0))
                .to(DayOfWeek.MONDAY, LocalTime.of(9, 13));
        WeeklyRestriction weeklyRestriction1 = new WeeklyRestriction();
        weeklyRestriction1.from(DayOfWeek.MONDAY, LocalTime.of(2, 20))
                .to(DayOfWeek.THURSDAY, LocalTime.of(11, 10));

        DailyRotation rotation2 = new DailyRotation();
        rotation2.setHandoffTime(10, 10);
        rotation2.addWeeklyRestriction(weeklyRestriction0);
        rotation2.addWeeklyRestriction(weeklyRestriction1);
        rotation2.setHandoffTime(1, 11);
        scheduleLayer2.setRotation(rotation2);
        scheduleLayer2.setStartDate(LocalDate.of(2023, 7, 12));

        ZonedDateTime startTime2 = scheduleLayer2.getStartTime(zoneId);
        ScheduleRule scheduleRule = new ScheduleRule();
        scheduleRule.addLayer(scheduleLayer1);
        scheduleRule.addLayer(scheduleLayer2);
        ZonedDateTime end = startTime2.plus(1, ChronoUnit.WEEKS);
        FinalSchedule finalSchedule = scheduleRule.getLayers().calculateFinalSchedule(startTime2, end, zoneId);

        System.out.println(ScheduleTestHelper.print(finalSchedule.getSchedule()));

        String finalScheduleStr = """
                Jack,2023-07-10T10:11+08:00[Asia/Shanghai]__2023-07-10T12:00+08:00[Asia/Shanghai]
                Jack,2023-07-10T13:00+08:00[Asia/Shanghai]__2023-07-10T18:00+08:00[Asia/Shanghai]
                Jack,2023-07-11T09:00+08:00[Asia/Shanghai]__2023-07-11T10:11+08:00[Asia/Shanghai]
                Jane,2023-07-11T10:11+08:00[Asia/Shanghai]__2023-07-11T12:00+08:00[Asia/Shanghai]
                Jane,2023-07-11T13:00+08:00[Asia/Shanghai]__2023-07-11T18:00+08:00[Asia/Shanghai]
                Nike,2023-07-12T01:11+08:00[Asia/Shanghai]__2023-07-13T01:11+08:00[Asia/Shanghai]
                Ben,2023-07-13T01:11+08:00[Asia/Shanghai]__2023-07-13T11:10+08:00[Asia/Shanghai]
                Jack,2023-07-13T11:10+08:00[Asia/Shanghai]__2023-07-13T12:00+08:00[Asia/Shanghai]
                Jack,2023-07-13T13:00+08:00[Asia/Shanghai]__2023-07-13T18:00+08:00[Asia/Shanghai]
                Jack,2023-07-14T09:00+08:00[Asia/Shanghai]__2023-07-14T10:11+08:00[Asia/Shanghai]
                Jane,2023-07-14T10:11+08:00[Asia/Shanghai]__2023-07-14T12:00+08:00[Asia/Shanghai]
                Jane,2023-07-14T13:00+08:00[Asia/Shanghai]__2023-07-14T18:00+08:00[Asia/Shanghai]
                Jane,2023-07-15T09:00+08:00[Asia/Shanghai]__2023-07-15T10:11+08:00[Asia/Shanghai]
                Shawn,2023-07-15T10:11+08:00[Asia/Shanghai]__2023-07-15T12:00+08:00[Asia/Shanghai]
                Shawn,2023-07-15T13:00+08:00[Asia/Shanghai]__2023-07-15T18:00+08:00[Asia/Shanghai]
                Shawn,2023-07-16T09:00+08:00[Asia/Shanghai]__2023-07-16T10:11+08:00[Asia/Shanghai]
                Jack,2023-07-16T10:11+08:00[Asia/Shanghai]__2023-07-16T12:00+08:00[Asia/Shanghai]
                Jack,2023-07-16T13:00+08:00[Asia/Shanghai]__2023-07-16T18:00+08:00[Asia/Shanghai]
                Nike,2023-07-17T02:20+08:00[Asia/Shanghai]__2023-07-18T01:11+08:00[Asia/Shanghai]
                Ben,2023-07-18T01:11+08:00[Asia/Shanghai]__2023-07-19T01:11+08:00[Asia/Shanghai]
                    """;

        ScheduleTestHelper.validateFinalSchedule(finalSchedule.getSchedule(), finalScheduleStr);


    }
}
