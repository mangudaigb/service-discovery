package com.apple.dhauli.agent.exchange.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LeaderElectionService {
    private static final Logger logger = LoggerFactory.getLogger(LeaderElectionService.class);

    @Value("${zookeeper.paths.leader}")
    private String LEADER_PATH;

    private LeaderSelector leaderSelector;

    private final CuratorFramework client;
    private final LeaderRoleService leaderRoleService;

    public LeaderElectionService(CuratorFramework client, LeaderRoleService leaderRoleService) {
        this.client = client;
        this.leaderRoleService = leaderRoleService;
    }

    @PostConstruct
    public void start() {
        this.leaderSelector = new LeaderSelector(client, LEADER_PATH, new LeaderSelectorListenerAdapter() {
            @Override
            public void takeLeadership(CuratorFramework client) throws Exception {
                leaderRoleService.startLeadership();
                try {
                    logger.info("Leadership is gained.");
                    Thread.sleep(Long.MAX_VALUE);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                } finally {
                    logger.info("Leadership is released.");
                    leaderRoleService.stopLeadership();
                }
            }
        });
        leaderSelector.autoRequeue();
        leaderSelector.start();
    }

    @PreDestroy
    public void stop() {
        logger.info("Closing Curator Cache");
        if (leaderSelector != null) {
            leaderSelector.close();
        }
        client.close();
    }
}
