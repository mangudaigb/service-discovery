package com.apple.dhauli.agent.exchange.dao.agent;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record AgentInterface(TransportProtocol protocol, String url) {
    public static class Builder {
        private TransportProtocol protocol;
        private String url;

        public AgentInterface build() {
            return new AgentInterface(protocol, url);
        }
    }
}
