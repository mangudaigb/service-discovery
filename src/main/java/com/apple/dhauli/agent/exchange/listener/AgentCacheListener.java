package com.apple.dhauli.agent.exchange.listener;

import com.apple.dhauli.agent.exchange.service.AgentDiscoveryCacheService;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AgentCacheListener implements CuratorCacheListener {
    private static final Logger logger = LoggerFactory.getLogger(AgentCacheListener.class);

    @Value("${zookeeper.paths.agents}")
    private String agentPath;

    private static final String agentRegex = "/dhauli/agents/([^/]+)/instances/([0-9a-fA-F-]{36})";
    private static final Pattern servicePattern = Pattern.compile(agentRegex);

    private final AgentDiscoveryCacheService agentDiscoveryCacheService;

    public AgentCacheListener(AgentDiscoveryCacheService agentDiscoveryCacheService) {
        this.agentDiscoveryCacheService = agentDiscoveryCacheService;
    }

    @Override
    public void event(Type type, ChildData oldData, ChildData data) {
        if (data.getPath().equals(agentPath)) {
            return;
        }
        if (type.equals(Type.NODE_CREATED)) {
            logger.info("Agent Node is added: {}", data);
        }
        if (type.equals(Type.NODE_DELETED)) {
            logger.info("Agent Node is removed: {}", data);
        }
        if (type.equals(Type.NODE_CHANGED)) {
            logger.info("Agent Node is updated: {}", data);
        }
    }

    public void handleNodeCreationEvent(ChildData data) {
        logger.info("Agent instance is received: {}", data);
        Matcher agentMatcher = servicePattern.matcher(data.getPath());
        if (agentMatcher.matches()) {
            String agentName = agentMatcher.group(1);
            String instanceId = agentMatcher.group(2);
            String agentPath = "/dhauli/agents/" + agentName;
            String json_data = new String(data.getData());
        }

    }
}
