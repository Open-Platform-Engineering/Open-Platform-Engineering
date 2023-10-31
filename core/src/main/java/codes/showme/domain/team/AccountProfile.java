package codes.showme.domain.team;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountProfile {

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("schedule_color")
    private String scheduleColor;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getScheduleColor() {
        return scheduleColor;
    }

    public void setScheduleColor(String scheduleColor) {
        this.scheduleColor = scheduleColor;
    }
}
