package codes.showme.domain.schedule;

import com.google.common.collect.*;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

public class GuavaRangeTest {
    @Test
    public void rangeIntersectionTest() {
        ZonedDateTime start = ZonedDateTime.of(2023, 7, 12, 10, 11, 0, 0, ZoneId.systemDefault());
        ZonedDateTime end = ZonedDateTime.of(2023, 7, 19, 10, 11, 0, 0, ZoneId.systemDefault());
        Range<ZonedDateTime> aRange = Range.openClosed(start, end);

        Range<ZonedDateTime> bRange = Range.openClosed(start.minus(10, ChronoUnit.HOURS),
                end.minus(6, ChronoUnit.DAYS));

        Range<ZonedDateTime> insection = Range.openClosed(ZonedDateTime.of(2023, 7, 12, 10, 11, 0, 0, ZoneId.systemDefault()), ZonedDateTime.of(2023, 7, 13, 10, 11, 0, 0, ZoneId.systemDefault()));
        Assert.assertEquals(
                insection, aRange.intersection(bRange));
    }

    @Test
    public void rangeIntersectionTest1() {
        ZonedDateTime start = ZonedDateTime.of(2023, 7, 12, 10, 11, 0, 0, ZoneId.systemDefault());
        ZonedDateTime end = ZonedDateTime.of(2023, 7, 19, 10, 11, 0, 0, ZoneId.systemDefault());
        Range<ZonedDateTime> aRange = Range.openClosed(start, end);

        Range<ZonedDateTime> bRange = Range.openClosed(start.plus(1, ChronoUnit.DAYS),
                end.minus(5, ChronoUnit.DAYS));

        Range<ZonedDateTime> insection = Range.openClosed(ZonedDateTime.of(2023, 7, 13, 10, 11, 0, 0, ZoneId.systemDefault()), ZonedDateTime.of(2023, 7, 14, 10, 11, 0, 0, ZoneId.systemDefault()));
        Assert.assertEquals(
                insection, aRange.intersection(bRange));
    }

    @Test
    public void rangeIntersectionTest2() {
        ZonedDateTime start = ZonedDateTime.of(2023, 7, 12, 10, 11, 3, 0, ZoneId.systemDefault());
        ZonedDateTime end = ZonedDateTime.of(2023, 7, 19, 10, 11, 0, 0, ZoneId.systemDefault());
        Range<ZonedDateTime> aRange = Range.openClosed(start, end);

        Range<ZonedDateTime> bRange = Range.openClosed(start.minus(1, ChronoUnit.DAYS),
                end.minus(5, ChronoUnit.DAYS));

        Range<ZonedDateTime> insection = Range.openClosed(
                ZonedDateTime.of(2023, 7, 12, 10, 11, 3, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2023, 7, 14, 10, 11, 0, 0, ZoneId.systemDefault()));

        Assert.assertEquals(
                insection, aRange.intersection(bRange));
    }

    @Test
    public void rangeIntersectionTest3() {
        ZonedDateTime start = ZonedDateTime.of(2023, 7, 12, 10, 11, 3, 0, ZoneId.systemDefault());
        ZonedDateTime end = ZonedDateTime.of(2023, 7, 19, 10, 11, 0, 0, ZoneId.systemDefault());
        Range<ZonedDateTime> aRange = Range.openClosed(start, end);

        Range<ZonedDateTime> bRange = Range.openClosed(start.minus(1, ChronoUnit.DAYS),
                end.minus(5, ChronoUnit.DAYS));

        Range<ZonedDateTime> insection = Range.openClosed(
                ZonedDateTime.of(2023, 7, 12, 10, 11, 3, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2023, 7, 14, 10, 11, 0, 0, ZoneId.systemDefault()));

        Assert.assertEquals(insection, aRange.intersection(bRange));
    }

