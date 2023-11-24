package codes.showme.server.schedule;

import codes.showme.domain.schedule.ScheduleRule;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.ZoneId;
import java.util.TimeZone;

public class CreateReq {
    @Size(min = 2)
    @Size(max = ScheduleRule.COLUMN_NAME_LENGTH)
    private String name;
    @Size(min = 2)
    @Size(max = ScheduleRule.COLUMN_DESCRIPTION_LENGTH)
    private String description;


    @NotNull
    @NotEmpty
    private String zoneId;

    public ScheduleRule convertToScheduleRule() {
        ScheduleRule result = new ScheduleRule();
        result.setName(name);
        result.setDescription(description);
        result.setZoneId(ZoneId.of(zoneId));
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }
}
