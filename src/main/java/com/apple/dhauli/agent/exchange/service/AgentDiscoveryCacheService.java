package com.apple.dhauli.agent.exchange.service;

import com.apple.dhauli.agent.exchange.dao.agent.AgentCard;
import com.apple.dhauli.agent.exchange.dao.instance.AgentInstance;
import com.apple.dhauli.agent.exchange.dao.instance.Instance;
import com.apple.dhauli.agent.exchange.exceptions.AgentExistsForPathException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentDiscoveryCacheService {
    public static final Logger LOGGER = LoggerFactory.getLogger(AgentDiscoveryCacheService.class);

    private final RedisTemplate<String, AgentInstance> redisTemplate;

    public AgentDiscoveryCacheService(RedisTemplate<String, AgentInstance> agentRedisTemplate) {
        this.redisTemplate = agentRedisTemplate;
    }

    public boolean doesAgentExistForPath(String agentPath) {
        String key = agentPath.replaceAll("/", ":");
        return redisTemplate.hasKey(key);
    }

    public void addAgentForPath(String agentPath, String agentName, AgentCard agentCard) {
//        String key = agentPath.replaceAll("/", ":");
//        redisTemplate.opsForValue().set(key, agentCard);
    }

    public void removeAgentForPath(String agentPath) {
//        String key = agentPath.replaceAll("/", ":");
//        redisTemplate.delete(key);
    }

    public void addAgentInstanceForPath(String agentPath, Instance instanceDetails) {
//        String key = agentPath.replaceAll("/", ":");
//        AgentInstance agentInstances = (AgentInstance) redisTemplate.opsForValue().get(key);
//        agentInstance.getAgentInstanceList().add(instanceDetails);
//        redisTemplate.opsForValue().set(key, agentInstance);
    }

    public void removeAgentInstanceForPath(String agentInstancePath) {
    }


}