    @Test
    public void rangeIntersectionTest4() {

        List<String> users = Lists.newArrayList("a", "b", "c");

        ZonedDateTime start = ZonedDateTime.of(2023, 7, 12, 10, 11, 3, 0, ZoneId.systemDefault());
        ZonedDateTime end = start.plus(1, ChronoUnit.WEEKS).plus(1, ChronoUnit.MINUTES);
        Range<ZonedDateTime> aRange = Range.openClosed(start, end);

        RangeSet<ZonedDateTime> nonRestrictions = TreeRangeSet.create();
        int i = 0;
        TreeRangeMap<ZonedDateTime, String> map = TreeRangeMap.create();
        for (ZonedDateTime index = start; index.isBefore(end); index = index.plus(1, ChronoUnit.DAYS)) {
            Range<ZonedDateTime> key = Range.openClosed(index, index.plus(1, ChronoUnit.DAYS));
            nonRestrictions.add(key);
            map.put(key, users.get(i % users.size()));
            i++;
        }

        // restriction daily
        LocalTime restrictionAPoint = LocalTime.of(12, 9);
        LocalTime restrictionBPoint = LocalTime.of(22, 9);

        RangeSet<ZonedDateTime> restrictionRanges = TreeRangeSet.create();
        i = 0;
        for (ZonedDateTime index = start; index.isBefore(end); index = index.plus(1, ChronoUnit.DAYS)) {
            ZonedDateTime aPoint = restrictionAPoint.atDate(index.toLocalDate()).atZone(ZoneId.systemDefault());
            ZonedDateTime bPoint = restrictionBPoint.atDate(index.toLocalDate()).atZone(ZoneId.systemDefault());
            if (bPoint.isBefore(aPoint)) {
                bPoint = bPoint.plus(1, ChronoUnit.DAYS);
            }
            Range<ZonedDateTime> key = Range.openClosed(index, bPoint);
            restrictionRanges.add(
                    key
            );
            map.put(key, users.get(i % users.size()));
            i++;

        }
        String restrictionRangeStr = """
                a:(2023-07-12T10:11:03Z[UTC]..2023-07-12T22:09Z[UTC]]
                a:(2023-07-12T22:09Z[UTC]..2023-07-13T10:11:03Z[UTC]]
                b:(2023-07-13T10:11:03Z[UTC]..2023-07-13T22:09Z[UTC]]
                b:(2023-07-13T22:09Z[UTC]..2023-07-14T10:11:03Z[UTC]]
                c:(2023-07-14T10:11:03Z[UTC]..2023-07-14T22:09Z[UTC]]
                c:(2023-07-14T22:09Z[UTC]..2023-07-15T10:11:03Z[UTC]]
                a:(2023-07-15T10:11:03Z[UTC]..2023-07-15T22:09Z[UTC]]
                a:(2023-07-15T22:09Z[UTC]..2023-07-16T10:11:03Z[UTC]]
                b:(2023-07-16T10:11:03Z[UTC]..2023-07-16T22:09Z[UTC]]
                b:(2023-07-16T22:09Z[UTC]..2023-07-17T10:11:03Z[UTC]]
                c:(2023-07-17T10:11:03Z[UTC]..2023-07-17T22:09Z[UTC]]
                c:(2023-07-17T22:09Z[UTC]..2023-07-18T10:11:03Z[UTC]]
                a:(2023-07-18T10:11:03Z[UTC]..2023-07-18T22:09Z[UTC]]
                a:(2023-07-18T22:09Z[UTC]..2023-07-19T10:11:03Z[UTC]]
                b:(2023-07-19T10:11:03Z[UTC]..2023-07-19T22:09Z[UTC]]
                b:(2023-07-19T22:09Z[UTC]..2023-07-20T10:11:03Z[UTC]]
                """;
        for (Map.Entry<Range<ZonedDateTime>, String> entry : map.asMapOfRanges().entrySet()) {
            System.out.println(entry.getValue() + ":" + entry.getKey());
        }


    }

    @Test
    public void rangeTest() {
        ZonedDateTime start = ZonedDateTime.of(2023, 7, 12, 10, 11, 0, 0, ZoneId.systemDefault());
        System.out.println(start.plus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS));
        System.out.println(start.range(ChronoField.HOUR_OF_DAY));
    }
}
