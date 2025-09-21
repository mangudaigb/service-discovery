package com.apple.dhauli.agent.exchange.dao.instance;

import com.apple.dhauli.agent.exchange.dao.agent.AgentCard;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgentInstance {
    @JsonProperty("name")
    private String agentName;
    @JsonProperty("card")
    private AgentCard agentCard;
    @JsonProperty("instances")
    private List<Instance> agentInstanceList = new ArrayList<>();

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public AgentCard getAgentCard() {
        return agentCard;
    }

    public void setAgentCard(AgentCard agentCard) {
        this.agentCard = agentCard;
    }

    public List<Instance> getAgentInstanceList() {
        return agentInstanceList;
    }

    public void setAgentInstanceList(List<Instance> agentInstanceList) {
        this.agentInstanceList = agentInstanceList;
    }
}
