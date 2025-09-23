package com.apple.dhauli.agent.exchange.service;

import com.apple.dhauli.agent.exchange.client.AgentFeignClient;
import com.apple.dhauli.agent.exchange.dao.MockAgentCard;
import com.apple.dhauli.agent.exchange.dao.agent.AgentCard;
import com.apple.dhauli.agent.exchange.dao.instance.AgentInstance;
import com.apple.dhauli.agent.exchange.dao.instance.Instance;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class AgentDiscoveryCacheService {
    public static final Logger logger = LoggerFactory.getLogger(AgentDiscoveryCacheService.class);

    private final RedisTemplate<String, AgentInstance> redisTemplate;
    private final ObjectMapper objectMapper;

    public AgentDiscoveryCacheService(RedisTemplate<String, AgentInstance> agentRedisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = agentRedisTemplate;
        this.objectMapper = objectMapper;
    }

    public boolean doesAgentExistForPath(String agentPath) {
        return redisTemplate.hasKey(agentPath);
    }

    public List<AgentInstance> getAllAgents() {
        Set<String> keySet = redisTemplate.keys("/dhauli/agents/*");
        if (keySet.isEmpty()) {
            logger.info("No Agents registered in redis.");
            return new ArrayList<>();
        }
        List<AgentInstance> agentInstanceList = redisTemplate.opsForValue().multiGet(keySet);
        if (agentInstanceList == null || agentInstanceList.isEmpty()) {
            logger.info("No agents are registered in redis for keys. Ideally should not happen as keys should have been deleted.");
            return new ArrayList<>();
        }
        logger.info("Found {} agents in redis", agentInstanceList.size());
        return agentInstanceList;
    }

    public List<AgentInstance> getAllAgentInstancesForName(String agentName) {
        Set<String> keySet = redisTemplate.keys("/dhauli/agents/" + agentName + "/instances/*");
        if (!keySet.isEmpty()) {
            logger.info("No services registered for the agent in redis.");
            return new ArrayList<>();
        }
        // TODO
        return null;
    }

    public void addAgentInstance(String agentPath, String agentName, Instance instance) {
        AgentInstance agentInstance = new AgentInstance();
        if (redisTemplate.hasKey(agentPath)) {
            logger.info("Agent already exists for path {}", agentPath);
            agentInstance = redisTemplate.opsForValue().get(agentPath);
            List<Instance> instanceList = agentInstance.getAgentInstanceList();
            boolean exists = instanceList.stream()
                    .anyMatch(i -> i.getId().equals(instance.getId()));
            if (exists) {
                logger.info("Skipping instance: {} for agent: {} as it already exists", instance.getId(), agentName);
                return;
            }
            instanceList.add(instance);
            logger.info("Added instance: {} for existing agent: {}", instance, agentName);
            agentInstance.setAgentInstanceList(instanceList);
        } else {
            logger.info("Adding instance: {} to new Agent: {}", instance, agentPath);
            agentInstance.setAgentName(agentName);
            try {
//                AgentCard agentCard = getAgentCardFromInstance(instance);
                AgentCard agentCard = MockAgentCard.get();
                agentInstance.setAgentCard(agentCard);
            } catch (FeignException e) {
                logger.error("Feign Client: failed to get agent card from url: {}", instance.getUrl());
                return;
            }
            agentInstance.getAgentInstanceList().add(instance);
        }
        redisTemplate.opsForValue().set(agentPath, agentInstance);
    }

    public void removeAgentForPath(String agentPath, String instanceId) {
        if (redisTemplate.hasKey(agentPath)) {
            AgentInstance agentInstance = redisTemplate.opsForValue().get(agentPath);
            List<Instance> newAgentInstances = agentInstance.getAgentInstanceList().stream()
                    .filter(instance -> !instance.getId().equals(instanceId))
                    .toList();
            if (newAgentInstances.isEmpty()) {
                logger.info("As instance count is 0, deleting the agent itself. Instance key: {}", instanceId);
                redisTemplate.delete(agentPath);
            } else {
                agentInstance.setAgentInstanceList(newAgentInstances);
                redisTemplate.opsForValue().set(agentPath, agentInstance);
                logger.info("Removed an instance: {} from agent instances.", instanceId);
            }
        } else {
            logger.info("No agent instance entry in redis for path {}", agentPath);
        }
    }

    private AgentCard getAgentCardFromInstance(Instance instance) throws FeignException {
        String agentCardUrl = instance.getUrl();
        AgentFeignClient agentFeignClient = Feign.builder()
                .decoder(new GsonDecoder())
                .encoder(new GsonEncoder())
                .target(AgentFeignClient.class, agentCardUrl);
        AgentCard agentCard = agentFeignClient.getAgentCard();
        return agentCard;
    }
}
