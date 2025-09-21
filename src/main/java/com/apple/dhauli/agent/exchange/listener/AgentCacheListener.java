package com.apple.dhauli.agent.exchange.listener;

import com.apple.dhauli.agent.exchange.dao.agent.AgentCard;
import com.apple.dhauli.agent.exchange.dao.instance.Instance;
import com.apple.dhauli.agent.exchange.service.AgentDiscoveryCacheService;
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
public class AgentCacheListener implements CuratorCacheListener {
    private static final Logger logger = LoggerFactory.getLogger(AgentCacheListener.class);
    private final ObjectMapper objectMapper;

    @Value("${zookeeper.paths.agents}")
    private String agentPath;

    private static final String agentRegex = "/dhauli/agents/([^/]+)/instances/([0-9a-fA-F-]{36})";
    private static final Pattern servicePattern = Pattern.compile(agentRegex);

    private final AgentDiscoveryCacheService agentDiscoveryCacheService;

    public AgentCacheListener(AgentDiscoveryCacheService agentDiscoveryCacheService, ObjectMapper objectMapper) {
        this.agentDiscoveryCacheService = agentDiscoveryCacheService;
        this.objectMapper = objectMapper;
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
            logger.error("Agent node update event received: {}, unexpected", data.getPath());
        }
    }

    public void handleNodeCreationEvent(ChildData data) {
        logger.info("Agent Node is received: {}", data);
        Matcher agentMatcher = servicePattern.matcher(data.getPath());
        if (agentMatcher.matches()) {
            String agentName = agentMatcher.group(1);
            String instanceId = agentMatcher.group(2);
            String agentPath = "/dhauli/agents/" + agentName;
            String json_data = new String(data.getData(), StandardCharsets.UTF_8);
            Instance instance = null;
            try {
                instance = objectMapper.readValue(json_data, Instance.class);
            } catch (JsonProcessingException e) {
                logger.error("Error parsing json data", e);
                throw new RuntimeException(e);
            }
            logger.info("Adding instance: {} to agent {}", instanceId, agentPath);
            agentDiscoveryCacheService.addAgentInstance(agentPath, agentName, instance);
        } else {
            logger.info("Create Agent Node is being skipped: {}", data.getPath());
        }
    }

    public void handleNodeDeletedEvent(ChildData data) {
        Matcher agentMatcher = servicePattern.matcher(data.getPath());
        if (agentMatcher.matches()) {
            String agentName = agentMatcher.group(1);
            String instanceId = agentMatcher.group(2);
            String agentPath = "/dhauli/agents/" + agentName;
            logger.info("Removing instance: {} from agent {}", instanceId, agentPath);
            agentDiscoveryCacheService.removeAgentForPath(agentPath, instanceId);
        } else {
            logger.info("Delete Agent Node is being skipped: {}", data.getPath());
        }
    }
}
