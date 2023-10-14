package codes.showme.domain.escalation;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = EscalationUserTarget.class, name = "user"),
        @JsonSubTypes.Type(value = EscalationScheduleTarget.class, name = "schedule") })
public interface EscalationTarget {

    String getDisplayName();

}
