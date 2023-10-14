package codes.showme.domain.schedule;

import codes.showme.domain.notification.NotifiedUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.TreeRangeMap;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class FinalSchedule implements Serializable {

    private static final long serialVersionUID = -3192665122870923733L;
    private TreeRangeMap<ZonedDateTime, NotifiedUser> schedule;

    public void setSchedule(TreeRangeMap<ZonedDateTime, NotifiedUser> schedule) {
        this.schedule = schedule;
    }

    @JsonIgnore
    public TreeRangeMap<ZonedDateTime, NotifiedUser> getSchedule() {
        return schedule;
    }
}
