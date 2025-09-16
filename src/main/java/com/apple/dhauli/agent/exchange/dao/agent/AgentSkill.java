package com.apple.dhauli.agent.exchange.dao.agent;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public record AgentSkill(
        String id,
        String name,
        String description,
        @JsonProperty("examples") List<String> exampleList,
        @JsonProperty("tags") List<String> tagList,
        @JsonProperty("qualifiers") List<String> qualifierList,
        @JsonProperty("inputModes") List<String> inputModeList,
        @JsonProperty("outputModes") List<String> outputModeList,
        @JsonProperty("security") List<Security> securityList
) {

    public static class Builder {
        private String id;
        private String name;
        private String description;
        private List<String> exampleList;
        private List<String> tagList;
        private List<String> qualifierList;
        private List<String> inputModeList;
        private List<String> outputModeList;
        private List<Security> securityList;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder exampleList(List<String> exampleList) {
            this.exampleList = exampleList;
            return this;
        }

        public Builder tagList(List<String> tagList) {
            this.tagList = tagList;
            return this;
        }

        public Builder qualifierList(List<String> qualifierList) {
            this.qualifierList = qualifierList;
            return this;
        }

        public Builder inputModeList(List<String> inputModeList) {
            this.inputModeList = inputModeList;
            return this;
        }

        public Builder outputModeList(List<String> outputModeList) {
            this.outputModeList = outputModeList;
            return this;
        }

        public Builder securityList(List<Security> securityList) {
            this.securityList = securityList;
            return this;
        }

        public AgentSkill build() {
            if (id == null || id.isEmpty()) {
                throw new IllegalArgumentException("Id cannot be empty");
            }
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty");
            }
            if (description == null || description.isEmpty()) {
                throw new IllegalArgumentException("Description cannot be empty");
            }
            if (inputModeList == null) {
                inputModeList = new ArrayList<>();
            }
            if (outputModeList == null) {
                outputModeList = new ArrayList<>();
            }
            if (securityList == null) {
                securityList = new ArrayList<>();
            }
            if (exampleList == null) {
                exampleList = new ArrayList<>();
            }
            if (tagList == null) {
                tagList = new ArrayList<>();
            }
            if (qualifierList == null) {
                qualifierList = new ArrayList<>();
            }
            return new AgentSkill(id, name, description, exampleList, tagList, qualifierList, inputModeList, outputModeList, securityList);
        }
    }
}
