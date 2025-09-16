package com.apple.dhauli.agent.exchange.dao.agent;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TransportProtocol {
    JSON_RPC("JSON_RPC"), GRPC("GRPC"), HTTP_JSON("HTTP_JSON"), WS_JSON("WS_JSON"), EVENT_JSON("EVENT_JSON");

    private final String protocol;

    TransportProtocol(String protocol) {
        this.protocol = protocol;
    }

    @JsonValue
    public String getProtocol() {
        return protocol;
    }

    public static TransportProtocol fromProtocol(String protocol) {
        return switch (protocol) {
            case "JSON_RPC" -> JSON_RPC;
            case "GRPC" -> GRPC;
            case "HTTP_JSON" -> HTTP_JSON;
            case "WS_JSON" -> WS_JSON;
            case "EVENT_JSON" -> EVENT_JSON;
            default -> throw new IllegalArgumentException("Unknown protocol: " + protocol);
        };
    }
}
