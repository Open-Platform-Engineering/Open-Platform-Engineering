package codes.showme.domain.schedule;

import codes.showme.domain.notification.NotifiedUser;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeMap;
import org.junit.Assert;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScheduleTestHelper {

    public static final String TIMERANGE_SEPARATOR = "__";
    public static final String USER_SEPARATOR = ",";

    public static String print(List<Range<ZonedDateTime>> rangeList) {
        String result = "";
        for (Range<ZonedDateTime> range : rangeList) {
            result += range.lowerEndpoint() + TIMERANGE_SEPARATOR + range.upperEndpoint();
            result += System.getProperty("line.separator");
        }
        return result;
    }

    public static String print(RangeSet<ZonedDateTime> rangeSet) {
        return print(Lists.newArrayList(rangeSet.asRanges()));
    }

    public static String print(TreeRangeMap<ZonedDateTime, NotifiedUser> layerSchedule) {
        String result = "";
        for (Map.Entry<Range<ZonedDateTime>, NotifiedUser> entry : layerSchedule.asMapOfRanges().entrySet()) {
            result += entry.getValue().getDisplayName()
                    + USER_SEPARATOR + entry.getKey().lowerEndpoint() + TIMERANGE_SEPARATOR + entry.getKey().upperEndpoint();
            result += System.getProperty("line.separator");
        }
        return result;
    }

    public static void validateFinalSchedule(RangeSet<ZonedDateTime> rangeSet, String exceptStr) {
        String[] exceptedList = exceptStr.split(System.getProperty("line.separator"));
        ArrayList<Range<ZonedDateTime>> ranges = Lists.newArrayList(rangeSet.asRanges());
        validateFinalSchedule(ranges, exceptStr);
    }

    public static void validateFinalSchedule(TreeRangeMap<ZonedDateTime, NotifiedUser> layerSchedule, String exceptStr) {
        String[] exceptedList = exceptStr.split(System.getProperty("line.separator"));

        ArrayList<Map.Entry<Range<ZonedDateTime>, NotifiedUser>> list = Lists.newArrayList(layerSchedule.asMapOfRanges().entrySet());
        for (int i = 0; i < list.size(); i++) {
            Map.Entry<Range<ZonedDateTime>, NotifiedUser> entry = list.get(i);
            String excepted = exceptedList[i];
            String[] userTimeRange = excepted.split(USER_SEPARATOR);
            String user = userTimeRange[0];
            String timeRange = userTimeRange[1];
            String[] lowerAndUpper = timeRange.split(TIMERANGE_SEPARATOR);
            ZonedDateTime lower = ZonedDateTime.parse(lowerAndUpper[0]);
            ZonedDateTime upper = ZonedDateTime.parse(lowerAndUpper[1]);
            Assert.assertEquals(
                    user, entry.getValue().getDisplayName()
            );
            Assert.assertEquals(lower, entry.getKey().lowerEndpoint());
            Assert.assertEquals(upper, entry.getKey().upperEndpoint());
        }
    }

    public static void validateFinalSchedule(List<Range<ZonedDateTime>> ranges, String exceptStr) {
        String[] exceptedList = exceptStr.split(System.getProperty("line.separator"));
        for (int i = 0; i < ranges.size(); i++) {
            String str = exceptedList[i];
            Range<ZonedDateTime> timeRange = ranges.get(i);
            String[] lowerAndUpper = str.split(TIMERANGE_SEPARATOR);
            ZonedDateTime lower = ZonedDateTime.parse(lowerAndUpper[0]);
            ZonedDateTime upper = ZonedDateTime.parse(lowerAndUpper[1]);
            Assert.assertEquals(lower, timeRange.lowerEndpoint());
            Assert.assertEquals(upper, timeRange.upperEndpoint());
        }
    }
}
