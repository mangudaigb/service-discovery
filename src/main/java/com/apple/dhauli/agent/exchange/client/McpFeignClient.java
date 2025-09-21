package com.apple.dhauli.agent.exchange.client;

import com.apple.dhauli.agent.exchange.dao.mcpserver.PromptDescriptor;
import com.apple.dhauli.agent.exchange.dao.mcpserver.ResourceDescriptor;
import com.apple.dhauli.agent.exchange.dao.mcpserver.ToolDescriptor;
import feign.RequestLine;

import java.util.List;

public interface McpFeignClient {
    @RequestLine("GET /tools")
    List<ToolDescriptor> getToolList();
    @RequestLine("GET /resources")
    List<ResourceDescriptor> getResourceList();
    @RequestLine("GET /prompts")
    List<PromptDescriptor> getPromptList();
}
