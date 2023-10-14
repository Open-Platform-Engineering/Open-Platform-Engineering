package codes.showme.domain.schedule;

import java.io.Serializable;
import java.time.temporal.ChronoUnit;

public class TimeRangeShift implements Serializable {

    private static final long serialVersionUID = -1562511126072718965L;

    private int value;
    private ChronoUnit unit;

    public static TimeRangeShift of(int shiftLengthValue, ChronoUnit shiftLengthUnit) {
        TimeRangeShift timeRageShift = new TimeRangeShift();
        timeRageShift.setUnit(shiftLengthUnit);
        timeRageShift.setValue(shiftLengthValue);
        return timeRageShift;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public ChronoUnit getUnit() {
        return unit;
    }

    public void setUnit(ChronoUnit unit) {
        this.unit = unit;
    }
}
