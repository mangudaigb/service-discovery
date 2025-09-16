package com.apple.dhauli.agent.exchange.dao.instance;

import java.util.ArrayList;
import java.util.List;

public class AgentInstance {
    private List<Instance> agentInstanceList = new ArrayList<Instance>();

    public List<Instance> getAgentInstanceList() {
        return agentInstanceList;
    }

    public void setAgentInstanceList(List<Instance> agentInstanceList) {
        this.agentInstanceList = agentInstanceList;
    }
}
