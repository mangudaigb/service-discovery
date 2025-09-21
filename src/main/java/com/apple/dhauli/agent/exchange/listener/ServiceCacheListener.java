package com.apple.dhauli.agent.exchange.listener;

import com.apple.dhauli.agent.exchange.dao.instance.Instance;
import com.apple.dhauli.agent.exchange.service.ServiceDiscoveryCacheService;
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
public class ServiceCacheListener implements CuratorCacheListener {

    private static final Logger logger = LoggerFactory.getLogger(ServiceCacheListener.class);

    @Value("${zookeeper.paths.services}")
    private String servicePath;

    private static final String serviceRegex = "/dhauli/services/([^/]+)/instances/([0-9a-fA-F-]{36})";
    private static final Pattern servicePattern = Pattern.compile(serviceRegex);

    private final ServiceDiscoveryCacheService serviceDiscoveryCacheService;
    private final ObjectMapper objectMapper;

    public ServiceCacheListener(ObjectMapper objectMapper, ServiceDiscoveryCacheService serviceDiscoveryCacheService) {
        this.objectMapper = objectMapper;
        this.serviceDiscoveryCacheService = serviceDiscoveryCacheService;
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
            logger.error("Service node update event received: {}, which should never happen", data.getPath());
        }
    }

    private void handleNodeCreationEvent(ChildData data) {
        logger.info("Service Node is received: {}", data);
        Matcher serviceMatcher = servicePattern.matcher(data.getPath());
        if (serviceMatcher.matches()) {
            String serviceName = serviceMatcher.group(1);
            String instanceId = serviceMatcher.group(2);
            String servicePath = "/dhauli/services/" + serviceName;
            String json_data = new String(data.getData(), StandardCharsets.UTF_8);
            Instance instance = null;
            try {
                instance = objectMapper.readValue(json_data, Instance.class);
            } catch (JsonProcessingException e) {
                logger.error("Error parsing json data", e);
                throw new RuntimeException(e);
            }
            logger.info("Adding instance: {} to service {}", instance, servicePath);
            serviceDiscoveryCacheService.addServiceInstance(servicePath, serviceName, instance);
        } else {
            logger.info("Create Service Node is being skipped: {}", data.getPath());
        }
    }

    private void handleNodeDeletedEvent(ChildData data) {
        Matcher serviceMatcher = servicePattern.matcher(data.getPath());
        if (serviceMatcher.matches()) {
            String serviceName = serviceMatcher.group(1);
            String instanceId = serviceMatcher.group(2);
            String servicePath = "/dhauli/services/" + serviceName;
            logger.info("Removing instance: {} from service {}", instanceId, servicePath);
            serviceDiscoveryCacheService.removeServiceForPath(servicePath, instanceId);
        } else {
            logger.info("Delete Service Node is being skipped: {}", data.getPath());
        }
    }
}
