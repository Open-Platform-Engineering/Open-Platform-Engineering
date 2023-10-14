package codes.showme.domain.schedule;

import codes.showme.domain.notification.NotifiedUser;
import codes.showme.domain.notification.NotifiedUserExample;
import com.google.common.collect.Lists;
import com.google.common.collect.TreeRangeMap;
import org.junit.Before;
import org.junit.Test;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CustomRotationTest {


    public static ZoneId zoneId = ZoneId.of("Asia/Shanghai");

    private ScheduleLayer scheduleLayer;

    private ArrayList<NotifiedUser> notifiedUsers = new ArrayList<>();
    private NotifiedUserExample jack = new NotifiedUserExample("Jack");
    private NotifiedUserExample jane = new NotifiedUserExample("Jane");
    private NotifiedUserExample shawn = new NotifiedUserExample("Shawn");

    private NotifiedUserExample nike = new NotifiedUserExample("Nike");
    private NotifiedUserExample ben = new NotifiedUserExample("Ben");

    private NotifiedUserExample musk = new NotifiedUserExample("Musk");
    private NotifiedUserExample luke = new NotifiedUserExample("Luke");

    @Before
    public void setUp() throws Exception {
        scheduleLayer = new ScheduleLayer();
        notifiedUsers.add(jack);
        notifiedUsers.add(jane);
        notifiedUsers.add(shawn);
        notifiedUsers.add(musk);
        scheduleLayer.setNotifiedUsers(notifiedUsers);
    }

    @Test
    public void dailyRestrictionTest() {
        ScheduleLayer scheduleLayer1 = new ScheduleLayer();
        ArrayList<NotifiedUser> notifiedUsers = new ArrayList<>();
        notifiedUsers.add(jack);
        notifiedUsers.add(jane);
        notifiedUsers.add(shawn);
        CustomRotation rotation = new CustomRotation();
        rotation.setShiftLengthValue(24);
        rotation.setShiftLengthUnit(ChronoUnit.HOURS);
        rotation.setHandoffDate(LocalDate.of(2023, 7, 10));
        rotation.setHandoffTime(LocalTime.of(10, 10));

        DailyRestriction dailyRestriction = new DailyRestriction();
        dailyRestriction.setFrom(LocalTime.of(9, 10));
        dailyRestriction.setTo(LocalTime.of(7, 18));
        rotation.setRestrictions(Arrays.asList(dailyRestriction));

        scheduleLayer1.setNotifiedUsers(notifiedUsers);
        scheduleLayer1.setRotation(rotation);
        scheduleLayer1.setStartDate(LocalDate.of(2023, 7, 10));

        TreeRangeMap<ZonedDateTime, NotifiedUser> schedule = scheduleLayer1.getSchedule(scheduleLayer1.getStartTime(zoneId).plus(1, ChronoUnit.WEEKS), zoneId);

        String exceptedStr = """
                Jack,2023-07-10T10:10+08:00[Asia/Shanghai]__2023-07-10T22:10+08:00[Asia/Shanghai]
                Jack,2023-07-10T22:10+08:00[Asia/Shanghai]__2023-07-11T07:18+08:00[Asia/Shanghai]
                Jane,2023-07-11T09:10+08:00[Asia/Shanghai]__2023-07-11T10:10+08:00[Asia/Shanghai]
                Jane,2023-07-11T10:10+08:00[Asia/Shanghai]__2023-07-11T22:10+08:00[Asia/Shanghai]
                Jane,2023-07-11T22:10+08:00[Asia/Shanghai]__2023-07-12T07:18+08:00[Asia/Shanghai]
                Shawn,2023-07-12T09:10+08:00[Asia/Shanghai]__2023-07-12T10:10+08:00[Asia/Shanghai]
                Shawn,2023-07-12T10:10+08:00[Asia/Shanghai]__2023-07-12T22:10+08:00[Asia/Shanghai]
                Shawn,2023-07-12T22:10+08:00[Asia/Shanghai]__2023-07-13T07:18+08:00[Asia/Shanghai]
                Jack,2023-07-13T09:10+08:00[Asia/Shanghai]__2023-07-13T10:10+08:00[Asia/Shanghai]
                Jack,2023-07-13T10:10+08:00[Asia/Shanghai]__2023-07-13T22:10+08:00[Asia/Shanghai]
                Jack,2023-07-13T22:10+08:00[Asia/Shanghai]__2023-07-14T07:18+08:00[Asia/Shanghai]
                Jane,2023-07-14T09:10+08:00[Asia/Shanghai]__2023-07-14T10:10+08:00[Asia/Shanghai]
                Jane,2023-07-14T10:10+08:00[Asia/Shanghai]__2023-07-14T22:10+08:00[Asia/Shanghai]
                Jane,2023-07-14T22:10+08:00[Asia/Shanghai]__2023-07-15T07:18+08:00[Asia/Shanghai]
                Shawn,2023-07-15T09:10+08:00[Asia/Shanghai]__2023-07-15T10:10+08:00[Asia/Shanghai]
                Shawn,2023-07-15T10:10+08:00[Asia/Shanghai]__2023-07-15T22:10+08:00[Asia/Shanghai]
                Shawn,2023-07-15T22:10+08:00[Asia/Shanghai]__2023-07-16T07:18+08:00[Asia/Shanghai]
                Jack,2023-07-16T09:10+08:00[Asia/Shanghai]__2023-07-16T10:10+08:00[Asia/Shanghai]
                Jack,2023-07-16T10:10+08:00[Asia/Shanghai]__2023-07-16T22:10+08:00[Asia/Shanghai]
                Jack,2023-07-16T22:10+08:00[Asia/Shanghai]__2023-07-17T07:18+08:00[Asia/Shanghai]
                """;

        ScheduleTestHelper.validateFinalSchedule(schedule, exceptedStr);

    }

    @Test
    public void getRangeTo() {

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
        ScheduleLayer scheduleLayer2 = new ScheduleLayer();
        ArrayList<NotifiedUser> notifiedUsers2 = new ArrayList<>();
        notifiedUsers2.add(nike);
        notifiedUsers2.add(ben);
        scheduleLayer2.setNotifiedUsers(notifiedUsers2);
        scheduleLayer2.setRotation(rotation2);
        scheduleLayer2.setStartDate(LocalDate.of(2023, 7, 12));


        ScheduleLayer scheduleLayer3 = new ScheduleLayer();
        ArrayList<NotifiedUser> notifiedUsers3 = new ArrayList<>();
        notifiedUsers3.add(musk);
        notifiedUsers3.add(luke);
        scheduleLayer3.setNotifiedUsers(notifiedUsers3);
        CustomRotation customRotation = new CustomRotation();
        WeeklyRestriction weeklyRestriction = new WeeklyRestriction();
        weeklyRestriction.setWeekFrom(DayOfWeek.FRIDAY);
        weeklyRestriction.setWeekTo(DayOfWeek.SUNDAY);
        weeklyRestriction.setTimeFrom(LocalTime.of(9, 0));
        weeklyRestriction.setTimeTo(LocalTime.of(23, 59));
        List<Restriction> restrictionList = Lists.newArrayList();
        restrictionList.add(weeklyRestriction);
        customRotation.setRestrictions(restrictionList);
        customRotation.setShiftLengthUnit(ChronoUnit.HOURS);
        customRotation.setShiftLengthValue(12);
        customRotation.setHandoffTime(LocalTime.of(8, 0));
        scheduleLayer3.setRotation(customRotation);
        scheduleLayer3.setStartDate(LocalDate.of(2023, 7, 10));

        ZonedDateTime startTime2 = scheduleLayer3.getStartTime(zoneId);
        ScheduleRule scheduleRule = new ScheduleRule();
        scheduleRule.addLayer(scheduleLayer1);
        scheduleRule.addLayer(scheduleLayer2);
//        scheduleRule.addLayer(scheduleLayer3);
        ZonedDateTime end = startTime2.plus(2, ChronoUnit.WEEKS);
        FinalSchedule finalSchedule = scheduleRule.calculateFinalSchedule(startTime2, end, zoneId);

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
                Nike,2023-07-19T01:11+08:00[Asia/Shanghai]__2023-07-20T01:11+08:00[Asia/Shanghai]
                Ben,2023-07-20T01:11+08:00[Asia/Shanghai]__2023-07-20T11:10+08:00[Asia/Shanghai]
                Jane,2023-07-20T11:10+08:00[Asia/Shanghai]__2023-07-20T12:00+08:00[Asia/Shanghai]
                Jane,2023-07-20T13:00+08:00[Asia/Shanghai]__2023-07-20T18:00+08:00[Asia/Shanghai]
                Jane,2023-07-21T09:00+08:00[Asia/Shanghai]__2023-07-21T10:11+08:00[Asia/Shanghai]
                Shawn,2023-07-21T10:11+08:00[Asia/Shanghai]__2023-07-21T12:00+08:00[Asia/Shanghai]
                Shawn,2023-07-21T13:00+08:00[Asia/Shanghai]__2023-07-21T18:00+08:00[Asia/Shanghai]
                Shawn,2023-07-22T09:00+08:00[Asia/Shanghai]__2023-07-22T10:11+08:00[Asia/Shanghai]
                Jack,2023-07-22T10:11+08:00[Asia/Shanghai]__2023-07-22T12:00+08:00[Asia/Shanghai]
                Jack,2023-07-22T13:00+08:00[Asia/Shanghai]__2023-07-22T18:00+08:00[Asia/Shanghai]
                Jack,2023-07-23T09:00+08:00[Asia/Shanghai]__2023-07-23T10:11+08:00[Asia/Shanghai]
                Jane,2023-07-23T10:11+08:00[Asia/Shanghai]__2023-07-23T12:00+08:00[Asia/Shanghai]
                Jane,2023-07-23T13:00+08:00[Asia/Shanghai]__2023-07-23T18:00+08:00[Asia/Shanghai]
                Nike,2023-07-24T02:20+08:00[Asia/Shanghai]__2023-07-24T08:00+08:00[Asia/Shanghai]
                """;

        ScheduleTestHelper.validateFinalSchedule(finalSchedule.getSchedule(), finalScheduleStr);


    }
}