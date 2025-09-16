package com.apple.dhauli.agent.exchange.dao.agent;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AgentType {
    REFLUX("REFLUX"), PLANNER("PLANNER");

    private final String value;

    AgentType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static AgentType fromValue(String v) {
        return valueOf(v);
    }
}
