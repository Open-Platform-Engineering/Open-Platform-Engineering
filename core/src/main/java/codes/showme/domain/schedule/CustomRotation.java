package codes.showme.domain.schedule;

import codes.showme.domain.notification.NotifiedUser;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.collect.TreeRangeMap;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * https://support.pagerduty.com/docs/schedule-examples#ex-3-configuration
 */
public class CustomRotation implements RotationType, Serializable {

    private static final long serialVersionUID = -3014720994413732818L;

    @JsonProperty("shift_length_value")
    private int shiftLengthValue;
    @JsonProperty("shift_length_unit")
    private ChronoUnit shiftLengthUnit = ChronoUnit.HOURS;

    @JsonProperty("handoff_date")
    private LocalDate handoffDate;

    @JsonProperty("handoff_time")
    private LocalTime handoffTime;

    @JsonProperty("restrictions")
    private List<Restriction> restrictions = Lists.newArrayList();

    public int getShiftLengthValue() {
        return shiftLengthValue;
    }

    public void setShiftLengthValue(int shiftLengthValue) {
        this.shiftLengthValue = shiftLengthValue;
    }

    public ChronoUnit getShiftLengthUnit() {
        return shiftLengthUnit;
    }

    public void setShiftLengthUnit(ChronoUnit shiftLengthUnit) {
        this.shiftLengthUnit = shiftLengthUnit;
    }

    @Override
    public boolean hasRestrictions() {
        return !restrictions.isEmpty();
    }

    public LocalDate getHandoffDate() {
        return handoffDate;
    }

    public void setHandoffDate(LocalDate handoffDate) {
        this.handoffDate = handoffDate;
    }

    public void setHandoffTime(LocalTime handoffTime) {
        this.handoffTime = handoffTime;
    }

    public List<Restriction> getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(List<Restriction> restrictions) {
        this.restrictions = restrictions;
    }

    @Override
    public TreeRangeMap<ZonedDateTime, NotifiedUser> getRangeTo(List<NotifiedUser> notifiedUsers,
                                                                LocalDate startDate, ZonedDateTime endTime, ZoneId zoneId) {

        ZonedDateTime startTime = startDate.atTime(getHandoffTime()).atZone(zoneId);

        if (!hasRestrictions()) {
            TreeRangeMap<ZonedDateTime, NotifiedUser> result = TreeRangeMap.create();
            int userIndex = 0;
            // 计算不存在restriction
            for (ZonedDateTime startIndex = startTime;
                 startIndex.isBefore(endTime);
                 startIndex = startIndex.plus(getShiftLengthValue(), getShiftLengthUnit())) {

                ZonedDateTime endIndex = startIndex.plus(getShiftLengthValue(), getShiftLengthUnit());

                Range<ZonedDateTime> range = Range.openClosed(startIndex, endIndex);

                result.put(range, notifiedUsers.get(userIndex % notifiedUsers.size()));
                userIndex++;
            }
            return result;
        }

        TreeRangeMap<ZonedDateTime, NotifiedUser> result = TreeRangeMap.create();
        for (Restriction restriction : restrictions) {
            result.putAll(restriction.generateRangesFrom(Range.openClosed(startTime, endTime), zoneId, TimeRangeShift.of(getShiftLengthValue(), getShiftLengthUnit()), notifiedUsers));
        }
        return result;


    }

    @Override
    public LocalTime getHandoffTime() {
        return handoffTime;
    }

    @Override
    public List<Range<ZonedDateTime>> getFullRangesWithoutRestriction(LocalDate startDate, ZonedDateTime endTime, ZoneId zoneId) {
        return null;
    }

}
