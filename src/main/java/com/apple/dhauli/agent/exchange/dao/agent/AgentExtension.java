package com.apple.dhauli.agent.exchange.dao.agent;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record AgentExtension(
        String url,
        String description,
        Boolean required,
        Map<String, Object> params) {

    public static class Builder {
        private String url;
        private String description;
        private Boolean required;
        private Map<String, Object> params;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder required(Boolean required) {
            this.required = required;
            return this;
        }

        public Builder params(Map<String, Object> params) {
            this.params = params;
            return this;
        }

        public AgentExtension build() {
            if (url == null || url.isEmpty()) {
                throw new IllegalArgumentException("url cannot be null or empty");
            }
            if (description == null || description.isEmpty()) {
                description = "";
            }
            if (required == null) {
                required = false;
            }
            if (params == null) {
                params = new HashMap<>();
            }
            return new AgentExtension(url, description, required, params);
        }
    }
}
