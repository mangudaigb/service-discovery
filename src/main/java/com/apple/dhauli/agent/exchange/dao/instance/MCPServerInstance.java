package com.apple.dhauli.agent.exchange.dao.instance;

import com.apple.dhauli.agent.exchange.dao.mcpserver.PromptDescriptor;
import com.apple.dhauli.agent.exchange.dao.mcpserver.ResourceDescriptor;
import com.apple.dhauli.agent.exchange.dao.mcpserver.ToolDescriptor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MCPServerInstance {
    @JsonProperty("mcp-server")
    private String mcpServerName;
    @JsonProperty("tools")
    private List<ToolDescriptor> toolDescriptorList = new ArrayList<>();
    @JsonProperty("resources")
    private List<ResourceDescriptor> resourceDescriptorList = new ArrayList<>();
    @JsonProperty("prompts")
    private List<PromptDescriptor> promptDescriptorList = new ArrayList<>();
    @JsonProperty("instances")
    private List<Instance> instanceList = new ArrayList<>();

    public String getMcpServerName() {
        return mcpServerName;
    }

    public void setMcpServerName(String mcpServerName) {
        this.mcpServerName = mcpServerName;
    }

    public List<ToolDescriptor> getToolDescriptorList() {
        return toolDescriptorList;
    }

    public void setToolDescriptorList(List<ToolDescriptor> toolDescriptorList) {
        this.toolDescriptorList = toolDescriptorList;
    }

    public List<ResourceDescriptor> getResourceDescriptorList() {
        return resourceDescriptorList;
    }

    public void setResourceDescriptorList(List<ResourceDescriptor> resourceDescriptorList) {
        this.resourceDescriptorList = resourceDescriptorList;
    }

    public List<PromptDescriptor> getPromptDescriptorList() {
        return promptDescriptorList;
    }

    public void setPromptDescriptorList(List<PromptDescriptor> promptDescriptorList) {
        this.promptDescriptorList = promptDescriptorList;
    }

    public List<Instance> getInstanceList() {
        return instanceList;
    }

    public void setInstanceList(List<Instance> instanceList) {
        this.instanceList = instanceList;
    }
}
