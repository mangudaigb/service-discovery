package com.apple.dhauli.agent.exchange.dao.agent;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public record AgentCardSignature(
        @JsonProperty("protected") String jwsHeader,
        String signature,
        Map<String, Object> header ) {
}
