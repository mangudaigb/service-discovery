package com.apple.dhauli.agent.exchange.watcher;

import com.apple.dhauli.agent.exchange.listener.AgentCacheListener;
import com.apple.dhauli.agent.exchange.listener.FunctionCacheListener;
import com.apple.dhauli.agent.exchange.listener.McpServerCacheListener;
import com.apple.dhauli.agent.exchange.listener.ServiceCacheListener;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ServiceWatcherService {
    private static final Logger logger = LoggerFactory.getLogger(ServiceWatcherService.class);

    @Value("${zookeeper.paths.services}")
    private String servicesPath;
    @Value("${zookeeper.paths.agents}")
    private String agentsPath;
    @Value("${zookeeper.paths.mcp-servers}")
    private String mcpServersPath;
    @Value("${zookeeper.paths.functions}")
    private String functionsPath;

    private final CuratorFramework client;
    private final ServiceCacheListener serviceCacheListener;
    private final AgentCacheListener agentCacheListener;
    private final McpServerCacheListener mcpServerCacheListener;
    private final FunctionCacheListener functionCacheListener;

    private CuratorCache serviceCuratorCache;
    private CuratorCache agentCuratorCache;
    private CuratorCache mcpServerCuratorCache;
    private CuratorCache functionCuratorCache;

    public ServiceWatcherService(CuratorFramework client,
                                 ServiceCacheListener serviceCacheListener,
                                 AgentCacheListener agentCacheListener,
                                 McpServerCacheListener mcpServerCacheListener,
                                 FunctionCacheListener functionCacheListener) {
        this.client = client;
        this.serviceCacheListener = serviceCacheListener;
        this.agentCacheListener = agentCacheListener;
        this.mcpServerCacheListener = mcpServerCacheListener;
        this.functionCacheListener = functionCacheListener;
    }

    public void startWatcher() throws Exception {
        startServiceWatcher();
        startAgentWatcher();
        startMcpServerWatcher();
        startFunctionWatcher();
    }

    public void stopWatcher() throws Exception {
        stopServiceWatcher();
        stopAgentWatcher();
        stopMcpServerWatcher();
        stopFunctionWatcher();
    }

    private void startServiceWatcher() throws Exception {
        serviceCuratorCache = CuratorCache.builder(client, servicesPath).build();
        serviceCuratorCache.listenable().addListener(serviceCacheListener);
        serviceCuratorCache.start();
        logger.info("Service watcher started");
    }

    private void stopServiceWatcher() {
        if (serviceCuratorCache != null) {
            logger.info("Closing Service Curator Cache");
            serviceCuratorCache.close();
            serviceCuratorCache = null;
        }
    }

    private void startAgentWatcher() throws Exception {
        agentCuratorCache = CuratorCache.builder(client, agentsPath).build();
        agentCuratorCache.listenable().addListener(agentCacheListener);
        agentCuratorCache.start();
        logger.info("Agent watcher started");
    }

    private void stopAgentWatcher() {
        if (agentCuratorCache != null) {
            logger.info("Closing Agent Curator Cache");
            agentCuratorCache.close();
            agentCuratorCache = null;
        }
    }

    private void startMcpServerWatcher() throws Exception {
        mcpServerCuratorCache = CuratorCache.builder(client, mcpServersPath).build();
        mcpServerCuratorCache.listenable().addListener(mcpServerCacheListener);
        mcpServerCuratorCache.start();
        logger.info("Mcp Server watcher started");
    }

    private void stopMcpServerWatcher() {
        if (mcpServerCuratorCache != null) {
            logger.info("Closing Mcp Server Curator Cache");
            mcpServerCuratorCache.close();
            mcpServerCuratorCache = null;
        }
    }

    private void startFunctionWatcher() throws Exception {
        functionCuratorCache = CuratorCache.builder(client, functionsPath).build();
        functionCuratorCache.listenable().addListener(functionCacheListener);
        functionCuratorCache.start();
        logger.info("Function watcher started");
    }

    private void stopFunctionWatcher() {
        if (functionCuratorCache != null) {
            logger.info("Closing Function Curator Cache");
            functionCuratorCache.close();
            functionCuratorCache = null;
        }
    }
}
