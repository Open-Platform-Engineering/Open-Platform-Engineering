package codes.showme.domain.platform;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class ServiceMaintenanceWindows {
    @JsonProperty("disabled")
    private boolean disabled = false;

    @JsonProperty("desc")
    private String description;

    @JsonProperty("time_zone")
    private String timeZone;

    @JsonProperty("start")
    private Date start;

    @JsonProperty("end")
    private Date end;









}
