package com.apple.dhauli.agent.exchange.listener;

import com.apple.dhauli.agent.exchange.service.McpServerDiscoveryCacheService;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class McpServerCacheListener implements CuratorCacheListener {
    private static final Logger logger = LoggerFactory.getLogger(McpServerCacheListener.class);

    @Value("${zookeeper.paths.mcp-servers}")
    private String mcpServerPath;

    private final McpServerDiscoveryCacheService mcpServerDiscoveryCacheService;

    public McpServerCacheListener(McpServerDiscoveryCacheService mcpServerDiscoveryCacheService) {
        this.mcpServerDiscoveryCacheService = mcpServerDiscoveryCacheService;
    }

    @Override
    public void event(Type type, ChildData oldData, ChildData data) {
        if (type.equals(Type.NODE_CREATED)) {
            logger.info("MCP Server Node is added: {}", data);
        }
        if (type.equals(Type.NODE_DELETED)) {
            logger.info("MCP Server Node is removed: {}", data);
        }
        if (type.equals(Type.NODE_CHANGED)) {
            logger.info("MCP Server Node is updated: {}", data);
        }
    }
}
