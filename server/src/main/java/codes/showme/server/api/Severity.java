package codes.showme.server.api;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Severity {
    CRITICAL("critical"),
    ERROR("error"),
    WARNING("warning"),
    INFO("info");

    private final String severity;

    Severity(String severity) {
        this.severity = severity;
    }

    @JsonValue
    public String getSeverity() {
        return severity;
    }

    @Override
    public String toString() {
        return getSeverity();
    }
}
