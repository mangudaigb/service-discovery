package com.apple.dhauli.agent.exchange.dao.agent;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record AgentCard(
        String protocolVersion,
        String version,
        String name,
        String description,
        String url,
        String preferredTransport, // optional
        @JsonProperty("additionalInterfaces") List<AgentInterface> additionalInterfaceList,
        String iconUrl, // optional
        @JsonProperty("provider") AgentProvider agentProvider, // optional
        @JsonProperty("capabilities") AgentCapability agentCapability,
        @JsonProperty("securitySchemes")Map<String, Object> securitySchemes,
        @JsonProperty("security") List<Security> securityList,
        List<String> defaultInputModes,
        List<String> defaultOutputModels,
        Boolean supportsAuthenticatedExtendedCard,
        @JsonProperty("signatures") List<AgentCardSignature> agentCardSignatureList,
        String id,
        @JsonProperty("type") AgentType agentType,
        @JsonProperty("repository") String repository,
        @JsonProperty("tags") List<String> tagList,
        @JsonProperty("qualifiers") List<String> qualifierList,
        @JsonProperty("skills") List<AgentSkill> agentSkillList ) {

    public static class Builder {
        private String protocolVersion;
        private String version;
        private String name;
        private String description;
        private String url;
        private String preferredTransport;
        private List<AgentInterface> additionalInterfaceList;
        private String iconUrl;
        private AgentProvider agentProvider;
        private AgentCapability agentCapability;
        private Map<String, Object> securitySchemes;
        private List<Security> securityList;
        private List<AgentCardSignature> agentCardSignatureList;
        private List<String> defaultInputModes;
        private List<String> defaultOutputModels;
        private Boolean supportsAuthenticatedExtendedCard;
        private List<AgentSkill> agentSkillList;
        private String id;
        private AgentType agentType;
        private String repository;
        private List<String> tagList;
        private List<String> qualifierList;

        public Builder protocolVersion(String protocolVersion) {
            this.protocolVersion = protocolVersion;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
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

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder preferredTransport(String preferredTransport) {
            this.preferredTransport = preferredTransport;
            return this;
        }

        public Builder additionalInterfaceList(List<AgentInterface> additionalInterfaceList) {
            this.additionalInterfaceList = additionalInterfaceList;
            return this;
        }

        public Builder iconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
            return this;
        }

        public Builder agentProvider(AgentProvider agentProvider) {
            this.agentProvider = agentProvider;
            return this;
        }

        public Builder agentCapability(AgentCapability agentCapability) {
            this.agentCapability = agentCapability;
            return this;
        }

        public Builder securitySchemes(Map<String, Object> securitySchemes) {
            this.securitySchemes = securitySchemes;
            return this;
        }

        public Builder securityList(List<Security> securityList) {
            this.securityList = securityList;
            return this;
        }

        public Builder agentCardSignatureList(List<AgentCardSignature> agentCardSignatureList) {
            this.agentCardSignatureList = agentCardSignatureList;
            return this;
        }

        public Builder defaultInputModes(List<String> defaultInputModes) {
            this.defaultInputModes = defaultInputModes;
            return this;
        }

        public Builder defaultOutputModels(List<String> defaultOutputModels) {
            this.defaultOutputModels = defaultOutputModels;
            return this;
        }

        public Builder supportsAuthenticatedExtendedCard(Boolean supportsAuthenticatedExtendedCard) {
            this.supportsAuthenticatedExtendedCard = supportsAuthenticatedExtendedCard;
            return this;
        }

        public Builder agentSkillList(List<AgentSkill> agentSkillList) {
            this.agentSkillList = agentSkillList;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder agentType(AgentType agentType) {
            this.agentType = agentType;
            return this;
        }

        public Builder repository(String repository) {
            this.repository = repository;
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

        public AgentCard build() {
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Agent name cannot be empty");
            }
            if (description == null || description.isEmpty()) {
                throw new IllegalArgumentException("Agent description cannot be empty");
            }
            if (url == null || url.isEmpty()) {
                throw new IllegalArgumentException("Agent url cannot be empty");
            }
            if (preferredTransport == null || preferredTransport.isEmpty()) {
                this.preferredTransport = "JSONRPC";
            }
            if (additionalInterfaceList == null || additionalInterfaceList.isEmpty()) {
                this.additionalInterfaceList = new ArrayList<>();
            }
            if (version == null || version.isEmpty()) {
                throw new IllegalArgumentException("Agent version cannot be empty");
            }
            if (agentCapability == null) {
                throw new IllegalArgumentException("Agent capability cannot be null");
            }
            if (defaultInputModes == null || defaultInputModes.isEmpty()) {
                defaultInputModes = new ArrayList<>();
            }
            if (defaultOutputModels == null || defaultOutputModels.isEmpty()) {
                defaultOutputModels = new ArrayList<>();
            }
            if (agentSkillList == null || agentSkillList.isEmpty()) {
                agentSkillList = new ArrayList<>();
            }
            if (supportsAuthenticatedExtendedCard == null) {
                supportsAuthenticatedExtendedCard = false;
            }
            if (agentCardSignatureList == null || agentCardSignatureList.isEmpty()) {
                agentCardSignatureList = new ArrayList<>();
            }
            if (id == null || id.isEmpty()) {
                throw new IllegalArgumentException("Agent ID cannot be empty");
            }
            if (agentType == null) {
                throw new IllegalArgumentException("Agent type cannot be empty");
            }
            if (repository == null || repository.isEmpty()) {
                throw new IllegalArgumentException("Agent repository cannot be empty");
            }
            if (tagList == null || tagList.isEmpty()) {
                tagList = new ArrayList<>();
            }
            if (qualifierList == null || qualifierList.isEmpty()) {
                qualifierList = new ArrayList<>();
            }

            return new AgentCard(protocolVersion, version, name, description, url, preferredTransport, additionalInterfaceList,
                    iconUrl, agentProvider, agentCapability, securitySchemes, securityList, defaultInputModes,
                    defaultOutputModels, supportsAuthenticatedExtendedCard, agentCardSignatureList, id, agentType,
                    repository, tagList, qualifierList, agentSkillList);
        }
    }
}
