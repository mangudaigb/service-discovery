package com.apple.dhauli.agent.exchange.dao.agent;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record AgentCapability(
        Boolean streaming,
        Boolean pushNotification,
        Boolean stateTransitionHistory,
        List<AgentExtension> agentExtension ){

    public AgentCapability {
        if (streaming != null) {
            streaming = false;
        }
        if (pushNotification != null) {
            pushNotification = false;
        }
        if (stateTransitionHistory != null) {
            stateTransitionHistory = false;
        }
    }

    public static class Builder {
        private Boolean streaming;
        private Boolean pushNotification;
        private Boolean stateTransitionHistory;
        private List<AgentExtension> agentExtensionList;

        public Builder streaming(Boolean streaming) {
            this.streaming = streaming;
            return this;
        }

        public Builder pushNotification(Boolean pushNotification) {
            this.pushNotification = pushNotification;
            return this;
        }

        public Builder stateTransitionHistory(Boolean stateTransitionHistory) {
            this.stateTransitionHistory = stateTransitionHistory;
            return this;
        }

        public Builder agentExtensionList(List<AgentExtension> agentExtensionList) {
            this.agentExtensionList = agentExtensionList;
            return this;
        }

        public AgentCapability build() {
            if (agentExtensionList == null) {
                this.agentExtensionList = new ArrayList<>();
            }
            return new AgentCapability(streaming, pushNotification, stateTransitionHistory, agentExtensionList);
        }
    }
}
