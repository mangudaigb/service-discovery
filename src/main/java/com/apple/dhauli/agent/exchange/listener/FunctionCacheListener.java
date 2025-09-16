package com.apple.dhauli.agent.exchange.listener;

import com.apple.dhauli.agent.exchange.service.FunctionDiscoveryCacheService;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FunctionCacheListener implements CuratorCacheListener {
    private static final Logger logger = LoggerFactory.getLogger(FunctionCacheListener.class);


    @Value("${zookeeper.paths.functions}")
    private String functionPath;

    private final FunctionDiscoveryCacheService functionDiscoveryCacheService;

    public FunctionCacheListener(FunctionDiscoveryCacheService functionDiscoveryCacheService) {
        this.functionDiscoveryCacheService = functionDiscoveryCacheService;
    }

    @Override
    public void event(Type type, ChildData oldData, ChildData data) {
        if (type.equals(Type.NODE_CREATED)) {
            logger.info("Function Node is added: {}", data);
        }
        if (type.equals(Type.NODE_DELETED)) {
            logger.info("Function Node is removed: {}", data);
        }
        if (type.equals(Type.NODE_CHANGED)) {
            logger.info("Function Node is updated: {}", data);
        }
    }
}
