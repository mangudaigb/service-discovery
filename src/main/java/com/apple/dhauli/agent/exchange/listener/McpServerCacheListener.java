package com.apple.dhauli.agent.exchange.listener;

import com.apple.dhauli.agent.exchange.dao.instance.Instance;
import com.apple.dhauli.agent.exchange.service.McpServerDiscoveryCacheService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class McpServerCacheListener implements CuratorCacheListener {
    private static final Logger logger = LoggerFactory.getLogger(McpServerCacheListener.class);

    @Value("${zookeeper.paths.mcp-servers}")
    private String mcpServerPath;

    private static final String mcpServerRegex = "/dhauli/mcps/([^/]+)/instances/([0-9a-fA-F-]{36})";
    private static final Pattern mcpServerPattern = Pattern.compile(mcpServerRegex);

    private final McpServerDiscoveryCacheService mcpServerDiscoveryCacheService;
    private final ObjectMapper objectMapper;

    public McpServerCacheListener(ObjectMapper objectMapper, McpServerDiscoveryCacheService mcpServerDiscoveryCacheService) {
        this.objectMapper = objectMapper;
        this.mcpServerDiscoveryCacheService = mcpServerDiscoveryCacheService;
    }

    @Override
    public void event(Type type, ChildData oldData, ChildData data) {
        if (type.equals(Type.NODE_CREATED)) {
            handleNodeCreationEvent(data);
        }
        if (type.equals(Type.NODE_DELETED)) {
            handleNodeDeletedEvent(oldData);
        }
        if (type.equals(Type.NODE_CHANGED)) {
            logger.info("Mcp Server node update event received: {}, unexpected", data.getPath());
        }
    }

    private void handleNodeCreationEvent(ChildData data) {
        logger.info("Mcp Server Node is received: {}", data);
        Matcher mcpServerMatcher = mcpServerPattern.matcher(data.getPath());
        if (mcpServerMatcher.matches()) {
            String mcpServerName = mcpServerMatcher.group(1);
            String instanceId = mcpServerMatcher.group(2);
            String mcpServerPath = "/dhauli/mcps/" + mcpServerName;
            String json_data = new String(data.getData(), StandardCharsets.UTF_8);
            Instance instance = null;
            try {
                instance = objectMapper.readValue(json_data, Instance.class);
            } catch (JsonProcessingException e) {
                logger.error("Error parsing json data", e);
                throw new RuntimeException(e);
            }
            logger.info("Adding instance: {} to mcp server {}", instanceId, mcpServerPath);
            mcpServerDiscoveryCacheService.addMcpServerInstance(mcpServerPath, mcpServerName, instance);
        } else {
            logger.info("Create Mcp Server Node is being skipped: {}", data.getPath());
        }
    }

    private void handleNodeDeletedEvent(ChildData data) {
        Matcher mspServerMatcher = mcpServerPattern.matcher(data.getPath());
        if (mspServerMatcher.matches()) {
            String mcpServerName = mspServerMatcher.group(1);
            String instanceId = mspServerMatcher.group(2);
            String mcpServerPath = "/dhauli/mcps/" + mcpServerName;
            logger.info("Removing instance: {} from MCP Server {}.", instanceId, mcpServerPath);
            mcpServerDiscoveryCacheService.removeMcpServerForPath(mcpServerPath, instanceId);
        } else {
            logger.info("Delete Mcp Server Node is being skipped: {}", data.getPath());
        }
    }
}
