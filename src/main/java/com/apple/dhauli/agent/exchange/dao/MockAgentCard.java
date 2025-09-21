package com.apple.dhauli.agent.exchange.dao;

import com.apple.dhauli.agent.exchange.dao.agent.AgentCapability;
import com.apple.dhauli.agent.exchange.dao.agent.AgentCard;
import com.apple.dhauli.agent.exchange.dao.agent.AgentSkill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class MockAgentCard {

    public static AgentCard get() {
        AgentCapability agentCapability = new AgentCapability.Builder().streaming(true).build();

        AgentSkill skill1 = new AgentSkill.Builder()
                .name("Mock Convert english to odia")
                .description("This agent converts english text to odia text based on odia literature of the 19th century")
                .exampleList(List.of(new String[]{"example one", "example two"}))
                .tagList(Collections.singletonList("test"))
                .build();

        AgentCard.Builder agentCardBuilder = new AgentCard.Builder();
        AgentCard agentCard = agentCardBuilder
                .withProtocolVersion("1.0")
                .withName("Mock Agent Card")
                .withId(UUID.randomUUID().toString())
                .withDescription("This is the description of a mock agent card.")
                .withUrl("http://mock-agent-card.com/")
                .withVersion("1.0")
                .withAgentCapability(agentCapability)
                .withAgentSkillList(List.of(skill1))
                .build();
        return agentCard;
    }
}
