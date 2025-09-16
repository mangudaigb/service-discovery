package com.apple.dhauli.agent.exchange.dao.agent;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AuthenticationMethod {
    AUTHENTICATION_UNSPECIFIED("AUTHENTICATION_UNSPECIFIED"), OAUTH("OAUTH"), API_KEY("API_KEY"), MTLS("MTLS");

    private final String authMethod;

    private AuthenticationMethod(String authMethod) {
        this.authMethod = authMethod;
    }

    @JsonValue
    public String getAuthMethod() {
        return authMethod;
    }

    public static AuthenticationMethod fromAuthMethod(String authMethod) {
        return switch (authMethod) {
            case "AUTHENTICATION_UNSPECIFIED" -> AUTHENTICATION_UNSPECIFIED;
            case "OAUTH" -> OAUTH;
            case "API_KEY" -> API_KEY;
            case "MTLS" -> MTLS;
            default -> throw new IllegalArgumentException("Invalid authentication method: " + authMethod);
        };
    }
}
