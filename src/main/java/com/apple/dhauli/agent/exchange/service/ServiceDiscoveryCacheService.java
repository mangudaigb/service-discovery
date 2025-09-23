package com.apple.dhauli.agent.exchange.service;

import com.apple.dhauli.agent.exchange.dao.instance.Instance;
import com.apple.dhauli.agent.exchange.dao.instance.ServiceInstance;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ServiceDiscoveryCacheService {
    private static final Logger logger = LoggerFactory.getLogger(ServiceDiscoveryCacheService.class);

    private final RedisTemplate<String, ServiceInstance> redisTemplate;
    private final ObjectMapper objectMapper;

    public ServiceDiscoveryCacheService(RedisTemplate<String, ServiceInstance> serviceRedisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = serviceRedisTemplate;
        this.objectMapper = objectMapper;
    }

    public boolean doesServiceExistForPath(String servicePath) {
        return redisTemplate.hasKey(servicePath);
    }

    public List<ServiceInstance> getAllServices() {
        Set<String> keySet = redisTemplate.keys("/dhauli/services/*");
        if (keySet.isEmpty()) {
            logger.info("No Services are registered in redis.");
            return new ArrayList<>();
        }
        List<ServiceInstance> serviceInstanceList = redisTemplate.opsForValue().multiGet(keySet);
        if (serviceInstanceList == null || serviceInstanceList.isEmpty()) {
            logger.info("No services are registered in redis for the keys. Ideally should not happen as keys should have been deleted.");
            return new ArrayList<>();
        }
        logger.info("Found {} services.", serviceInstanceList.size());
        return serviceInstanceList;
    }

    public void addServiceInstance(String servicePath, String serviceName, Instance instance) {
        ServiceInstance serviceInstance = new ServiceInstance();
        if (redisTemplate.hasKey(servicePath)) {
            logger.info("Service: {} exists in redis.", servicePath);
            serviceInstance = (ServiceInstance) redisTemplate.opsForValue().get(servicePath);
            List<Instance> instanceList = serviceInstance.getInstanceList();
            boolean exists = instanceList.stream()
                            .anyMatch(i -> i.getId().equals(instance.getId()));
            if (exists) {
                logger.info("Skipping instance: {} for service: {} as it already exists.", instance, servicePath);
                return;
            }
            instanceList.add(instance);
            logger.info("Adding instance: {} to already existing Service: {}.", instance, servicePath);
            serviceInstance.setInstanceList(instanceList);
        } else {
            logger.info("Adding instance: {} to new Service: {}.", instance, servicePath);
            serviceInstance.setServiceName(serviceName);
            serviceInstance.getInstanceList().add(instance);
        }
        redisTemplate.opsForValue().set(servicePath, serviceInstance);
    }

    public void removeServiceForPath(String servicePath, String instanceId) {
        if (redisTemplate.hasKey(servicePath)) {
            ServiceInstance serviceInstance = redisTemplate.opsForValue().get(servicePath);
            List<Instance> newServiceInstances = serviceInstance.getInstanceList().stream()
                    .filter(instance -> !instance.getId().equals(instanceId))
                    .toList();
            if (newServiceInstances.isEmpty()) {
                logger.info("As instances count is 0, deleting the service itself. Service key: {}", instanceId);
                redisTemplate.delete(servicePath);
            } else {
                serviceInstance.setInstanceList(newServiceInstances);
                redisTemplate.opsForValue().set(servicePath, serviceInstance);
                logger.info("Removed an instance: {} from service instances.", instanceId);
            }
        } else {
            logger.info("No service instance entry in redis found for path {}", servicePath);
        }
    }
}
