package com.apple.dhauli.agent.exchange.service;

import com.apple.dhauli.agent.exchange.client.McpFeignClient;
import com.apple.dhauli.agent.exchange.dao.instance.Instance;
import com.apple.dhauli.agent.exchange.dao.instance.MCPServerInstance;
import com.apple.dhauli.agent.exchange.dao.mcpserver.PromptDescriptor;
import com.apple.dhauli.agent.exchange.dao.mcpserver.ResourceDescriptor;
import com.apple.dhauli.agent.exchange.dao.mcpserver.ToolDescriptor;
import feign.Feign;
import feign.FeignException;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class McpServerDiscoveryCacheService {
    private static final Logger logger = LoggerFactory.getLogger(McpServerDiscoveryCacheService.class);

    private final RedisTemplate<String, MCPServerInstance> redisTemplate;

    public McpServerDiscoveryCacheService(RedisTemplate<String, MCPServerInstance> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean doesMcpServerExistForPath(String path) {
        return redisTemplate.hasKey(path);
    }

    public List<MCPServerInstance> getAllMcpServers() {
        Set<String> keySet = redisTemplate.keys("/dhauli/mcps/*/instances/*");
        if (keySet.isEmpty()) {
            logger.debug("No Mcp Servers registered in redis.");
            return new ArrayList<>();
        }
        List<MCPServerInstance> mcpInstanceList = redisTemplate.opsForValue().multiGet(keySet);
        if (mcpInstanceList == null || mcpInstanceList.isEmpty()) {
            logger.info("No instances of MCP server are registered in redis");
            return new ArrayList<>();
        }
        logger.info("Found {} mcp-servers in cache", mcpInstanceList.size());
        return mcpInstanceList;
    }

    public List<MCPServerInstance> getMcpServerListForName(String mcpServerName) {
        Set<String> keySet = redisTemplate.keys("/dhauli/mcps/" + mcpServerName + "/instances/*");
        if (!keySet.isEmpty()) {
            logger.debug("No mcp servers registered in redis");
            return new ArrayList<>();
        }
//        TODO
        return null;
    }

    public void addMcpServerInstance(String mcpServerPath, String mcpServerName, Instance instance) {
        MCPServerInstance mcpServerInstance = new MCPServerInstance();
        if (redisTemplate.hasKey(mcpServerPath)) {
            logger.debug("MCP Server exists for path {}", mcpServerPath);
            mcpServerInstance = redisTemplate.opsForValue().get(mcpServerPath);
            List<Instance> instanceList = mcpServerInstance.getInstanceList();
            boolean exists = instanceList.stream()
                    .anyMatch(i -> i.getId().equals(instance.getId()));
            if (exists) {
                logger.info("Skipping instance: {} for mcp-server: {} as it already exists", instance.getId(), mcpServerName);
                return;
            }
            instanceList.add(instance);
            logger.info("Added instance: {} for mcp-server: {}", instance.getId(), mcpServerName);
            mcpServerInstance.setInstanceList(instanceList);
        } else {
            logger.info("Adding instance: {} for mcp-server: {}", instance.getId(), mcpServerName);
            mcpServerInstance.setMcpServerName(mcpServerName);
            try {
                List<ToolDescriptor> toolDescriptors = getMcpServerToolList(instance);
                mcpServerInstance.setToolDescriptorList(toolDescriptors);
                List<ResourceDescriptor> resourceDescriptors = getMMcpServerResourcceList(instance);
                mcpServerInstance.setResourceDescriptorList(resourceDescriptors);
                List<PromptDescriptor> promptDescriptors = getMcpServerPromptList(instance);
                mcpServerInstance.setPromptDescriptorList(promptDescriptors);
            } catch (FeignException e) {
                logger.error("Feign Client: Failed to get MCP server details from url: {}", instance.getUrl());
                return;
            }
            mcpServerInstance.getInstanceList().add(instance);
        }
        redisTemplate.opsForValue().set(mcpServerPath, mcpServerInstance);
    }

    public void removeMcpServerForPath(String mcpServerPath, String instanceId) {
        if (redisTemplate.hasKey(mcpServerPath)) {
            MCPServerInstance mcpServerInstance = redisTemplate.opsForValue().get(mcpServerPath);
            List<Instance> newMcpServerInstances = mcpServerInstance.getInstanceList().stream()
                    .filter(instance -> !instance.getId().equals(instanceId))
                    .toList();
            if (newMcpServerInstances.isEmpty()) {
                logger.info("As instance count is 0, deleting the mcp server itself. Instance key: {}", instanceId);
                redisTemplate.delete(mcpServerPath);
            } else {
                mcpServerInstance.setInstanceList(newMcpServerInstances);
                redisTemplate.opsForValue().set(mcpServerPath, mcpServerInstance);
                logger.info("Removed an instance: {} from mcp server instances.", instanceId);
            }
        } else {
            logger.info("No mcp server instance entry in redis for path {}", mcpServerPath);
        }
    }

    private List<ToolDescriptor> getMcpServerToolList(Instance instance) throws FeignException {
        String mcpToolsUrl = instance.getUrl() + "/tools";
        McpFeignClient mcpFeignClient = Feign.builder()
                .decoder(new GsonDecoder())
                .encoder(new GsonEncoder())
                .target(McpFeignClient.class, mcpToolsUrl);
        List<ToolDescriptor> toolDescriptorList = mcpFeignClient.getToolList();
        return toolDescriptorList;
    }

    private List<ResourceDescriptor> getMMcpServerResourcceList(Instance instance) throws FeignException {
        String mcpResourcesUrl = instance.getUrl() + "/resources";
        McpFeignClient mcpFeignClient = Feign.builder()
                .decoder(new GsonDecoder())
                .encoder(new GsonEncoder())
                .target(McpFeignClient.class, mcpResourcesUrl);
        List<ResourceDescriptor> resourceDescriptorList = mcpFeignClient.getResourceList();
        return resourceDescriptorList;
    }

    private List<PromptDescriptor> getMcpServerPromptList(Instance instance) throws FeignException {
        String mcpPromptsUrl = instance.getUrl() + "/prompts";
        McpFeignClient mcpFeignClient = Feign.builder()
                .decoder(new GsonDecoder())
                .encoder(new GsonEncoder())
                .target(McpFeignClient.class, mcpPromptsUrl);
        List<PromptDescriptor> promptDescriptorList = mcpFeignClient.getPromptList();
        return promptDescriptorList;
    }
}
