package com.apple.dhauli.agent.exchange.configuration;

import com.apple.dhauli.agent.exchange.listener.ServiceCacheListener;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZookeeperCuratorConfig {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperCuratorConfig.class);
    private static final String PATH_TO_WATCH = "/dhauli/services";

    @Value("${zookeeper.connect}")
    private String zookeeperConnect;

    @Value("${zookeeper.session.timeout}")
    private int zookeeperSessionTimeout;

    @Value("${zookeeper.session.retryInterval}")
    private int retryInterval;

    @Bean(name = "zkClient", destroyMethod = "close")
    public CuratorFramework curatorFramework() {
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(zookeeperSessionTimeout, retryInterval);
        CuratorFramework zkClient = CuratorFrameworkFactory.newClient(zookeeperConnect, retryPolicy);
        zkClient.start();
        return zkClient;
    }

//    @Bean(name = "serviceCache", destroyMethod = "close")
//    public CuratorCache curatorCache(CuratorFramework zkClient, ServiceCacheListener serviceCacheListener) throws Exception {
//        logger.info("Starting the Service Watcher");
//        zkClient.start();
//        zkClient.blockUntilConnected();
//        logger.info("Service Watcher is connected to zookeeper and ready");
//
//        if (zkClient.checkExists().forPath(PATH_TO_WATCH) == null) {
//            zkClient.create().creatingParentsIfNeeded().forPath(PATH_TO_WATCH);
//            logger.info("Created the path " + PATH_TO_WATCH);
//        }
//
//        CuratorCache serviceCache = CuratorCache.builder(zkClient, PATH_TO_WATCH).build();
//        serviceCache.listenable().addListener(serviceCacheListener);
//        serviceCache.start();
//        return serviceCache;
//    }
}
