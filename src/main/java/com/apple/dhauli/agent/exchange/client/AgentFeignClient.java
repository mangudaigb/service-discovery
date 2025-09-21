package com.apple.dhauli.agent.exchange.client;

import com.apple.dhauli.agent.exchange.dao.agent.AgentCard;
import feign.RequestLine;

public interface AgentFeignClient {
    @RequestLine("GET /.well-known/agent.json")
    AgentCard getAgentCard();
}
