package codes.showme.domain.escalation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ComparisonChain;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EscalationUserTarget implements EscalationTarget, Serializable {

    private static final long serialVersionUID = -29892232161170759L;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("display_name")
    private String displayName;

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public void setUser(long userId, String displayName) {
        this.userId = userId;
        this.displayName =  displayName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
