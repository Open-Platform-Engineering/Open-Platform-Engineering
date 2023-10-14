package codes.showme.domain.schedule;

import codes.showme.domain.notification.NotifiedUser;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Range;
import com.google.common.collect.TreeRangeMap;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public class ScheduleLayers implements Serializable {

    private static final long serialVersionUID = -3657424936501401266L;

    @JsonProperty("list")
    private List<ScheduleLayer> list = new ArrayList<>();

    public FinalSchedule calculateFinalSchedule(ZonedDateTime from, ZonedDateTime to, ZoneId zoneId) {
        if (from.isAfter(to) || list.isEmpty()) {
            return new FinalSchedule();
        }

        FinalSchedule finalSchedule = new FinalSchedule();
        TreeRangeMap<ZonedDateTime, NotifiedUser> schedule = TreeRangeMap.create();
        for (ScheduleLayer scheduleLayer : list) {
            for (Map.Entry<Range<ZonedDateTime>, NotifiedUser> entry :
                    scheduleLayer.getSchedule(to, zoneId).asMapOfRanges().entrySet()) {
                schedule.put(entry.getKey(), entry.getValue());
            }
        }
        finalSchedule.setSchedule(schedule);
        return finalSchedule;
    }

    public int size() {
        return list.size();
    }

    public List<ScheduleLayer> getList() {
        return Collections.unmodifiableList(list);
    }

    public ScheduleLayers add(ScheduleLayer scheduleLayer) {
        list.add(scheduleLayer);
        return this;
    }

}
