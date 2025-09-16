package com.apple.dhauli.agent.exchange.dao.agent;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record AgentProvider(
        String organization,
        String url,
        String team,
        String user ) {
    public static class Builder {
        private String organization;
        private String url;
        private String team;
        private String user;

        public Builder organization(String organization) {
            this.organization = organization;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder team(String team) {
            this.team = team;
            return this;
        }

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public AgentProvider build() {
            if (organization == null || organization.isEmpty() || url == null || url.isEmpty()) {
                throw new IllegalArgumentException("Both organization and url are required");
            }
            return new AgentProvider(organization, url, team, user);
        }
    }
}
